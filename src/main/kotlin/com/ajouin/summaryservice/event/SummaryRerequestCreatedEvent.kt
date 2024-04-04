package com.ajouin.summaryservice.event

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class SummaryRerequestCreatedEvent @JsonCreator constructor (
    @JsonProperty("id") val id: UUID,
    @JsonProperty("content") val content: String,
)
