package com.aicc.aicodecompletionideaplugin.config;

import com.intellij.ui.IdeBorderFactory
import javax.swing.JPanel
import javax.swing.JTextField

class AICCSettingsPanel {

    lateinit var mainPanel: JPanel
    lateinit var modelField: JTextField

    init {
        mainPanel.border = IdeBorderFactory.createTitledBorder("Plugin Settings")
    }
}
