package adudecalledleo.tbsquared.app.ui.dialog;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import adudecalledleo.tbsquared.app.ui.render.FaceCategoryListCellRenderer;
import adudecalledleo.tbsquared.app.ui.render.FaceGridCellRenderer;
import adudecalledleo.tbsquared.face.Face;
import adudecalledleo.tbsquared.face.FaceCategory;
import adudecalledleo.tbsquared.face.FacePool;
import adudecalledleo.tbsquared.util.Pair;
import org.jetbrains.annotations.Nullable;

public final class FaceSelectDialog extends ModalDialog implements WindowListener {
    private static final Pair<FaceCategory, Face> RESULT_BLANK = new Pair<>(FaceCategory.NONE, Face.BLANK);

    private final FacePool faces;

    private Pair<FaceCategory, Face> result;

    public FaceSelectDialog(Component owner, FacePool faces, @Nullable Face currentFace) {
        super(owner);
        this.faces = faces;
        if (currentFace != null) {
            if (currentFace.isBlank()) {
                this.result = RESULT_BLANK;
            } else {
                this.result = new Pair<>(faces.getCategoryOf(currentFace), currentFace);
            }
        }
        setRootPane(new RootPane());
        pack();

        addWindowListener(this);
    }

    public Pair<FaceCategory, Face> showDialog() {
        setVisible(true);
        return result;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        result = null;
    }

    //region no-ops
    @Override
    public void windowOpened(WindowEvent e) { }

    @Override
    public void windowClosed(WindowEvent e) { }

    @Override
    public void windowIconified(WindowEvent e) { }

    @Override
    public void windowDeiconified(WindowEvent e) { }

    @Override
    public void windowActivated(WindowEvent e) { }

    @Override
    public void windowDeactivated(WindowEvent e) { }
    //endregion

    private final class RootPane extends JRootPane implements ListSelectionListener, ActionListener {
        private final DefaultListModel<FaceCategory> mdlCategories;
        private final DefaultListModel<Face> mdlFaces;

        private final JList<FaceCategory> lstCategories;
        private final JTextField txtFaceSearch;
        private final JList<Face> lstFaces;
        private final JButton btnOK, btnCancel;

        public RootPane() {
            mdlCategories = new DefaultListModel<>();
            mdlCategories.addElement(FaceCategory.NONE);
            mdlCategories.addAll(faces.getCategories());

            mdlFaces = new DefaultListModel<>();

            lstCategories = new JList<>(mdlCategories);
            lstCategories.setCellRenderer(new FaceCategoryListCellRenderer());
            lstCategories.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            txtFaceSearch = new JTextField(); // TODO implement this
            lstFaces = new JList<>(mdlFaces);
            lstFaces.setCellRenderer(new FaceGridCellRenderer());
            lstFaces.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            lstFaces.setLayoutOrientation(JList.HORIZONTAL_WRAP);
            lstFaces.setVisibleRowCount(0);

            btnOK = createBtn("OK");
            btnCancel = createBtn("Cancel");

            JPanel listPanel = new JPanel(new BorderLayout());
            listPanel.add(txtFaceSearch, BorderLayout.PAGE_START);
            listPanel.add(new JScrollPane(lstFaces), BorderLayout.CENTER);

            JPanel btnPanel = new JPanel(new GridLayout(1, 2));
            btnPanel.add(btnOK);
            btnPanel.add(btnCancel);

            setLayout(new BorderLayout());
            add(new JScrollPane(lstCategories), BorderLayout.LINE_START);
            add(listPanel, BorderLayout.CENTER);
            add(btnPanel, BorderLayout.PAGE_END);
            setDefaultButton(btnOK);

            if (result != null) {
                lstCategories.setSelectedValue(result.left(), true);
            }
            if (result != null && !result.right().isBlank()) {
                lstFaces.setSelectedValue(result.right(), true);
            }

            lstCategories.addListSelectionListener(this);
            lstFaces.addListSelectionListener(this);
        }

        private JButton createBtn(String label) {
            var btn = new JButton(label);
            btn.addActionListener(this);
            return btn;
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            var src = e.getSource();
            if (src == lstCategories) {
                var selected = lstCategories.getSelectedValue();
                if (selected == adudecalledleo.tbsquared.face.FaceCategory.NONE) {
                    FaceSelectDialog.this.result = RESULT_BLANK;
                    mdlFaces.clear();
                    lstFaces.setEnabled(false);
                } else {
                    mdlFaces.clear();
                    mdlFaces.addAll(selected.getFaces());
                    lstFaces.setEnabled(true);
                }
            } else if (src == lstFaces) {
                FaceSelectDialog.this.result = new Pair<>(lstCategories.getSelectedValue(), lstFaces.getSelectedValue());
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            var src = e.getSource();
            if (src == btnOK) {
                FaceSelectDialog.this.setVisible(false);
                FaceSelectDialog.this.dispose();
            } else if (src == btnCancel) {
                result = null;
                FaceSelectDialog.this.setVisible(false);
                FaceSelectDialog.this.dispose();
            }
        }
    }
}
