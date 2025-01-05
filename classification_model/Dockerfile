# Use an official Python runtime as a parent image
FROM python:3.12-slim

# Install required system dependencies for PostgreSQL and Python build tools
RUN apt-get update && apt-get install -y \
    libpq-dev gcc \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Set the working directory in the container
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . /app

# Copy the .env file into the container (if it is in the root directory)
COPY .env .env

# Install any needed Python packages specified in requirements.txt
RUN pip install --no-cache-dir -r requirements.txt

# Make port 5500 available to the world outside this container
EXPOSE 5500

# Define environment variable
ENV FLASK_APP=service/ClassificationService.py

# Run the application
CMD ["flask", "run", "--host=0.0.0.0", "--port=5500"]