package com.ajouin.summaryservice.event

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class SummaryResponseCreatedEvent @JsonCreator constructor (
    @JsonProperty("id") val id: Long,
    @JsonProperty("content") val content: String,
)
