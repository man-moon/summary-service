package com.ajouin.summaryservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class SummaryServiceApplication

fun main(args: Array<String>) {
	runApplication<SummaryServiceApplication>(*args)
}
