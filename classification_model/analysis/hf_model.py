import torch
from transformers import pipeline
import sys

# Set the device to CPU
device = torch.device("cpu")

pipe = pipeline("text-generation", model="meta-llama/Meta-Llama-3-8B", framework="pt", device=device, truncation=True)

# Generate text
# text = pipe("Once upon a time", max_length=50, num_return_sequences=5)
# print(text)

if __name__ == "__main__":
    # Step 5: Wait for user input and make predictions
    print("Enter values for 'Fever, Cough, Fatigue")
    for line in sys.stdin:
        if 'exit' == line.rstrip():
            print("Exiting the program.")
            break
        input_values = [value.strip() for value in line.split(',')]
        text = pipe("Tell a story about Fever, Cough, and Fatigue.", max_length=50, num_return_sequences=1,
                    temperature=0.7)
        print(text)

