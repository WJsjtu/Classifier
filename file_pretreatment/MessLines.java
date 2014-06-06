package file_pretreatment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import logistic_regression.Initial_Logistic;
import Jama.Matrix;

public class MessLines implements Runnable{
//	private Matrix training_attrs;
//	private Matrix test_attrs;
//	private Matrix data;
//	private int rows;
//	private int columns;
	
	
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
		Matrix data=Matrix.read(new BufferedReader(new FileReader(GUI.Main.path)));
		int rows=data.getRowDimension();
		int columns=data.getColumnDimension();
		for(int i=0;i<10;i++){
			Matrix test_attrs=data.getMatrix(rows/10*i, rows/10*(i+1)-1, 0, columns-2);
			Matrix test_types=data.getMatrix(rows/10*i, rows/10*(i+1)-1, columns-1, columns-1);
			
			Matrix training_attr1=data.getMatrix(0, rows/10*i-1, 0, columns-2);
			Matrix training_type1=data.getMatrix(0, rows/10*i-1, columns-1, columns-1);
			Matrix training_attr2=data.getMatrix(rows/10*(i+1), rows-1, 0, columns-2);
			Matrix training_type2=data.getMatrix(rows/10*(i+1), rows-1, columns-1, columns-1);
			
			Matrix training_attrs=combine(training_attr1, training_attr2, true);
			Matrix training_constant=new Matrix(training_attrs.getRowDimension(),1,1);
			Matrix test_constant=new Matrix(test_attrs.getRowDimension(),1,1);
			
			new Initial_Logistic(combine(training_attrs, training_constant, false), 
					combine(training_type1, training_type2, true), combine(test_attrs, test_constant, false),
					test_types, 5).init();
		}
	}
	
	public Matrix combine(Matrix a, Matrix b, boolean vertical){
		Matrix c = null;
		int rows1=a.getRowDimension();
		int columns1=a.getColumnDimension();
		int rows2=b.getRowDimension();
		int columns2=b.getColumnDimension();
		
		if(vertical){
			
			if(columns1!=columns2){
				//throws IllegalArgumentException;
			}
	
			else{
				c=new Matrix(rows1+rows2, columns1);
				c.setMatrix(0, rows1-1, 0, columns1-1, a);
				c.setMatrix(rows1, rows1+rows2-1, 0, columns1-1, b);
			}
		}
		
		else{
			if(rows1!=rows2){
				//throws IllegalArgumentException;
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
