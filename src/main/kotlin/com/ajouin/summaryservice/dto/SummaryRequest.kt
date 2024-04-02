package com.ajouin.summaryservice.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class SummaryRequest @JsonCreator constructor (
    @JsonProperty("id") val id: UUID,
    @JsonProperty("content") val content: String,
)
