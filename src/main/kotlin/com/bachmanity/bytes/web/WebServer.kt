package com.bachmanity.bytes.web

import com.bachmanity.bytes.service.QuoteService
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.html.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*

fun startServer(port: Int? = null) {
    val actualPort = port ?: System.getenv("PORT")?.toIntOrNull() ?: 8080
    val quoteService = QuoteService()

    embeddedServer(Netty, port = actualPort) {
        install(ContentNegotiation) {
            json()
        }

        routing {
            // API endpoints
            get("/api/daily") {
                call.respond(quoteService.getDailyQuote())
            }

            get("/api/random") {
                call.respond(quoteService.getRandomQuote())
            }

            // Main UI
            get("/") {
                call.respondHtml(HttpStatusCode.OK) {
                    brutalistPage()
                }
            }
        }
    }.start(wait = true)
}

private fun HTML.brutalistPage() {
    head {
        title { +"BACHMANITY BYTES" }
        meta { charset = "utf-8" }
        meta { name = "viewport"; content = "width=device-width, initial-scale=1" }
        link {
            rel = "icon"
            href = "data:image/svg+xml,<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'><rect width='100' height='100' fill='%230a0a0a'/><text x='50' y='70' font-size='60' font-family='monospace' fill='%2300ff41' text-anchor='middle'>></text></svg>"
        }
        style {
            unsafe {
                raw(brutalistCSS)
            }
        }
    }
    body {
        div("scanlines")
        div("container") {
            h1 { +"BACHMANITY BYTES" }
            p("subtitle") { +"Daily Silicon Valley Quotes" }
            div("quote-box") {
                div("quote-text") {
                    id = "quote-text"
                    +"Loading..."
                }
                div("quote-meta") {
                    span("character") { id = "character" }
                    span("episode") { id = "episode" }
                }
            }
            div("buttons") {
                button {
                    id = "daily-btn"
                    +"[ DAILY ]"
                }
                button {
                    id = "random-btn"
                    +"[ RANDOM ]"
                }
            }
        }
        script {
            unsafe {
                raw(clientScript)
            }
        }
    }
}

private val brutalistCSS = """
@import url('https://fonts.googleapis.com/css2?family=VT323&display=swap');

* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

body {
    background: #0a0a0a;
    color: #00ff41;
    font-family: 'VT323', 'Courier New', monospace;
    min-height: 100vh;
    display: flex;
    justify-content: center;
    align-items: center;
    overflow: hidden;
}

.scanlines {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    pointer-events: none;
    background: repeating-linear-gradient(
        0deg,
        rgba(0, 0, 0, 0.15),
        rgba(0, 0, 0, 0.15) 1px,
        transparent 1px,
        transparent 2px
    );
    z-index: 1000;
}

.container {
    max-width: 800px;
    padding: 40px;
    border: 4px solid #00ff41;
    background: #0a0a0a;
    position: relative;
}

.container::before {
    content: '';
    position: absolute;
    top: -2px;
    left: -2px;
    right: -2px;
    bottom: -2px;
    border: 2px solid #ff4444;
    pointer-events: none;
}

h1 {
    font-size: 3rem;
    text-align: center;
    margin-bottom: 10px;
    letter-spacing: 8px;
}

.subtitle {
    text-align: center;
    font-size: 1.2rem;
    color: #666;
    margin-bottom: 40px;
    letter-spacing: 4px;
}

.quote-box {
    border: 2px solid #00ff41;
    padding: 30px;
    margin-bottom: 30px;
    background: rgba(0, 255, 65, 0.05);
    min-height: 200px;
    display: flex;
    flex-direction: column;
    justify-content: center;
}

.quote-text {
    font-size: 1.8rem;
    line-height: 1.6;
    margin-bottom: 20px;
}

.quote-text::before {
    content: '> ';
    color: #ff4444;
}

.quote-meta {
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-top: 1px solid #00ff41;
    padding-top: 15px;
    font-size: 1.4rem;
}

.character {
    color: #00ffff;
}

.character::before {
    content: '// ';
    color: #666;
}

.episode {
    color: #ff4444;
}

.buttons {
    display: flex;
    gap: 20px;
    justify-content: center;
}

button {
    background: transparent;
    border: 2px solid #00ff41;
    color: #00ff41;
    font-family: 'VT323', 'Courier New', monospace;
    font-size: 1.5rem;
    padding: 15px 30px;
    cursor: pointer;
    transition: all 0.1s;
}

button:hover {
    background: #00ff41;
    color: #0a0a0a;
}

button:active {
    transform: scale(0.98);
}

#random-btn {
    border-color: #ff4444;
    color: #ff4444;
}

#random-btn:hover {
    background: #ff4444;
    color: #0a0a0a;
}
""".trimIndent()

private val clientScript = """
async function fetchQuote(type) {
    try {
        const response = await fetch('/api/' + type);
        const quote = await response.json();

        document.getElementById('quote-text').textContent = quote.text;
        document.getElementById('character').textContent = quote.character;
        document.getElementById('episode').textContent = 'S' + quote.season + 'E' + quote.episode;
    } catch (error) {
        document.getElementById('quote-text').textContent = 'ERROR: Failed to load quote';
    }
}

document.getElementById('daily-btn').addEventListener('click', () => fetchQuote('daily'));
document.getElementById('random-btn').addEventListener('click', () => fetchQuote('random'));

// Load daily quote on page load
fetchQuote('daily');
""".trimIndent()
