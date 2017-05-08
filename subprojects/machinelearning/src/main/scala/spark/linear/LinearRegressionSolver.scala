package spark.linear

import org.apache.log4j.{Level, Logger}
import org.apache.spark.mllib.regression._
import org.apache.spark.rdd.RDD


/**
  * Created by loick on 5/11/16.
  */
case class LinearRegressionSolver() extends LinearRegressionTrait {

  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)

  def run(parsedData: RDD[LabeledPoint], numIterations: Int = 100000, stepSize: Double = 0.0001, miniBatch : Double = 0.001) = {
    //var lr = new LinearRegressionWithSGD().setIntercept(true)

    //lr.optimizer.setNumIterations(numIterations)
    //lr.optimizer.setStepSize(stepSize)
    //lr.optimizer.setMiniBatchFraction(miniBatch)
    //lr.optimizer.setConvergenceTol(0.0001)
    //lr.optimizer.setRegParam(1)
    //lr.optimizer.setUpdater(new L1Updater())
    //lr.optimizer.setGradient(new HingeGradient)

    LinearRegressionWithSGD.train(parsedData, numIterations, stepSize, miniBatch)
    //lr.run(parsedData, Vectors.dense(scala.util.Random.nextInt(100)/100.0d,scala.util.Random.nextInt(100)/100.0d,scala.util.Random.nextInt(100)/100.0d))

  }


}
