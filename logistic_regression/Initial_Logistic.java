package logistic_regression;

import java.io.FileWriter;
import java.io.IOException;

import file_pretreatment.Cross_validation;

import score.Calculate;
import Jama.Matrix;

/**
 * 
 * @author zy
 * @function train the data to get weighs,
 * use the weighs to predict test data type
 * give the test data type to calculate scores
 */
public class Initial_Logistic {
	int attr_num;	
	int type_num;		
	int training_instance_num;
	int test_instance_num;
	
	/**
	 * a represents the learning rate
	 */
	final double a=0.01;
	
	/**
	 * times represents the iteration times
	 */
	int times=500;

	Matrix training_attrs;	
	Matrix training_types;
	
	Matrix test_attrs;	
	Matrix test_types;
	
	Matrix[] weighs;	
	
	double[] prediction_type;	
	
	/**
	 * @interface
	 * @param training_attrs: the training data features matrix
	 * @param training_types: the training data types matrix
	 * @param test_attrs:	the test data features matrix
	 * @param test_types:	the test data types matrix
	 * @param type_num:		the total number of all possible types
	 */
	public Initial_Logistic(Matrix training_attrs, Matrix training_types,
			Matrix test_attrs, Matrix test_types, int type_num){
		this.training_attrs=training_attrs;
		this.training_types=training_types;
		this.test_attrs=test_attrs;
		this.test_types=test_types;
		this.type_num=type_num;
		this.attr_num=test_attrs.getColumnDimension()-1;
		this.test_instance_num=test_attrs.getRowDimension();
		this.training_instance_num=training_attrs.getRowDimension();
		
		weighs=new Matrix[type_num];
		prediction_type=new double[test_instance_num];
	}
	
	
	public void init() {
		for(int i=0;i<type_num;i++){
			weighs[i]=new Matrix(attr_num+1,1,1);
		}
		
		for(int i=0;i<type_num;i++){
			training(i, times);
		}
		
		try {
			for(int i=0;i<type_num;i++){
				FileWriter fw=new FileWriter(".\\results\\logistic_regression\\type"+(int)(i+Cross_validation.start_num)+".txt",true);
				for(int j=0;j<weighs[i].getRowDimension();j++)
					fw.write(""+(int)weighs[i].get(j, 0)+"\r\n");
				fw.write("\r\n");
				fw.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		predict_type();
	}
	
	
	public void training(int type, int times) {
		
		/**
		 * error represents the deviation between predicted type and current type
		 */
		Matrix error=new Matrix(training_instance_num,1);
		
		/**
		 * current_type represents 0 for not this type, 1 for this type(one vs all)
		 */
		Matrix current_type=new Matrix(training_instance_num,1);
		
		for(int i=0;i<training_instance_num;i++){
			if(training_types.get(i,0)!=(type+Cross_validation.start_num))
				current_type.set(i, 0, 0);
			else
				current_type.set(i, 0, 1);
		}
		
		/**
		 * @function calculate error, and amend the weighs
		 */
		for(int i=0;i<times;i++){
			error=current_type.minus(return_type(training_attrs.times(weighs[type])));
			weighs[type].plusEquals(training_attrs.transpose().times(a).times(error));
		}
		
	}
	
	/**
	 * 
	 * @author zy
	 * @function the same as sigmod function
	 * @param todo
	 * @return
	 */
	public Matrix return_type(Matrix todo){		
		for(int i=0;i<training_instance_num;i++){
			todo.set(i, 0, 1/(1+Math.pow(Math.E,-todo.get(i, 0))));
		}
		return todo;
	}
	
	/**
	 * 
	 * @author zy
	 * @function use weighs to predict test data type
	 */
	public void predict_type() {
		double max_score=0;
		int max_type=0;
		for(int i=0;i<test_instance_num;i++){
			for(int j=0;j<type_num;j++){
				Matrix score=(test_attrs.getMatrix(new int[]{i}, 0, attr_num)).times(weighs[j]);
				if(score.get(0, 0)>max_score){
					max_score=score.get(0, 0);
					max_type=(int) (j+Cross_validation.start_num);
				}
			}
			prediction_type[i]=max_type;
			max_score=0;
		}
		
		/**
		 * @interface
		 * input params: the test data type matrix your model predict, 
		 * 				the real type matrix of test data,
		 * 				the total number of all possible types
		 */
		Calculate.calculate(new Matrix((double[])prediction_type,1).transpose(), test_types, type_num);
	}
}
	
	