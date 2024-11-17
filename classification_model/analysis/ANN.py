# Import necessary libraries
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Dense
from tensorflow.keras.optimizers import Adam

class Ann:
    def __init__(self, dataset: pd.DataFrame, target_column: str):
        self.dataset = dataset
        self.target_column = target_column

        # Step 1: Load your dataset (replace this with your own dataset)
        # For example, if you're using a CSV file:
        data = pd.read_csv('your_dataset.csv')

        # Step 2: Split data into features (X) and labels (y)
        X = data.drop('target_column', axis=1)  # Replace 'target_column' with the actual column name
        y = data['target_column']

        # Step 3: Preprocess the data
        # Normalize features
        scaler = StandardScaler()
        X_scaled = scaler.fit_transform(X)

        # Split into train and test sets
        X_train, X_test, y_train, y_test = train_test_split(X_scaled, y, test_size=0.2, random_state=42)

        # Step 4: Build the ANN model
        model = Sequential()

        # Add input layer and first hidden layer
        model.add(Dense(32, activation='relu', input_shape=(X_train.shape[1],)))  # 32 neurons in hidden layer

        # Add second hidden layer
        model.add(Dense(16, activation='relu'))  # 16 neurons in hidden layer

        # Add output layer
        model.add(Dense(1, activation='sigmoid'))  # Use 'softmax' if you have a multi-class classification problem

        # Step 5: Compile the model
        model.compile(optimizer=Adam(learning_rate=0.001), loss='binary_crossentropy', metrics=['accuracy'])

        # Step 6: Train the model
        history = model.fit(X_train, y_train, epochs=50, batch_size=32, validation_split=0.2)

        # Step 7: Make predictions
        test_loss, test_acc = model.evaluate(X_test, y_test)
        print(f'Test accuracy: {test_acc:.2f}')

