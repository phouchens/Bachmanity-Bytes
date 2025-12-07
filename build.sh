#!/bin/bash

set -e

echo "🔨 Building Bachmanity Bytes..."
echo

# Set GRAALVM_HOME if not already set
if [ -z "$GRAALVM_HOME" ]; then
    export GRAALVM_HOME="/Library/Java/JavaVirtualMachines/graalvm-21.jdk/Contents/Home"
    echo "ℹ️  Using GraalVM at: $GRAALVM_HOME"
fi

# Build JAR
echo "📦 Building JAR..."
./gradlew clean build

echo
echo "🚀 Building native image (this may take a few minutes)..."
./gradlew nativeCompile

echo
echo "✅ Build complete!"
echo
echo "Native executable: build/native/nativeCompile/bachmanity-bytes"
echo "JAR file: build/libs/bachmanity-bytes-1.0.0.jar"
echo
echo "Run with: ./build/native/nativeCompile/bachmanity-bytes"
