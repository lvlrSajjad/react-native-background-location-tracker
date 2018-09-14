
# react-native-background-location-tracker [![npm version](https://img.shields.io/npm/v/sajjad-background-location-tracker.svg)](https://www.npmjs.com/package/sajjad-background-location-tracker)


this library tracks users location even when the app completely swiped. and sends the data to your backend controller.
the only way to stop the service is StopLocationService method.

## Getting started

`$ npm install sajjad-background-location-tracker --save`

### Mostly automatic installation

`$ react-native link sajjad-background-location-tracker`

### Manual installation


#### iOS

Not Supported Yet , Comming Soon

#### Android

1. Open up `android/app/src/main/java/[...]/MainApplication.java`
  - Add `import com.backgroundlocationtracker.sajjadBackgroundLocationTrackerPackage;` to the imports at the top of the file
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

you need a backend controller to receive 'application/json' type post params (Receives An AuthKey Param, Latitude Param, Longitude Param) 

set the name of params and backend controller link and other data to StartLocationService method like below example

to stop the service use StopLocationService method.


```javascript
import React, {Component} from 'react';
import {Platform, StyleSheet, Text, View, TouchableOpacity} from 'react-native';
import BackgroundLocationTracking from 'sajjad-background-location-tracker';

const instructions = Platform.select({
    ios: 'Press Cmd+R to reload,\n' + 'Cmd+D or shake for dev menu',
    android:
    'Double tap R on your keyboard to reload,\n' +
    'Shake or press menu button for dev menu',
});

type Props = {};
export default class App extends Component<Props> {
    render() {
        return (

            <View style={styles.container}>
                <TouchableOpacity
                    onPress={() => {
                        BackgroundLocationTracking.StartLocationService(
                            "AuthKey",
                            "http://www.ControllerLink.com/controller",
                            "Notification Title",
                            "Notification Subtitle",
                            "Latitude Json Param Name", 
                            "Longitude Json Param Name",
                            "AuthKey Json Param Name"
                            );
                    }}
                    style={{width: '90%', height: 36, backgroundColor: "#03A9F4", borderRadius: 4, margin: 16}}/>
                <TouchableOpacity
                    onPress={() => {
                        BackgroundLocationTracking.StopLocationService();
                    }}
                    style={{width: '90%', height: 36, backgroundColor: "#03A9F4", borderRadius: 4, margin: 16}}/>

                <Text style={styles.welcome}>Welcome to React Native!</Text>
                <Text style={styles.instructions}>To get started, edit App.js</Text>
                <Text style={styles.instructions}>{instructions}</Text>
            </View>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#F5FCFF',
    },
    welcome: {
        fontSize: 20,
        textAlign: 'center',
        margin: 10,
    },
    instructions: {
        textAlign: 'center',
        color: '#333333',
        marginBottom: 5,
    },
});

```
  
