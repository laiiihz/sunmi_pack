package tech.laihz.sunmi_scan

enum class ScanResType(val value:Int) {
    Res1080(0x0001),
    Res720(0x0002),
    Best(0x0003),
}

fun getScanResType(value: Int?): ScanResType {
    return when (value) {
        1 -> ScanResType.Res1080
        2 -> ScanResType.Res720
        3 -> ScanResType.Best
        else -> ScanResType.Best
    }
}