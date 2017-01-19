# ValiFi

[![Download](https://api.bintray.com/packages/mlykotom/maven/valifi/images/download.svg)](https://bintray.com/mlykotom/maven/valifi/_latestVersion)

* Are you tired of creating forms in app and checking all the possible inputs?
* Would you like to validate things in realtime instead of after submit?
* Are you using data binding or would like to try?

### ValiFi is for you!

__ValiFi__ is android library for validating fields or whole forms. It's working with data binding and validations are visible immediately when user adds input. It's highly customizable and simple for use.

# How to use

## Initialize the library

#### 1. Add gradle dependency
```groovy
compile 'com.mlykotom:valifi:0.3.0'
```
#### 2. Setup project with data binding 
``` groovy
android{
    ...
    dataBinding {
		enabled = true
	}
    ...
}
```

#### 3. Install the library in application class
```java
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        ValiFi.install(this);
    }
}
```

## Use in your code


#### 0. Bind your ViewModel/Fragment/etc to layout (you may already have this)
(examples uses inloop's library https://github.com/inloop/AndroidViewModel)
You may refer to Android's reference for data binding: https://developer.android.com/topic/libraries/data-binding/index.html

#### 1. Create field you want to validate
```java
public final ValiFieldEmail email = new ValiFieldEmail();
```

#### 2. Use it from layout

```xml
<android.support.design.widget.TextInputLayout
    ...
    app:errorEnabled="true"
    app:error="@{viewModel.email.error}">

    <android.support.design.widget.TextInputEditText
        ... 
        android:text="@={viewModel.email.value}"
        android:hint="E-mail address" />
</android.support.design.widget.TextInputLayout>

...

<Button
    ...
    android:enabled="@{viewModel.email.isValid}"
    android:text="Submit" />
```

#### That's it! 

When user types his e-mail, it will automatically validates input and enables/disables submit button.

# Customizations
There are plenty of options to customizate ValiFi.

## Coming soon :)

# Examples

1. MVVM approach (preferred) [here](https://github.com/mlykotom/valifi/tree/master/example-viewmodel)
2. Classic fragment approach [here](https://github.com/mlykotom/valifi/tree/master/example)

# License
    Copyright 2016 Tomas Mlynaric

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
