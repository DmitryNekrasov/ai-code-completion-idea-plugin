package com.aicc.aicodecompletionideaplugin

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages

class AICCStatsPopupDialogAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val message = "Total completions: ${AICCStatistic.totalCompletionsNumber}\n" +
                "Successful completions: ${AICCStatistic.successfulCompletionsNumber}\n" +
                "User engagement: ${AICCStatistic.userEngagement}%\n" +
                "Average completion time: %.2f s".format(AICCStatistic.averageCompletionTime)
        Messages.showMessageDialog(
            event.project,
            message,
            event.presentation.description,
            Messages.getInformationIcon()
        )
    }
}