# Classification of medical data using machine learning

This project is a part of my bachelor thesis. The goal is to make application, which can based on input data (symptoms
and patient profile) classify the patient's disease.

Classification output should be a list of diseases (sorted by probability) that the patient may have based on the 
input data.

This repository contains three main parts:

1. `analysis` package which should run different types of classification algorithms on the dataset and provide
performance metrics for each of them
2. `datasetGenerator` package which should generate a dataset from the given datasets and add more features to it
3. `classifier` package which should provide one of the classification algorithms and use it to classify the data
4. `preprocessing` package which should provide preprocessing methods for the dataset
5. `EvaluatorService` package which should provide a REST API for the classifier

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
## Docker (Run in a container)

Build docker image:
```zsh
docker build -t classification-service .
```

Run docker container:
```zsh
docker run -p 5500:5500 classification-service
```