package pl.polsl.zui.piotrgazda

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.opengl.GLES11.*
import android.opengl.GLSurfaceView
import android.opengl.GLU
import android.os.Bundle
import android.util.DisplayMetrics
import javax.microedition.khronos.opengles.GL10


class GameBoard : Activity(),  SensorEventListener
{

    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null
    private var mySurfaceView:MyGLSurfaceView? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        mySurfaceView = MyGLSurfaceView(applicationContext)
        setContentView(mySurfaceView)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    override fun onResume() {
        super.onResume()
        if (accelerometer != null) {
            sensorManager!!.registerListener(this, accelerometer, 20)//SensorManager.SENSOR_DELAY_GAME)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this, accelerometer)
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event!!.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            // In this example, alpha is calculated as t / (t + dT),
            // where t is the low-pass filter's time-constant and
            // dT is the event delivery rate.

            val alpha: Float = 0.8f
            val gravity: FloatArray = floatArrayOf(0.0f, 0.0f, -9.81f)
            val linear_acceleration: FloatArray = floatArrayOf(0.0f, 0.0f, 0.0f)

            // Isolate the force of gravity with the low-pass filter.
            gravity[0] = alpha * gravity[0] + (1 - alpha) * (event?.values?.get(0) ?: 0f)
            gravity[1] = alpha * gravity[1] + (1 - alpha) * (event?.values?.get(1) ?: 0f)
            gravity[2] = alpha * gravity[2] + (1 - alpha) * (event?.values?.get(2) ?: 0f)

            // Remove the gravity contribution with the high-pass filter.
            linear_acceleration[0] = event!!.values[0] - gravity[0]
            linear_acceleration[1] = event.values[1] - gravity[1]
            linear_acceleration[2] = event.values[2] - gravity[2]

            mySurfaceView?.setAcceleration(linear_acceleration[1]);

        }
    }
}
class MyGLSurfaceView(context: Context) : GLSurfaceView(context)
{

    private var renderer:MyRenderer? = null

    private var acceleration:Float = 0.0f

    fun setAcceleration(newAcceleration:Float){

        if(newAcceleration != 0.0f)
        {
            acceleration = -newAcceleration
        }
    }
    init
    {
        renderer = MyRenderer(context, MediaPlayer.create(context, R.raw.pilka))
        setRenderer(renderer)
    }

    inner class MyRenderer(private var myContext: Context, private var mediaPlayer:MediaPlayer ) : GLSurfaceView.Renderer
    {
        private  val metrics: DisplayMetrics = myContext.resources.displayMetrics
        private val ratio =  metrics.widthPixels.toFloat() / metrics.heightPixels.toFloat();

        private val ratioHeightToWidth = metrics.heightPixels.toFloat()/metrics.widthPixels.toFloat()

        private val angle = 0f

        private val vertexCount = 100

        private val center_x = 0.0f

        private val center_y = 0.0f

        private val radius = 0.2f * ratio

        private val myCircle = Circle(vertexCount, radius,
                angle, ratioHeightToWidth, center_x, center_y)

        private var direction = 1.0f

        private var translationOffset = 0.0f

        private var translationStep = 0.001f

        private var playHitSound = false

        private var movedSinceHit = false

        private var attenuation = 2.0f

        private var reboundDirection = false

        private var reachedEdge = false

        private var hitAcceleration = 0.0f

        init{
            myCircle.createCircle()
        }

        override fun onDrawFrame(gl: GL10?)
        {
            gl.apply {
                // Set GL_MODELVIEW transformation mode
                glMatrixMode(GL10.GL_MODELVIEW)
                glLoadIdentity()                     // reset the matrix to its default state
            }

            // When using GL_MODELVIEW, you must set the camera view
            GLU.gluLookAt(gl, 0f, 0f, -3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)

            if(playHitSound && movedSinceHit){
                mediaPlayer?.start()
                playHitSound = false
                movedSinceHit = false
            }
            translationOffset += translationStep  * acceleration
            if((translationOffset + radius / ratioHeightToWidth <= 1.0f / ratioHeightToWidth)&&(translationOffset - radius / ratioHeightToWidth >= -1.0f / ratioHeightToWidth)){
                hitAcceleration = translationOffset
                myCircle.moveCenterX(translationOffset)
                movedSinceHit = true
                playHitSound = false
                attenuation = 2.0f
            }else{
                if(attenuation < 16f ){
                        if(translationOffset + radius / ratioHeightToWidth >= 1.0f / ratioHeightToWidth) {
                            reachedEdge = false
                        }else if(translationOffset - radius / ratioHeightToWidth <= -1.0f / ratioHeightToWidth){
                            reachedEdge = true
                        }
                    if(reachedEdge) {
                        if(!reboundDirection) {
                        translationOffset -= hitAcceleration/ attenuation
                        }else{
                                translationOffset += hitAcceleration/attenuation
                            }
                    }else{
                        if(!reboundDirection) {
                            translationOffset += hitAcceleration/ attenuation
                        }else{
                            translationOffset -= hitAcceleration/attenuation //(translationStep  * acceleration)
                        }
                    }
                    if((translationOffset + radius / ratioHeightToWidth <= 1.0f / ratioHeightToWidth)
                            &&(translationOffset - radius / ratioHeightToWidth >= -1.0f / ratioHeightToWidth)) {
                        myCircle.moveCenterX(translationOffset)
                    }
                    attenuation *=2
                    reboundDirection = !reboundDirection
                }
                playHitSound = true
            }

            //na 4.5
           /* if(playHitSound && movedSinceHit){
                mediaPlayer?.start()
                playHitSound = false
                movedSinceHit = false
            }
            translationOffset += translationStep  * acceleration
            if((translationOffset + radius / ratioHeightToWidth <= 1.0f / ratioHeightToWidth)&&(translationOffset - radius / ratioHeightToWidth >= -1.0f / ratioHeightToWidth)){
                myCircle.moveCenterX(translationOffset)
                movedSinceHit = true
                playHitSound = false
            }else{
                playHitSound = true
            }*/

            myCircle.createCircle()

            //na 3.5
             /*   if(abs(translationOffset) + radius / ratioHeightToWidth > 1.0f / ratioHeightToWidth){
                    direction*=-1.0f
                }
                translationOffset += 0.01f*direction
                gl?.glTranslatef(translationOffset, 0f, 0f)*/


            gl?.glPushMatrix()
            myCircle.drawCircle(gl)
            gl?.glPopMatrix()
        }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int)
    {
        //val ratio: Float = width.toFloat() / height.toFloat()
        //metrics.widthPixels.toFloat() / metrics.heightPixels.toFloat()

          gl.apply {
           //   glViewport(0, 0, width, height)
             glViewport(0, 0, metrics.widthPixels, metrics.heightPixels)
              // make adjustments for screen ratio
            //  val ratio: Float = width.toFloat() / height.toFloat()

              glMatrixMode(GL10.GL_PROJECTION)            // set matrix to projection mode
              glLoadIdentity()                            // reset the matrix to its default state
              glFrustumf(-ratio, ratio, -1f, 1f, 3f, 7f)  // apply the projection matrix
          }
  /*
            gl?.glViewport(0, 0, width, height)
            gl?.glMatrixMode(GL10.GL_PROJECTION)
            gl?.glLoadIdentity()
            GLU.gluPerspective(gl, 45.0f, ratio, -1.0f, -10.0f)*/
          gl?.glClearColor(0.0f, 1.0f, 0.0f, 1.0f)

    }

    override fun onSurfaceCreated(gl: GL10?, config: javax.microedition.khronos.egl.EGLConfig?)
    {

    }

}
}


