package com.example.w4_d3_ar

import android.graphics.Point
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode

class MainActivity : AppCompatActivity() {

    val TAG = "w4_d3_arcore"
    private lateinit var arFrag: ArFragment
    private var modelRenderable: ModelRenderable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_add_tree).setOnClickListener {
            Log.d(TAG, "onCreate: button clicked")
            add3dObject() }
        arFrag = supportFragmentManager.findFragmentById(R.id.sceneform_fragment) as ArFragment
        ModelRenderable.builder()
            .setSource(this, Uri.parse("untitled.gltf"))
            .setIsFilamentGltf(true).setAsyncLoadEnabled(true).setRegistryId("Hat")
            .build()
            .thenAccept { modelRenderable = it }.exceptionally {
                Log.e(TAG, "something went wrong ${it.localizedMessage}")
                null
            }
    }

    private fun add3dObject() {
        val frame = arFrag.arSceneView.arFrame
        if (frame != null && modelRenderable != null) {
            val pt = getScreenCenter()
            // get list of HitResult of the given location in the camera view
            val hits = frame.hitTest(pt.x.toFloat(), pt.y.toFloat())
            for (hit in hits) {
                val trackable = hit.trackable
                if (trackable is Plane) {
                    val anchorNode = AnchorNode(hit!!.createAnchor())
                    anchorNode.parent = arFrag.arSceneView.scene
                    val mNode = TransformableNode(arFrag.transformationSystem)
                    mNode.renderable = modelRenderable
                    mNode.scaleController.minScale = 0.02f
                    mNode.scaleController.maxScale = 0.3f
                    mNode.localScale = Vector3(0.03f, 0.03f, 0.03f)
                    mNode.parent = anchorNode
                    mNode.select()
                    break
                }
            }
        }
    }

    private fun getScreenCenter(): Point {
        val view = findViewById<View>(android.R.id.content)
        return Point(view.width / 2, view.height / 2)
    }

}