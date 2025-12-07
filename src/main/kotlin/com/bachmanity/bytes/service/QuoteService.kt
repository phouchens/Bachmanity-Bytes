package com.bachmanity.bytes.service

import com.bachmanity.bytes.model.Quote
import com.bachmanity.bytes.model.QuoteCollection
import kotlinx.serialization.json.Json
import java.time.LocalDate
import kotlin.random.Random

class QuoteService {
    private val quotes: List<Quote>

    init {
        val jsonString = this::class.java.classLoader
            .getResourceAsStream("quotes.json")
            ?.bufferedReader()
            ?.use { it.readText() }
            ?: throw IllegalStateException("Could not load quotes.json from resources")

        val quoteCollection = Json.decodeFromString<QuoteCollection>(jsonString)
        quotes = quoteCollection.quotes
    }

    /**
     * Get a deterministic quote based on the current date.
     * Same quote will be returned for the same date.
     */
    fun getDailyQuote(): Quote {
        val today = LocalDate.now()
        val daysSinceEpoch = today.toEpochDay()
        val index = (daysSinceEpoch % quotes.size).toInt()
        return quotes[index]
    }

    /**
     * Get a random quote.
     */
    fun getRandomQuote(): Quote {
        return quotes.random()
    }

    /**
     * Get all quotes.
     */
    fun getAllQuotes(): List<Quote> {
        return quotes
    }
}
