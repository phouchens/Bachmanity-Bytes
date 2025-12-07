package com.bachmanity.bytes.model

import kotlinx.serialization.Serializable

@Serializable
data class Quote(
    val text: String,
    val character: String,
    val season: Int,
    val episode: Int
)

@Serializable
data class QuoteCollection(
    val quotes: List<Quote>
)
