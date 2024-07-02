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

/**
 * This class represents a status bar widget for the AICC plugin.
 * It extends the EditorBasedWidget and implements the StatusBarWidget.TextPresentation interface.
 *
 * @property project The project in which this widget is used.
 * @property status The current status of the AICC plugin, displayed on the widget.
 */
class AICCStatusBarWidget(project: Project) : EditorBasedWidget(project), StatusBarWidget.TextPresentation {
    /**
     * This method returns the ID of the widget.
     * @return The ID of the widget.
     */
    override fun ID() = ID

    /**
     * This method returns the presentation of the widget.
     * @return The presentation of the widget.
     */
    override fun getPresentation() = this

    /**
     * This method returns the alignment of the widget.
     * @return The alignment of the widget.
     */
    override fun getAlignment() = Component.LEFT_ALIGNMENT

    /**
     * This property represents the current status of the AICC plugin (depends on Ollama server status).
     * It is displayed on the widget.
     */
    var status: String = "waiting..."

    /**
     * This method returns the text to be displayed on the widget.
     * @return The text to be displayed on the widget.
     */
    override fun getText() = "AICC Status: $status"

    /**
     * This method returns the tooltip text of the widget.
     * @return The tooltip text of the widget.
     */
    override fun getTooltipText() = ID

    companion object {
        /**
         * This constant represents the ID of the widget.
         */
        const val ID = "AICCStatusBarWidget"
    }
}

/**
 * This object manages the AICCStatusBarWidget.
 * It provides a method to update the status displayed on the AICCStatusBarWidget.
 */
object AICCStatusBarWidgetManager {
    /**
     * This method updates the status displayed on the AICCStatusBarWidget.
     *
     * @param status The new status to be displayed on the AICCStatusBarWidget.
     */
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