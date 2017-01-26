# ValiFi

[![Download](https://api.bintray.com/packages/mlykotom/maven/valifi/images/download.svg)](https://bintray.com/mlykotom/maven/valifi/_latestVersion)
[![Build Status](https://travis-ci.org/mlykotom/valifi.svg?branch=master)](https://travis-ci.org/mlykotom/valifi)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-ValiFi-blue.svg?style=flat)](https://android-arsenal.com/details/1/5153)
Close



* Are you tired of creating forms in app and checking all the possible inputs?
* Would you like to validate things in realtime instead of after submit?
* Are you using data binding or would like to try?

### ValiFi is for you!

* __ValiFi__ is android library for validating fields or whole forms. 
* It's working with data binding and validations are visible immediately when user adds input. 
* It's highly customizable and simple for use.

<img src="https://raw.githubusercontent.com/mlykotom/valifi/master/graphics/example-email.gif" width="30%" />

# How to use

## Initialize the library

#### 1. Add gradle dependency
```groovy
compile 'com.mlykotom:valifi:1.0.0'
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
You may refer to Android's reference for data binding: https://developer.android.com/topic/libraries/data-binding/index.html

(examples uses inloop's library https://github.com/inloop/AndroidViewModel)

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
    android:enabled="@{viewModel.email.isValid}"
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

Or if you have more than one field:
```java
@Override
public void onDestroy() {
	ValiFieldBase.destroyAll(email, password);
	super.onDestroy();
}
```

Or if you use form (see [Forms!](https://github.com/mlykotom/valifi#forms)):
```java
@Override
public void onDestroy() {
	form.destroy();
	super.onDestroy();
}
```

#### And That's it! 

When user types his e-mail, it will automatically validates input and enables/disables submit button.

# Customizations
There are plenty of options to customizate ValiFi.

### Globally (for whole app)
```java
public class MyApplication extends Application {
    @Override
    public void onCreate() {
	ValiFi.install(this, 
		new ValiFi.Builder()
			.setErrorResource(ValiFi.Builder.ERROR_RES_EMAIL, R.string.my_custom_email_error)
			.setPattern(ValiFi.Builder.PATTERN_EMAIL, Patterns.EMAIL_ADDRESS)
			.build()
	);
    }
}
```

### Locally (for specified field)

```java
public final ValiFieldEmail email = new ValiFieldEmail("default nullable value", "Custom error message");
```

### Specify your field by adding validators
```java
public final ValiFieldText fieldWithDifferentValidations = new ValiFieldText();

fieldWithDifferentValidations
	.addRangeLengthValidator(3, 10)
	.setEmptyAllowed(true)
	.addPatternValidator("pattern not valid", Patterns.IP_ADDRESS)
	.addCustomValidator("custom not valid", new ValiFieldBase.PropertyValidator<String>() {
		@Override
		public boolean isValid(@Nullable String value) {
			return whenThisIsValid;
		}
	});

```

## Forms!
Want to allow submit after all fields valid?

```java
public final ValiFieldEmail email = new ValiFieldEmail();
public final ValiFieldPassword password = new ValiFieldPassword();

public final ValiFiForm form = new ValiFiForm(email, password);
```

```xml
<Button
	...
	android:enabled="@{viewModel.form.isValid}"
	...
	android:text="Submit" />
```

# Examples

1. MVVM approach (preferred) [here](https://github.com/mlykotom/valifi/tree/master/example-viewmodel)
2. Classic fragment approach [here](https://github.com/mlykotom/valifi/tree/master/example)

# License
    Copyright 2017 Tomas Mlynaric

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
