package com.example.w4_d5_artracking

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.ar.core.*
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode

class MainActivity : AppCompatActivity() {

    private lateinit var arFrag: ArFragment
    private var viewRenderable: ViewRenderable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        arFrag = supportFragmentManager.findFragmentById(R.id.fragArImg) as ArFragment
        ViewRenderable.builder().setView(this, R.layout.rend_text).build()
            .thenAccept { viewRenderable = it }
    }

    override fun onResume() {
        super.onResume()
        arFrag.arSceneView.scene.addOnUpdateListener { frameUpdate() }
    }

    private fun frameUpdate() {
        val arFrame = arFrag.arSceneView.arFrame
        if (arFrame == null || arFrame.camera.trackingState !=
            TrackingState.TRACKING
        ) {
            Log.d("Ar tracking", "frameUpdate: return because arframe null")
            return
        }
        val updatedAugmentedImages = arFrame.getUpdatedTrackables(AugmentedImage::class.java)
        updatedAugmentedImages.forEach {
            when (it.trackingState) {
                null -> return@forEach
                TrackingState.PAUSED -> {
                    val text = "detected_img_need_more_info"
                    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
                }
                TrackingState.STOPPED -> {
                    val text = "track_stop"
                    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
                }
                TrackingState.TRACKING -> {
                    val anchors = it.anchors
                    if (anchors.isEmpty()) {
                        findViewById<ImageView>(R.id.fitToScanImg).visibility =
                            View.GONE
                        // Create anchor and anchor node in the center of the image.
                        val pose = it.centerPose
                        val anchor = it.createAnchor(pose)
                        val anchorNode = AnchorNode(anchor)
                        //Attach anchor node in the scene
                        anchorNode.parent = arFrag.arSceneView.scene
                        // Create a node as a child node of anchor node, and define node's renderable according to augmented image
                        val imgNode =
                            TransformableNode(arFrag.transformationSystem)
                        imgNode.parent = anchorNode
                        viewRenderable?.view?.findViewById<TextView>(
                            R.id.txtImgTrack)?.text = it.name
                        imgNode.localRotation = Quaternion.axisAngle(Vector3(1f, 0f, 0f), -90f)
                        imgNode.renderable = viewRenderable
                    }
                }
            }
        }
    }

}

class TrackImgFrag : ArFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = super.onCreateView(inflater, container,
            savedInstanceState)
        // Disable plane renderer and turn off instruction controller
        arSceneView.planeRenderer.isVisible = false
        arSceneView.planeRenderer.isEnabled = false
        instructionsController.isEnabled = false
        instructionsController.isVisible = false
        return view
    }

    override fun onCreateSessionConfig(session: Session?): Config {
        val config = super.onCreateSessionConfig(session)
        // Create image database and set it as a part of session configuration
        setupAugmentedImageDatabase(config, session)
        return config
    }

    private fun setupAugmentedImageDatabase(config: Config, session: Session?) {
        val augmentedImageDb = AugmentedImageDatabase(session)
        val assetManager = requireContext().assets
        listOf("mountain", "dog").forEach {
            val inputStream = assetManager.open("$it.jpg")
            val augmentedImageBitmap =
                BitmapFactory.decodeStream(inputStream)
            augmentedImageDb.addImage(it, augmentedImageBitmap, 0.1f)
            Log.d("Ar tracking", "setupAugmentedImageDatabase: read file $it")
        }
        config.augmentedImageDatabase = augmentedImageDb
    }
}

