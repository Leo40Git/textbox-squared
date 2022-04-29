package adudecalledleo.tbsquared.app.ui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

import adudecalledleo.tbsquared.app.plugin.api.renderer.BackgroundRenderer;
import adudecalledleo.tbsquared.app.plugin.api.renderer.SceneRendererProvider;
import adudecalledleo.tbsquared.app.ui.listener.SceneRendererUpdatedListener;
import adudecalledleo.tbsquared.app.ui.listener.TextUpdatedListener;
import adudecalledleo.tbsquared.app.ui.text.UnderlineHighlighter;
import adudecalledleo.tbsquared.app.ui.text.ZigZagHighlighter;
import adudecalledleo.tbsquared.app.ui.util.UnmodifiableAttributeSetView;
import adudecalledleo.tbsquared.color.Palette;
import adudecalledleo.tbsquared.data.DefaultMutableDataTracker;
import adudecalledleo.tbsquared.data.MutableDataTracker;
import adudecalledleo.tbsquared.font.FontProvider;
import adudecalledleo.tbsquared.parse.DOMParser;
import adudecalledleo.tbsquared.parse.node.ContainerNode;
import adudecalledleo.tbsquared.parse.node.Node;
import adudecalledleo.tbsquared.parse.node.NodeRegistry;
import adudecalledleo.tbsquared.parse.node.color.ColorNode;
import adudecalledleo.tbsquared.parse.node.color.ColorSelector;
import adudecalledleo.tbsquared.parse.node.style.StyleNode;
import adudecalledleo.tbsquared.text.Span;
import adudecalledleo.tbsquared.util.render.Colors;

public final class TextboxEditorPane extends JEditorPane
        implements SceneRendererUpdatedListener, DOMParser.SpanTracker, ActionListener {
    private static final ZigZagHighlighter HLP_ERROR = new ZigZagHighlighter(Color.RED);
    private static final Map<Color, UnderlineHighlighter> HLP_ESCAPED_COLORS = new HashMap<>();

    private static Highlighter.HighlightPainter getEscapedColorHighlightPainter(Color color) {
        return HLP_ESCAPED_COLORS.computeIfAbsent(color, UnderlineHighlighter::new);
    }

    private static final String AC_ADD_MOD_COLOR = "add_mod.color";
    private static final String AC_ADD_MOD_STYLE = "add_mod.style";

    private final TextUpdatedListener textUpdatedListener;
    private final Timer updateTimer;
    private final Map<Object, Action> actions;
    private final Map<Rectangle2D, String> errors;
    private final SimpleAttributeSet styleNormal, styleMod;
    private final JPopupMenu popupMenu;

    private final MutableDataTracker ctx;
    private BackgroundRenderer backgroundRenderer;
    private FontProvider fonts;
    private Palette palette;
    private boolean forceCaretRendering;
    private final List<Span> escapedSpans;

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

        this.ctx = new DefaultMutableDataTracker();
        this.forceCaretRendering = false;
        this.escapedSpans = new LinkedList<>();

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

        super.paintComponent(g);
    }

    @Override
    public String getToolTipText(MouseEvent event) {
        var point = event.getPoint();
        for (var entry : errors.entrySet()) {
            if (entry.getKey().contains(point.x, point.y)) {
                return entry.getValue();
            }
        }
        return null;
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
        backgroundRenderer = newProvider.getTextboxBackgroundRenderer();
        fonts = newProvider.getTextboxFonts();
        palette = newProvider.getTextboxPalette().orElse(null);

        ctx.set(ColorSelector.PALETTE, palette);

        Color defaultColor = newProvider.getTextboxTextColor();
        setCaretColor(defaultColor);
        StyleConstants.setForeground(styleNormal, defaultColor);

        var defaultFont = fonts.getDefaultBaseFont();
        StyleConstants.setFontFamily(styleNormal, defaultFont.getFamily());
        StyleConstants.setFontSize(styleNormal, defaultFont.getSize());
        StyleConstants.setFontFamily(styleMod, defaultFont.getFamily());
        StyleConstants.setFontSize(styleMod, defaultFont.getSize());

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

            var result = DOMParser.parse(NodeRegistry.getDefault(), this, getText());

            if (result.hasErrors()) {
                highlightEscapesUncolored(doc);
                for (var error : result.errors()) {
                    try {
                        Rectangle2D startRect, endRect;

                        startRect = modelToView2D(error.start());
                        endRect = modelToView2D(error.end());

                        errors.put(new Rectangle2D.Double(startRect.getX(), startRect.getY(),
                                        endRect.getX() - startRect.getX(), Math.max(startRect.getHeight(), endRect.getHeight())),
                                error.message());
                    } catch (BadLocationException e) {
                        //Logger.error("Failed to generate tooltip bounds for error!", e);
                    }

                    try {
                        getHighlighter().addHighlight(error.start(), error.end(), HLP_ERROR);
                        doc.setCharacterAttributes(error.start(), error.length(), styleNormal, true);
                    } catch (BadLocationException e) {
                        //Logger.error("Failed to properly highlight error!", e);
                    }
                }
            } else {
                highlight0(doc, result.document().getChildren(), style, styleEscaped);
            }
        }
    }

    @Override
    public void markEscaped(int start, int end) {
        this.escapedSpans.add(new Span(start, end - start));
    }

    private void highlightEscapesUncolored(StyledDocument doc) {
        for (var span : escapedSpans) {
            doc.setCharacterAttributes(span.start(), span.length(), styleMod, true);
        }
    }

    private void highlight0(StyledDocument doc, List<Node> nodes, MutableAttributeSet style, MutableAttributeSet styleEscaped) {
        for (var node : nodes) {
            var opening = node.getOpeningSpan();
            var closing = node.getClosingSpan();
            if (!opening.isValid() || !closing.isValid()) {
                continue;
            }
            doc.setCharacterAttributes(opening.start(), opening.length(), styleMod, true);
            doc.setCharacterAttributes(closing.start(), closing.length(), styleMod, true);
            if (node instanceof ColorNode nColor) {
                var color = nColor.getSelector().getColor(this.ctx);

                var attr = nColor.getAttributes().get("value");
                if (attr != null) {
                    var attrStyle = new SimpleAttributeSet(styleMod);
                    StyleConstants.setForeground(style, color);
                    doc.setCharacterAttributes(attr.valueSpan().start(), attr.valueSpan().length(),
                            attrStyle, true);
                }

                style = new SimpleAttributeSet(style);
                StyleConstants.setForeground(style, color);
            } else if (node instanceof StyleNode nStyle) {
                String fontKey = nStyle.getFontKey();
                Integer size = nStyle.getSize();
                Color color = null;
                if (nStyle.getColorSelector() != null) {
                    color = nStyle.getColorSelector().getColor(this.ctx);

                    var colorAttr = nStyle.getAttributes().get("color");
                    if (colorAttr != null) {
                        var attrStyle = new SimpleAttributeSet(styleMod);
                        StyleConstants.setForeground(style, color);
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
                        // TODO log
                    }
                }
            }

            if (node instanceof ContainerNode container) {
                highlight0(doc, container.getChildren(), style, styleEscaped);
            }
        }
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
