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
     * The number of cache miss events. A cache miss occurs when the requested data is not found in the cache.
     */
    var cacheMissNumber: Int = 0

    /**
     * The number of cache hit events. A cache hit occurs when the requested data is found in the cache.
     */
    var cacheHitNumber: Int = 0

    /**
     * Generates the text to be displayed on the status bar widget.
     * It includes the current status of the AICC plugin and the cache hit/miss counts.
     *
     * @return A string representation of the AICC status, cache misses, and cache hits.
     */
    override fun getText() = "| AICC Status: $status | Cache Miss: $cacheMissNumber | Cache Hit: $cacheHitNumber |"

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
 * Manages updates to the AICC status bar widget, including status, cache miss, and cache hit numbers.
 * This object provides a centralized way to update the widget's display information.
 */
object AICCStatusBarWidgetManager {
    /**
     * Updates the status displayed on the AICC status bar widget.
     *
     * @param status The new status to be displayed.
     */
    fun updateStatus(status: String) {
        update { this.status = status }
    }

    /**
     * Updates the number of cache miss events to be displayed on the AICC status bar widget.
     * A cache miss occurs when the requested data is not found in the cache.
     *
     * @param cacheMissNumber The new number of cache miss events.
     */
    fun updateCacheMissNumber(cacheMissNumber: Int) {
        update { this.cacheMissNumber = cacheMissNumber }
    }

    /**
     * Updates the number of cache hit events to be displayed on the AICC status bar widget.
     * A cache hit occurs when the requested data is found in the cache.
     *
     * @param cacheHitNumber The new number of cache hit events.
     */
    fun updateCacheHitNumber(cacheHitNumber: Int) {
        update { this.cacheHitNumber = cacheHitNumber }
    }

    /**
     * Private helper function to apply updates to the AICCStatusBarWidget.
     *
     * @param updateField A lambda function that applies updates to the AICCStatusBarWidget.
     */
    private fun update(updateField: AICCStatusBarWidget.() -> Unit) {
        WindowManager.getInstance().getStatusBar(ProjectManager.getInstance().openProjects.first())?.also { statusBar ->
            statusBar.getWidget(AICCStatusBarWidget.ID)?.apply {
                if (this is AICCStatusBarWidget) {
                    updateField()
                    statusBar.updateWidget(AICCStatusBarWidget.ID)
                }
            }
        }
    }
}