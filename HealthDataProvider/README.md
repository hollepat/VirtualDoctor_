# Health Data Provider

Purpose: This provider is used to collect vital signs data from the patient. The data collected includes the following:
- Heart Rate
- Step Count

# Dependencies

Install the Android SDK to run the emulator.

## Configuration

This is only simulation provider, so it's running in wear os emulator. To run the emulator, you need to have 
Android SDK installed.

1. Create your own AVD by running the following command:
```
cd <path_to_android_sdk>/cmdline-tools/latest/bin
./avdmanager create avd -n wearos_avd -k "system-images;android-34;android-wear;arm64-v8a" -d "wearos_large_round"
```

You can run the emulator by running the following command:
```
cd <path_to_android_sdk>/emulator
./emulator -avd wearos_avd
```

## Running the application

To run the application you have to have get the emulator up and running. Once the emulator is running, you can run the application by 
using `play` button in the Android Studio, which should run the application on the emulator as set in `AndroidManifest.xml` file.

## FAQs

Display running emulators:
```
ps aux | grep qemu
```

Kill running emulators:
```
kill <PID>
```

Remove any leftover lock files to prevent potential issues:
```
rm -rf ~/.android/avd/wearos_avd.avd/*.lock
```