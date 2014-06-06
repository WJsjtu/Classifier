package logistic_regression;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import Jama.Matrix;

public class Initial_Logistic {
	int attr_num;	
	int type_num;		
	int training_instance_num;
	int test_instance_num;
	final double a=0.01;
	int times=500;
//	Matrix training_attrs=new Matrix(training_instance_num,attr_num+1,1);	
//	Matrix training_types=new Matrix(training_instance_num,1);
//	
//	Matrix test_attrs=new Matrix(test_instance_num,attr_num+1,1);	
//	Matrix test_types=new Matrix(test_instance_num,1);
	
	Matrix training_attrs;	
	Matrix training_types;
	
	Matrix test_attrs;	
	Matrix test_types;
	
	Matrix[] weighs;	
	
	double[] history;	
	double[] prediction_score;
	int[] prediction_type;	
	
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
		history=new double[times];
		prediction_score=new double[test_instance_num];
		prediction_type=new int[test_instance_num];
	}
	
	
	public void init() throws IOException {
		Calendar c=Calendar.getInstance();
		for(int i=0;i<type_num;i++){
			weighs[i]=new Matrix(attr_num+1,1,1);
		}
		
		for(int i=0;i<type_num;i++){
			training(i, times);
			System.out.println("Complete "+(i+1)+" times ");
		}
			
		predict_type();
		Calendar cal=Calendar.getInstance();
		System.out.println("Time cost:¡¡"+(cal.getTimeInMillis()-c.getTimeInMillis())/1000+"s");
	}
	
	
	public void training(int type, int times) throws IOException{
		Matrix error=new Matrix(training_instance_num,1);
		Matrix current_type=new Matrix(training_instance_num,1);
		
		for(int i=0;i<training_instance_num;i++){
			if(training_types.get(i,0)!=(type+1))
				current_type.set(i, 0, 0);
			else
				current_type.set(i, 0, 1);
		}
		
		for(int i=0;i<times;i++){
			history[i]=weighs[type].get(0, 0);
			error=current_type.minus(return_type(training_attrs.times(weighs[type])));
			weighs[type].plusEquals(training_attrs.transpose().times(a).times(error));
		}
		
		FileWriter fw=new FileWriter("results\\history"+(type+1)+".txt");
		for(int i=0;i<times;i++){
			fw.write(""+history[i]+"\n");
		}
		fw.close();
		
		FileWriter fw2=new FileWriter("results\\weighs"+(type+1)+".txt");
		for(int i=0;i<=attr_num;i++){
			fw2.write(""+weighs[type].get(i, 0)+"\n");
		}
		fw2.close();
	}
	
	public Matrix return_type(Matrix todo){		
		for(int i=0;i<training_instance_num;i++){
			todo.set(i, 0, 1/(1+Math.pow(Math.E,-todo.get(i, 0))));
		}
		return todo;
	}
	
	public void predict_type() throws IOException{
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
			prediction_score[i]=max_score;
			prediction_type[i]=max_type;
			max_score=0;
		}
		
		FileWriter fw=new FileWriter("results\\predict_type.txt");
		for(int i=0;i<prediction_type.length;i++){
			fw.write(""+prediction_type[i]+"\n");
		}
		fw.close();
		
		FileWriter fw2=new FileWriter("results\\predict_score.txt");
		for(int i=0;i<prediction_score.length;i++){
			fw2.write(""+prediction_score[i]+"\n");
		}
		fw2.close();
		
		calculate_F1();
		precision();
	}
	
	public void calculate_F1() throws IOException{
		double[] Macro_f1s=new double[5];	
		double[] precisions=new double[5];	
		double[] recalls=new double[5];	
		
		int[] every_test_type=new int[5];
		int[] every_predict_type=new int[5];
		int[] all_right_type=new int[5];
		
		for(int i=0;i<test_instance_num;i++){
			int t=(int) test_types.get(i, 0);
			int p=prediction_type[i];
			every_test_type[t-1]++;
			every_predict_type[p-1]++;
			if(t==p)
				all_right_type[t-1]++;
		}
		
		for(int i=0;i<5;i++){
			System.out.println("all: "+all_right_type[i]);
			System.out.println("every predict: "+every_predict_type[i]);
			System.out.println("every test: "+every_test_type[i]);
			precisions[i]=all_right_type[i]*1.0/every_predict_type[i];
			recalls[i]=all_right_type[i]*1.0/every_test_type[i];
			Macro_f1s[i]=2*precisions[i]*recalls[i]*1.0/(precisions[i]+recalls[i]);
		}
		
		FileWriter fw=new FileWriter("results\\precisions.txt");
		for(int i=0;i<precisions.length;i++){
			fw.write(""+precisions[i]+"\n");
		}
		fw.close();
		
		FileWriter fw2=new FileWriter("results\\recalls.txt");
		for(int i=0;i<recalls.length;i++){
			fw2.write(""+recalls[i]+"\n");
		}
		fw2.close();
		
		FileWriter fw3=new FileWriter("results\\Macro_f1s.txt");
		for(int i=0;i<Macro_f1s.length;i++){
			fw3.write(""+Macro_f1s[i]+"\n");
		}
		fw3.close();
	}
	
	public void precision(){
		int right=0;
		for(int i=0;i<test_instance_num;i++){
			if(test_types.get(i, 0)==prediction_type[i])
				right++;
		}
		System.out.println("Precision is: "+(right*100.0)/test_instance_num+"%");
	}
}
