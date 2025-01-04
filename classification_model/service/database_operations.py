import logging
import os
from pathlib import Path

import pandas as pd
import psycopg2
from psycopg2 import sql
from psycopg2.extras import RealDictCursor

# Database connection settings remain the same
DB_HOST = os.getenv("DB_HOST", "localhost")
DB_PORT = os.getenv("DB_PORT", "5432")
DB_USER = os.getenv("DB_USER", "postgres")
DB_PASSWORD = os.getenv("DB_PASSWORD", "postgrespassword")
DB_NAME = os.getenv("DB_NAME", "healthdb")

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Rename columns to match the new database schema
# The KEYS are DATASET column names and the VALUES are the DATABASE column names
column_mapping = {
    'Disease': 'disease',
    'Fever': 'fever',
    'Cough': 'cough',
    'Fatigue': 'fatigue',
    'Difficulty Breathing': 'difficulty_breathing',
    'Age': 'age',
    'Gender': 'gender',
    'Blood Pressure': 'blood_pressure',
    'Cholesterol Level': 'cholesterol_level',
    'Outcome Variable': 'outcome_variable',
    'Headache': 'headache',
    'Sore Throat': 'sore_throat',
    'Runny Nose': 'runny_nose',
    'Temperature': 'temperature',
    'bmi': 'bmi'
}

def check_dataset_columns(dataset: pd.DataFrame):
    """
    Check if the dataset columns match the expected columns.
    """
    expected_columns = set(column_mapping.keys())
    dataset_columns = set(dataset.columns)

    if dataset_columns != expected_columns:
        missing_columns = expected_columns - dataset_columns
        extra_columns = dataset_columns - expected_columns
        logger.error(f"Dataset columns do not match the expected columns.")
        logger.error(f"Missing columns: {missing_columns}")
        logger.error(f"Extra columns: {extra_columns}")
        return False
    else:
        logger.info("Dataset columns match the expected columns.")
        return True



def import_csv_to_postgresql(csv_file_path: Path, table_name):
    """
    Imports CSV data into PostgreSQL table using the new standardized column naming convention.
    The function handles the mapping between CSV headers and database column names.
    """
    try:
        # Establish database connection
        conn = psycopg2.connect(
            host=DB_HOST,
            port=DB_PORT,
            user=DB_USER,
            password=DB_PASSWORD,
            dbname=DB_NAME
        )
        cursor = conn.cursor()
        logger.info(f"Connected to database {DB_NAME}.")

        # Read the CSV file to get the data
        df = pd.read_csv(csv_file_path)

        # Check if the dataset columns match the expected columns
        if not check_dataset_columns(df):
            raise ValueError("Dataset columns do not match the expected columns.")

        # Rename the columns in the DataFrame
        df.rename(columns=column_mapping, inplace=True)

        # Create a temporary CSV file with the correct column names
        temp_csv_path = csv_file_path.with_name(f"{csv_file_path.stem}_temp.csv")
        df.to_csv(temp_csv_path, index=False)

        # Import the data using the temporary file
        with open(temp_csv_path, 'r') as f:
            cursor.copy_expert(
                sql.SQL("COPY {} ({}) FROM STDIN WITH CSV HEADER").format(
                    sql.Identifier(table_name),
                    sql.SQL(', ').join(map(sql.Identifier, df.columns))
                ),
                f
            )

        # Remove temporary file
        os.remove(temp_csv_path)

        conn.commit()
        logger.info(f"CSV file successfully imported into table {table_name}.")

    except Exception as e:
        logger.error(f"Error while importing CSV: {e}")
        raise
    finally:
        if cursor:
            cursor.close()
        if conn:
            conn.close()


def load_database_data(database_uri, table_name):
    """
    Reads a table from the database and returns it as a Pandas DataFrame.
    The function now works with the new standardized column names.

    Args:
        database_uri (str): The connection URI for the database
        table_name (str): The name of the table to read

    Returns:
        pandas.DataFrame: The table data with standardized column names
    """
    conn = None
    try:
        # Create connection
        conn = psycopg2.connect(database_uri)
        cursor = conn.cursor(cursor_factory=RealDictCursor)

        # Query to fetch data from the table
        query = sql.SQL("SELECT * FROM {}").format(sql.Identifier(table_name))
        cursor.execute(query)

        if cursor.rowcount == 0:
            logger.error(f"No data found in table {table_name}.")
            return None

        # Fetch all rows and convert to DataFrame
        rows = cursor.fetchall()
        df = pd.DataFrame(rows)

        # Remove ID column if present
        if 'id' in df.columns:
            df.drop('id', axis=1, inplace=True)

        # Reverse the mapping for database to standardized mapping
        reverse_mapping = {v: k for k, v in column_mapping.items()}
        df.rename(columns=reverse_mapping, inplace=True)

        logger.info(f"Data loaded successfully from table {table_name}.")
        return df

    except Exception as e:
        logger.error(f"An error occurred while loading data: {e}")
        raise
    finally:
        if conn:
            conn.close()