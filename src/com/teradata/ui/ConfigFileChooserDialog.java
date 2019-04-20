package com.teradata.ui;

import com.intellij.ide.util.PropertiesComponent;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;

public class ConfigFileChooserDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JFileChooser list1;
    PropertiesComponent config;
    private String path;

    public String getPath() {
        return path;
    }

    public ConfigFileChooserDialog() {
        setContentPane(contentPane);
        setModal(true);
        setSize(600, 400);
        setLocation(40, 40);
        getRootPane().setDefaultButton(buttonOK);
        config = PropertiesComponent.getInstance();
        String path = config.getValue("path");
        System.out.println(path);
        if (path != null && !path.isEmpty()) {
            this.path = path;
        }


        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        File file = list1.getSelectedFile();
        path = file.getAbsolutePath();
        config.setValue("path", path);
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        ConfigFileChooserDialog dialog = new ConfigFileChooserDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
