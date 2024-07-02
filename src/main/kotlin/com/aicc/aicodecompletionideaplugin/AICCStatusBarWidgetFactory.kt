package com.aicc.aicodecompletionideaplugin

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory
import com.intellij.openapi.wm.impl.status.EditorBasedWidget
import java.awt.Component

class AICCStatusBarWidgetFactory : StatusBarWidgetFactory {
    override fun getId() = "AICCStatusBarWidget"

    override fun getDisplayName() = "AICCStatusBarWidget"

    override fun createWidget(project: Project): StatusBarWidget {
        return AICCStatusBarWidget(project)
    }

    override fun disposeWidget(widget: StatusBarWidget) {
        // Dispose resources if needed
    }

    override fun isAvailable(project: Project): Boolean {
        // Return true if the widget is available for the project
        return true
    }

    override fun canBeEnabledOn(statusBar: com.intellij.openapi.wm.StatusBar): Boolean {
        // Return true if the widget can be enabled on the status bar
        return true
    }
}

class AICCStatusBarWidget(project: Project) : EditorBasedWidget(project), StatusBarWidget.TextPresentation {

    override fun ID(): String {
        return "AICCStatusBarWidget"
    }

    override fun getPresentation(): StatusBarWidget.WidgetPresentation {
        return this
    }

    override fun getAlignment(): Float {
        return Component.LEFT_ALIGNMENT
    }

    override fun getText() = "AICC Status Bar Widget TEXT"

    override fun getTooltipText(): String {
        return "AICC Status Bar Widget"
    }
}


