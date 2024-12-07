import pandas as pd
from lightgbm import LGBMClassifier
from pandas import DataFrame
from sklearn.linear_model import LogisticRegression
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler, LabelEncoder
from sklearn.naive_bayes import GaussianNB
from sklearn.neighbors import KNeighborsClassifier
from sklearn.ensemble import RandomForestClassifier, StackingClassifier, GradientBoostingClassifier
from sklearn.neural_network import MLPClassifier
from sklearn.metrics import accuracy_score, classification_report
from path import Path
from torch.backends.mkl import verbose
from xgboost import XGBClassifier
from sklearn import svm
import matplotlib.pyplot as plt

from preprocessing.DataPreprocessor import DataPreprocessor
from service.ClassificationService import data_preprocessing

# Import functions from the analysis package

dataset_path = Path("../datasets/kaggle/Disease Symptoms and Patient Profile Dataset_balanced.csv")
dataset = pd.read_csv(dataset_path)

# Split the dataset into training and testing datasets
train_dataset, test_dataset = train_test_split(dataset, test_size=0.2, random_state=42)
target_column = "Disease"

data_preprocessing = DataPreprocessor()

def analysis_of_classifiers(train_data: DataFrame, test_data: DataFrame, target_column: str):
    """
    This function trains and tests several classifiers on a dataset.
    """

    # Clean the labels in both datasets
    # train_data[target_column] = clean_labels(train_data[target_column])
    # test_data[target_column] = clean_labels(test_data[target_column])

    X_train, y_train, y_label_encoder_train = data_preprocessing.preprocess_data(train_data)
    X_test, y_test, y_label_encoder_test = data_preprocessing.preprocess_data(test_data)


    # Check if the feature names match between the training and testing datasets
    check_feature_names_match(X_train, X_test)

    # # Separate features and target for training data
    # X_train = train_data.drop(target_column, axis=1)
    # y_train = train_data[target_column]
    #
    # # Separate features and target for testing data
    # X_test = test_data.drop(target_column, axis=1)
    # y_test = test_data[target_column]

    # Scale the features
    scaler = StandardScaler()
    X_train_scaled = scaler.fit_transform(X_train)
    X_test_scaled = scaler.transform(X_test)

    # estimators = [
    #     ('LightGBM', LGBMClassifier(n_estimators=5, random_state=42)),
    #     ('gb', GradientBoostingClassifier(n_estimators=5, random_state=42)),
    # ]

    # Define classifiers
    classifiers = {
        'Naive Bayes': GaussianNB(),
        'K-Neighbors': KNeighborsClassifier(),
        'Random Forest': RandomForestClassifier(n_estimators=10, random_state=42),
        'XGBoost': XGBClassifier(n_estimators=3, random_state=42),
        'LightGBM': LGBMClassifier(n_estimators=10, random_state=42), # TODO - resolve Warnings
        'MLP Classifier': MLPClassifier(random_state=42, max_iter=1000), # Multi-layer Perceptron
        # 'Stacking Classifier': StackingClassifier(estimators=estimators, final_estimator=LogisticRegression()),
        # 'SVM': svm.SVC()
    }

    results = dict()

    # Train and test each classifier
    for name, clf in classifiers.items():
        print(f"\nTraining and testing {name}:")

        if name == 'XGBoost':
            # label_encoder = LabelEncoder()
            # y_train_encoded = label_encoder.fit_transform(y_train)
            # y_test_encoded = label_encoder.transform(y_test)

            # Train the model with encoded labels
            clf.fit(X_train_scaled, y_train)

            # Make predictions and decode them
            y_pred = clf.predict(X_test_scaled)
            # y_pred = label_encoder.inverse_transform(y_pred_encoded)
        elif name == 'LightGBM':

            # Train the model
            clf.fit(X_train_scaled, y_train)

            # Make predictions
            y_pred = clf.predict(X_test_scaled, force_col_wise=True)

        else:
            # Train the model
            clf.fit(X_train_scaled, y_train)

            # Make predictions
            y_pred = clf.predict(X_test_scaled)

        # Calculate accuracy
        accuracy = accuracy_score(y_test, y_pred)
        print(f"Accuracy: {accuracy:.4f}")

        # Save the results
        results[name] = accuracy

        # Print classification report
        # print("Classification Report:")
        # print(classification_report(y_test, y_pred))

    visualize_results_performance(results)


def visualize_results_performance(results):
    # Bar chart for classifier performance
    plt.figure(figsize=(10, 6))
    plt.bar(results.keys(), results.values(), color='skyblue')
    plt.xlabel('Classifier', fontsize=14)
    plt.ylabel('Accuracy', fontsize=14)
    plt.title('Classifier Accuracy Comparison', fontsize=16)
    plt.xticks(rotation=45)
    plt.ylim(0, 1)
    plt.tight_layout()
    plt.show()

def clean_labels(labels):
    """Strip any extra whitespace from labels."""
    return labels.str.strip()

def check_feature_names_match(X_train: pd.DataFrame, X_test: pd.DataFrame):
    """
    Check if the feature names in X_test match those in X_train.

    Parameters:
    X_train (pd.DataFrame): The training dataset used to fit the scaler.
    X_test (pd.DataFrame): The test dataset to be transformed.

    Returns:
    None: Raises an error if the feature names do not match.
    """
    train_columns = X_train.columns
    test_columns = X_test.columns

    # Find columns that are in train but not in test
    missing_in_test = set(train_columns) - set(test_columns)

    # Find columns that are in test but not in train
    extra_in_test = set(test_columns) - set(train_columns)

    if missing_in_test or extra_in_test:
        raise ValueError(f"Feature names mismatch:\n"
                         f"Missing in test dataset: {missing_in_test}\n"
                         f"Extra in test dataset: {extra_in_test}")
    else:
        print("Feature names match between training and testing datasets.")

    # Optionally, reorder the columns in X_test to match X_train
    X_test_aligned = X_test[train_columns]

    return X_test_aligned

if __name__ == "__main__":
    analysis_of_classifiers(train_dataset, test_dataset, target_column)