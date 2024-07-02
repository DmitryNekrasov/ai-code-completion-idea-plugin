package com.aicc.aicodecompletionideaplugin

import io.github.amithkoujalgi.ollama4j.core.OllamaAPI

object OllamaLLM : LLM {
    val api: OllamaAPI by lazy { OllamaAPI("http://localhost:11434/") }

    val isEnable: Boolean
        get() {
            return try {
                api.ping()
            } catch (e: Exception) {
                false
            }
        }

    override fun call(prefix: String, suffix: String): String? {
        if (isEnable) {
            AICCStatusBarWidgetManager.updateStatus("Ollama server is reachable")
        } else {
            AICCStatusBarWidgetManager.updateStatus("Ollama server is not reachable")
        }
        return ""
    }
}
