from enum import Enum

import logging
import numpy as np
from sklearn.ensemble import RandomForestClassifier
from sklearn.ensemble import StackingClassifier
from sklearn.ensemble import GradientBoostingClassifier
from sklearn.linear_model import LogisticRegression
from sklearn.model_selection import train_test_split

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
        self.stacking_model = None

        self._train_model()

    # Train the model
    def _train_model(self) -> None:
        # Step 2: Preprocess the data
        X, y, self.label_encoder_y = self.data_preprocessing.preprocess_data()

        # Step 3: Split the data into training and testing sets
        X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

        # Step 4: Define the model
        estimators = [
            ('rf', RandomForestClassifier(n_estimators=10, random_state=42)),
            ('gb', GradientBoostingClassifier(n_estimators=10, random_state=42)),
        ]

        self.stacking_model = StackingClassifier(estimators=estimators, final_estimator=LogisticRegression())

        # Step 5: Train the model
        try:
            self.stacking_model.fit(X_train, y_train)
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
            logger.error(f"Error: {e}. Please ensure valid inputs.")
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
        probabilities = self.stacking_model.predict_proba(input_array)[0]
        disease_names = self.label_encoder_y.inverse_transform(range(len(probabilities)))
        return dict(zip(disease_names, probabilities))