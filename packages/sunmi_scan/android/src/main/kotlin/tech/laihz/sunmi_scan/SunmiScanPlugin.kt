package tech.laihz.sunmi_scan

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.annotation.NonNull
import com.google.gson.Gson
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry


/** SunmiScanPlugin */
class SunmiScanPlugin: FlutterPlugin,MethodCallHandler , EventChannel.StreamHandler,ActivityAware,PluginRegistry.ActivityResultListener {
  private lateinit var channel: MethodChannel
  private lateinit var eventChannel: EventChannel
  private var sink: EventChannel.EventSink? = null

  private var hostActivity: Activity? = null
  private var hostBinding: ActivityPluginBinding? =null
  private val gson: Gson = Gson()

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "sunmi_scan")
    eventChannel = EventChannel(flutterPluginBinding.binaryMessenger, "sunmi_scan/scanner")
    channel.setMethodCallHandler(this)
    eventChannel.setStreamHandler(this)

  }

  override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
    sink = events
  }

  override fun onCancel(arguments: Any?) {
    sink?.endOfStream()
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    eventChannel.setStreamHandler(null)
    channel.setMethodCallHandler(null)
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    hostBinding = binding
    hostActivity = binding.activity
    binding.addActivityResultListener(this)
  }

  override fun onDetachedFromActivityForConfigChanges() {
    hostActivity = null
    hostBinding?.removeActivityResultListener(this)
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    hostBinding = binding
    hostActivity = binding.activity
    binding.addActivityResultListener(this)
  }

  override fun onDetachedFromActivity() {
    hostActivity = null
    hostBinding = null
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    when(call.method){
      "scan" -> {
        val ppi:Int = call.argument<Int>("ppi") ?: 3
        val sound: Boolean = call.argument<Boolean>("sound") ?: true
        val vibrate:Boolean = call.argument<Boolean>("vibrate") ?: false
        val multiCodes:Boolean = call.argument<Boolean>("multiCodes")?: false
        val showSettings: Boolean = call.argument<Boolean>("settings") ?: false
        val showAlbum:Boolean = call.argument<Boolean>("album")?: false

        val intent: Intent = Intent("com.sunmi.scan")
        intent.setPackage("com.sunmi.sunmiqrcodescanner")
        intent.putExtra("CURRENT_PPI", getScanResType(ppi).value)
        intent.putExtra("PLAY_SOUND",sound)
        intent.putExtra("PLAY_VIBRATE",vibrate)
        intent.putExtra("IDENTIFY_MORE_CODE",multiCodes)
        intent.putExtra("IS_SHOW_SETTING",showSettings)
        intent.putExtra("IS_SHOW_ALBUM",showAlbum)
        if(hostActivity ==null) result.success(false)
        else {
          hostActivity!!.startActivityForResult(intent,0x0001)
          result.success(true)
        }
      }
      else -> result.notImplemented()
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
    if (requestCode == 1 && data != null) {
      val bundle = data.extras ?: return true
      val result =  bundle.getSerializable("data") as ArrayList<*>? ?: return true
      val it = result.iterator()
      while (it.hasNext()) {
        val rawHashMap = it.next()
        if(rawHashMap is HashMap<*,*>){
          val type = rawHashMap["TYPE"]
          val value = rawHashMap["VALUE"]
          if(type is String  && value is String){
            sink?.success(gson.toJson(ScanResult(type,value)))
          }
        }
      }
    }
    return true
  }

}
