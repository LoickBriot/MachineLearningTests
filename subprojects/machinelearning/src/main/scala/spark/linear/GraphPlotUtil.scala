package spark.linear

import breeze.linalg._
import breeze.plot.{Figure, _}
import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.{LabeledPoint, LinearRegressionWithSGD}


/**
  * Created by loick on 5/11/16.
  */
object GraphPlotUtil {

  def plotDatas(absSeq: Seq[Double], ordSeqs: Seq[Seq[Double]],xLogScale : Boolean = false, yLogScale: Boolean = false) : Figure = {
    val fig = Figure()
    val plt = fig.subplot(0)
    plt.legend = true
    plt.logScaleX = xLogScale
    plt.logScaleY = yLogScale

    ordSeqs.foreach{ordSeq =>
      plt += plot(absSeq, ordSeq)
      fig.refresh()
    }

    return fig
  }

}
