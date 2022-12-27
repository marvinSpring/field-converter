package com.marvin;

import com.intellij.codeInsight.actions.MultiCaretCodeInsightAction;
import com.intellij.codeInsight.actions.MultiCaretCodeInsightActionHandler;
import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegate;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.impl.SelectionModelImpl;
import com.intellij.openapi.project.Project;
import com.marvin.handler.FieldFlowMultiCaretCodeInsightActionHandler;
import com.twelvemonkeys.lang.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;


/**
 * Menu action to replace a selection of characters with a fixed string.
 *
 * @author Marvin
 * @Date: 2022-04-15
 * @see AnAction
 */
public class FieldFlowAction extends MultiCaretCodeInsightAction {

    /**
     * Replaces the run of text selected by the primary caret with a fixed string.
     *
     * @param e Event related to this action
     */
    @Override
    public void actionPerformed(@NotNull final AnActionEvent e) {
        // Get all the required data from data keys
        // Editor and Project were verified in update(), so they are not null.
        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        final Document document = editor.getDocument();
        // Work off of the primary caret to get the selection info
        List<Caret> allCarets = editor.getCaretModel().getAllCarets();
        for (Caret caret : allCarets) {
                int start = caret.getSelectionStart();
                int end = caret.getSelectionEnd();
                String selectedText = caret.getSelectedText();
                //selected text can not be null
                if (!StringUtil.isEmpty(selectedText)) {
                    // Replace the selection with a fixed string.
                    // Must do this document change in a write action context.
                    WriteCommandAction.runWriteCommandAction(project, () -> {
                                if (checkTextIsContainsUnderline(selectedText)) {
                                    document.replaceString(start, end, PropertyFactory.replaceDataBaseFieldToJavaObjectProperties(selectedText));
                                } else {
                                    document.replaceString(start, end, PropertyFactory.replaceJavaObjectPropertiesToDataBaseField(selectedText));
                                }
                            }
                    );
                    // De-select the text range that was just replaced
                    caret.removeSelection();
            }
        }

    }

    /**
     * check selectedText is contains underline {@example user_action}
     * @param selectedText selectedText
     * @return true underline text ,false other text
     */
    private boolean checkTextIsContainsUnderline(String selectedText) {
        return selectedText.contains("_");
    }

    /**
     * Sets visibility and enables this action menu item if:
     * <ul>
     *   <li>a project is open</li>
     *   <li>an editor is active</li>
     *   <li>some characters are selected</li>
     * </ul>
     *
     * @param e Event related to this action
     */
    @Override
    public void update(@NotNull final AnActionEvent e) {
        // Get required data keys
        final Project project = e.getProject();
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        // Set visibility and enable only in case of existing project and editor and if a selection exists
        e.getPresentation().setEnabledAndVisible(
                project != null && editor != null && editor.getSelectionModel().hasSelection()
        );
    }

    @Override
    protected @NotNull MultiCaretCodeInsightActionHandler getHandler() {
        return new FieldFlowMultiCaretCodeInsightActionHandler();
    }


}
