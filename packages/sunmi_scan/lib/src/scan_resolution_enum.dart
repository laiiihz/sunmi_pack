/// scan resolution
enum ScanRes {
  /// 1920x1080 resolution
  res1080,

  /// 1280x720 resolution
  res720,

  /// best resolution match device
  best,
}

/// scan res extention
extension ScanResExt on ScanRes {
  /// get value from enum
  int get intValue {
    switch (this) {
      case ScanRes.res1080:
        return 1;
      case ScanRes.res720:
        return 2;
      case ScanRes.best:
        return 3;
    }
  }
}
