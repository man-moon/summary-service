package com.ajouin.summaryservice.listener

import com.ajouin.summaryservice.dto.SummaryRequest
import com.ajouin.summaryservice.logger
import com.ajouin.summaryservice.service.SummaryService
import com.fasterxml.jackson.databind.ObjectMapper
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.stereotype.Component

@Component
class SummaryRequestEventListener(
    private val objectMapper: ObjectMapper,
    private val summaryService: SummaryService,
) {

    @SqsListener("\${events.queues.summary-request-queue}")
    suspend fun receiveContentRequest(message: String) {

        logger.info { "Received message: $message" }
        val request: SummaryRequest = objectMapper.readValue(message, SummaryRequest::class.java)

        summaryService.processSummaryRequest(request)
    }
}