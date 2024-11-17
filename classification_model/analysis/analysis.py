import pandas as pd
from lightgbm import LGBMClassifier
from sklearn.preprocessing import StandardScaler, LabelEncoder
from sklearn.naive_bayes import GaussianNB
from sklearn.neighbors import KNeighborsClassifier
from sklearn.ensemble import RandomForestClassifier
from sklearn.neural_network import MLPClassifier
from sklearn.metrics import accuracy_score, classification_report
from path import Path
from xgboost import XGBClassifier
from sklearn import svm

from analysis import check_feature_names_match, clean_labels

train_dataset_path = Path("../datasets/train/Training.csv")
test_dataset_path = Path("../datasets/test/Testing.csv")

target_column = "prognosis"

def analysis_of_classifiers(train_dataset: Path, test_dataset: Path, target_column: str):
    """
    This function trains and tests several classifiers on a dataset.
    """

    # Load the data
    train_data = pd.read_csv(train_dataset)
    test_data = pd.read_csv(test_dataset)

    # Clean the labels in both datasets
    train_data[target_column] = clean_labels(train_data[target_column])
    test_data[target_column] = clean_labels(test_data[target_column])

    # Check if the feature names match between the training and testing datasets
    check_feature_names_match(train_data.drop(target_column, axis=1), test_data.drop(target_column, axis=1))

    # Separate features and target for training data
    X_train = train_data.drop(target_column, axis=1)
    y_train = train_data[target_column]

    # Separate features and target for testing data
    X_test = test_data.drop(target_column, axis=1)
    y_test = test_data[target_column]

    # Scale the features
    scaler = StandardScaler()
    X_train_scaled = scaler.fit_transform(X_train)
    X_test_scaled = scaler.transform(X_test)

    # Define classifiers
    classifiers = {
        'Naive Bayes': GaussianNB(),
        'K-Neighbors': KNeighborsClassifier(),
        'Random Forest': RandomForestClassifier(random_state=42),
        'XGBoost': XGBClassifier(random_state=42),
        'LightGBM': LGBMClassifier(random_state=42), # TODO - resolve Warnings
        'MLP Classifier': MLPClassifier(random_state=42, max_iter=1000), # Multi-layer Perceptron
        'SVM': svm.SVC()
    }

    # Train and test each classifier
    for name, clf in classifiers.items():
        print(f"\nTraining and testing {name}:")

        if name == 'XGBoost':
            label_encoder = LabelEncoder()
            y_train_encoded = label_encoder.fit_transform(y_train)
            y_test_encoded = label_encoder.transform(y_test)

            # Train the model with encoded labels
            clf.fit(X_train_scaled, y_train_encoded)

            # Make predictions and decode them
            y_pred_encoded = clf.predict(X_test_scaled)
            y_pred = label_encoder.inverse_transform(y_pred_encoded)
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

        # Print classification report
        # print("Classification Report:")
        # print(classification_report(y_test, y_pred))


if __name__ == "__main__":
    analysis_of_classifiers(train_dataset_path, test_dataset_path, target_column)
