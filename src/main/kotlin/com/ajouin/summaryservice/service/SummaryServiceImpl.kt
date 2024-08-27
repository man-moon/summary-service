package com.ajouin.summaryservice.service

import com.aallam.ktoken.Encoding
import com.aallam.ktoken.Tokenizer
import com.aallam.openai.api.chat.*
import com.aallam.openai.api.exception.RateLimitException
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.Chat
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig
import com.ajouin.summaryservice.dto.Summary
import com.ajouin.summaryservice.dto.SummaryRequest
import com.ajouin.summaryservice.event.SummaryRerequestCreatedEvent
import com.ajouin.summaryservice.event.SummaryResponseCreatedEvent
import com.ajouin.summaryservice.logger
import com.ajouin.summaryservice.publisher.SummaryResponseEventPublisher
import com.google.gson.Gson
import com.google.gson.JsonParser
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.integration.handler.advice.RateLimiterRequestHandlerAdvice
import org.springframework.stereotype.Service
import java.net.ConnectException
import java.util.concurrent.atomic.AtomicInteger

@Service
@RefreshScope
class SummaryServiceImpl(
    private val openAIConfig: OpenAIConfig,
    private val eventPublisher: SummaryResponseEventPublisher,
    @Value("\${openai.model}") private val model: String,
    @Value("\${openai.prompt}") private val prompt: String,
) : SummaryService {

    override suspend fun processSummaryRequest(summaryRequest: SummaryRequest) {

        try {
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
            logger.info { "summaryRequestId=${summaryRequest.id} 정상적으로 처리됨" }
        } catch (e: Exception) {
            when (e) {
                is RateLimitException -> logger.error { "OpenAI api 요청 제한 발생" }
                else -> logger.error { e.message }
            }
        } finally {
            logger.debug { "70초 대기" }
            delay(70000)
        }
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

        var result: ChatCompletion? = null
        while (result == null) {
            try {
                result = openAI.chatCompletion(chatCompletionRequest)
            } catch (e: ConnectException) {
                logger.error { "ConnectException 발생으로 재시도" }
                delay(1000)
            } catch (e: Exception) {
                logger.error { e.message }
                throw e
            }
        }

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