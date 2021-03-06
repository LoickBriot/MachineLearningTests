package deeplearning4j

import java.io.File

import org.datavec.api.records.reader.RecordReader
import org.datavec.api.records.reader.impl.csv.CSVRecordReader
import org.datavec.api.split.{InputSplit, FileSplit}
import org.datavec.api.util.ClassPathResource
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator
import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator
import org.deeplearning4j.eval.Evaluation
import org.deeplearning4j.nn.api.OptimizationAlgorithm
import org.deeplearning4j.nn.conf.{NeuralNetConfiguration, Updater}
import org.deeplearning4j.nn.conf.layers.{DenseLayer, OutputLayer}
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.nn.weights.WeightInit
import org.deeplearning4j.optimize.listeners.ScoreIterationListener
import org.deeplearning4j.util.ModelSerializer
import org.nd4j.linalg.activations.Activation
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;


/**A Simple Multi Layered Perceptron (MLP) applied to digit classification for
  * the MNIST Dataset (http://yann.lecun.com/exdb/mnist/).
  *
  * This file builds one input layer and one hidden layer.
  *
  * The input layer has input dimension of numRows*numColumns where these variables indicate the
  * number of vertical and horizontal pixels in the image. This layer uses a rectified linear unit
  * (relu) activation function. The weights for this layer are initialized by using Xavier initialization
  * (https://prateekvjoshi.com/2016/03/29/understanding-xavier-initialization-in-deep-neural-networks/)
  * to avoid having a steep learning curve. This layer will have 1000 output signals to the hidden layer.
  *
  * The hidden layer has input dimensions of 1000. These are fed from the input layer. The weights
  * for this layer is also initialized using Xavier initialization. The activation function for this
  * layer is a softmax, which normalizes all the 10 outputs such that the normalized sums
  * add up to 1. The highest of these normalized values is picked as the predicted class.
  *
  */

object deeplearning4j_wine {

  def main(args: Array[String]) : Unit = {
    //number of rows and columns in the input pictures
    val numRows = 13;
    val numColumns = 1;
    val outputNum = 3; // number of output classes
    val batchSize = 100; // batch size for each epoch
    val rngSeed = 13; // random number seed for reproducibility
    val numEpochs = 40; // number of epochs to perform


    val recordReader:RecordReader = new CSVRecordReader(0, "\t")
    recordReader.initialize(new FileSplit(new File("subprojects/machinelearning/src/main/scala/deeplearning4j/example_data/NormalizedDataSet_training.csv")))


    val recordReader2:RecordReader = new CSVRecordReader(0, "\t")
    recordReader2.initialize(new FileSplit(new File("subprojects/machinelearning/src/main/scala/deeplearning4j/example_data/NormalizedDataSet_test.csv")))

    //get the dataset using the record reader. The datasetiterator handles vectorization

    val mnistTrain = new RecordReaderDataSetIterator(recordReader, 100,0,3)
    val mnistTest = new RecordReaderDataSetIterator(recordReader2, 78,0,3)


    println("Build model....");
    val conf = new NeuralNetConfiguration.Builder()
      // .seed(rngSeed) //include a random seed for reproducibility
      // use stochastic gradient descent as an optimization algorithm
      .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
      .iterations(1)
      .learningRate(0.1) //specify the learning rate
      .updater(Updater.NESTEROVS).momentum(0.9) //specify the rate of change of the learning rate.
      //.regularization(true).l2(1e-4)
      .list()
      .layer(0, new DenseLayer.Builder() //create the first, input layer with xavier initialization
        .nIn(numRows * numColumns)
        .nOut(26)
        .activation(Activation.RELU)
        .weightInit(WeightInit.XAVIER)
        .build())
      .layer(1, new OutputLayer.Builder(LossFunction.NEGATIVELOGLIKELIHOOD) //create hidden layer
        .nIn(26)
        .nOut(outputNum)
        .activation(Activation.SOFTMAX)
        .weightInit(WeightInit.XAVIER)
        .build())
      .pretrain(false).backprop(true) //use backpropagation to adjust weights
      .build();

    val model = new MultiLayerNetwork(conf);
    model.init();
    //print the score with every 1 iteration
    model.setListeners(new ScoreIterationListener(1));




    println("Train model....");
    for( i <- 0 until numEpochs ){
      println(s"Train model.... Epoch ${i}");
      model.fit(mnistTrain);
    }


    //ModelSerializer.writeModel(model,"/home/loick/wine_neuralnetwork.mdd", true)

   // val model2 = ModelSerializer.restoreMultiLayerNetwork("/home/loick/wine_neuralnetwork.mdd",true)


    println("Evaluate model....");
    val eval = new Evaluation(outputNum);
    while(mnistTest.hasNext()){
      val next = mnistTest.next();
      val output = model.output(next.getFeatureMatrix()); //get the networks prediction
      println(s"${output}   ${next.getLabels}")
      eval.eval(next.getLabels(), output); //check the prediction against the true class
    }

    println(eval.stats());
    println("****************Example finished********************");

  }

}
