# sunmi_scan

## Getting Started

* initiate

> call `SunmiScan.init()` before scan

* scan

```dart
final result = await SunmiScan.scan()
```

| param | description | ... |
| --- | --- | --- |
| sound | play audio when scan done | `true`(by default) | 
| ppi | scan resolution | `1920x1080`, `280x720`, `BEST` (by default,best resolution match current device) |
| vibrate | vibrate when scan done | `false`(by default) | 
| settings | show settings button on the app bar | `false`(by default) | 
| album | pick qrcode from album | `false`(by default) |

* advanced usage
  * listen data from scan
    ```dart
    SunmiScan.stream.listen((event){
    // your code here
    })
    ```
  * call scan
    ```dart
    SunmiScan.scanRaw()
    ```