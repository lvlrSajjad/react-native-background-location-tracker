
# react-native-background-location-tracker

## Getting started

`$ npm install sajjad-background-location-tracker --save`

### Mostly automatic installation

`$ react-native link sajjad-background-location-tracker`

### Manual installation


#### iOS

Not Supported Yet , Comming Soon

#### Android

1. Open up `android/app/src/main/java/[...]/MainApplication.java`
  - Add `import com.reactlibrary.sajjadBackgroundLocationTrackerPackage;` to the imports at the top of the file
  - Add `new sajjadBackgroundLocationTrackerPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':sajjad-background-location-tracker'
  	project(':sajjad-background-location-tracker').projectDir = new File(rootProject.projectDir, 	'../node_modules/sajjad-background-location-tracker/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':sajjad-background-location-tracker')
  	```


## Usage
```javascript
import BackgroundLocationTracker from 'sajjad-background-location-tracker';

// TODO: What to do with the module?
sajjadBackgroundLocationTracker;
```
  