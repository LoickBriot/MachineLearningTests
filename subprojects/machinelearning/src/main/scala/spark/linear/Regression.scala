package spark.linear

import breeze.numerics._
import org.apache.log4j.{Level, Logger}
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.{SparkConf, SparkContext}


/**
  * Created by loick on 5/11/16.
  */
object Regression extends LinearRegressionTrait {

  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)

  def main(args: Array[String]) {

    val conf = new SparkConf().setAppName("Word Count").setMaster("local");
    val sc = new SparkContext(conf)

    val inputSeq = (1 to 100).map{  i => (i.toDouble, (1/(i*i) + i * i + 200 - scala.util.Random.nextInt(400)).toDouble) }.toSeq

    var normalizedInputSeq = inputSeq

    normalizedInputSeq.foreach(println)
  //  normalizedInputSeq.foreach(println)
    val funcSeq: Seq[Double => Double] = Seq(P0, P_2, P1, P2)

    val parsedData = LinearRegressionUtil.getRDDFromList(normalizedInputSeq, funcSeq, sc)

    var res = Map[Vector, Double]()

    for(i <- 1 to 1) {
      val solver = new LinearRegressionSolver()
      val stepSize = pow(10.0d, -7) *1.6
      val numIteration = pow(10,5)
      val miniBatch = 0.006
      println(s"\n\nTEST FOR STEPSIZE = ${stepSize}")
      println(s"TEST FOR NUMITE = ${numIteration}")
      println(s"TEST FOR MINIBATCH = ${miniBatch}")
      val model = solver.run(parsedData, numIterations = numIteration, stepSize = stepSize, miniBatch = miniBatch)



      val valuesAndPreds = LinearRegressionUtil.getPredictionsFromModel(model, normalizedInputSeq, funcSeq)
      val MSE = valuesAndPreds.map { case (x, y, p) => math.pow((y - p), 2) }.sum / valuesAndPreds.size.toDouble
      println("training Mean Squared Error = " + MSE)
      println("Weights of model : ")
      println(model.weights)
      res += (model.weights -> MSE)

      GraphPlotUtil.plotDatas(valuesAndPreds.map(_._1), Seq(normalizedInputSeq.map(_._2), valuesAndPreds.map(_._3)))
    }



    println("\n\n\n\n")
    res.toSeq.sortBy(_._2).foreach(println)



    sc.stop()


  }
}

