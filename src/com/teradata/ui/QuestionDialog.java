package com.teradata.ui;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.Messages;
import com.teradata.template.TemplateEngine;
import com.teradata.action.QuicklyCode;
import com.teradata.db.DataBaseUtil;
import com.teradata.db.DatabaseFactory;
import com.teradata.db.FieldInfo;
import com.teradata.document.DocumentWrite;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.HashMap;
import java.util.List;

public class QuestionDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField tableNameText;
    private JTextField configText;
    private JButton chooseFileButton;
    private String path;
    private AnActionEvent anActionEvent = null;
    public int start;
    public int end;
    public String target;

    public void setQuicklyCode(QuicklyCode quicklyCode) {
        this.quicklyCode = quicklyCode;
    }

    private QuicklyCode quicklyCode;
    private final int WIDTH = 600;
    private final int HEIGHT = 200;
    PropertiesComponent config;


    public void setAnActionEvent(AnActionEvent anActionEvent) {
        this.anActionEvent = anActionEvent;
    }

    public QuestionDialog() {
        setContentPane(contentPane);
        setModal(true);
        setSize(WIDTH, HEIGHT);
        Point p = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
        setBounds(p.x - WIDTH / 2, p.y - HEIGHT / 2, WIDTH, HEIGHT);
        getRootPane().setDefaultButton(buttonOK);
        configText.setEditable(false);


        config = PropertiesComponent.getInstance();
        path = config.getValue("path");
        String tableName = config.getValue("tableName");
        if (tableName != null) {
            tableNameText.setText(tableName);
        }
        if (path != null) {
            configText.setText(path);
        }
        chooseFileButton.addActionListener(e -> {
            ConfigFileChooserDialog configFileChooserDialog = new ConfigFileChooserDialog();
            configFileChooserDialog.setVisible(true);
            configFileChooserDialog.pack();
            path = configFileChooserDialog.getPath();
            configText.setText(path);
        });
        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public final String QUICKLY_CODE = "QuicklyCode";

    private void onOK() {

        String table = tableNameText.getText();
        if (table == null || table.isEmpty()) {
            Messages.showErrorDialog("tableName can't be null!", QUICKLY_CODE);
            return;
        }
        String config = configText.getText();
        if (config == null || config.isEmpty()) {
            Messages.showErrorDialog("config should be choose!", QUICKLY_CODE);
            return;
        }
        this.config.setValue("path", config);
        this.config.setValue("tableName", table);


        File file = new File(config);
        if (!file.exists()) {
            Messages.showErrorDialog("file:" + file.getAbsolutePath() + " is not exist,please check it ", QUICKLY_CODE);
            return;
        }

        DataBaseUtil dataBaseUtil = null;
        try {
            dataBaseUtil = DatabaseFactory.readFile(file);
        } catch (Exception e) {
            Messages.showErrorDialog(e.getMessage(), QUICKLY_CODE);
            return;
        }
        List<FieldInfo> tableFieldsBySql = null;
        try {
            tableFieldsBySql = dataBaseUtil.getTableFieldsBySql(table);
        } catch (Exception e) {
            Messages.showErrorDialog(e.getMessage(), QUICKLY_CODE);
            return;
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("field", tableFieldsBySql);


        Editor editor = PlatformDataKeys.EDITOR.getData(anActionEvent.getDataContext());
        if(editor==null){
            Messages.showErrorDialog("please choose a area to replace",QUICKLY_CODE);
        }
        CaretModel caretModel = editor.getCaretModel();
        List<Caret> allCarets = caretModel.getAllCarets();
//        int start = caretModel.getVisualLineStart();
//        int end = caretModel.getVisualLineEnd();

        for (Caret caret : allCarets) {
            int x = caret.getOffset();
            int y = caret.getLeadSelectionOffset();
            final int start = caret.getSelectionStart();
            final int end = caret.getSelectionEnd();
            DocumentWrite documentWrite = new DocumentWrite(anActionEvent);
            String content = documentWrite.get();
            content = content.substring(start,end);
            String target = null;
            try {
                target = TemplateEngine.render(content, map,dataBaseUtil);

            } catch (Exception e) {
                Messages.showErrorDialog(e.getMessage(), QUICKLY_CODE);
            }
            while (target.contains("\r\n")) {
                target = target.replace("\r\n", "\n");
            }
            this.start = start;
            this.end = end;
            this.target = target;
//            documentWrite.replace(start, end, target);

            break;
        }


        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {

    }
}
