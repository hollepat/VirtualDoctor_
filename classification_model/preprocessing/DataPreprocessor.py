import logging

import numpy as np
from pandas import DataFrame
from sklearn.preprocessing import LabelEncoder, OrdinalEncoder

# TODO add to the dataset if possible
# life_styles = ['ACTIVE', 'SEDENTARY', 'MODERATE', 'ATHLETE']
# locations = ['AFRICA', 'ASIA', 'EUROPE', 'NORTH AMERICA', 'SOUTH AMERICA', 'AUSTRALIA']

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

min_instances = 10

class DataPreprocessor:
    def __init__(self):
        # self.data = data
        self.le_gender = LabelEncoder()
        self.le_y = LabelEncoder()

        self.label_encoder_outcome = LabelEncoder()
        # self.label_encoder_blood_pressure = OrdinalEncoder(categories=[['Low', 'Normal', 'High']])
        # self.label_encoder_cholesterol = OrdinalEncoder(categories=[['Low', 'Normal', 'High']])

        # Define the feature categories
        self.binary_cols = ['Fever', 'Cough', 'Fatigue', 'Difficulty Breathing', 'Headache', 'Sore Throat',
                            'Runny Nose']
        self.categorical_ordinal_cols = ['Blood Pressure', 'Cholesterol Level']
        self.categorical_non_ordinal_cols = ['Gender', 'Outcome Variable'] # Don't use 'Outcome Variable' for now
        self.numerical_cols = ['Age', 'Temperature', 'bmi']

        self.frequent_diseases = None

    def preprocess_data(self, data: DataFrame) -> tuple:
        """
        Preprocess the dataset for the classifier.

        :param data: Raw dataset containing the features and target.
        :returns: A tuple containing X (features) encoded, y (target) encoded, and label_encoder_y (label encoder for the target).
        :rtype: tuple
        """
        disease_counts = data['Disease'].value_counts()
        # Filter out rare diseases (less than 2 occurrences)
        self.frequent_diseases = disease_counts[disease_counts >= min_instances].index
        data_filtered = data[data['Disease'].isin(self.frequent_diseases)]
        logger.info(f"Dataset preprocessing with min_instances={min_instances}")
        logger.info(f"\nOriginal number of samples: {len(data)}")
        logger.info(f"Number of samples after filtering rare diseases: {len(data_filtered)}")
        logger.info(f"Original number of diseases: {len(disease_counts)}")
        logger.info(f"Number of diseases after filtering: {len(self.frequent_diseases)}")

        data_encoded = data_filtered.copy()

        # Encode the target variable (Disease)
        data_encoded['Disease'] = self.le_y.fit_transform(data_filtered['Disease'])

        # Encode Gender
        data_encoded['Gender'] = self.le_gender.fit_transform(data_filtered['Gender'])

        # Convert Yes/No to 1/0 for symptom columns
        bin_map = {'Yes': 1, 'No': 0}
        for col in self.binary_cols:
            data_encoded[col] = data_filtered[col].map(bin_map)

        # Convert Blood Pressure to numeric
        bp_map = {'Low': 0, 'Normal': 1, 'High': 2}
        data_encoded['Blood Pressure'] = data_filtered['Blood Pressure'].map(bp_map)

        # Convert Cholesterol Level to numeric
        chol_map = {'Low': 0, 'Normal': 1, 'High': 2}
        data_encoded['Cholesterol Level'] = data_filtered['Cholesterol Level'].map(chol_map)

        # Outcome Variable
        ov_map = {'Positive': 1, 'Negative': 0}
        data_encoded['Outcome Variable'] = data_filtered['Outcome Variable'].map(ov_map)

        # Prepare features and target
        features = self.binary_cols + self.categorical_ordinal_cols + self.categorical_non_ordinal_cols + self.numerical_cols

        X = data_encoded[features]
        y = data_encoded['Disease']

        return X, y, self.le_y


    def calculate_class_weights(self, y_train):
        """
        Get class weights for imbalanced classes.
        @return: weights for each class
        """

        class_weights = dict(zip(
            range(len(self.frequent_diseases)),
            len(y_train) / (len(self.frequent_diseases) * np.bincount(y_train))
        ))

        return class_weights

    def get_number_of_classes(self):
        return len(self.frequent_diseases)

    # Function to encode input values
    def encode_input(self, input_params: dict) -> list:

        input_dict = self._map_symptoms_to_input_dict(input_params)

        # BINARY COLUMNS (symptoms)
        bin_map = {'Yes': 1, 'No': 0}
        for symptom in self.binary_cols:
            input_dict[symptom] = bin_map[input_dict[symptom]]

        input_dict['Gender'] = self.le_gender.fit_transform([input_dict['Gender']])[0] # fit_transform expects a 1D array

        # Convert Blood Pressure to numeric
        bp_map = {'Low': 0, 'Normal': 1, 'High': 2}
        input_dict['Blood Pressure'] = bp_map[input_dict['Blood Pressure']]

        # Convert Cholesterol Level to numeric
        chol_map = {'Low': 0, 'Normal': 1, 'High': 2}
        input_dict['Cholesterol Level'] = chol_map[input_dict['Cholesterol Level']]

        # Outcome Variable when predicting if a patient has a disease --> set to 1
        input_dict['Outcome Variable'] = 1

        assert all([type(value) != list for value in input_dict.values()]), "Input dict contains lists"
        assert all([value is not None for value in input_dict.values()]), "Input dict contains None values"

        return list(input_dict.values())

    def decode_output(self, y_encoded):
        return self.le_y.inverse_transform(y_encoded)

    def _map_symptoms_to_input_dict(self, json_data):
        # Initialize the input_dict with 'No' for all symptoms
        input_dict = {symptom: 'No' for symptom in self.binary_cols}
        input_dict.update({clinical_sign: 'Normal' for clinical_sign in self.categorical_ordinal_cols})

        # Update the input_dict based on the symptoms in the JSON data
        for symptom in json_data['symptoms']:
            if symptom in input_dict:
                input_dict[symptom] = 'Yes'

        for clinical_sign in json_data['healthData'].keys():
            if clinical_sign in input_dict:  # If classification model considers the vital sign
                value = json_data['healthData'][clinical_sign]
                input_dict[clinical_sign] = convert_health_data(clinical_sign, value)

        # Add other necessary fields from the JSON data
        input_dict['Age'] = json_data['age']
        input_dict['Gender'] = json_data['gender']
        input_dict['Temperature'] = json_data['healthData']['Temperature']
        input_dict['BMI'] = json_data['healthData']['BMI']
        input_dict['Cholesterol Level'] = convert_health_data('cholesterolLevel', json_data['cholesterolLevel'], json_data['age'])

        return input_dict

def convert_health_data(key, value, age=None):
    if key == "Cholesterol Level":
        return convert_cholesterol_level(value, age)
    elif key == "Blood Pressure":
        return convert_blood_pressure(value)
    elif key in ["Temperature", "BMI"]:
        return value
    else:
        # Here add more vital signs, for now model doesn't consider them
        return "Normal"


def convert_cholesterol_level(value, age):
    """
    Categorize cholesterol level. In units of mg/dL.
    source: https://www.hopkinsmedicine.org/health/treatment-tests-and-therapies/lipid-panel#:~:text=Here%20are%20the%20ranges%20for,or%20above%20240%20mg%2FdL
    @param value: cholesterol level in mg/dL
    @return: 'Low', 'Normal', 'High' or None if value is invalid
    """
    if age < 20:
        # Child
        return child_cholesterol_level(value)
    else:
        # Adult
        return adult_cholesterol_level(value)

def adult_cholesterol_level(value):
    if 0 < value < 100:
        return "Low"
    elif 100 <= value < 160:
        return "Normal"
    elif 160 <= value:
        return "High"
    else:
        return None

def child_cholesterol_level(value):
    if 0 < value < 240:
        return "Normal"
    elif 240 <= value:
        return "High"
    else:
        return None


def convert_blood_pressure(value):
    """
    Categorize systolic blood pressure. In units of mmHg.
    source: https://www.medicinenet.com/blood_pressure_chart_reading_by_age/article.htm
    @param value: systolic blood pressure in mmHg
    @return: 'Low', 'Normal', 'High' or None if value is invalid
    """
    if 0 < value <= 90:
        return "Low"
    elif 90 < value < 130:
        return "Normal"
    elif 130 <= value:
        return "High"
    else:
        return None
