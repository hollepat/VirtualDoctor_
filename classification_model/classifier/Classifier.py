import logging
from enum import Enum

import numpy as np
from sklearn.model_selection import train_test_split
from tqdm import tqdm
from xgboost import XGBClassifier

from preprocessing.DataPreprocessor import DataPreprocessor

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

class ClassifierStatus(Enum):
    NOT_TRAINED = "Model has not been trained"
    TRAINED = "Model is trained"
    ERROR = "Error in model training or prediction"

# Classification Model Class
class Classifier:
    def __init__(self, data_preprocessing: DataPreprocessor):
        self.data_preprocessing = data_preprocessing
        self.status = ClassifierStatus.NOT_TRAINED

        # For prediction
        self.label_encoder_y = None
        self.model = None

    # Train the model
    def train_model(self, data) -> None:
        # Step 2: Preprocess the data
        try:
            X, y, self.label_encoder_y = self.data_preprocessing.preprocess_data(data)
        except Exception as e:
            logger.error(f"Error during data preprocessing: {e}")
            self.status = ClassifierStatus.ERROR
            return

        # Step 3: Split the data into training and testing sets with stratification
        X_train, X_test, y_train, y_test = train_test_split(
            X, y,
            test_size=0.2,
            random_state=42,
            stratify=y
        )

        # Calculate class weights to handle imbalance
        class_weights = self.data_preprocessing.calculate_class_weights(y_train)

        # Initialize XGBoost classifier
        n_classes = self.data_preprocessing.get_number_of_classes()
        print(f"\nTraining model with {n_classes} diseases")

        # Step 4: Define the model
        self.model = XGBClassifier(
            objective='multi:softprob',
            num_class=n_classes,
            learning_rate=0.1,
            max_depth=6,
            n_estimators=5,
            random_state=42,
            eval_metric=['mlogloss'],
            early_stopping_rounds=10,
            # scale_pos_weight=1,  # This parameter is used for binary classification, but I do multi-class as is set by object='multi:softprob'
            min_child_weight=1,  # Help with class imbalance
            subsample=0.8,  # Prevent overfitting
            colsample_bytree=0.8  # Prevent overfitting
        )

        # Step 5: Train the model
        try:
            tqdm(self.model.fit(
                X_train,
                y_train,
                eval_set=[(X_test, y_test)],
                verbose=True,
                # Convert class weights to sample weights
                sample_weight=np.array([class_weights[y] for y in y_train])
            ), desc="Training model")
            logger.info(f"Model training completed successfully with {X_train.shape[1]} features.")
        except Exception as e:
            logger.error(f"Error during model training: {e}")
            self.status = ClassifierStatus.ERROR
            return

        self.status = ClassifierStatus.TRAINED

    # Evaluate the external data
    def predict(self, encoded_features) -> dict:
        if self.status is not ClassifierStatus.TRAINED:
            logger.error("Model has not been trained. Please train the model before making predictions.")
            return {"error": "Model has not been trained. Please train the model before making predictions."}

        try:
            result = self._predict_disease_probabilities(encoded_features)

            # Sort the results in descending order just for better readability
            sorted_ddx = dict(sorted(result.items(), key=lambda item: item[1], reverse=True))

            logger.info("Disease probabilities:")
            for disease, prob in sorted_ddx.items():
                logger.info(f"{disease}: {prob * 100:.2f}%")

            # Round percentage to 4 decimal places
            rounded_ddx = {disease: round(prob * 100, 2) for disease, prob in sorted_ddx.items()}

            return {"predictions": rounded_ddx}
        except Exception as e:
            logger.error(f"Error during prediction: {e}. Please ensure valid inputs.")
            return {"error": str(e)}

    # Function to predict probabilities --> DDX
    def _predict_disease_probabilities(self, input_features) -> dict:
        """
        Predict disease probabilities based on encoded input features.

        :param input_features: Encoded input features for prediction.
        :type input_features: list
        :return: A dictionary mapping disease names to their predicted probabilities.
        :rtype: dict
        """
        input_array = np.array(input_features).reshape(1, -1)
        probabilities = self.model.predict_proba(input_array)[0]
        disease_names = self.label_encoder_y.inverse_transform(range(len(probabilities)))
        return dict(zip(disease_names, probabilities))