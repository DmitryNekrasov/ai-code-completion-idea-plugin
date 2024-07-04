package com.aicc.aicodecompletionideaplugin

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages

/**
 * Defines an action to display a popup dialog showing statistics about code completions.
 * This action is intended to provide users with insights into their usage of the AI code completion feature,
 * including total number of completions, number of successful completions, user engagement percentage,
 * and average completion time.
 */
class AICCStatsPopupDialogAction : AnAction() {
    /**
     * Handles the action of displaying the statistics popup dialog.
     * This method is triggered when the action is invoked by the user.
     *
     * @param event The event that triggered this action, containing context about the action's invocation.
     */
    override fun actionPerformed(event: AnActionEvent) {
        // Construct the message to be displayed in the popup dialog
        val message = "Total completions: ${AICCStatistic.totalCompletionsNumber}\n" +
                "Successful completions: ${AICCStatistic.successfulCompletionsNumber}\n" +
                "User engagement: ${AICCStatistic.userEngagement}%\n" +
                "Average completion time: %.2f s".format(AICCStatistic.averageCompletionTime)
        // Display the constructed message in a popup dialog to the user.
        Messages.showMessageDialog(
            event.project,
            message,
            event.presentation.description,
            Messages.getInformationIcon()
        )
    }
}