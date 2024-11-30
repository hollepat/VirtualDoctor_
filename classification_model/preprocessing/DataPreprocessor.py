import logging

from sklearn.preprocessing import LabelEncoder, OrdinalEncoder

# TODO add to the dataset if possible
# life_styles = ['ACTIVE', 'SEDENTARY', 'MODERATE', 'ATHLETE']
# locations = ['AFRICA', 'ASIA', 'EUROPE', 'NORTH AMERICA', 'SOUTH AMERICA', 'AUSTRALIA']

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

class DataPreprocessor:
    def __init__(self, data):
        self.data = data
        self.label_encoder_gender = LabelEncoder()
        self.label_encoder_outcome = LabelEncoder()
        self.label_encoder_blood_pressure = OrdinalEncoder(categories=[['Low', 'Normal', 'High']])
        self.label_encoder_cholesterol = OrdinalEncoder(categories=[['Low', 'Normal', 'High']])

        # Define the feature categories
        self.binary_cols = ['Fever', 'Cough', 'Fatigue', 'Difficulty Breathing', 'Headache', 'Sore Throat', 'Runny Nose']
        self.categorical_ordinal_cols = ['Blood Pressure', 'Cholesterol Level']
        self.categorical_non_ordinal_cols = ['Gender', 'Outcome Variable']
        self.numerical_cols = ['Age', 'Temperature', 'BMI']

    def preprocess_data(self) -> tuple:
        """
        Preprocess the data for the classifier.

        :returns: A tuple containing X (features), y (target), and label_encoder_y (label encoder for the target).
        :rtype: tuple
        """
        # Step 2: Preprocess the data
        # Encode categorical features
        self.data['Gender'] = self.data['Gender'].apply(lambda row: row.upper())
        self.data['Gender'] = self.label_encoder_gender.fit_transform(self.data['Gender'])  # Assuming Gender is 'Male'/'Female'

        self.data['Outcome Variable'] = self.label_encoder_outcome.fit_transform(self.data['Outcome Variable'])  # Assuming Outcome Variable is 'Yes'/'No'

        self.data['Blood Pressure'] = self.label_encoder_blood_pressure.fit_transform(self.data['Blood Pressure'].values.reshape(-1, 1))

        self.data['Cholesterol Level'] = self.label_encoder_cholesterol.fit_transform(self.data['Cholesterol Level'].values.reshape(-1, 1))

        # Map 'Yes' to 1 and 'No' to 0
        self.data[self.binary_cols] = self.data[self.binary_cols].apply(lambda col: col.map({'Yes': 1, 'No': 0}))

        # Fill missing values if necessary
        self.data = self.data.fillna(0)

        # Separate features (symptoms + demographics + vital signs) and target (disease)
        X = self.data.drop(columns=['Disease'])  # Features: symptoms, demographics, vital signs, outcome variable
        y = self.data['Disease']  # Target: 'Disease'

        # Define all possible classes (replace with actual classes if known)
        all_possible_classes = self.data['Disease'].unique()

        # Fit the label encoder with all possible classes
        label_encoder_y = LabelEncoder()
        label_encoder_y.fit(all_possible_classes)

        # Encode the target (disease labels) to ensure they are contiguous integers
        y_encoded = label_encoder_y.transform(y)

        # Assertions
        assert len(all_possible_classes) == len(label_encoder_y.classes_), "Mismatch in number of classes"
        assert X.shape[1] == len(self.binary_cols) + len(self.categorical_ordinal_cols) + len(self.categorical_non_ordinal_cols) + len(
            self.numerical_cols), "Mismatch in number of features"

        return X, y_encoded, label_encoder_y

    # Function to encode input values
    def encode_input(self, input_values: dict) -> list:

        input_dict = self._map_symptoms_to_input_dict(input_values)

        # Encode the input values
        for symptom in self.binary_cols:
            input_dict[symptom] = 1 if input_dict[symptom] == 'Yes' else 0
        input_dict['Gender'] = self.label_encoder_gender.transform([input_dict['Gender']])[0]
        input_dict['Blood Pressure'] = self.label_encoder_blood_pressure.transform([[input_dict['Blood Pressure']]])[0][0]
        input_dict['Cholesterol Level'] = self.label_encoder_cholesterol.transform([[input_dict['Cholesterol Level']]])[0][0]
        input_dict['Outcome Variable'] = self.label_encoder_outcome.transform(["Positive"])[0] # We always assume the outcome is positive

        return list(input_dict.values())

    def _map_symptoms_to_input_dict(self, json_data):
        # Initialize the input_dict with 'No' for all symptoms
        input_dict = {symptom: 'No' for symptom in self.binary_cols}
        input_dict.update({vital_sign: 'Normal' for vital_sign in self.categorical_ordinal_cols})

        # Update the input_dict based on the symptoms in the JSON data
        for symptom in json_data['symptoms']:
            if symptom in input_dict:
                input_dict[symptom] = 'Yes'

        for vital_sign in json_data['vitalSigns'].keys():
            if vital_sign in input_dict:   # If classification model considers the vital sign
                value = json_data['vitalSigns'][vital_sign]
                input_dict[vital_sign] = convert_vital_sign(vital_sign, value)

        # Add other necessary fields from the JSON data
        input_dict['Age'] = json_data['age']
        input_dict['Gender'] = json_data['gender']
        input_dict['Temperature'] = json_data['vitalSigns']['Temperature']
        input_dict['BMI'] = json_data['vitalSigns']['BMI']
        input_dict['Cholesterol Level'] = convert_vital_sign('cholesterolLevel', json_data['cholesterolLevel'])

        return input_dict

def convert_vital_sign(key, value):
    if key == "Cholesterol Level":
        return convert_cholesterol_level(value)
    elif key == "Blood Pressure":
        return convert_blood_pressure(value)
    elif key in ["Temperature", "BMI"]:
        return value
    else:
        # Add more vital signs, for now model doesn't consider them
        return "Normal"

def convert_cholesterol_level(value):
    if value < 125:
        return "Low"
    elif 125 <= value < 200:
        return "Normal"
    else:
        return "High"

def convert_blood_pressure(value):
    if value < 120:
        return "Low"
    elif 120 <= value < 140:
        return "Normal"
    else:
        return "High"
