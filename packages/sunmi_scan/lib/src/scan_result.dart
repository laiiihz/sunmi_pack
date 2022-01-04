import 'dart:convert';

class ScanResult {
  final String type;
  final String value;
  ScanResult({
    required this.type,
    required this.value,
  });

  Map<String, dynamic> toMap() {
    return {
      'type': type,
      'value': value,
    };
  }

  factory ScanResult.fromMap(Map<String, dynamic> map) {
    return ScanResult(
      type: map['type'] ?? '',
      value: map['value'] ?? '',
    );
  }

  String toJson() => json.encode(toMap());

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
