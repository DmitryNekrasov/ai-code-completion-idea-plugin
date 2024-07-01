package com.aicc.aicodecompletionideaplugin

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages

class AICCStatsPopupDialogAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val message = "TODO: Stats will be shown here"
        Messages.showMessageDialog(
            event.project,
            message,
            event.presentation.description,
            Messages.getInformationIcon()
        )
    }
}