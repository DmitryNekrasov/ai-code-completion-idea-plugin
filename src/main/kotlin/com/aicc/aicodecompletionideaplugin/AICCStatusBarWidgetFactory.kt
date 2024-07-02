package com.aicc.aicodecompletionideaplugin

import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory
import com.intellij.openapi.wm.WindowManager
import com.intellij.openapi.wm.impl.status.EditorBasedWidget
import java.awt.Component

class AICCStatusBarWidgetFactory : StatusBarWidgetFactory {
    override fun getId() = ID

    override fun getDisplayName() = ID

    override fun createWidget(project: Project) = AICCStatusBarWidget(project)

    override fun isAvailable(project: Project) = true

    override fun canBeEnabledOn(statusBar: com.intellij.openapi.wm.StatusBar) = true

    companion object {
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