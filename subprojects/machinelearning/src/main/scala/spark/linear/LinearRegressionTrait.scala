package spark.linear

import breeze.numerics.pow



/**
  * Created by loick on 5/11/16.
  */
trait LinearRegressionTrait {

  def P_4(x:Double) : Double = pow(x,-4)
  def P_3(x:Double) : Double = pow(x,-3)
  def P_2(x:Double) : Double = pow(x,-2)
  def P_1(x:Double) : Double = pow(x,-1)
  def P0(x:Double) : Double = pow(x,0)
  def P1(x:Double) : Double = pow(x,1)
  def P2(x:Double) : Double = pow(x,2)
  def P3(x:Double) : Double = pow(x,3)
  def P4(x:Double) : Double = pow(x,4)

}
