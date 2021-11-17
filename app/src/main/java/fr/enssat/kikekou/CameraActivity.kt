package fr.enssat.kikekou

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.pm.PackageManager
import android.graphics.*
import android.util.Log
import android.util.Size
import android.view.View
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.custom.CustomObjectDetectorOptions

//Naming convention: camera_layout.xml layout -> CameraLayoutBinding
import fr.enssat.kikekou.databinding.CameraLayoutBinding

class CameraActivity : AppCompatActivity() {
    private val REQUEST_CODE = 123456

    private lateinit var binding: CameraLayoutBinding
    private lateinit var objectDetector: ObjectDetector
    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding view element from layout
        binding =  CameraLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
     }


    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        when (requestCode) {
            REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    initCameraAndTF()
                } else {
                    Toast.makeText(this, this.getString(R.string.permissionToGrant), Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    override fun onResume() {
        super.onResume()
        //Do not forget to declare  <uses-permission android:name="android.permission.CAMERA"/> in your manifest
        if (ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            initCameraAndTF()
        } else {
            val permissions = arrayOf(android.Manifest.permission.CAMERA)
            ActivityCompat.requestPermissions(this,permissions,REQUEST_CODE)
        }
   }

    //Only the original thread that created a view can touch its views.
    private fun initCameraAndTF() = binding.previewView.post {

        // tensor flow lite model to detect object in camera preview
        // model retrieve from
        // https://tfhub.dev/google/object_detection/mobile_object_labeler_v1/1
        // store in assets folder of the project
        val TFModel = LocalModel.Builder()
            .setAssetFilePath("lite-model_object_detection_mobile_object_labeler_v1_1.tflite")
            .build()

        // Detect oject according to TF model
        // Stream mode to skip image when camera move too fast
        // Threshold confidence up to 50% to classify object according to Tensor Flow model
        val customObjectDetectorOptions =
            CustomObjectDetectorOptions.Builder(TFModel)
                .setDetectorMode(CustomObjectDetectorOptions.STREAM_MODE)
                .enableClassification()
                .setClassificationConfidenceThreshold(0.5f)
                .setMaxPerObjectLabelCount(3)
                .build()
        objectDetector = ObjectDetection.getClient(customObjectDetectorOptions)


        // future do not block
        // get() is used to get the instance of the future when available
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener( {

                val cameraProvider = cameraProviderFuture.get()

                // Set up the view finder use case to display camera preview
                val preview = Preview.Builder()
                    .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                    .build()

                // Set up the image analysis use case which will process frames in real time
            val imageAnalysis = ImageAnalysis.Builder()
                .setTargetResolution(Size(1080, 2340))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this),
                @androidx.camera.core.ExperimentalGetImage { imageProxy ->
                    val rotationDegrees = imageProxy.imageInfo.rotationDegrees
                    val image = imageProxy.image
                    if(image != null) {
                        val processImage = InputImage.fromMediaImage(image, rotationDegrees)
                        objectDetector.process(processImage)
                            .addOnFailureListener {
                                Log.e("CameraActivity","Error: $it.message")
                                imageProxy.close()
                            }.addOnSuccessListener { objects ->
                                for( it in objects) {
                                    if(binding.layout.childCount > 1)  {
                                        binding.layout.removeViewAt(1)
                                    }
                                    val element = Draw(this, it.boundingBox, it.labels.firstOrNull()?.text ?: "Undefined")
                                    binding.layout.addView(element,1)
                                }
                                imageProxy.close()
                            }
                    }
                })

                // Create a new camera selector each time, enforcing lens facing
                val cameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

                // Apply declared configs to CameraX using the same lifecycle owner
                cameraProvider.unbindAll()
                val camera = cameraProvider.bindToLifecycle(
                    this as LifecycleOwner, cameraSelector, preview, imageAnalysis)

                // Use the camera object to link our preview use case with the view
                preview.setSurfaceProvider(binding.previewView.surfaceProvider)
            },
            ContextCompat.getMainExecutor(this)
        )
      }

    private class Draw(context: Context?, var rect: Rect, var text: String) : View(context) {
        lateinit var paint: Paint
        lateinit var textPaint: Paint

        init {
            init()
        }

        private fun init() {
            paint = Paint()
            paint.color = Color.RED
            paint.strokeWidth = 20f
            paint.style = Paint.Style.STROKE

            textPaint = Paint()
            textPaint.color = Color.RED
            textPaint.style = Paint.Style.FILL
            textPaint.textSize = 80f
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            canvas.drawText(text, rect.centerX().toFloat(), rect.centerY().toFloat(), textPaint)
            canvas.drawRect(rect.left.toFloat(), rect.top.toFloat(), rect.right.toFloat(), rect.bottom.toFloat(), paint)
        }
    }
}