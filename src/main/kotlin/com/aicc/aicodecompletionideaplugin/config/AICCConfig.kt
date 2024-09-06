package com.aicc.aicodecompletionideaplugin.config

import com.aicc.aicodecompletionideaplugin.OllamaLLM
import com.intellij.openapi.options.SearchableConfigurable
import javax.swing.JComponent

class AICCConfig : SearchableConfigurable {

    private var panel: AICCSettingsPanel? = null

    override fun createComponent(): JComponent {
        return AICCSettingsPanel().also { panel = it }.mainPanel
    }

    override fun isModified(): Boolean {
        val panel = this.panel ?: return false
        val state = AICCState.getInstance()
        return panel.modelField.text != state.model
    }

    override fun reset() {
        val panel = this.panel ?: return
        val state = AICCState.getInstance()
        panel.modelField.text = state.model
        OllamaLLM.changeModel(state.model)
    }

    override fun apply() {
        val panel = this.panel ?: return
        val state = AICCState.getInstance()
        state.model = panel.modelField.text
        OllamaLLM.changeModel(state.model)
    }

    override fun disposeUIResources() {
        this.panel = null
    }

    override fun getDisplayName() = "AI code completion idea"
    override fun getId() = "com.aicc.aicodecompletionideaplugin.config.AICCConfig"
}