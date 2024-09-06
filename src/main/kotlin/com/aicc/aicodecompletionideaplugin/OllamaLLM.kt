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

    /**
     * The model used for code generation in the Ollama API.
     */
    private var model = "codellama:7b-code"

    /**
     * Attempts to generate a code completion suggestion by querying the Ollama API.
     * It constructs a request with a combination of prefix and suffix, handling retries
     * in case of timeouts.
     *
     * @param prefix The part of the code before the cursor.
     * @param suffix The part of the code after the cursor.
     * @return The generated completion suggestion, or null if no suggestion could be generated.
     */
    override fun call(prefix: String, suffix: String): String? {
        if (isEnable) {
            AICCStatusBarWidgetManager.updateStatus("OK")
            for (i in 0..<RETRY_COUNT) {
                val suggestion = try {
                    OllamaAPI(HOST).apply {
                        setRequestTimeoutSeconds(4)
                    }.generate(model, "<PRE> $prefix <SUF>$suffix <MID>", options).response.let {
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

    /**
     * Changes the model used by the LLM.
     * It will also clear the cache
     */
    override fun changeModel(model: String) {
        this.model = model
        AICCCache.clear()
    }

    /**
     * Lazily initialized options for the Ollama API call.
     * These options include settings such as the temperature for the generation process.
     */
    private val options: Options by lazy {
        OptionsBuilder()
            .setTemperature(0.4f)
            .build()
    }

    /**
     * Checks if the Ollama API is reachable and can be used for generating suggestions.
     * This is determined by attempting to ping the Ollama API.
     *
     * @return True if the Ollama API is reachable, false otherwise.
     */
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
