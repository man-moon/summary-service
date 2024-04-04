package com.ajouin.summaryservice.publisher

import com.ajouin.summaryservice.config.EventQueuesProperties
import com.ajouin.summaryservice.event.SummaryRerequestCreatedEvent
import com.ajouin.summaryservice.event.SummaryResponseCreatedEvent
import com.ajouin.summaryservice.logger
import com.fasterxml.jackson.databind.ObjectMapper
import io.awspring.cloud.sqs.operations.SqsTemplate
import org.springframework.stereotype.Component

@Component
class SummaryResponseEventPublisher(
    private val objectMapper: ObjectMapper,
    private val sqsTemplate: SqsTemplate,
    private val eventQueuesProperties: EventQueuesProperties,
) {

    fun publish(event: SummaryResponseCreatedEvent) {

        val messagePayload = objectMapper.writeValueAsString(event)

        sqsTemplate.send { to ->
            to.queue(eventQueuesProperties.summaryResponseQueue)
                .payload(messagePayload)
        }

        logger.info { "Message sent id: ${event.id}" }
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