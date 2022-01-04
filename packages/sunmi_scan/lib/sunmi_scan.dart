import 'dart:async';

import 'package:flutter/services.dart';
import 'package:sunmi_scan/src/scan_resolution_enum.dart';
import 'package:sunmi_scan/src/scan_result.dart';

class SunmiScan {
  static const MethodChannel _channel = MethodChannel('sunmi_scan');
  static const EventChannel _eventChannel = EventChannel('sunmi_scan/scanner');
  static late Stream<ScanResult> stream;
  static Stream<ScanResult> init() {
    stream = _eventChannel
        .receiveBroadcastStream()
        .map((event) => ScanResult.fromJson(event));
    return stream;
  }

  /// 调用扫码，需要搭配stream listen api
  ///
  /// call scan with stream listen api
  ///
  /// example:
  ///
  /// * setup a listener
  ///
  /// ```
  /// SunmiScan.stream.listen((event){
  ///   // your code here
  /// })
  /// ```
  /// * call scan
  ///
  /// ```
  /// SunmiScan.scanRaw()
  /// ```
  static Future<bool> scanRaw({
    ScanRes res = ScanRes.best,
    bool sound = true,
    bool vibrate = false,
    bool multiCodes = false,
    bool settings = false,
    bool album = false,
  }) async {
    return await _channel.invokeMethod(
          'scan',
          {
            'ppi': res.intValue,
            'sound': sound,
            'vibrate': vibrate,
            'multiCodes': multiCodes,
            'settings': settings,
            'album': album,
          },
        ) ??
        false;
  }

  /// 扫码
  ///
  /// scan code
  ///
  /// sound 声音
  /// vibrate 震动
  /// settings 显示设置
  /// album 相册
  static Future<ScanResult?> scan({
    ScanRes res = ScanRes.best,
    bool sound = true,
    bool vibrate = false,
    bool multiCodes = false,
    bool settings = false,
    bool album = false,
  }) async {
    Completer<ScanResult> _cpt = Completer<ScanResult>();
    late StreamSubscription<ScanResult> subscription;
    subscription = stream.listen((event) {
      subscription.cancel();
      _cpt.complete(event);
    });
    bool result = await scanRaw(
      res: res,
      sound: sound,
      vibrate: vibrate,
      multiCodes: multiCodes,
      settings: settings,
      album: album,
    );

    if (!result) {
      subscription.cancel();
      return null;
    }
    return _cpt.future;
  }
}
