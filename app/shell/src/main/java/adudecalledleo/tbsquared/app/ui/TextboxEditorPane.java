package adudecalledleo.tbsquared.app.ui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.List;
import java.util.*;

import javax.swing.Timer;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

import adudecalledleo.tbsquared.app.plugin.api.renderer.BackgroundRenderer;
import adudecalledleo.tbsquared.app.plugin.api.renderer.SceneRendererProvider;
import adudecalledleo.tbsquared.app.ui.listener.SceneRendererUpdatedListener;
import adudecalledleo.tbsquared.app.ui.listener.TextUpdatedListener;
import adudecalledleo.tbsquared.app.ui.text.UnderlineHighlighter;
import adudecalledleo.tbsquared.app.ui.text.ZigZagHighlighter;
import adudecalledleo.tbsquared.app.ui.util.ErrorMessageBuilder;
import adudecalledleo.tbsquared.app.ui.util.UnmodifiableAttributeSetView;
import adudecalledleo.tbsquared.data.DefaultMutableDataTracker;
import adudecalledleo.tbsquared.data.MutableDataTracker;
import adudecalledleo.tbsquared.font.FontProvider;
import adudecalledleo.tbsquared.icon.Icon;
import adudecalledleo.tbsquared.icon.IconPool;
import adudecalledleo.tbsquared.parse.DOMParser;
import adudecalledleo.tbsquared.parse.node.ContainerNode;
import adudecalledleo.tbsquared.parse.node.Node;
import adudecalledleo.tbsquared.parse.node.NodeRegistry;
import adudecalledleo.tbsquared.parse.node.color.ColorNode;
import adudecalledleo.tbsquared.parse.node.color.ColorParser;
import adudecalledleo.tbsquared.parse.node.style.IconNode;
import adudecalledleo.tbsquared.parse.node.style.StyleNode;
import adudecalledleo.tbsquared.text.Span;
import adudecalledleo.tbsquared.util.render.Colors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TextboxEditorPane extends JEditorPane
        implements SceneRendererUpdatedListener, DOMParser.SpanTracker, ActionListener {
    private static final Logger LOGGER = LoggerFactory.getLogger("textbox-squared/TextboxEditorPane");

    private static final ZigZagHighlighter HLP_ERROR = new ZigZagHighlighter(Color.RED);
    private final Map<Color, UnderlineHighlighter> hlpEscapedColors = new HashMap<>();

    private Highlighter.HighlightPainter getEscapedColorHighlightPainter(Color color) {
        return hlpEscapedColors.computeIfAbsent(color, UnderlineHighlighter::new);
    }

    private final Map<Icon, ImageIcon> iconCache = new HashMap<>();

    private ImageIcon createImageIconForIcon(Icon icon) {
        return iconCache.computeIfAbsent(icon, icon1 -> new ImageIcon(icon1.image(), icon1.name()));
    }

    private static final String AC_ADD_MOD_COLOR = "add_mod.color";
    private static final String AC_ADD_MOD_STYLE = "add_mod.style";

    private final TextUpdatedListener textUpdatedListener;
    private final Timer updateTimer;
    private final Map<Object, Action> actions;
    private final Map<Rectangle2D, String> errors;
    private final SimpleAttributeSet styleNormal, styleMod;
    private final JPopupMenu popupMenu;

    private final MutableDataTracker domMeta;
    private BackgroundRenderer backgroundRenderer;
    private FontProvider fonts;
    private boolean renderTextWithAntialiasing;
    private Color defaultTextColor;
    private IconPool icons;
    private boolean forceCaretRendering;
    private final List<Span> escapedSpans;
    private final Set<Span> nodeDeclSpans;

    public TextboxEditorPane(TextUpdatedListener textUpdatedListener) {
        this.textUpdatedListener = textUpdatedListener;
        this.errors = new HashMap<>();

        this.styleNormal = new SimpleAttributeSet();
        StyleConstants.setForeground(this.styleNormal, Color.WHITE);
        this.styleMod = new SimpleAttributeSet(this.styleNormal);
        StyleConstants.setForeground(this.styleMod, Color.GRAY);

        setDocument(new StyledDocumentImpl());
        setEditorKit(new EditorKitImpl(styleNormal));

        this.updateTimer = new Timer(250, e -> SwingUtilities.invokeLater(() -> {
            this.textUpdatedListener.onTextUpdated(this.getText());
            highlight();
        }));
        this.updateTimer.setRepeats(false);
        this.updateTimer.setCoalesce(true);

        this.actions = createActionTable();

        getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                TextboxEditorPane.this.updateTimer.restart();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                TextboxEditorPane.this.updateTimer.restart();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // this one is ignored, because we're the only ones updating the attributes
            }
        });

        setOpaque(true);
        setBackground(Colors.TRANSPARENT);

        ToolTipManager.sharedInstance().registerComponent(this);
        this.popupMenu = createPopupMenu();
        addMouseListener(new MouseAdapter() {
            private final Position.Bias[] biasRet = new Position.Bias[1];

            private void mousePopupTriggered(MouseEvent e) {
                var point = e.getPoint();
                if (getCaret().getMark() < 0) /* if there are no selected characters */ {
                    int pos = getUI().viewToModel2D(TextboxEditorPane.this, point, biasRet);
                    if (pos >= 0) {
                        setCaretPosition(pos);
                    }
                }
                TextboxEditorPane.this.popupMenu.show(TextboxEditorPane.this, point.x, point.y);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    e.consume();
                    mousePopupTriggered(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    e.consume();
                    mousePopupTriggered(e);
                }
            }
        });

        this.domMeta = new DefaultMutableDataTracker();
        this.renderTextWithAntialiasing = true;
        this.forceCaretRendering = false;
        this.escapedSpans = new LinkedList<>();
        this.nodeDeclSpans = new HashSet<>();

        highlight();
    }

    private Map<Object, Action> createActionTable() {
        HashMap<Object, Action> actions = new HashMap<>();
        Action[] actionsArray = getActions();
        for (Action a : actionsArray) {
            actions.put(a.getValue(Action.NAME), a);
        }
        return actions;
    }

    private JPopupMenu createPopupMenu() {
        final var menu = new JPopupMenu();
        JMenuItem item;

        item = new JMenuItem(actions.get(DefaultEditorKit.cutAction));
        item.setText("Cut");
        menu.add(item);
        item = new JMenuItem(actions.get(DefaultEditorKit.copyAction));
        item.setText("Copy");
        menu.add(item);
        item = new JMenuItem(actions.get(DefaultEditorKit.pasteAction));
        item.setText("Paste");
        menu.add(item);

        JMenu modsMenu = new JMenu("Add Modifier...");
        modsMenu.setMnemonic(KeyEvent.VK_M);
        item = new JMenuItem("Color");
        item.setActionCommand(AC_ADD_MOD_COLOR);
        item.addActionListener(this);
        item.setMnemonic(KeyEvent.VK_C);
        modsMenu.add(item);
        item = new JMenuItem("Style");
        item.setActionCommand(AC_ADD_MOD_STYLE);
        item.addActionListener(this);
        item.setMnemonic(KeyEvent.VK_S);
        modsMenu.add(item);

        menu.addSeparator();
        menu.add(modsMenu);

        return menu;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO implement dialogs
        switch (e.getActionCommand()) {
            case AC_ADD_MOD_COLOR -> {
            }
            case AC_ADD_MOD_STYLE -> {
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setBackground(Colors.TRANSPARENT);
        g2d.clearRect(0, 0, getWidth(), getHeight());
        backgroundRenderer.renderBackground(g2d, 0, 0, getWidth(), getHeight());

        if (popupMenu.isVisible() || forceCaretRendering) {
            // force caret to be drawn, so user knows where pasted text/modifiers will be inserted
            getCaret().setVisible(true);
        }

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                renderTextWithAntialiasing
                        ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON
                        : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        super.paintComponent(g2d);
    }

    @Override
    public String getToolTipText(MouseEvent event) {
        ErrorMessageBuilder emb = null;
        var point = event.getPoint();
        for (var entry : errors.entrySet()) {
            if (entry.getKey().contains(point.x, point.y)) {
                if (emb == null) {
                    emb = new ErrorMessageBuilder(entry.getValue());
                } else {
                    emb.add(entry.getValue());
                }
            }
        }
        if (emb == null) {
            return null;
        } else {
            return emb.toString();
        }
    }

    public void flushChanges(boolean highlight) {
        updateTimer.stop();
        SwingUtilities.invokeLater(() -> {
            textUpdatedListener.onTextUpdated(getText());
            if (highlight) {
                highlight();
            }
        });
    }

    @Override
    public void setText(String t) {
        super.setText(t);
        updateTimer.stop();
        SwingUtilities.invokeLater(this::highlight);
    }

    @Override
    public void onSceneRendererUpdated(SceneRendererProvider newProvider) {
        hlpEscapedColors.clear();
        iconCache.clear();

        backgroundRenderer = newProvider.getTextboxBackgroundRenderer();
        fonts = newProvider.getTextboxFonts();
        var palette = newProvider.getTextboxPalette().orElse(null);
        icons = newProvider.getIcons().orElse(null);

        domMeta.set(StyleNode.FONTS, fonts);
        if (palette == null) {
            domMeta.remove(ColorParser.PALETTE);
        } else {
            domMeta.set(ColorParser.PALETTE, palette);
        }
        if (icons == null) {
            domMeta.remove(IconPool.ICONS);
        } else {
            domMeta.set(IconPool.ICONS, icons);
        }

        defaultTextColor = newProvider.getTextboxTextColor();
        setCaretColor(defaultTextColor);
        StyleConstants.setForeground(styleNormal, defaultTextColor);

        var defaultFont = fonts.getDefaultBaseFont();
        StyleConstants.setFontFamily(styleNormal, defaultFont.getFamily());
        StyleConstants.setFontSize(styleNormal, defaultFont.getSize());
        StyleConstants.setFontFamily(styleMod, defaultFont.getFamily());
        StyleConstants.setFontSize(styleMod, defaultFont.getSize());
        renderTextWithAntialiasing = fonts.getDefaultFontMetadata().shouldRenderWithAntialiasing();

        flushChanges(true);
    }

    private void highlight() {
        errors.clear();
        escapedSpans.clear();
        if (getDocument() instanceof StyledDocument doc) {
            MutableAttributeSet style = styleNormal, styleEscaped = styleMod;
            doc.setParagraphAttributes(0, doc.getLength(), style, true);
            doc.setCharacterAttributes(0, doc.getLength(), style, true);
            getHighlighter().removeAllHighlights();

            var result = DOMParser.parse(NodeRegistry.getDefault(), getText(), domMeta, this);

            highlight0(doc, result.document().getChildren(), style, styleEscaped);
            highlightTrackedSpans(doc);

            if (result.hasErrors()) {
                for (var error : result.errors()) {
                    try {
                        Rectangle2D startRect, endRect;

                        startRect = modelToView2D(error.start());
                        endRect = modelToView2D(error.end());

                        var errorRect = new Rectangle2D.Double(startRect.getX(), startRect.getY(),
                                endRect.getX() - startRect.getX(), Math.max(startRect.getHeight(), endRect.getHeight()));
                        errors.put(errorRect, error.message() + " (" + (error.start() + 1) + "-" + (error.end() + 1) + ")");
                    } catch (BadLocationException e) {
                        LOGGER.info("Failed to generate tooltip bounds for error", e);
                    }

                    try {
                        getHighlighter().addHighlight(error.start(), error.end(), HLP_ERROR);
                    } catch (BadLocationException e) {
                        LOGGER.info("Failed to properly highlight error", e);
                    }
                }
            }
        }
    }

    @Override
    public void markEscaped(int start, int end) {
        LOGGER.info("adding escaped span - {}, {}", start, end);
        this.escapedSpans.add(new Span(start, end - start));
    }

    @Override
    public void markNodeDecl(String node, int start, int end) {
        this.nodeDeclSpans.add(new Span(start, end - start));
    }

    private void highlight0(StyledDocument doc, List<Node> nodes, MutableAttributeSet style, MutableAttributeSet styleEscaped) {
        var oldStyle = style;
        var oldStyleEscaped = styleEscaped;
        for (var node : nodes) {
            style = oldStyle;
            styleEscaped = oldStyleEscaped;

            var opening = node.getOpeningSpan();
            var closing = node.getClosingSpan();
            if (!opening.isValid() || !closing.isValid()) {
                continue;
            }
            nodeDeclSpans.remove(opening);
            nodeDeclSpans.remove(closing);
            doc.setCharacterAttributes(opening.start(), opening.length(), styleMod, true);
            doc.setCharacterAttributes(closing.start(), closing.length(), styleMod, true);
            if (node instanceof ColorNode nColor) {
                var color = nColor.getColor();
                if (color == null) {
                    color = defaultTextColor;
                }

                var attr = nColor.getAttributes().get("value");
                if (attr != null) {
                    var attrStyle = new SimpleAttributeSet(styleMod);
                    StyleConstants.setForeground(attrStyle, color);
                    doc.setCharacterAttributes(attr.valueSpan().start(), attr.valueSpan().length(),
                            attrStyle, true);
                }

                style = new SimpleAttributeSet(style);
                StyleConstants.setForeground(style, color);
            } else if (node instanceof StyleNode nStyle) {
                String fontKey = nStyle.getFontKey();
                Integer size = nStyle.getSize();
                Color color = null;
                if (nStyle.isColorSet()) {
                    color = nStyle.getColor();
                    if (color == null) {
                        color = defaultTextColor;
                    }

                    var colorAttr = nStyle.getAttributes().get("color");
                    if (colorAttr != null) {
                        var attrStyle = new SimpleAttributeSet(styleMod);
                        StyleConstants.setForeground(attrStyle, color);
                        doc.setCharacterAttributes(colorAttr.valueSpan().start(), colorAttr.valueSpan().length(),
                                attrStyle, true);
                    }
                }

                boolean escapedModified = fontKey != null || size != null;
                boolean normalModified = escapedModified || color != null;
                if (normalModified) style = new SimpleAttributeSet(style);
                if (escapedModified) styleEscaped = new SimpleAttributeSet(styleEscaped);

                if (fontKey != null) {
                    var font = fonts.getBaseFont(fontKey);
                    StyleConstants.setFontFamily(style, font.getFamily());
                    StyleConstants.setFontFamily(styleEscaped, font.getFamily());
                }

                if (size != null) {
                    final int newSize = StyleConstants.getFontSize(styleNormal) + size;
                    StyleConstants.setFontSize(style, newSize);
                    StyleConstants.setFontSize(styleEscaped, newSize);
                }

                if (color != null) {
                    StyleConstants.setForeground(style, color);
                }
            } else if (node instanceof IconNode nIcon) {
                if (icons != null) {
                    var icon = createImageIconForIcon(icons.getIcon(nIcon.getIconName()));
                    StyleConstants.setIcon(style, icon);
                    StyleConstants.setIcon(styleEscaped, icon);
                }
            }

            final Span contentSpan = node.getContentSpan();
            doc.setCharacterAttributes(contentSpan.start(), contentSpan.length(), style, true);
            final Color contentColor = StyleConstants.getForeground(style);
            var it = escapedSpans.iterator();
            while (it.hasNext()) {
                var escapedSpan = it.next();
                if (escapedSpan.isIn(contentSpan)) {
                    it.remove();
                    doc.setCharacterAttributes(escapedSpan.start(), escapedSpan.length(), styleEscaped, true);
                    try {
                        getHighlighter().addHighlight(escapedSpan.start(), escapedSpan.end(), getEscapedColorHighlightPainter(contentColor));
                    } catch (BadLocationException e) {
                        LOGGER.info("Failed to add highlighter for escaped color", e);
                    }
                }
            }

            if (node instanceof ContainerNode container) {
                highlight0(doc, container.getChildren(), style, styleEscaped);
            }
        }
    }

    private void highlightTrackedSpans(StyledDocument doc) {
        var hl = getEscapedColorHighlightPainter(defaultTextColor);
        for (var span : escapedSpans) {
            doc.setCharacterAttributes(span.start(), span.length(), styleMod, true);
            try {
                getHighlighter().addHighlight(span.start(), span.end(), hl);
            } catch (BadLocationException e) {
                LOGGER.info("Failed to add highlighter for escaped color", e);
            }
        }
        escapedSpans.clear();

        for (var span : nodeDeclSpans) {
            doc.setCharacterAttributes(span.start(), span.length(), styleMod, true);
        }
        nodeDeclSpans.clear();
    }

    private static final class StyledDocumentImpl extends DefaultStyledDocument {
        @Override
        public void insertString(final int offs, String str, final AttributeSet a) throws BadLocationException {
            str = str.replaceAll("\t", "    ");
            super.insertString(offs, str, a);
        }
    }

    private static final class EditorKitImpl extends StyledEditorKit {
        private final UnmodifiableAttributeSetView inputAttributes;

        public EditorKitImpl(AttributeSet inputAttributes) {
            this.inputAttributes = new UnmodifiableAttributeSetView(inputAttributes);
        }

        @Override
        public MutableAttributeSet getInputAttributes() {
            return inputAttributes;
        }

        @Override
        public ViewFactory getViewFactory() {
            return new ViewFactoryImpl();
        }

        private static class ViewFactoryImpl implements ViewFactory {
            @Override
            public View create(final Element elem) {
                final String kind = elem.getName();
                switch (kind) {
                case AbstractDocument.ContentElementName:
                    return new LabelView(elem);
                case AbstractDocument.ParagraphElementName:
                    return new ParagraphViewImpl(elem);
                case AbstractDocument.SectionElementName:
                    return new BoxView(elem, View.Y_AXIS);
                case StyleConstants.ComponentElementName:
                    return new ComponentView(elem);
                case StyleConstants.IconElementName:
                    return new IconView(elem);
                default:
                    break;
                }
                return new LabelView(elem);
            }

            private static class ParagraphViewImpl extends ParagraphView {
                public ParagraphViewImpl(final Element elem) {
                    super(elem);
                }

                @Override
                protected void layout(final int width, final int height) {
                    super.layout(Short.MAX_VALUE, height);
                }

                @Override
                public float getMinimumSpan(final int axis) {
                    return super.getPreferredSpan(axis);
                }
            }
        }
    }
}
