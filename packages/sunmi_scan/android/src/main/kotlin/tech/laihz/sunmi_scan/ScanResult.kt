package tech.laihz.sunmi_scan

import com.google.gson.annotations.SerializedName

data class ScanResult(
        @SerializedName("type") val type: String,
        @SerializedName("value") val value: String
)
