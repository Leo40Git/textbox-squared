package adudecalledleo.tbsquared.app.ui.dialog.insert;

import java.awt.*;
import java.awt.event.*;

import adudecalledleo.tbsquared.app.ui.dialog.ModalDialog;

public abstract class InsertDialog extends ModalDialog implements WindowListener {
    public InsertDialog(Component owner) {
        super(owner);
        addWindowListener(this);
    }

    @Override
    public void windowOpened(WindowEvent e) { }

    @Override
    public void windowClosing(WindowEvent e) { }

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
}
