package com.ajouin.summaryservice.config

import com.aallam.openai.api.http.Timeout
import com.aallam.openai.client.OpenAIConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import kotlin.time.Duration.Companion.seconds

@Configuration
@RefreshScope
class OpenAIConfig(
    @Value("\${openai.api-key}")
    private val apiKey: String,
) {

    @Bean
    fun openAiConfig(): OpenAIConfig {
        return OpenAIConfig(
            token = apiKey,
            timeout = Timeout(socket = 60.seconds),
        )
    }
}