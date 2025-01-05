# Classification of medical data using machine learning

This project is a part of my bachelor thesis. The goal is to make application, which can based on the input data (symptoms
and patient profile) classify the patient's disease.

Classification output should be a list of diseases (sorted by probability) that the patient may have based on the 
input data.

This repository contains three main parts:

1. `datasets` package containing dataset used in the project and other possible options (not used in the project), it also contains a notebook with data analysis and preprocessing
3. `classifier` package which should provide one of the classification algorithms and use it to classify the data
4. `preprocessing` package which should provide preprocessing methods for the dataset
5. `service` package which should provide a REST API for the classifier

## Dependencies

- [Python 3.12](https://www.python.org/downloads/release/python-312/)
- all needed packages in `requirements.txt`

```
pip install -r ./requirements.txt
```

### Create python virtual environment (venv)

Move to the project directory and run the following commands:

1. Create a virtual environment:
```zsh
python3 -m venv venv
```

2. Activate the virtual environment:
``` zsh
source path/to/venv/bin/activate
```

3. Deactivate the virtual environment:
```zsh
deactivate
```

### Missing `libomp` on MacOS

First, install `libomp` using Homebrew:
```
brew install libomp
```

If using virtualenv (which is recommended) you add the following lines 
to the end of `/path/to/your/venv/bin/activate`:
```
export DYLD_LIBRARY_PATH="/opt/homebrew/opt/libomp/lib:$DYLD_LIBRARY_PATH"
export LIBRARY_PATH="$LIBRARY_PATH:/opt/homebrew/opt/libomp/lib"
export CPATH="$CPATH:/opt/homebrew/opt/libomp/include"
```

## To run the service

1. Entry point is `service/ClassificationService.py`
2. To insert the dataset, use this flag '--import-data'


## Docker (Run in a container)

Build docker image:
```zsh
docker build -t classification-service .
```

Run docker container:
```zsh
docker run --name classifier -p 5500:5500 classification-service
```

Stop docker container:
```zsh
docker stop classifier
```

NOTE: currently, there is a __problem__ when running the service in a container. The service is not training the model.

## Datasets

## Multiple Disease Prediction Bot

Url: https://github.com/Sudhanshu-Ambastha/Multiple-Disease-Prediction-Bot

This dataset wasn't used in the project because it does not represent the designed structure of features. It contains
only symptoms and diseases, but NOT patient profiles and health data (vitals).

## Disease Symptoms and Patient Profiles Dataset

Due to its structure, this dataset was used in the project. It contains patient profiles and health data as well.

'Disease Symptoms and Patient Profiles Dataset_exported.csv' - This dataset is a collection of patient profiles and 
their symptoms. Url: https://www.kaggle.com/datasets/uom190346a/disease-symptoms-and-patient-profile-dataset

Usage of the dataset: https://www.kaggle.com/code/priyanshusethi/classification-model-comparison-diseases

In the repository you will find the following files:
  - 'Disease Symptoms and Patient Profiles Dataset_exported.csv' - The original dataset in csv format.
  - 'Disease Symptoms and Patient Profiles Dataset_exported_updated.csv' - Dataset with filtered classes which have small amount of samples and new features.
  - '

