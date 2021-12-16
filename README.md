# AppCrashTracker
Its a kind of toolkit to track the exception arising in the application and it will generate a json and can store it in your phone in txt file.

No need to worry and think to add more line of code. Simple is better. So a single invoke is enough to make better app.

<i>Online tracker coming soon!!</i>

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=applibgroup_AppCrashTracker&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=applibgroup_AppCrashTracker)
[![Build](https://github.com/applibgroup/AppCrashTracker/actions/workflows/main.yml/badge.svg)](https://github.com/applibgroup/AppCrashTracker/actions/workflows/main.yml)

# Input

```java

public class MainAbility extends Ability {
    private String writePermission =  "ohos.permission.WRITE_USER_STORAGE";

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        // Requesting permission to write on user storage
        if (verifySelfPermission(writePermission) != IBundleManager.PERMISSION_GRANTED
                && canRequestPermission(writePermission)) {
            requestPermissionsFromUser(new String[]{writePermission}, 101);
        }
        //Initializing AppCrashTracker
        ACT.init(this);

        super.setMainRoute(MainAbilitySlice.class.getName());
    }
}

```

# Output
```jsonobject
{
  "Allocated_VM_Size": "7.5 MB",
  "App_Version": "1.0.0",
  "Battery_Charging_Status": "Battery Full",
  "Battery_Charging_Via": "USB",
  "Battery_Percentage": "HIGH",
  "Brand": "Huawei",
  "Class": "MainAbility",
  "Country": "United States",
  "Device": "WGR-W09NM",
  "Device_Orientation": "Potrait",
  "External_Free_Space": "6.2 GB",
  "External_Memory_Size": "7.9 GB",
  "Height": "1221",
  "Internal_Free_Space": "6.3 GB",
  "Internal_Memory_Size": "7.9 GB",
  "Localized_Message": "For input string: \"asdf\"",
  "Message": "For input string: \"asdf\"",
  "Native_Allocated_Size": "26 MB",
  "Network_Mode": "Wifi",
  "Package_Name": "com.example.appcrashtracker",
  "Release": "",
  "SDCard_Status": "External Storage Not Supported",
  "SDK": "0",
  "Screen_Layout": "Extra Large Screen",
  "Stack_Trace": "java.lang.NumberFormatException: For input string: "asdf"
                  	at java.lang.Integer.parseInt(Integer.java:615)
                  	at java.lang.Integer.parseInt(Integer.java:650)
                  	at com.example.appcrashtracker.slice.MainAbilitySlice.crashMe(MainAbilitySlice.java:27)
                  	at com.example.appcrashtracker.slice.MainAbilitySlice.lambda$onStart$0$MainAbilitySlice(MainAbilitySlice.java:20)
                  	at com.example.appcrashtracker.slice.-$$Lambda$MainAbilitySlice$EgwfRuuCRykr_9cSmKEf3JBlac4.onClick(Unknown Source:2)
                  	at ohos.agp.window.wmc.AGPEngineAdapter.nativeDispatchTouchEvent(Native Method)
                  	at ohos.agp.window.wmc.AGPEngineAdapter.processTouchEvent(AGPEngineAdapter.java:196)
                  	at ohos.agp.window.wmc.AGPWindow.dispatchTouchEvent(AGPWindow.java:749)
                  	at ohos.aafwk.ability.AbilityWindow.dispatchTouchEvent(AbilityWindow.java:413)
                  	at ohos.aafwk.ability.Ability.dispatchTouchEvent(Ability.java:1096)
                  	at ohos.abilityshell.AbilityShellActivityDelegate.convertTouchEventThenDispatch(AbilityShellActivityDelegate.java:627)
                  	at ohos.abilityshell.AbilityShellActivity.dispatchTouchEvent(AbilityShellActivity.java:162)
                  	at com.android.internal.policy.DecorView.dispatchTouchEvent(DecorView.java:685)
                  	at android.view.View.dispatchPointerEvent(View.java:13957)
                  	at android.view.ViewRootImpl$ViewPostImeInputStage.processPointerEvent(ViewRootImpl.java:6420)
                  	at android.view.ViewRootImpl$ViewPostImeInputStage.onProcess(ViewRootImpl.java:6215)
                  	at android.view.ViewRootImpl$InputStage.deliver(ViewRootImpl.java:5604)
                  	at android.view.ViewRootImpl$InputStage.onDeliverToNext(ViewRootImpl.java:5657)
                  	at android.view.ViewRootImpl$InputStage.forward(ViewRootImpl.java:5623)
                  	at android.view.ViewRootImpl$AsyncInputStage.forward(ViewRootImpl.java:5781)
                  	at android.view.ViewRootImpl$InputStage.apply(ViewRootImpl.java:5631)
                  	at android.view.ViewRootImpl$AsyncInputStage.apply(ViewRootImpl.java:5838)
                  	at android.view.ViewRootImpl$InputStage.deliver(ViewRootImpl.java:5604)
                  	at android.view.ViewRootImpl$InputStage.onDeliverToNext(ViewRootImpl.java:5657)
                  	at android.view.ViewRootImpl$InputStage.forward(ViewRootImpl.java:5623)
                  	at android.view.ViewRootImpl$InputStage.apply(ViewRootImpl.java:5631)
                  	at android.view.ViewRootImpl$InputStage.deliver(ViewRootImpl.java:5604)
                  	at android.view.ViewRootImpl.deliverInputEvent(ViewRootImpl.java:8698)
                  	at android.view.ViewRootImpl.doProcessInputEvents(ViewRootImpl.java:8618)
                  	at android.view.ViewRootImpl.enqueueInputEvent(ViewRootImpl.java:8571)
                  	at android.view.ViewRootImpl$WindowInputEventReceiver.onInputEvent(ViewRootImpl.java:8956)
                  	at android.view.InputEventReceiver.dispatchInputEvent(InputEventReceiver.java:239)
                  	at android.os.MessageQueue.nativePollOnce(Native Method)
                  	at android.os.MessageQueue.next(MessageQueue.java:363)
                  	at android.os.Looper.loop(Looper.java:176)
                  	at android.app.ActivityThread.main(ActivityThread.java:8668)
                  	at java.lang.reflect.Method.invoke(Native Method)
                  	at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:513)
                  	at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:1109)",
  "Tablet": "true",
  "VM_Heap_Size": "9.7 MB",
  "VM_Max_Heap_Size": "2.2 MB",
  "VM_free_Heap_Size": "384 MB",
  "Width": "800"
}
```
In a crash.txt file

# Requirements

<ul>
<li>Add Gradle Dependencies</li>
<li>Init from onCreate Method with context and default class</li>
<li>Add write to user storage permission</li>
<li>Add Configuration boolean.json . Please refer <a href="https://github.com/applibgroup/AppCrashTracker/blob/main/appcrashtracker/src/main/resources/base/element/boolean.json">Configuration</a> link</li>
</ul>

# Add Gradle Dependencies
1. For using AppCrashTracker module in sample app, include the source code and add the below dependencies in entry/build.gradle to generate hap/support.har.

```
   dependencies {
      implementation project(':appcrashtracker')
      implementation fileTree(dir: 'libs', include: ['*.har'])
      testCompile 'junit:junit:4.12'
   }
```

2. For using AppCrashTracker in separate application using har file, add the har file in the entry/libs folder and add the dependencies in entry/build.gradle file.

```
   dependencies {
      implementation fileTree(dir: 'libs', include: ['*.har'])
      testCompile 'junit:junit:4.12'
   }
```

3. For using AppCrashTracker from a remote repository in separate application, add the below dependencies in entry/build.gradle file.

```
   dependencies {
      implementation 'dev.applibgroup:appcrashtracker:1.0.0'
      testCompile 'junit:junit:4.12'
   }
```

# Configuration
It is nothing much but you just need to add boolean json file in resources/base/element directory of your application to customize the data that you want as result. 
For example if you do not want to see the height and width of the device you just need to add the an boolean object with value false.
The name must be same. Below are the names of boolean value. 

```json
   {
     "boolean": [
       {
         "name": "class_name",
         "value": true
       },
       {
         "name": "message",
         "value": false
       },
       {
         "name": "localized_message",
         "value": true
       },
       {
         "name": "causes",
         "value": false
       },
       {
         "name": "stack_trace",
         "value": false
       },
       {
         "name": "brand_name",
         "value": true
       },
       {
         "name": "device_name",
         "value": false
       },
       {
         "name": "sdk_version",
         "value": false
       },
       {
         "name": "release",
         "value": false
       },
       {
         "name": "height",
         "value": true
       },
       {
         "name": "width",
         "value": true
       },
       {
         "name": "app_version",
         "value": false
       },
       {
         "name": "tablet",
         "value": false
       },
       {
         "name": "package_name",
         "value": false
       },
       {
         "name": "device_orientation",
         "value": false
       },
       {
         "name": "screen_layout",
         "value": false
       },
       {
         "name": "vm_heap_size",
         "value": false
       },
       {
         "name": "allocated_vm_size",
         "value": false
       },
       {
         "name": "vm_max_heap_size",
         "value": false
       },
       {
         "name": "vm_free_heap_size",
         "value": false
       },
       {
         "name": "native_allocated_size",
         "value": false
       },
       {
         "name": "battery_percentage",
         "value": false
       },
       {
         "name": "battery_charging",
         "value": false
       },
       {
         "name": "battery_charging_via",
         "value": false
       },
       {
         "name": "sd_card_status",
         "value": false
       },
       {
         "name": "internal_memory_size",
         "value": false
       },
       {
         "name": "external_memory_size",
         "value": false
       },
       {
         "name": "internal_free_space",
         "value": false
       },
       {
         "name": "external_free_space",
         "value": false
       },
       {
         "name": "network_mode",
         "value": false
       },
       {
         "name": "country",
         "value": false
       }
     ]
   }

```



# License
```license
Copyright 2015-2016 Ganesh Krishnamoorthy

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

```

Please give me feedback! Thanks!!


[![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/macroday/appcrashtracker/trend.png)](https://bitdeli.com/free "Bitdeli Badge")
