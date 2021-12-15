[![Build](https://github.com/applibgroup/nice-spinner/actions/workflows/main.yml/badge.svg)](https://github.com/applibgroup/nice-spinner/actions/workflows/main.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=applibgroup_nice-spinner&metric=alert_status)](https://sonarcloud.io/dashboard?id=applibgroup_nice-spinner)

# Nice Spinner 

## Introduction:
NiceSpinner is a nice arrow based spinner which has different way to display its content.

# Source
This library has been inspired by [arcadefire\\nice-spinner](https://github.com/arcadefire/nice-spinner).


## Usage

The usage is pretty straightforward. Add the tag into the XML layout:
```xml
<org.angmarch.views.NiceSpinner
        ohos:id="$+id:nice_spinner"
        ohos:width="match_parent"
        ohos:height="match_content"
        ohos:left_margin="16vp"
        ohos:top_margin="8vp"
        ohos:right_margin="16vp"
        ohos:background_color="#f2f2f2"
        ohos:dropDownListPaddingBottom="40"
        ohos:popupTextAlignment="0"
        ohos:arrowTint="#000000"
      />
```

 Then use this snippet to populate it with contents:
```java
 NiceSpinner nspinner = (NiceSpinner) findComponentById(ResourceTable.Id_nice_spinner);
 List<String> dataset = new LinkedList<String>(Arrays.asList("One", "Two", "Three", "Four", "Five"));
 spinner.setItems(dataset);
                      
```

#### Listeners
For listening to the item selection actions, you can just use the following snippet:
```java
spinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
    @Override
    public void onItemSelected(NiceSpinner parent, Component view, int position, CharSequence text) {
        showToast("Clicked: " + text);
    }
 });
```

### setBackgroundSelector
To set the background selector need to define selector xml in graphics and set it to the setBackgroundSelector
spinner.setBackgroundSelector(ResourceTable.Graphic_selectdialog_changecolor);

Note: xml based array is not supported.

#### Attributes
You can add attributes to customize the view. Available attributes:

| name                      | type      | info                                                   |
|------------------------   |-----------|--------------------------------------------------------|
| arrowTint                 | color     | sets the color on the drop-down arrow                  |
| hideArrow                 | boolean   | set whether show or hide the drop-down arrow           |                |
| textTint                  | color     | set the text color                                     |
| dropDownListPaddingBottom | dimension | set the bottom padding of the drop-down list           |
| popupTextAlignment        | enum      | set the horizontal alignment of the default popup text |

## Installation instructions:

```
Method 1:
    Generate the .har package through the library and add the .har package to the libs folder.
    Add the following code to the entry gradle:
        implementation fileTree  (dir: 'libs', include: ['*.jar', '*.har'])

Method 2:
    allprojects{
        repositories{
            mavenCentral()
        }
    }
implementation 'dev.applibgroup:nicespinner:1.0.0'
```
## License

    Copyright (C) 2015 Angelo Marchesin.
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
    http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
