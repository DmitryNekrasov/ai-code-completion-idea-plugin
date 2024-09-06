package com.aicc.aicodecompletionideaplugin.config

import com.aicc.aicodecompletionideaplugin.OllamaLLM
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.State
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "com.aicc.aicodecompletionideaplugin.config.AICCState",
    storages = [Storage("aicodecompletionideaplugin.xml")]
)
class AICCState : PersistentStateComponent<AICCState> {

    @JvmField
    var model: String = "codellama:7b-code"

    override fun getState(): AICCState = this

    override fun loadState(state: AICCState) {
        XmlSerializerUtil.copyBean(state, this)
        OllamaLLM.changeModel(state.model)
    }

    companion object {
        fun getInstance(): AICCState {
            return ApplicationManager.getApplication().getService(AICCState::class.java)
        }
    }
}
