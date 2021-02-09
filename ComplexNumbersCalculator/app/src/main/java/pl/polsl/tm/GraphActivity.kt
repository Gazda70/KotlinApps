package pl.polsl.tm


import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.PointsGraphSeries
import kotlin.math.abs


class GraphActivity : AppCompatActivity() {

    var realis = 0.0
    var imaginaris = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)

        realis = intent.getDoubleExtra(REALIS, 0.0)
        imaginaris = intent.getDoubleExtra(IMAGINARIS, 0.0)

        val graph = findViewById<View>(R.id.gaussianGraph) as GraphView

        setViewportX(graph)
        setViewportY(graph)

        val series: PointsGraphSeries<DataPoint> = PointsGraphSeries<DataPoint>(
            arrayOf<DataPoint>(
                DataPoint(realis, imaginaris)
            )
        )
        graph.addSeries(series)
        series.shape = PointsGraphSeries.Shape.POINT
        series.color = Color.BLUE
    }

    private fun setViewportX(graph:GraphView){
        graph.viewport.isXAxisBoundsManual = true
        graph.viewport.isXAxisBoundsManual = true;
        graph.viewport.setMinX(-2*abs(realis))
        graph.viewport.setMaxX(2*abs(realis))
    }

    private fun setViewportY(graph:GraphView){
        graph.viewport.isYAxisBoundsManual = true
        graph.viewport.isYAxisBoundsManual = true;
        graph.viewport.setMinY(-2*abs(imaginaris))
        graph.viewport.setMaxY(2*abs(imaginaris))
    }
}