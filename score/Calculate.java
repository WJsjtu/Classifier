package score;

import java.text.DecimalFormat;

import GUI.Main;
import Jama.Matrix;

/**
 * 
 * @author zy
 * @function calculate the correct_rate and f1-score from confusion_matrix
 */
public class Calculate {
	static Matrix predict;
	static Matrix fact;
	static int type;
	static double[][] confusion_matrix;
	static DecimalFormat df=new DecimalFormat("0.000000");
	
	public static void init(int type){
		Calculate.confusion_matrix=new double[type][type];
	}
	
	public static void calculate(Matrix predict, Matrix fact, int type){
		Calculate.predict=predict;
		Calculate.fact=fact;
		Calculate.type=type;
		build_matrix();
	}
	
	/**
	 * 
	 * @author zy
	 * @function use the the test results to build confusion_matrix
	 */
	public static void build_matrix(){
		for(int i=0;i<Calculate.predict.getRowDimension();i++){
			try{
			Calculate.confusion_matrix[(int) Calculate.fact.get(i, 0)-1][(int) Calculate.predict.get(i, 0)-1]++;
			}
			catch(Exception e){
				e.printStackTrace();
				System.out.println(Calculate.predict.get(i, 0));
				System.out.println(Calculate.fact.get(i, 0));
				System.exit(1);
			}
		}
	}
	
	/**
	 * 
	 * @author zy
	 * @function calculate the correct_rate
	 */
	public static void correct_rate(){
		Main.mp.setMessage("=== Summary ===\n\n");
		double correct_num=0;
		double total_num=0;
		for(int i=0;i<Calculate.type;i++){
			for(int j=0;j<Calculate.type;j++){
				total_num+=confusion_matrix[i][j];
				if(i==j)
					correct_num+=confusion_matrix[i][j];
			}
		}
		
		Main.mp.appendMessage("Correctly Classified Instances\t\t"+(int)correct_num+"\t"+
				df.format(correct_num*100.0/total_num)+"%\n");
		Main.mp.appendMessage("Incorrectly Classified Instances\t"+(int)(total_num-correct_num)+"\t"+
				df.format((1-correct_num/total_num)*100.0)+"%\n");
		Main.mp.appendMessage("Total Number of Instances\t\t"+(int)total_num+"\n\n");
	}
	
	/**
	 * 
	 * @author zy
	 * @function calculate precision, recall, f1-score using the confusion_matrix
	 */
	public static void f1_score(){
		Main.mp.appendMessage("=== Detailed Accuracy By Class ===\n\n");
		Main.mp.appendMessage("Classes\t\tPrecision\t\tRecall\t\t\tF1-score\n");
		
		for(int i=0;i<Calculate.type;i++){
			int predict=0;
			int fact=0;
			double precision=0;
			double recall=0;
			double f1=0;
			
			for(int j=0;j<Calculate.type;j++){
				predict+=Calculate.confusion_matrix[j][i];
				fact+=Calculate.confusion_matrix[i][j];
			}
			
			if(predict!=0)
				precision=Calculate.confusion_matrix[i][i]/predict;
			if(fact!=0)
				recall=Calculate.confusion_matrix[i][i]/fact;
			if(precision!=0 && recall!=0)
				f1=2*precision*recall/(precision+recall);
			Main.mp.appendMessage(""+(i+1)+"\t\t"+df.format(precision)+"\t\t"+df.format(recall)+"\t\t"+df.format(f1)+"\n");
		}
	}
	
	/**
	 * 
	 * @author zy
	 * @function print the confusion_matrix
	 */
	public static void print_matrix(){
		Main.mp.appendMessage("\n=== Confusion Matrix ===\n\n");
		Main.mp.appendMessage("Prediction type\n");
		for(int i=0;i<Calculate.type;i++)
			Main.mp.appendMessage(""+(i+1)+"\t");
		Main.mp.appendMessage("Real type\n\n");
		for(int i=0;i<Calculate.type;i++){
			for(int j=0;j<Calculate.type;j++){
				Main.mp.appendMessage(""+(int)Calculate.confusion_matrix[i][j]+"\t");
			}
			Main.mp.appendMessage(""+(i+1)+"\n");
		}
	}
	
	public static void print(){
		correct_rate();
		f1_score();
		print_matrix();
	}
}
