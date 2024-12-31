import logging
import os

import pandas as pd
import psycopg2
from psycopg2 import sql
from psycopg2.extras import RealDictCursor

# Database connection settings
DB_HOST = os.getenv("DB_HOST", "localhost")
DB_PORT = os.getenv("DB_PORT", "5432")
DB_USER = os.getenv("DB_USER", "postgres")
DB_PASSWORD = os.getenv("DB_PASSWORD", "postgrespassword")
DB_NAME = os.getenv("DB_NAME", "healthdb")

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

def import_csv_to_postgresql(csv_file_path, table_name):
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

        # If table doesn't exist, load the CSV file into the table
        with open(csv_file_path, 'r') as f:
            # Skip the header row by setting HEADER = TRUE in COPY command
            cursor.copy_expert(
                sql.SQL(
                    "COPY {}(\"Disease\", \"Fever\", \"Cough\", \"Fatigue\", \"Difficulty Breathing\", \"Age\", \"Gender\", \"Blood Pressure\", \"Cholesterol Level\", \"Outcome Variable\", \"Headache\", \"Sore Throat\", \"Runny Nose\", \"Temperature\", \"bmi\") FROM STDIN WITH CSV HEADER").format(
                    sql.Identifier(table_name)),
                f
            )

        conn.commit()
        logger.info(f"CSV file successfully imported into table {table_name}.")

        cursor.close()
        conn.close()

    except Exception as e:
        print(f"Error while importing CSV: {e}")


def load_database_data(database_uri, table_name):
    """
    Reads a table from the database and returns it as a Pandas DataFrame using psycopg2.

    :param database_uri: str, the connection URI for the database
    :param table_name: str, the name of the table to read
    :return: pandas.DataFrame containing the table data
    """
    # Parse the database URI
    conn = psycopg2.connect(database_uri)

    try:
        # Create a cursor object to execute queries
        cursor = conn.cursor(cursor_factory=RealDictCursor)

        # Query to fetch data from the table
        query = f"SELECT * FROM {table_name}"
        cursor.execute(query)

        # Fetch all rows and convert to DataFrame
        rows = cursor.fetchall()
        df = pd.DataFrame(rows)

        if 'id' in df.columns:
            df.drop('id', axis=1, inplace=True)

        # Close cursor and connection
        cursor.close()
        logger.info(f"Data loaded successfully from table {table_name}.")

        return df
    except Exception as e:
        logger.error(f"An error occurred: {e}")
        return None
    finally:
        # Ensure connection is closed
        if conn:
            conn.close()
