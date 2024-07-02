package com.aicc.aicodecompletionideaplugin

import io.github.amithkoujalgi.ollama4j.core.OllamaAPI
import io.github.amithkoujalgi.ollama4j.core.types.OllamaModelType
import io.github.amithkoujalgi.ollama4j.core.utils.Options
import io.github.amithkoujalgi.ollama4j.core.utils.OptionsBuilder

object OllamaLLM : LLM {
    private val api: OllamaAPI by lazy { OllamaAPI("http://localhost:11434/") }

    private val options: Options by lazy {
        OptionsBuilder()
            .setTemperature(0.4f)
            .build()
    }

    private val isEnable: Boolean
        get() {
            return try {
                api.ping()
            } catch (e: Exception) {
                false
            }
        }

    override fun call(prefix: String, suffix: String): String? {
        return if (isEnable) {
            AICCStatusBarWidgetManager.updateStatus("OK")
            api.generate("codellama:7b-code", "<PRE> $prefix <SUF>$suffix <MID>", options).response
        } else {
            AICCStatusBarWidgetManager.updateStatus("Ollama server is not reachable")
            null
        }
    }
}
