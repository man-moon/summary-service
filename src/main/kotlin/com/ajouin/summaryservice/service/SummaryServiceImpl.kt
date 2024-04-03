package com.ajouin.summaryservice.service

import com.aallam.openai.api.chat.*
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.Chat
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig
import com.ajouin.summaryservice.dto.Summary
import com.ajouin.summaryservice.dto.SummaryRequest
import com.ajouin.summaryservice.event.SummaryResponseCreatedEvent
import com.ajouin.summaryservice.logger
import com.ajouin.summaryservice.publisher.SummaryResponseEventPublisher
import com.google.gson.Gson
import com.google.gson.JsonParser
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.stereotype.Service

@Service
@RefreshScope
class SummaryServiceImpl(
    private val openAIConfig: OpenAIConfig,
    private val eventPublisher: SummaryResponseEventPublisher,
    @Value("\${openai.model}") private val model: String,
    @Value("\${openai.prompt}") private val prompt: String,
) : SummaryService {

    override suspend fun processSummaryRequest(summaryRequest: SummaryRequest) {

        val content: ChatCompletion = summaryContent(summaryRequest)
        val summary = jsonToObject(content)

        eventPublisher.publish(
            SummaryResponseCreatedEvent(
                id = summaryRequest.id,
                content = summary.firstSentence
                        + "\n"
                        + summary.secondSentence
                        + "\n"
                        + summary.thirdSentence
            )
        )
    }

    override suspend fun summaryContent(summaryRequest: SummaryRequest): ChatCompletion {

        val openAI = OpenAI(openAIConfig)

        val chatCompletionRequest = ChatCompletionRequest(
            model = ModelId(model),
            temperature = 0.3,
            responseFormat = ChatResponseFormat(type = "json_object"),
            messages = listOf(
                ChatMessage(
                    role = ChatRole.System,
                    content = prompt
                ),
                ChatMessage(
                    role = ChatRole.User,
                    content = summaryRequest.content,
                )
            )
        )

        // coroutine
        val result = openAI.chatCompletion(chatCompletionRequest)

        return result
    }

    private fun jsonToObject(content: ChatCompletion): Summary {

        val summaryResult: Content? = content.choices[0].message.messageContent

        val gson = Gson()
        val contentJson = gson.toJson(summaryResult)
        val summaryJson = JsonParser.parseString(contentJson).asJsonObject.get("content").asString
        val summary = gson.fromJson(summaryJson, Summary::class.java)
        return summary
    }
}