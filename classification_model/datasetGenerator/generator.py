# Import necessary libraries
import pandas as pd
from sklearn.datasets import make_classification
from faker import Faker
import random
import symptoms # Contains a list of symptoms

# Initialize Faker to generate random patient data
fake = Faker()


def create_dataset_with_specific_features(num_records: int = 100) -> pd.DataFrame:
    """
    Creates a synthetic dataset for classification tasks with specific features with given number of records.
    @param num_records:
    @return:
    """
    data = []

    # Example list of diseases
    diseases = ['Disease A', 'Disease B', 'Disease C', 'Disease D']

    for _ in range(num_records):
        record = {}

        # Patient Demographics
        record['Age'] = random.randint(1, 100)
        record['Weight'] = random.uniform(40, 120)
        record['Height'] = random.uniform(1.5, 2.1)
        record['Gender'] = random.choice(['Male', 'Female'])
        record['Geographic Location'] = fake.country()
        record['Lifestyle'] = random.choice(['Active', 'Sedentary', 'Moderate'])

        # Symptoms (1 indicates presence, 0 indicates absence)
        for symptom in symptoms.symptoms:
            record[symptom] = random.choice([0, 1])

        # Medical Tests and Clinical Parameters
        record['Blood Pressure'] = random.choice(['Low', 'Normal', 'High'])
        record['Cholesterol'] = random.choice(['Low', 'Normal', 'High'])
        record['Blood Sugar'] = random.choice(['Low', 'Normal', 'High'])
        record['Blood Oxygen Saturation'] = random.choice(['Low', 'Normal', 'High'])
        record['Respiratory Rate'] = random.choice(['Low', 'Normal', 'High'])
        record['Heart Rate'] = random.choice(['Low', 'Normal', 'High'])
        record['Skin Temperature'] = random.choice(['Low', 'Normal', 'High'])

        # Disease label
        record['Disease'] = random.choice(diseases)

        data.append(record)

    # Convert to pandas DataFrame and save as CSV
    df = pd.DataFrame(data)
    return df

# Function to create a synthetic dataset
def create_dataset(n_samples: int = 1000, n_features: int = 20, n_classes: int = 2) -> pd.DataFrame:
    """
    Creates a synthetic dataset for classification tasks.

    Parameters:
        n_samples (int): Number of samples to generate.
        n_features (int): Number of features.
        n_classes (int): Number of target classes.

    Returns:
        pd.DataFrame: A dataset with features and a target column.
    """

    # Generate synthetic data
    X, y = make_classification(n_samples=n_samples,
                               n_features=n_features,
                               n_informative=10,  # Number of informative features
                               n_redundant=5,  # Number of redundant features
                               n_classes=n_classes,
                               random_state=42)

    # Create a DataFrame for features
    feature_names = [f"feature_{i}" for i in range(1, n_features + 1)]
    df = pd.DataFrame(X, columns=feature_names)

    # Add the target column
    df['target'] = y

    return df


if __name__ == "__main__":
    # Create a synthetic dataset
    # dataset = create_dataset(n_samples=1000, n_features=20, n_classes=2)
    dataset = create_dataset_with_specific_features(num_records=1000)

    # Display the first few rows of the dataset
    print(dataset.head())

    # Save the dataset to a CSV file
    dataset.to_csv('syntheticdatasets/synthetic_disease_dataset.csv', index=False)

