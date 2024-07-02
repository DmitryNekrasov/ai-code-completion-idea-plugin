package com.aicc.aicodecompletionideaplugin

import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory
import com.intellij.openapi.wm.WindowManager
import com.intellij.openapi.wm.impl.status.EditorBasedWidget
import java.awt.Component

/**
 * This class is a factory for creating instances of `AICCStatusBarWidget`.
 * It implements the `StatusBarWidgetFactory` interface.
 */
class AICCStatusBarWidgetFactory : StatusBarWidgetFactory {
    /**
     * Returns the unique identifier for this widget factory.
     */
    override fun getId() = ID

    /**
     * Returns the display name for this widget factory.
     */
    override fun getDisplayName() = ID

    /**
     * Creates a new instance of `AICCStatusBarWidget` for the given project.
     */
    override fun createWidget(project: Project) = AICCStatusBarWidget(project)

    /**
     * Checks if this widget is available for the given project.
     * Always returns true.
     */
    override fun isAvailable(project: Project) = true

    /**
     * Checks if this widget can be enabled on the given status bar.
     * Always returns true.
     */
    override fun canBeEnabledOn(statusBar: com.intellij.openapi.wm.StatusBar) = true

    companion object {
        /**
         * The unique identifier for this widget factory.
         */
        const val ID = "AICCStatusBarWidgetFactory"
    }
}

class AICCStatusBarWidget(project: Project) : EditorBasedWidget(project), StatusBarWidget.TextPresentation {
    override fun ID() = ID

    override fun getPresentation() = this

    override fun getAlignment() = Component.LEFT_ALIGNMENT

    var status: String = "waiting..."

    override fun getText() = "AICC Status: $status"

    override fun getTooltipText() = ID

    companion object {
        const val ID = "AICCStatusBarWidget"
    }
}

object AICCStatusBarWidgetManager {
    fun updateStatus(status: String) {
        WindowManager.getInstance().getStatusBar(ProjectManager.getInstance().openProjects.first())?.also { statusBar ->
            statusBar.getWidget(AICCStatusBarWidget.ID)?.apply {
                if (this is AICCStatusBarWidget) {
                    this.status = status
                    statusBar.updateWidget(AICCStatusBarWidget.ID)
                }
            }
        }
    }
}