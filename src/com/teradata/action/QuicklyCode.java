package com.teradata.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;

import com.intellij.openapi.command.CommandProcessor;
import com.teradata.document.DocumentWrite;
import com.teradata.ui.QuestionDialog;


public class QuicklyCode extends AnAction {

    private DocumentWrite documentWrite;

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
//        Application application = ApplicationManager.getApplication();
        CommandProcessor.getInstance().runUndoTransparentAction(()->{
            String content = documentWrite.get();
            documentWrite.set(content.substring(0,start)+target+content.substring(end));
        });
    }


}
