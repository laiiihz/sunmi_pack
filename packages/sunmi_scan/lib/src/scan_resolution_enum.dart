enum ScanRes {
  res1080,
  res720,
  best,
}

extension ScanResExt on ScanRes {
  get intValue {
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
