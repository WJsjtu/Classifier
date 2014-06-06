package logistic_regression;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import Jama.Matrix;

public class Main {
	final static int attr_num=10;	
	final static int type_num=5;		
	final static int training_instance_num=3649;	//4000;	
	final static int test_instance_num=1824;	//1473;
	final static double a=0.01;
	static Matrix training_attrs=new Matrix(training_instance_num,attr_num+1,1);	
	static Matrix training_types=new Matrix(training_instance_num,1);
	
	static Matrix test_attrs=new Matrix(test_instance_num,attr_num+1,1);	
	static Matrix test_types=new Matrix(test_instance_num,1);
	
	static Matrix[] weighs=new Matrix[type_num];	
	
	static double[] history=new double[500];	
	static double[] prediction_score=new double[test_instance_num];
	static int[] prediction_type=new int[test_instance_num];	
	
	
	public static void main(String[] args) throws IOException {
		Calendar c=Calendar.getInstance();
		for(int i=0;i<type_num;i++){
			weighs[i]=new Matrix(attr_num+1,1,1);
		}
		//readFile("training.data");
		readFile2("page-blocks.data");
		for(int i=0;i<type_num;i++){
			training(i, 500);
			System.out.println("Complete "+(i+1)+" times ");
		}
		
		//testing("test.data");
		predict_type();
		Calendar cal=Calendar.getInstance();
		System.out.println("Time cost:¡¡"+(cal.getTimeInMillis()-c.getTimeInMillis())/1000+"s");
	}
	
	public static void readFile2(String path) throws IOException{
		BufferedReader br=new BufferedReader(new FileReader(path));
		String line="";
		int tr_row=0;
		int te_row=0;
		int flag=0;
		while((line=br.readLine())!=null){
			String[] attributes=line.split(" +");
			
			if(flag%3!=2){
				for(int i=1;i<attributes.length-1;i++){
					training_attrs.set(tr_row, i, Math.log(Double.valueOf(attributes[i])));
				}
				
				training_types.set(tr_row, 0, Integer.parseInt(attributes[attributes.length-1]));
				tr_row++;
				flag++;
			}
			
			else{
				for(int i=1;i<attributes.length-1;i++){
					test_attrs.set(te_row, i, Math.log(Double.valueOf(attributes[i])));
				}
				
				test_types.set(te_row, 0, Integer.parseInt(attributes[attributes.length-1]));
				te_row++;
				flag=0;
			}
		}
		br.close();
	}
	
	public static void readFile(String path) throws IOException{
		BufferedReader br=new BufferedReader(new FileReader(path));
		String line="";
		int row=0;
		while((line=br.readLine())!=null){
			String[] attributes=line.split(" +");
			for(int i=1;i<attributes.length-1;i++)
				//training_attrs.set(row, i, Math.log(Double.valueOf(attributes[i])));
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
		
		FileWriter fw=new FileWriter("results\\history"+(type+1)+".txt");
		for(int i=0;i<500;i++){
			fw.write(""+history[i]+"\n");
		}
		fw.close();
		
		FileWriter fw2=new FileWriter("results\\weighs"+(type+1)+".txt");
		for(int i=0;i<=attr_num;i++){
			fw2.write(""+weighs[type].get(i, 0)+"\n");
		}
		fw2.close();
	}
	
	public static Matrix return_type(Matrix todo){		
		for(int i=0;i<training_instance_num;i++){
			todo.set(i, 0, 1/(1+Math.pow(Math.E,-todo.get(i, 0))));
		}
		return todo;
	}
	
	public static void testing(String path) throws NumberFormatException, IOException{
		BufferedReader br=new BufferedReader(new FileReader(path));
		String line="";
		int row=0;
		while((line=br.readLine())!=null){
			String[] attributes=line.split(" +");
			for(int i=1;i<attributes.length-1;i++)
				//test_attrs.set(row, i, Math.log(Double.valueOf(attributes[i])));
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
	
	public static void calculate_F1() throws IOException{
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
	
	public static void precision(){
		int right=0;
		for(int i=0;i<test_instance_num;i++){
			if(test_types.get(i, 0)==prediction_type[i])
				right++;
		}
		System.out.println("Precision is: "+(right*100.0)/test_instance_num+"%");
	}
}
