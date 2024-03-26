package com.ajouin.summaryservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SummaryServiceApplication

fun main(args: Array<String>) {
	runApplication<SummaryServiceApplication>(*args)
}
