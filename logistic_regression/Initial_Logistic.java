package logistic_regression;

import score.Calculate;
import Jama.Matrix;

public class Initial_Logistic {
	int attr_num;	
	int type_num;		
	int training_instance_num;
	int test_instance_num;
	final double a=0.01;
	int times=500;

	Matrix training_attrs;	
	Matrix training_types;
	
	Matrix test_attrs;	
	Matrix test_types;
	
	Matrix[] weighs;	
	
	double[] prediction_type;	
	
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
		
		predict_type();
	}
	
	
	public void training(int type, int times) {
		Matrix error=new Matrix(training_instance_num,1);
		Matrix current_type=new Matrix(training_instance_num,1);
		
		for(int i=0;i<training_instance_num;i++){
			if(training_types.get(i,0)!=(type+1))
				current_type.set(i, 0, 0);
			else
				current_type.set(i, 0, 1);
		}
		
		for(int i=0;i<times;i++){
			error=current_type.minus(return_type(training_attrs.times(weighs[type])));
			weighs[type].plusEquals(training_attrs.transpose().times(a).times(error));
		}
		
	}
	
	public Matrix return_type(Matrix todo){		
		for(int i=0;i<training_instance_num;i++){
			todo.set(i, 0, 1/(1+Math.pow(Math.E,-todo.get(i, 0))));
		}
		return todo;
	}
	
	public void predict_type() {
		double max_score=0;
		int max_type=0;
		for(int i=0;i<test_instance_num;i++){
			for(int j=0;j<5;j++){
				Matrix score=(test_attrs.getMatrix(new int[]{i}, 0, attr_num)).times(weighs[j]);
				if(score.get(0, 0)>max_score){
					max_score=score.get(0, 0);
					max_type=(j+1);
				}
			}
			prediction_type[i]=max_type;
			max_score=0;
		}
		
		Calculate.calculate(new Matrix((double[])prediction_type,1).transpose(), test_types, type_num);
	}
}
	
	