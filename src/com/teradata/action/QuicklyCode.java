package com.teradata.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.ui.Messages;
import com.teradata.document.DocumentWrite;
import com.teradata.ui.QuestionDialog;

import java.util.Objects;


public class QuicklyCode extends AnAction {

    private DocumentWrite documentWrite;

    private final String QUICKLY_CODE = "QuicklyCode";
    private final String DO_NOT_CHOOSE_ANY_CODE = "you don't choose any code";

    @Override
    public void actionPerformed(AnActionEvent e) {


        QuestionDialog questionDialog = new QuestionDialog();
        documentWrite = new DocumentWrite(e);
        questionDialog.setAnActionEvent(e);
        questionDialog.setTitle("QuicklyCode");
        questionDialog.setVisible(true);
        questionDialog.pack();
        int start = questionDialog.start;
        int end = questionDialog.end;
        String target = questionDialog.target;
        CommandProcessor.getInstance().runUndoTransparentAction(() -> {
            String content = documentWrite.get();
            if (content != null && content.length() != 0) {
                Application app = ApplicationManager.getApplication();
                app.runWriteAction(() -> {
                    Document document = Objects.requireNonNull(e.getData(PlatformDataKeys.EDITOR)).getDocument();
                    document.setReadOnly(false);
                    document.setText(content.substring(0, start) + target + content.substring(end));
                });
            } else {
                Messages.showErrorDialog(DO_NOT_CHOOSE_ANY_CODE, QUICKLY_CODE);
            }
        });
    }


}
