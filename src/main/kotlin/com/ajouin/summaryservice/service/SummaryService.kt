package com.ajouin.summaryservice.service

import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatMessage
import com.ajouin.summaryservice.dto.SummaryRequest

interface SummaryService {

    suspend fun processSummaryRequest(summaryRequest: SummaryRequest)

    suspend fun summaryContent(summaryRequest: SummaryRequest): ChatCompletion
}