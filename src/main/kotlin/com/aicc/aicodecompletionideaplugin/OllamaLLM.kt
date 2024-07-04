package com.aicc.aicodecompletionideaplugin

import io.github.amithkoujalgi.ollama4j.core.OllamaAPI
import io.github.amithkoujalgi.ollama4j.core.utils.Options
import io.github.amithkoujalgi.ollama4j.core.utils.OptionsBuilder
import java.net.http.HttpTimeoutException

object OllamaLLM : LLM {
    override fun call(prefix: String, suffix: String): String? {
        if (isEnable) {
            AICCStatusBarWidgetManager.updateStatus("OK")
            for (i in 0..<RETRY_COUNT) {
                val suggestion = try {
                    OllamaAPI(HOST).apply {
                        setRequestTimeoutSeconds(4)
                    }.generate(MODEL, "<PRE> $prefix <SUF>$suffix <MID>", options).response.let {
                        if (it.endsWith(END)) it.substring(0, it.length - END.length).trim(' ', '\t', '\n') else it
                    }
                } catch (e: HttpTimeoutException) {
                    continue
                }
                if (suggestion.isNotBlank()) {
                    return suggestion
                }
            }
        } else {
            AICCStatusBarWidgetManager.updateStatus("Ollama server is not reachable")
        }
        return null
    }

    private val options: Options by lazy {
        OptionsBuilder()
            .setTemperature(0.4f)
            .build()
    }

    private val isEnable: Boolean
        get() {
            return try {
                OllamaAPI(HOST).ping()
            } catch (e: Exception) {
                false
            }
        }

    /**
     * The host URL for the Ollama API.
     */
    private const val HOST = "http://localhost:11434/"

    /**
     * The model used for code generation in the Ollama API.
     */
    private const val MODEL = "codellama:7b-code"

    /**
     * The end of text marker used in the responses from the Ollama API.
     */
    private const val END = "<EOT>"

    /**
     * The number of attempts to retry the API call in case of a timeout.
     * This constant defines how many times the system should attempt to call the Ollama API
     * before giving up, in the event of a network timeout or other transient errors.
     */
    private const val RETRY_COUNT = 4
}
