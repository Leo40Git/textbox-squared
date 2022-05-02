package adudecalledleo.tbsquared.app.test;

import java.awt.*;

import javax.swing.*;

import adudecalledleo.tbsquared.app.Bootstrap;
import adudecalledleo.tbsquared.app.ui.TextboxEditorPane;

public final class TextboxNotepad {
    public static void main(String[] args) {
        Bootstrap.initPlugins();
        Bootstrap.setSystemLookAndFeel();

        TextboxEditorPane notepad = new TextboxEditorPane(newText -> { });
        notepad.onSceneRendererUpdated(TestSceneRendererProvider.INSTANCE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(notepad, BorderLayout.CENTER);

        JFrame frame = new JFrame("Textbox Notepad");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.requestFocus();
    }
}
