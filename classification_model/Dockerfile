# Use an official Python runtime as a parent image
FROM python:3.12-slim

# Set the working directory in the container
WORKDIR /app

# Copy the current directory contents into the container at /app
# TODO - Copy only relevant packages
COPY . /app

# Install any needed packages specified in requirements.txt
# TODO - Add requirements_prod.txt to specify only relevant libraries
RUN pip install --no-cache-dir -r requirements.txt

# Make port 5500 available to the world outside this container
EXPOSE 5500

# Define environment variable
ENV FLASK_APP=service/ClassificationService.py

# Run the application
CMD ["flask", "run", "--host=0.0.0.0", "--port=5500"]