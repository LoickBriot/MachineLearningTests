package spark.linear

import org.apache.spark.SparkContext
import org.apache.spark.mllib.linalg._
import org.apache.spark.mllib.regression.{LabeledPoint, LinearRegressionModel}
import org.apache.spark.rdd.RDD
/**
  * Created by loick on 5/11/16.
  */
object LinearRegressionUtil {


  def getRDDFromFile(filename:String, funcSeq: Seq[Double => Double], sc: SparkContext): RDD[LabeledPoint] = {

    val content = scala.io.Source.fromFile(filename, "UTF-8").mkString
    val inputSeq = content.split("\n").map { el =>
      val parts = el.split(";")
      (parts(0).toDouble, parts(1).toDouble)
    }

    getRDDFromList(inputSeq, funcSeq, sc)
  }


  def getRDDFromList(inputSeq: Seq[(Double,Double)], funcSeq: Seq[Double => Double], sc: SparkContext) : RDD[LabeledPoint] = {

    return sc.parallelize(inputSeq).map { el =>
      val x = el._1
      val y = el._2
      LabeledPoint(y, getVectorDenseFromFuncSeq(x,funcSeq) )
    }.cache()

  }


  def getVectorDenseFromFuncSeq(x:Double, funcSeq: Seq[Double => Double]): Vector = {
    return Vectors.dense(funcSeq.map(f=> f(x)).toArray)
  }

  def getPredictionsFromModel(model: LinearRegressionModel, inputSeq: Seq[(Double,Double)], funcSeq: Seq[Double => Double]) : Seq[(Double, Double, Double)] = {
    return inputSeq.map { el =>
      val x = el._1
      val y = el._2
      val point = LabeledPoint(y, getVectorDenseFromFuncSeq(x,funcSeq))
      val prediction = model.predict(point.features)
      (x, y, prediction)
    }
  }
}
