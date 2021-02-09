package pl.polsl.zui.piotrgazda
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.opengles.GL10
import kotlin.math.abs

class Circle(private var vertexCount: Int, private var radius: Float,
             private var angle: Float, private val ratio: Float,
             center_x1: Float, center_y1: Float
) {

    private var center_x = center_x1
        set(value) {
            field = value
        }

    private var center_y = center_y1
        set(value) {
            field = value
        }
    public fun moveCenterX(offset:Float){
        this.center_x = offset
    }
    private lateinit var totalVertices: FloatArray




    fun createCircle() {
        // Outer vertices of the circle
        //val outerVertexCount = vertexCount -1

        // Create a buffer for vertex data
        val verticesX = FloatArray(vertexCount)// (x,y) for each vertex
        val verticesY = FloatArray(vertexCount)
        // Center vertex for triangle fan
        verticesX[0] = center_x
        verticesY[0] = center_y

        for (x in 1 until vertexCount) {
            val percent: Float = (x.toFloat() / (vertexCount.toFloat() - 2.0f))
            val rad: Float = percent * 2f * Math.PI.toFloat()

            //Vertex position
            verticesX[x] = (center_x + radius * kotlin.math.cos(rad) / ratio)
            verticesY[x] = (center_y + radius * kotlin.math.sin(rad) * ratio)

            totalVertices = FloatArray(vertexCount * 2)
            for (i in 0 until vertexCount) {
                totalVertices[(i * 2)] = verticesX[i]
                totalVertices[(i * 2) + 1] = verticesY[i]
            }
        }
    }

    fun drawCircle(gl: GL10?) {

        // Draw circle as a filled shape
        //glDrawArrays(GL_TRIANGLE_FAN, 0, vertexCount)
       val buffer = ByteBuffer.allocateDirect(vertexCount * 8)
        buffer.order(ByteOrder.nativeOrder())

        val verticesBuffer = buffer.asFloatBuffer()
        verticesBuffer.put(totalVertices)
        verticesBuffer.position(0)
        gl?.glClear(GL10.GL_COLOR_BUFFER_BIT or GL10.GL_DEPTH_BUFFER_BIT)
        gl?.glColor4f(0.0f, 0.0f, 1.0f, 1.0f)
        //gl?.glRotatef(angle++, 0.0f, 0.0f, 1.0f)
        gl?.glEnableClientState(GL10.GL_VERTEX_ARRAY)
        gl?.glVertexPointer(2, GL10.GL_FLOAT, 0, verticesBuffer)
        //for(x in 0 until vertexCount - 3 step 2) {
        gl?.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, vertexCount)
        //}
        gl?.glDisableClientState(GL10.GL_VERTEX_ARRAY)


    }

  fun moveBetweenScreenEnds(translationOffset: Float, gl: GL10?,direction:Float, ratioHeightToWidth:Float):Float{

      gl?.glTranslatef(translationOffset, 0f, 0f)
      if(abs(translationOffset) + radius / ratioHeightToWidth > 1.0f / ratioHeightToWidth){
          return direction*-1.0f
      }
        return 1.0f
    }

}