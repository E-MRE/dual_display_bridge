package com.mre.dual_display_bridge

import android.app.Activity
import android.content.Context
import android.hardware.display.DisplayManager
import android.os.Handler
import android.os.Looper
import android.view.Display
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.embedding.engine.loader.FlutterLoader
import io.flutter.FlutterInjector

class DualDisplayBridgePlugin: FlutterPlugin, MethodChannel.MethodCallHandler, ActivityAware {
    private lateinit var channel : MethodChannel
    private var activity: Activity? = null
    private var context: Context? = null
    
    private var secondaryEngine: FlutterEngine? = null
    private var presentation: SecondaryDisplayPresentation? = null

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        context = flutterPluginBinding.applicationContext
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "dual_display_bridge")
        channel.setMethodCallHandler(this)
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        val displayManager = context?.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
        
        when (call.method) {
            "getDisplays" -> {
                val displayList = displayManager.displays.map { 
                    mapOf("id" to it.displayId, "name" to it.name) 
                }
                result.success(displayList)
            }
            "showSecondaryDisplay" -> {
                val displays = displayManager.displays
                if (displays.size > 1) {
                    if (presentation == null) {
                        setupSecondaryDisplay(displays[1])
                    } else {
                        Handler(Looper.getMainLooper()).post { presentation?.show() }
                    }
                    result.success(true)
                } else {
                    result.error("NO_DISPLAY", "İkinci ekran bulunamadı", null)
                }
            }
            "hideSecondaryDisplay" -> {
                presentation?.hide()
                result.success(true)
            }
            "refreshSecondaryDisplay" -> {
                presentation?.dismiss()
                presentation = null
                secondaryEngine?.destroy()
                secondaryEngine = null
                result.success(true)
            }
            else -> result.notImplemented()
        }
    }

    private fun setupSecondaryDisplay(display: Display) {
        val act = activity ?: return
        val ctx = context ?: return

        val loader: FlutterLoader = FlutterInjector.instance().flutterLoader()
        loader.ensureInitializationComplete(ctx, null)

        secondaryEngine = FlutterEngine(ctx)
        val dartEntrypoint = DartExecutor.DartEntrypoint(loader.findAppBundlePath(), "secondaryMain")
        secondaryEngine?.dartExecutor?.executeDartEntrypoint(dartEntrypoint)

        Handler(Looper.getMainLooper()).post {
            secondaryEngine?.let { engine ->
                engine.lifecycleChannel.appIsResumed()
                presentation = SecondaryDisplayPresentation(act, display, engine)
                presentation?.show()
            }
        }
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity
    }

    override fun onDetachedFromActivityForConfigChanges() { activity = null }
    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) { activity = binding.activity }
    override fun onDetachedFromActivity() { activity = null }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
        presentation?.dismiss()
        secondaryEngine?.destroy()
    }
}