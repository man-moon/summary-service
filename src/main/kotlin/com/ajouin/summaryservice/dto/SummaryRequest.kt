package com.ajouin.summaryservice.dto

import java.util.UUID

data class SummaryRequest(
    val id: UUID,
    val content: String,
)
