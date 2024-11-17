import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestClassifier
from sklearn.ensemble import StackingClassifier
from sklearn.preprocessing import LabelEncoder, StandardScaler, OrdinalEncoder
from pathlib import Path
from sklearn.linear_model import LogisticRegression

"""
Input example:
Enter values for 'Fever, Cough, Fatigue, Difficulty Breathing, Age, Gender (0 for Male, 1 for Female), Blood Pressure, Cholesterol Level':
Yes,No,Yes,Yes,19,Female,Low,Normal,Positive
"""

"""
Output example:
Disease probabilities:
Flu: 75.32%
Cold: 10.45%
Bronchitis: 8.15%
Angina: 6.08%
"""

# Usage of dataset on kaggle: https://www.kaggle.com/code/neuromanxer/classification-model-pipeline
# Step 1: Load the dataset
url = Path("../datasets/kaggle/Disease Symptoms and Patient Profile Dataset_exported.csv")  # Replace with actual dataset path
data = pd.read_csv(url)

# Step 2: Preprocess the data
# Define the feature categories
binary_cols = ['Fever', 'Cough', 'Fatigue', 'Difficulty Breathing']
categorical_ordinal_cols = ['Blood Pressure', 'Cholesterol Level']
categorical_non_ordinal_cols = ['Gender', 'Outcome Variable']
numerical_cols = ['Age']

# Encode categorical features
label_encoder_gender = LabelEncoder()
data['Gender'] = label_encoder_gender.fit_transform(data['Gender'])  # Assuming Gender is 'Male'/'Female'

label_encoder_outcome = LabelEncoder()
data['Outcome Variable'] = label_encoder_outcome.fit_transform(data['Outcome Variable'])  # Assuming Outcome Variable is 'Yes'/'No'

label_encoder_blood_pressure = OrdinalEncoder(categories=[['Low', 'Normal', 'High']])
data['Blood Pressure'] = label_encoder_blood_pressure.fit_transform(data['Blood Pressure'].values.reshape(-1, 1))

label_encoder_cholesterol = OrdinalEncoder(categories=[['Low', 'Normal', 'High']])
data['Cholesterol Level'] = label_encoder_cholesterol.fit_transform(data['Cholesterol Level'].values.reshape(-1, 1))

# Map 'yes' to 1 and 'no' to 0
data[binary_cols] = data[binary_cols].apply(lambda col: col.map({'Yes': 1, 'No': 0}))

# Fill missing values if necessary
# data = data.fillna(0)

# Separate features (symptoms + demographics + vital signs) and target (disease)
X = data.drop(columns=['Disease'])  # Features: symptoms, demographics, vital signs, outcome variable
y = data['Disease']  # Target: 'Disease'

# Define all possible classes (replace with actual classes if known)
all_possible_classes = data['Disease'].unique()

# Fit the label encoder with all possible classes
label_encoder_y = LabelEncoder()
label_encoder_y.fit(all_possible_classes)

# Encode the target (disease labels) to ensure they are contiguous integers
y_encoded = label_encoder_y.transform(y)

# Assertions
assert len(all_possible_classes) == len(label_encoder_y.classes_), "Mismatch in number of classes"
assert X.shape[1] == len(binary_cols) + len(categorical_ordinal_cols) + len(categorical_non_ordinal_cols) + len(numerical_cols), "Mismatch in number of features"

# Step 3: Split the dataset
X_train, X_test, y_train, y_test = train_test_split(X, y_encoded, test_size=0.2, random_state=42)

# Step 4: Train the ForestClassifier model
# model = RandomForestClassifier(random_state=42)
# model.fit(X_train, y_train)

# model = xgb.XGBClassifier(use_label_encoder=False, eval_metric='mlogloss')
# model.fit(X_train, y_train)

estimators = [
    ('rf', RandomForestClassifier(n_estimators=10, random_state=42)),
]

stacking_model = StackingClassifier(estimators=estimators, final_estimator=LogisticRegression())
stacking_model.fit(X_train, y_train)


# Function to encode input values
def encode_input(input_values):
    input_dict = {
        'Fever': input_values[0],
        'Cough': input_values[1],
        'Fatigue': input_values[2],
        'Difficulty Breathing': input_values[3],
        'Age': input_values[4],
        'Gender': input_values[5],
        'Blood Pressure': input_values[6],
        'Cholesterol Level': input_values[7],
        'Outcome Variable': input_values[8],
    }

    input_dict['Fever'] = 1 if input_dict['Fever'] == 'Yes' else 0
    input_dict['Cough'] = 1 if input_dict['Cough'] == 'Yes' else 0
    input_dict['Fatigue'] = 1 if input_dict['Fatigue'] == 'Yes' else 0
    input_dict['Difficulty Breathing'] = 1 if input_dict['Difficulty Breathing'] == 'Yes' else 0
    input_dict['Gender'] = label_encoder_gender.transform([input_dict['Gender']])[0]
    input_dict['Blood Pressure'] = label_encoder_blood_pressure.transform([[input_dict['Blood Pressure']]])[0][0]
    input_dict['Cholesterol Level'] = label_encoder_cholesterol.transform([[input_dict['Cholesterol Level']]])[0][0]
    input_dict['Outcome Variable'] = label_encoder_outcome.transform([input_dict['Outcome Variable']])[0]

    return list(input_dict.values())

# Function to predict probabilities --> DDX
def predict_disease_probabilities(input_features):
    input_array = np.array(input_features).reshape(1, -1)
    probabilities = stacking_model.predict_proba(input_array)[0]
    disease_names = label_encoder_y.inverse_transform(range(len(probabilities)))
    return dict(zip(disease_names, probabilities))

if __name__ == "__main__":
    # Step 5: Wait for user input and make predictions
    while True:
        print(
            "Enter values for 'Fever, Cough, Fatigue, Difficulty Breathing, Age, Gender (0 for Male, 1 for Female), Blood Pressure, Cholesterol Level' separated by commas:")
        user_input = input()

        if user_input.strip().lower() == "exit":
            print("Exiting the program.")
            break

        input_values = [value.strip() for value in user_input.split(',')]

        try:
            encoded_input = encode_input(input_values)
            result = predict_disease_probabilities(encoded_input)
            sorted_result = dict(sorted(result.items(), key=lambda item: item[1], reverse=True))
            print("Disease probabilities:")
            for disease, prob in sorted_result.items():
                print(f"{disease}: {prob * 100:.2f}%")
        except Exception as e:
            print(f"Error: {e}. Please ensure valid inputs.")