# Charter

![ChartView icon](https://github.com/Cuieney/BarChart-master/blob/master/bar.gif)


## Usage

Please refer to the [Resize](https://github.com/Cuieney/BarChart-master/tree/master/example "a Safari extension").
 for seeing it in action.

Shortcut to [attrs.xml](https://github.com/Cuieney/BarChart-master/blob/master/library/src/main/res/values/attrs.xml "a Safari extension").
## XML
Add library dependency to your build.gradle file then copy this code to layout

```
  <com.example.library.BarChartView
            android:id="@+id/chart"
            android:layout_marginTop="25dp"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:axisColor="@color/colorPrimary"
            app:hideGirdLine="false"
            app:barColor="@color/colorPrimary"
            app:max="300"
            app:type="line"
            app:radius="15"
            app:yAxisTxtColor="@color/colorAccent" />

```

##Developed by
cuieney

##License
```
Copyright 2017 cuieney

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
