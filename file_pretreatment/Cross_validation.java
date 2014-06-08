package file_pretreatment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JOptionPane;

import logistic_regression.Initial_Logistic;
import score.Calculate;
import GUI.Main;
import Jama.Matrix;
import decision_tree.C45;
import decision_tree.ID3;

/**
 * 
 * @author zy
 * @function read file and start 10 fold cross_validation
 */
public class Cross_validation implements Runnable{
	int type_num=0;
	public static double start_num=1;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			ReadFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void ReadFile() throws IOException{
		Matrix data=null;
		try{
			data=Matrix.read(new BufferedReader(new FileReader(GUI.Main.path)));
		}
		catch(NumberFormatException e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Unrecognized data format", 
					"can't parse the data file to double value, please ensure the data file format as ReadMe said",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		type_num=count_type_num(data.getMatrix(0, data.getRowDimension()-1, data.getColumnDimension()-1, 
				data.getColumnDimension()-1));
		//System.out.println("num type"+type_num);
		Calculate.init(type_num);
		int rows=data.getRowDimension();
		int columns=data.getColumnDimension();
		for(int i=0;i<10;i++){
			
			/**
			 * @function when i==9, we need to include the bottom lines ahead of the rows/10*(i+1)
			 */
			int end=0;
			if(i==9)
				end=rows-1;
			else
				end=rows/10*(i+1)-1;
			
			/**
			 * @function slice the matrix into the test-data part, the part above the test-data part, 
			 * the part below the test-data part,
			 * and need to combine the following two part into one training-data part
			 */
			Matrix test_attrs=data.getMatrix(rows/10*i, end, 0, columns-2);
			Matrix test_types=data.getMatrix(rows/10*i, end, columns-1, columns-1);
			
			Matrix training_attr1=data.getMatrix(0, rows/10*i-1, 0, columns-2);
			Matrix training_type1=data.getMatrix(0, rows/10*i-1, columns-1, columns-1);
			Matrix training_attr2=data.getMatrix(end+1, rows-1, 0, columns-2);
			Matrix training_type2=data.getMatrix(end+1, rows-1, columns-1, columns-1);
			
			Matrix training_attrs=combine(training_attr1, training_attr2, true);
			Matrix training_constant=new Matrix(training_attrs.getRowDimension(),1,1);
			Matrix test_constant=new Matrix(test_attrs.getRowDimension(),1,1);
			
			/**
			 * @function choose the right algorithm code to run
			 */
			Matrix total_training_attrs=combine(training_attrs, training_constant, false);
			Matrix total_training_types=combine(training_type1, training_type2, true);
			if(Main.algorithm==0){
				new Initial_Logistic(total_training_attrs, 
						total_training_types, combine(test_attrs, test_constant, false),
						test_types, type_num).init();
			}
			
			else if(Main.algorithm==1){
				C45 C45test = new C45(combine(training_attrs, total_training_types, false), null);
				double[] res = C45test.Root.MatrixTest(combine(test_attrs, test_constant, false));
				Calculate.calculate(new Matrix(res,1).transpose(), test_types, type_num);
				FileWriter fw=new FileWriter(".\\results\\C45\\C45_"+(int)(i+Cross_validation.start_num)+".txt");
				fw.write(C45test.TreeString());
				fw.close();
			}
			
			else if(Main.algorithm==2){
				ID3 ID3test = new ID3(combine(training_attrs, total_training_types, false), null);
				double[] res = ID3test.Root.MatrixTest(combine(test_attrs, test_constant, false));
				Calculate.calculate(new Matrix(res,1).transpose(), test_types, type_num);
				FileWriter fw=new FileWriter(".\\results\\ID3\\ID3_"+(int)(i+Cross_validation.start_num)+".txt");
				fw.write(ID3test.TreeString());
				fw.close();
			}
		}
		Main.bp.setStatus("Complete!");
		Main.tp.enable_button();
		Calculate.print();
	}
	
	/**
	 * @function count the number of types
	 * @param types
	 * @return
	 */
	public int count_type_num(Matrix types){
		//types.print(0, 0);
		ArrayList<Double> count=new ArrayList<>();
		for(int i=0;i<types.getRowDimension();i++){
			if(!count.contains(types.get(i, 0))){
				count.add(types.get(i, 0));
			}
		}
		Collections.sort(count);
		start_num=count.get(0);
		return count.size();
	}
	
	/**
	 * 
	 * @author zy
	 * @function concat two matrix vertically or horizontally
	 * @param a
	 * @param b
	 * @param vertical 	true for vertically, false for horizontally
	 * @return
	 */
	public Matrix combine(Matrix a, Matrix b, boolean vertical){
		Matrix c = null;
		int rows1=a.getRowDimension();
		int columns1=a.getColumnDimension();
		int rows2=b.getRowDimension();
		int columns2=b.getColumnDimension();
		
		if(vertical){
			if(columns1!=columns2){
				throw new IllegalArgumentException("All columns must have the same length");
			}
	
			else{
				c=new Matrix(rows1+rows2, columns1);
				c.setMatrix(0, rows1-1, 0, columns1-1, a);
				c.setMatrix(rows1, rows1+rows2-1, 0, columns1-1, b);
			}
		}
		
		else{
			if(rows1!=rows2){
				throw new IllegalArgumentException("All rows must have the same length");
			}
			
			else{
				c=new Matrix(rows1, columns1+columns2);
				c.setMatrix(0, rows1-1, 0, columns1-1, a);
				c.setMatrix(0, rows1-1, columns1, columns1+columns2-1, b);
			}
		}
		return c;
	}

}
