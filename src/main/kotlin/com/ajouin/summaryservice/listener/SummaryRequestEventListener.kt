package com.ajouin.summaryservice.listener

import com.ajouin.summaryservice.dto.SummaryRequest
import com.ajouin.summaryservice.logger
import com.ajouin.summaryservice.service.SummaryService
import com.fasterxml.jackson.databind.ObjectMapper
import io.awspring.cloud.sqs.annotation.SqsListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import org.springframework.stereotype.Component

//@Component
//class SummaryRequestEventListener(
//    private val objectMapper: ObjectMapper,
//    private val summaryService: SummaryService,
//) {
//
//    private val coroutineScope = CoroutineScope(Dispatchers.IO)
//
//    @SqsListener("\${events.queues.summary-request-queue}")
//    fun receiveContentRequest(message: String) {
//
//        coroutineScope.launch {
//            val request: SummaryRequest = objectMapper.readValue(message, SummaryRequest::class.java)
//            logger.info { "Received message: ${request.id}" }
//            summaryService.processSummaryRequest(request)
//        }
//    }
//}

@Component
class SummaryRequestEventListener(
    private val objectMapper: ObjectMapper,
    private val summaryService: SummaryService,
) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val semaphore = Semaphore(permits = 10)  // 동시에 처리할 수 있는 요청 수를 10개로 제한

    @SqsListener("\${events.queues.summary-request-queue}")
    fun receiveContentRequest(message: String) {
        coroutineScope.launch {
            semaphore.withPermit {
                val request: SummaryRequest = objectMapper.readValue(message, SummaryRequest::class.java)
                logger.info { "Received message: ${request.id}" }
                summaryService.processSummaryRequest(request)
            }
        }
    }
}