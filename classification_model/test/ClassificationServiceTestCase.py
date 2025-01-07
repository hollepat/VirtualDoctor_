import json
import unittest

import pandas as pd
from parameterized import parameterized

from service.ClassificationService import app, classifier


class ClassificationServiceTestCase(unittest.TestCase):

    def setUp(self):
        self.app = app.test_client()
        self.app.testing = True

        try:
            training_data = pd.read_csv('../datasets/kaggle/Disease Symptoms and Patient Profile Dataset_updated.csv')
            if training_data is None:
                raise ValueError("CSV file loaded but contains no data")
            classifier.train_model(training_data)
        except Exception as e:
            raise Exception(f"Failed to load training data: {str(e)}")

    @parameterized.expand([
        (
                "Osteoporosis",
                {
                    "age": 55,
                    "height": 170,
                    "weight": 65,
                    "gender": "FEMALE",
                    "lifestyle": "SEDENTARY",
                    "location": "EUROPE",
                    "symptoms": ["Fatigue", "Difficulty Breathing"],
                    "cholesterolLevel": 200,
                    "healthData": {
                        "Temperature": 36.5,
                        "BMI": 22.5,
                        "Blood Pressure": 80
                    }
                }
        ),
        (
                "Asthma",
                {
                    "age": 32,
                    "height": 165,
                    "weight": 55,
                    "gender": "FEMALE",
                    "lifestyle": "ACTIVE",
                    "location": "EUROPE",
                    "symptoms": ["Difficulty Breathing", "Sore Throat"],
                    "cholesterolLevel": 120,
                    "healthData": {
                        "Temperature": 39.0,
                        "BMI": 22.5,
                        "Blood Pressure": 90
                    }
                }
        ),
        (
                "Hypertension",
                {
                    "age": 35,
                    "height": 180,
                    "weight": 70,
                    "gender": "MALE",
                    "lifestyle": "ACTIVE",
                    "location": "EUROPE",
                    "symptoms": ["Fever", "Fatigue", "Headache"],
                    "cholesterolLevel": 250,
                    "healthData": {
                        "Temperature": 36.5,
                        "BMI": 22.5,
                        "Blood Pressure": 85
                    }
                }
        ),
    ])
    def test_classify_correct_classification(self, expected_disease, input_data):
        response = self.app.post('/classify',
                                 data=json.dumps(input_data),
                                 content_type='application/json')

        self.assertEqual(response.status_code, 200)
        response_data = json.loads(response.data)
        self.assertIn('service_version', response_data)
        self.assertIn('predictions', response_data)
        self.assertIn(expected_disease, response_data['predictions'].keys())

    def test_classify_empty_input(self):
        input_data = {}
        response = self.app.post('/classify',
                                 data=json.dumps(input_data),
                                 content_type='application/json')

        self.assertEqual(response.status_code, 400)
        response_data = json.loads(response.data)
        self.assertIn('error', response_data)


if __name__ == '__main__':
    unittest.main()