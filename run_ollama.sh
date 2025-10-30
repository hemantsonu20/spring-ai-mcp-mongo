#!/bin/bash

update-ca-certificates

echo "Starting Ollama server..."
/usr/bin/ollama serve &

sleep 5

echo "Starting to pull the 'mistral' model..."
/usr/bin/ollama pull mistral
echo "Model 'mistral' has been pulled successfully."

wait
