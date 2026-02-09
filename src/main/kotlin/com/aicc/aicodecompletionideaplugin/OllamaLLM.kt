package com.aicc.aicodecompletionideaplugin

import io.github.amithkoujalgi.ollama4j.core.OllamaAPI
import io.github.amithkoujalgi.ollama4j.core.utils.Options
import io.github.amithkoujalgi.ollama4j.core.utils.OptionsBuilder
import java.net.http.HttpTimeoutException

/**
 * Implementation of the LLM interface using the Ollama API for generating code completions.
 * This object encapsulates the functionality to communicate with the Ollama API, handling
 * network timeouts and retries, to fetch code completion suggestions based on the given prefix
 * and suffix.
 */
object OllamaLLM : LLM {

    private var model = "codellama:7b-code"

    private const val HOST = "http://localhost:11434/"
    private const val END = "<EOT>"
    private const val RETRY_COUNT = 4

    private val api = OllamaAPI(HOST).apply {
        setRequestTimeoutSeconds(5)
    }

    private val options: Options by lazy {
        OptionsBuilder()
            .setTemperature(0.4f)
            .build()
    }

    override fun call(prefix: String, suffix: String): String? {
        for (i in 0..<RETRY_COUNT) {
            val suggestion = try {
                api.generate(model, "<PRE> $prefix <SUF>$suffix <MID>", options).response.let {
                    if (it.endsWith(END)) it.substring(0, it.length - END.length).trim(' ', '\t', '\n') else it
                }
            } catch (_: HttpTimeoutException) {
                continue
            } catch (_: Exception) {
                AICCStatusBarWidgetManager.updateStatus("Ollama server is not reachable")
                return null
            }
            AICCStatusBarWidgetManager.updateStatus("OK")
            if (suggestion.isNotBlank()) {
                return suggestion
            }
        }
        return null
    }

    override fun changeModel(model: String) {
        this.model = model
        AICCCache.clear()
    }
}
