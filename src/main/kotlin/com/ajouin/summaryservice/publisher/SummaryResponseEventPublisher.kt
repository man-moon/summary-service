package com.ajouin.summaryservice.publisher

import com.ajouin.summaryservice.config.EventQueuesProperties
import com.ajouin.summaryservice.event.SummaryRerequestCreatedEvent
import com.ajouin.summaryservice.event.SummaryResponseCreatedEvent
import com.ajouin.summaryservice.logger
import com.fasterxml.jackson.databind.ObjectMapper
import io.awspring.cloud.sqs.operations.SqsTemplate
import org.springframework.stereotype.Component
import java.net.ConnectException

@Component
class SummaryResponseEventPublisher(
    private val objectMapper: ObjectMapper,
    private val sqsTemplate: SqsTemplate,
    private val eventQueuesProperties: EventQueuesProperties,
) {

    fun publish(event: SummaryResponseCreatedEvent) {
        val messagePayload = objectMapper.writeValueAsString(event)
        val maxRetries = 5
        var attempt = 0

        while (true) {
            try {
                sqsTemplate.send { to ->
                    to.queue(eventQueuesProperties.summaryResponseQueue)
                        .payload(messagePayload)
                }
                logger.info { "Message sent id: ${event.id}" }
                break
            } catch (e: Exception) {
                if (e.cause is ConnectException) {
                    attempt++
                    if (attempt >= maxRetries) {
                        logger.error { "Failed to send message after $maxRetries attempts, giving up: ${event.id}" }
                        throw e
                    }
                    logger.warn { "ConnectException occurred, retrying... (attempt $attempt of $maxRetries)" }
                    Thread.sleep(1000L * attempt)
                } else {
                    logger.error { "An unexpected error occurred: ${e.message}" }
                    throw e
                }
            }
        }
    }

    fun publish(event: SummaryRerequestCreatedEvent) {

        val messagePayload = objectMapper.writeValueAsString(event)

        sqsTemplate.send { to ->
            to.queue(eventQueuesProperties.summaryRerequestQueue)
                .payload(messagePayload)
        }

        logger.info { "Re-request id: ${event.id}" }
    }
}