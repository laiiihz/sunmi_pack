import 'dart:convert';

/// scan result
///
/// 扫描结果
class ScanResult {
  /// scan type
  final String type;

  /// scan value
  final String value;
  ScanResult({
    required this.type,
    required this.value,
  });

  /// convert object to map
  Map<String, dynamic> toMap() {
    return {
      'type': type,
      'value': value,
    };
  }

  /// get object from map
  factory ScanResult.fromMap(Map<String, dynamic> map) {
    return ScanResult(
      type: map['type'] ?? '',
      value: map['value'] ?? '',
    );
  }

  /// convert object to json
  String toJson() => json.encode(toMap());

  /// get object from json string
  factory ScanResult.fromJson(String source) =>
      ScanResult.fromMap(json.decode(source));

  @override
  String toString() => 'ScanResult(type: $type, value: $value)';

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;

    return other is ScanResult && other.type == type && other.value == value;
  }

  @override
  int get hashCode => type.hashCode ^ value.hashCode;
}
