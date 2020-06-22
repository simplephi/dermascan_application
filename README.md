# Skin Cancer Image Classification application (Dermiscan)
According to the World Health Organization (WHO), The incidence of both non-melanoma and melanoma skin cancers has been increasing over the past decades. Currently, between 2 and 3 million non-melanoma skin cancers and 132,000 melanoma skin cancers occur globally each year. One in every three cancers diagnosed is a skin cancer and, according to Skin Cancer Foundation Statistics, one in every five Americans will develop skin cancer in their lifetime.

## Overview

This is an application that can make it easier for doctors and the general public to be able to predict skin diseases by taking pictures of the skin with a smartphone camera.

<img src="images/2.png?raw=true" width="200px" />
<img src="images/3.png?raw=true" width="200px" />
<img src="images/4.png?raw=true" width="200px" />

<!-- TODO(b/124116863): Add app screenshot. -->

### Dataset & Model

We provide a dataset bundled in this App: Final Project Model based on
Training data from Skin Cancer dataset : ISIC Dataset

```
https://www.isic-archive.com/#!/topWithHeader/onlyHeaderTop/gallery
```

For details of the dataset used, visit [this link](https://www.isic-archive.com).

Downloading, extracting, and placing the model in the assets folder is managed
automatically by download.gradle.

### Results

Based on the results from the test model, we get the value of accuracy.

Accuracy
```
 0.8123
```

Download

For download of Dermascan Application Package File (APK), please check and visit this link
```
https://github.com/simplephi/dermascan_application/releases/tag/v1.0
```

### Techniques

We use CNN architecture and image augmentation to train models with an accuracy rate of up to 82%

## Requirements

*   Android Studio 3.2 (installed on a Linux, Mac or Windows machine)

*   Android device in
    [developer mode](https://developer.android.com/studio/debug/dev-options)
    with USB debugging enabled

*   USB cable (to connect Android device to your computer)

## Build and run

### Step 1. Clone the TensorFlow examples source code

Clone the TensorFlow examples GitHub repository to your computer to get the demo
application.

```
git clone https://github.com/simplephi/dermiscan_mobile.git
```

Open the TensorFlow source code in Android Studio. To do this, open Android

<img src="images/classifydemo_img1.png?raw=true" />

### Step 2. Build the Android Studio project

Select `Build -> Make Project` and check that the project builds successfully.
You will need Android SDK configured in the settings. You'll need at least SDK
version 23. The `build.gradle` file will prompt you to download any missing
libraries.

The file `download.gradle` directs gradle to download the two models used in the
example, placing them into `assets`.

<img src="images/classifydemo_img4.png?raw=true" style="width: 40%" />

<img src="images/classifydemo_img2.png?raw=true" style="width: 60%" />

<aside class="note"><b>Note:</b><p>`build.gradle` is configured to use
TensorFlow Lite's nightly build.</p><p>If you see a build error related to
compatibility with Tensorflow Lite's Java API (for example, `method X is
undefined for type Interpreter`), there has likely been a backwards compatible
change to the API. You will need to run `git pull` in the examples repo to
obtain a version that is compatible with the nightly build.</p></aside>

### Step 3. Install and run the app

Connect the Android device to the computer and be sure to approve any ADB
permission prompts that appear on your phone. Select `Run -> Run app.` Select
the deployment target in the connected devices to the device on which the app
will be installed. This will install the app on the device.

<img src="images/classifydemo_img5.png?raw=true" style="width: 60%" />

<img src="images/classifydemo_img6.png?raw=true" style="width: 70%" />

<img src="images/classifydemo_img7.png?raw=true" style="width: 40%" />

<img src="images/classifydemo_img8.png?raw=true" style="width: 80%" />

To test the app, open the app called `TFL Classify` on your device. When you run
the app the first time, the app will request permission to access the camera.
Re-installing the app may require you to uninstall the previous installations.

## Assets folder
_Do not delete the assets folder content_. If you explicitly deleted the
files, choose `Build -> Rebuild` to re-download the deleted model files into the
assets folder.

## Authors

* **Indra Fransiskus Alam** - *cleverdarkness@gmail.com* - [Github](https://github.com/simplephi)
* **Sangsaka Wira** - *sangsakawira@gmail.com* - [Github](https://github.com/SangsakaWira)


## Acknowledgments

* TensorFlow Lite ([TFlite](https://www.tensorflow.org/lite))
* Machine Learning Crash Courses ([MLCC](https://developers.google.com/machine-learning/crash-course/))
