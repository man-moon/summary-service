package com.ajouin.summaryservice.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "events.queues")
class EventQueuesProperties (
    val summaryRequestQueue: String,
    val summaryResponseQueue: String,
)