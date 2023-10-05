package com.example.sera_build_4

import android.annotation.SuppressLint
import android.content.AsyncQueryHandler
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.*
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.Surface
import android.view.TextureView
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.example.sera_build_4.ml.BestFloat32
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp

class MainActivity : AppCompatActivity() {

    val paint = Paint()
    lateinit var imgProcessor: ImageProcessor
    lateinit var bmp: Bitmap
    lateinit var imgView: ImageView
    lateinit var handler: Handler
    lateinit var camMan: CameraManager
    lateinit var cameraView: TextureView
    lateinit var camDevice: CameraDevice
    lateinit var model:BestFloat32

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        get_permission()

        imgProcessor = ImageProcessor.Builder().add(ResizeOp(300,300, ResizeOp.ResizeMethod.BILINEAR)).build()

        model = BestFloat32.newInstance(this)

        val handlerThread = HandlerThread("imageThread")
        handlerThread.start()
        handler = Handler(handlerThread.looper)

        imgView = findViewById(R.id.imageView)
        cameraView = findViewById(R.id.CameraView)

        cameraView.surfaceTextureListener = object:TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(p0: SurfaceTexture, p1: Int, p2: Int) {
                open_camera()
            }

            override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture, p1: Int, p2: Int) {
            }

            override fun onSurfaceTextureDestroyed(p0: SurfaceTexture): Boolean {
                return false
            }

            // Inside the onSurfaceTextureUpdated method
            override fun onSurfaceTextureUpdated(p0: SurfaceTexture) {
                bmp = cameraView.bitmap!!

                var image = TensorImage.fromBitmap(bmp)
                image = imgProcessor.process(image)

                val outputs = model.process(image)
                val output = outputs.outputAsCategoryList

                var mute = bmp.copy(Bitmap.Config.ARGB_8888, true)
                val canv = Canvas(mute)

                // Draw rectangles for detected objects
                val objectRectPaint = Paint()
                objectRectPaint.color = Color.RED
                objectRectPaint.style = Paint.Style.STROKE
                objectRectPaint.strokeWidth = 5f

                val labelPaint = Paint()
                labelPaint.color = Color.WHITE
                labelPaint.textSize = 24f

                for (category in output) {

                    // Draw a rectangle around the detected object
                    val label = category.label
                    val x = mute.width / 2
                    val y = mute.height / 2

                    // Draw a rectangle
                    canv.drawRect(
                        (x - 50).toFloat(),
                        (y - 50).toFloat(),
                        (x + 50).toFloat(),
                        (y + 50).toFloat(),
                        objectRectPaint)

                    // Draw a label
                    canv.drawText(
                        label,
                        (x - 50).toFloat(),
                        (y - 60).toFloat(),
                        labelPaint)
                }


                // Update the ImageView with the modified bitmap
                imgView.setImageBitmap(mute)
            }
        }

            camMan = getSystemService(Context.CAMERA_SERVICE) as CameraManager

    }

    override fun onDestroy() {
        super.onDestroy()
        model.close()
    }

    @SuppressLint("MissingPermission")
    fun open_camera(){
        camMan.openCamera(camMan.cameraIdList[0], object: CameraDevice.StateCallback(){
            override fun onOpened(p0: CameraDevice) {
                camDevice = p0

                var planeView = cameraView.surfaceTexture
                var plane = Surface(planeView)

                var imagerequest = camDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                imagerequest.addTarget(plane)

                camDevice.createCaptureSession(listOf(plane),object: CameraCaptureSession.StateCallback(){
                    override fun onConfigured(p0: CameraCaptureSession) {
                        p0.setRepeatingRequest(imagerequest.build(), null, null)
                    }

                    override fun onConfigureFailed(p0: CameraCaptureSession) {
                    }
                }, handler)
            }

            override fun onDisconnected(p0: CameraDevice) {
            }

            override fun onError(p0: CameraDevice, p1: Int) {
            }
        }, handler )
    }

    fun get_permission(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(android.Manifest.permission.RECORD_AUDIO),101)
        }
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA),101)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
            get_permission()
        }
    }

}