package logistic_regression;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import Jama.Matrix;

public class Main {
	final static int attr_num=54;	
	final static int type_num=7;		
	final static int training_instance_num=450000;	
	final static int test_instance_num=131012;
	final static double a=0.05;
	static Matrix training_attrs=new Matrix(training_instance_num,attr_num+1,1);	
	static Matrix training_types=new Matrix(training_instance_num,1);
	
	static Matrix test_attrs=new Matrix(test_instance_num,attr_num+1,1);	
	static Matrix test_types=new Matrix(test_instance_num,1);
	
	static Matrix[] weighs=new Matrix[type_num];	
	
	static double[] history=new double[100];	
	static double[] prediction_score=new double[test_instance_num];
	static int[] prediction_type=new int[test_instance_num];	
	
	
	public static void main(String[] args) throws IOException {
		Calendar c=Calendar.getInstance();
		for(int i=0;i<type_num;i++){
			weighs[i]=new Matrix(attr_num+1,1,1);
		}
		readFile("D:\\Zhanyang\\Classification\\training.data");
		for(int i=0;i<type_num;i++){
			training(i, 100);
			System.out.println("Complete "+(i+1)+" times ");
		}
		
		testing("D:\\Zhanyang\\Classification\\test.data");
		Calendar cal=Calendar.getInstance();
		System.out.println("Time cost:¡¡"+(cal.getTimeInMillis()-c.getTimeInMillis())/1000+"  s");
	}
	
	public static void readFile(String path) throws IOException{
		BufferedReader br=new BufferedReader(new FileReader(path));
		String line="";
		int row=0;
		while((line=br.readLine())!=null){
			String[] attributes=line.split(",");
			for(int i=0;i<attributes.length-1;i++)
				training_attrs.set(row, i, Double.valueOf(attributes[i]));
			training_types.set(row, 0, Double.valueOf(attributes[attributes.length-1]));
			row++;
		}
		br.close();
	}
	
	public static void training(int type, int times) throws IOException{
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
		
		FileWriter fw=new FileWriter("D:\\Zhanyang\\Classification\\results\\history"+(type+1)+".txt");
		for(int i=0;i<100;i++){
			fw.write(""+history[i]+"\n");
		}
		fw.close();
		
		FileWriter fw2=new FileWriter("D:\\Zhanyang\\Classification\\results\\weighs"+(type+1)+".txt");
		for(int i=0;i<=attr_num;i++){
			fw.write(""+weighs[type].get(i, 0)+"\n");
		}
		fw2.close();
	}
	
	public static Matrix return_type(Matrix todo){		
		for(int i=0;i<training_instance_num;i++){
//			if(todo.get(i, 0)>0)
//				todo.set(i, 0, 1);
//			else
//				todo.set(i, 0, 0);
			todo.set(i, 0, 1/(1+Math.pow(Math.E,-todo.get(i, 0))));
		}
		return todo;
	}
	
	public static void testing(String path) throws NumberFormatException, IOException{
		BufferedReader br=new BufferedReader(new FileReader(path));
		String line="";
		int row=0;
		while((line=br.readLine())!=null){
			String[] attributes=line.split(",");
			for(int i=0;i<attributes.length-1;i++)
				test_attrs.set(row, i, Double.valueOf(attributes[i]));
			test_types.set(row, 0, Double.valueOf(attributes[attributes.length-1]));
			row++;
		}
		br.close();
		predict_type();
	}
	
	public static void predict_type() throws IOException{
		double max_score=0;
		int max_type=0;
		for(int i=0;i<test_instance_num;i++){
			for(int j=0;j<7;j++){
				Matrix score=(test_attrs.getMatrix(new int[]{i}, 0, attr_num)).times(weighs[j]);
				if(score.get(0, 0)>max_score){
					max_score=score.get(0, 0);
					max_type=(j+1);
				}
			}
			prediction_score[i]=max_score;
			prediction_type[i]=max_type;
		}
		
		FileWriter fw=new FileWriter("D:\\Zhanyang\\Classification\\results\\predict_type.txt");
		for(int i=0;i<prediction_type.length;i++){
			fw.write(""+prediction_type[i]+"\n");
		}
		fw.close();
		
		calculate_F1();
	}
	
	public static void calculate_F1() throws IOException{
		double[] Macro_f1s=new double[7];	
		double[] precisions=new double[7];	
		double[] recalls=new double[7];	
		
		int[] every_test_type=new int[7];
		int[] every_predict_type=new int[7];
		int[] all_right_type=new int[7];
		
		for(int i=0;i<test_instance_num;i++){
			int t=(int) test_types.get(i, 0);
			int p=prediction_type[i];
			every_test_type[t-1]++;
			every_predict_type[p-1]++;
			if(t==p)
				all_right_type[t-1]++;
		}
		
		for(int i=0;i<7;i++){
			precisions[i]=all_right_type[i]/every_predict_type[i];
			recalls[i]=all_right_type[i]/every_test_type[i];
			Macro_f1s[i]=2*precisions[i]*recalls[i]/(precisions[i]+recalls[i]);
		}
		
		FileWriter fw=new FileWriter("D:\\Zhanyang\\Classification\\results\\precisions.txt");
		for(int i=0;i<precisions.length;i++){
			fw.write(""+precisions[i]+"\n");
		}
		fw.close();
		
		FileWriter fw2=new FileWriter("D:\\Zhanyang\\Classification\\results\\recalls.txt");
		for(int i=0;i<recalls.length;i++){
			fw.write(""+recalls[i]+"\n");
		}
		fw2.close();
		
		FileWriter fw3=new FileWriter("D:\\Zhanyang\\Classification\\results\\Macro_f1s.txt");
		for(int i=0;i<Macro_f1s.length;i++){
			fw.write(""+Macro_f1s[i]+"\n");
		}
		fw3.close();
	}
}
