package com.marvin.handler;

import com.intellij.codeInsight.actions.MultiCaretCodeInsightActionHandler;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public class FieldFlowMultiCaretCodeInsightActionHandler extends MultiCaretCodeInsightActionHandler {
    @Override
    public void invoke(@NotNull Project project, @NotNull Editor editor, @NotNull Caret caret, @NotNull PsiFile file) {
        //do some
    }
}
