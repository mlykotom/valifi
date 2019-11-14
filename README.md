# ValiFi

[![Download](https://api.bintray.com/packages/mlykotom/maven/valifi/images/download.svg)](https://bintray.com/mlykotom/maven/valifi/_latestVersion)
[![Build Status](https://travis-ci.org/mlykotom/valifi.svg?branch=master)](https://travis-ci.org/mlykotom/valifi)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-ValiFi-blue.svg?style=flat)](https://android-arsenal.com/details/1/5153)
[![API](https://img.shields.io/badge/API-14%2B-blue.svg?style=flat)](https://android-arsenal.com/api?level=14)

* __ValiFi__ is android library for validating fields or whole forms. 
* It's working with __data binding__ and validations are visible immediately when user adds input. 
* It's highly customizable and simple for use.
* works with kotlin

<img src="https://raw.githubusercontent.com/mlykotom/valifi/master/graphics/example-single.gif" width="30%" />  <img src="https://raw.githubusercontent.com/mlykotom/valifi/master/graphics/example-form.gif" width="30%" />

# Features
* predefined fields (password, email, username, ..)
* forms for lots of fields
* custom and asynchronous validations
* option for own fields

# How to use

## Initialize the library

#### 1. Add gradle dependency
```groovy
implementation 'com.mlykotom:valifi:1.5.0'
```
#### 2. Setup project with data binding 
``` groovy
android{
    ...
    dataBinding.enabled = true
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


#### 1. Create field you want to validate
```java
public final ValiFieldEmail email = new ValiFieldEmail();
```

#### 2. Use it from layout
Library uses two-way data binding so be careful of adding android:text="__@=__{...}"

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
    android:enabled="@{viewModel.email.valid}"
    android:text="Submit" />
```

#### 3. Destroy the field
In order to prevent leaks, field __must__ be destroyed before destroying the screen!

This is easily done by calling:
```java
@Override
public void onDestroy() {
	email.destroy();
	super.onDestroy();
}
```
#### That's it! 

When user types his e-mail, it will automatically validates input and enables/disables submit button.

# For customizations information visit [Wiki](https://github.com/mlykotom/valifi/wiki)


# App Examples

1. MVVM approach (__preferred__) [here](https://github.com/mlykotom/valifi/tree/master/example-arch-viewmodel)
2. Classic fragment approach [here](https://github.com/mlykotom/valifi/tree/master/example-android)
3. __[deprecated + removed]__ MVVM approach [here](https://github.com/mlykotom/valifi/tree/b540b1fe480fcdec6fdee9816b79e862882d5835/example-viewmodel)

# License
    Copyright 2018 Tomas Mlynaric

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
