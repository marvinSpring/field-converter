package com.marvin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.twelvemonkeys.lang.StringUtil;
import org.jetbrains.annotations.NotNull;


/**
 * Menu action to replace a selection of characters with a fixed string.
 *
 * @author Marvin
 * @Date: 2022-04-15
 * @see AnAction
 */
public class FieldFlowAction extends AnAction {

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
        Caret primaryCaret = editor.getCaretModel().getPrimaryCaret();
        int start = primaryCaret.getSelectionStart();
        int end = primaryCaret.getSelectionEnd();
        String selectedText = primaryCaret.getSelectedText();
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
            primaryCaret.removeSelection();
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


}
