package com.mre.dual_display_bridge

import android.app.Presentation
import android.content.Context
import android.os.Bundle
import android.view.Display
import io.flutter.embedding.android.FlutterView
import io.flutter.embedding.engine.FlutterEngine

class SecondaryDisplayPresentation(context: Context, display: Display, private val engine: FlutterEngine) : 
    Presentation(context, display) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val flutterView = FlutterView(context)
        flutterView.attachToFlutterEngine(engine)
        setContentView(flutterView)
    }
}