package com.bachmanity.bytes

import com.bachmanity.bytes.model.Quote
import com.bachmanity.bytes.service.QuoteService
import com.bachmanity.bytes.web.startServer
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int

class BachmanityBytes : CliktCommand(
    name = "bachmanity-bytes",
    help = "Get your daily dose of Silicon Valley wisdom"
) {
    override fun run() = Unit
}

class DailyCommand : CliktCommand(
    name = "daily",
    help = "Get today's Silicon Valley quote"
) {
    private val verbose by option("-v", "--verbose", help = "Show episode details").flag()

    override fun run() {
        val service = QuoteService()
        val quote = service.getDailyQuote()
        printQuote(quote, verbose)
    }
}

class RandomCommand : CliktCommand(
    name = "random",
    help = "Get a random Silicon Valley quote"
) {
    private val verbose by option("-v", "--verbose", help = "Show episode details").flag()

    override fun run() {
        val service = QuoteService()
        val quote = service.getRandomQuote()
        printQuote(quote, verbose)
    }
}

class ServeCommand : CliktCommand(
    name = "serve",
    help = "Start the web UI server"
) {
    private val port by option("-p", "--port", help = "Port to run the server on").int()

    override fun run() {
        val actualPort = port ?: System.getenv("PORT")?.toIntOrNull() ?: 8080
        println("Starting Bachmanity Bytes web server on http://localhost:$actualPort")
        startServer(port)
    }
}

class DefaultCommand : CliktCommand(
    name = "bachmanity-bytes",
    help = "Get your daily dose of Silicon Valley wisdom",
    invokeWithoutSubcommand = true
) {
    private val random by option("-r", "--random", help = "Get a random quote instead of daily").flag()
    private val verbose by option("-v", "--verbose", help = "Show episode details").flag()

    override fun run() {
        val service = QuoteService()
        val quote = if (random) service.getRandomQuote() else service.getDailyQuote()
        printQuote(quote, verbose)
    }
}

fun printQuote(quote: Quote, verbose: Boolean) {
    println()
    println("\"${quote.text}\"")
    println()
    if (verbose) {
        println("  — ${quote.character} (S${quote.season}E${quote.episode})")
    } else {
        println("  — ${quote.character}")
    }
    println()
}

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        DefaultCommand().main(emptyArray())
    } else {
        BachmanityBytes()
            .subcommands(DailyCommand(), RandomCommand(), ServeCommand())
            .main(args)
    }
}
