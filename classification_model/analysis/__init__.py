import pandas as pd

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