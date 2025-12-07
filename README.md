# Bachmanity Bytes

> Get your daily dose of Silicon Valley wisdom

A CLI tool that delivers memorable quotes from HBO's Silicon Valley. Built with Kotlin and compiled to a native binary with GraalVM for blazing-fast startup.

## Features

- **Daily Quote**: Get a deterministic quote based on the current date (same quote per day)
- **Random Quote**: Get a random quote whenever you need inspiration
- **Episode Details**: Optional verbose mode to see which season/episode the quote is from
- **Fast Startup**: Native binary with instant startup time
- **No Dependencies**: Self-contained executable, no JVM required (when built as native image)

## Installation

### Prerequisites

- **JDK 17+** (for running JAR) or **GraalVM 22+** (for native image)
- **Gradle** (or use the included wrapper)

### Building from Source

#### Option 1: Quick Build (Recommended)

```bash
# Clone or download the project
cd bachmanity-bytes

# Run the build script
./build.sh
```

This will automatically:
- Build a runnable JAR
- Build a native image (requires GraalVM)
- Show you how to run the application

#### Option 2: Manual Build

**Build JAR:**
```bash
./gradlew build
java -jar build/libs/bachmanity-bytes-1.0.0.jar
```

**Build Native Image (requires GraalVM):**

First, set `GRAALVM_HOME` to point to your GraalVM installation:

```bash
# For one-time use
export GRAALVM_HOME="/Library/Java/JavaVirtualMachines/graalvm-21.jdk/Contents/Home"

# Or add to your shell profile (~/.zshrc or ~/.bashrc) for permanent use
echo 'export GRAALVM_HOME="/Library/Java/JavaVirtualMachines/graalvm-21.jdk/Contents/Home"' >> ~/.zshrc
```

Then build and run:

```bash
./gradlew nativeCompile
./build/native/nativeCompile/bachmanity-bytes
```

Alternatively, set it inline:

```bash
GRAALVM_HOME=/Library/Java/JavaVirtualMachines/graalvm-21.jdk/Contents/Home ./gradlew nativeCompile
```

### Installing GraalVM (Required for Native Image)

For the best performance, install GraalVM:

**macOS (Homebrew):**
```bash
brew install --cask graalvm/tap/graalvm-ce-java17
```

**SDKMAN:**
```bash
sdk install java 22.3.r17-grl
sdk use java 22.3.r17-grl
```

**Manual:**
Download from [graalvm.org](https://www.graalvm.org/downloads/)

## Usage

### Basic Usage

```bash
# Get today's quote (default behavior)
bachmanity-bytes

# Or explicitly:
bachmanity-bytes daily
```

**Output:**
```
"This guy f***s!"

  — Russ Hanneman
```

### Random Quote

```bash
# Get a random quote
bachmanity-bytes random

# Or use the flag:
bachmanity-bytes --random
bachmanity-bytes -r
```

### Verbose Mode

Show season and episode information:

```bash
bachmanity-bytes --verbose
bachmanity-bytes daily -v
bachmanity-bytes random --verbose
```

**Output:**
```
"Always Blue! Always Blue! Always Blue!"

  — Richard Hendricks (S3E10)
```

### Help

```bash
bachmanity-bytes --help
bachmanity-bytes daily --help
bachmanity-bytes random --help
```

## Examples

```bash
# Daily quote
$ bachmanity-bytes
"I don't want to live in a world where someone else makes the world a better place better than we do."
  — Gavin Belson

# Random quote with episode details
$ bachmanity-bytes random -v
"Aviato. My Aviato."
  — Erlich Bachman (S1E1)

# Daily quote with details
$ bachmanity-bytes daily --verbose
"Three comma club!"
  — Russ Hanneman (S2E1)
```

## Project Structure

```
bachmanity-bytes/
├── src/main/
│   ├── kotlin/com/bachmanity/bytes/
│   │   ├── Main.kt              # CLI entry point
│   │   ├── model/
│   │   │   └── Quote.kt         # Data models
│   │   └── service/
│   │       └── QuoteService.kt  # Quote logic
│   └── resources/
│       └── quotes.json          # Quote database (40 quotes)
├── build.gradle.kts             # Build configuration
├── build.sh                     # Build script
└── README.md
```

## Technical Details

- **Language**: Kotlin 1.9.22
- **Build Tool**: Gradle 8.5
- **CLI Framework**: Clikt 4.2.1
- **Serialization**: kotlinx-serialization-json 1.6.2
- **Compilation**: GraalVM Native Image (optional)

### Why GraalVM?

GraalVM Native Image provides:
- **Instant startup**: ~5ms vs ~500ms for JVM
- **Low memory**: ~10-50MB vs hundreds of MB
- **Single executable**: No JVM installation required
- **Native performance**: Ahead-of-time compilation

## Quote Database

The project includes 40 memorable quotes from Silicon Valley, featuring:
- Gavin Belson's philosophical musings
- Russ Hanneman's confidence
- Erlich Bachman's entrepreneurial wisdom
- Jared Dunn's concerning insights
- Jian-Yang's memorable lines
- Dinesh and Gilfoyle's banter
- And many more!

## Development

### Adding More Quotes

Edit `src/main/resources/quotes.json`:

```json
{
  "quotes": [
    {
      "text": "Your quote here",
      "character": "Character Name",
      "season": 1,
      "episode": 1
    }
  ]
}
```

### Building for Different Platforms

Native images are platform-specific. Build on:
- **macOS**: for macOS binary
- **Linux**: for Linux binary
- **Windows**: for Windows executable

Cross-compilation is not supported by GraalVM.

## License

This project is for educational and entertainment purposes.

Silicon Valley is property of HBO.

## Contributing

Feel free to submit issues or pull requests!

### Ideas for Contributions

- Add more quotes
- Add quote filtering by character
- Add quote search functionality
- Create install script
- Add shell completion
- Create web API version

---

