import logging
from flask import Flask, request, jsonify
import pandas as pd

from preprocessing.DataPreprocessor import DataPreprocessor
from classifier.Classifier import Classifier
from pathlib import Path

from dotenv import load_dotenv
load_dotenv()  # This will load the .env file manually

from config import SERVICE_VERSION

# TODO update README to be up to date

"""
Input example:
Enter values for 'Fever, Cough, Fatigue, Difficulty Breathing, Age, Gender, Blood Pressure, Cholesterol Level, Headache, Sore Throat, Runny Nose, Temperature, BMI':
Yes,No,Yes,Yes,19,Female,Low,Normal,Positive, Yes, No, No, 36.5, 20
"""

"""
Output example:
Disease probabilities:
Flu: 75.32%
Cold: 10.45%
Bronchitis: 8.15%
Angina: 6.08%
"""
# Output should in total sum up to 100%.

# Step 1: Load the dataset
url = Path("../datasets/kaggle/disease_dataset.csv")
# url = Path("/app/datasets/kaggle/Disease Symptoms and Patient Profile Dataset_balanced.csv") // For Docker
data = pd.read_csv(url)

data_preprocessing = DataPreprocessor(data)
classifier = Classifier(data_preprocessing)

# Flask server to handle evaluation requests
app = Flask(__name__)

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Route for evaluating external data
@app.route('/evaluate', methods=['POST'])
def evaluate():
    logger.info("Model evaluation request received, computing...")
    try:
        # Extract the input data (JSON format expected)
        input_data = request.get_json()
        logger.info(f"Received data: {input_data}")

        # External features (assumed to be in 'features' field)
        encoded_X = data_preprocessing.encode_input(input_data)
        logger.info(f"Encoded input: {encoded_X}")

        # Perform evaluation using the provided data
        result = classifier.predict(encoded_X)
        result['service_version'] = SERVICE_VERSION

        return jsonify(result)

    except Exception as e:
        logger.error(f"Error during evaluation: {e.with_traceback()}")
        return jsonify({"error": str(e)})

# Run the server
if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5500)