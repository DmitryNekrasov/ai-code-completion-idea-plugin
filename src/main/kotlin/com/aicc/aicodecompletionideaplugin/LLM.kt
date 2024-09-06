package com.aicc.aicodecompletionideaplugin

/**
 * This is an interface for a Large Language Model (LLM).
 * It provides a method to generate a completion suggestion based on a given prefix and suffix.
 */
interface LLM {
    /**
     * This method generates a completion suggestion based on a given prefix and suffix.
     *
     * @param prefix The part of the code before the cursor.
     * @param suffix The part of the code after the cursor.
     * @return The generated completion suggestion, or null if no suggestion could be generated.
     */
    fun call(prefix: String, suffix: String): String?

    /**
     * This method changes the current model used by the LLM.
     *
     * @param model The name of the model to be used.
     */
    fun changeModel(model: String)
}