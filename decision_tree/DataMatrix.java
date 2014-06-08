package decision_tree;

import Jama.Matrix;

public class DataMatrix extends Matrix{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param data
	 */
	public DataMatrix(double[][] data){
		super(data);
	}
	
	/**
	 * 
	 * @param matrix
	 */
	public DataMatrix(Matrix matrix){
		super(matrix.getArrayCopy());
	}
	
	/**
	 * 
	 * @param columnindex
	 * @return
	 */
	public double[] getRowsFromColumn(int columnindex){
		double[] result = new double[this.getRowDimension()];
		for(int i = 0; i < this.getRowDimension(); i++){
			result[i] = this.get(i, columnindex);
		}
		return result;
	}
	
	/**
	 * 
	 * @param columnindex
	 * @param index
	 * @return
	 */
	public double[] getRowsFromColumn(int columnindex, int[] index){
		double[] result = new double[index.length];
		for(int i = 0; i < index.length; i++){
			result[i] = this.get(index[i], columnindex);
		}
		return result;
	}
	
	/**
	 * 
	 * @param columnindex
	 * @param index
	 * @return
	 */
	public double DiscreteGain(int columnindex, int[] index){
		double info = 0;
		QSort sort = new QSort(this.getRowsFromColumn(columnindex,index));
		for(QSort.SubClass cls : sort.QSortClass()){
			Column typecl = new Column(this.getRowsFromColumn(this.getColumnDimension() - 1, cls.set));
			info += (double)cls.length / (double)index.length * typecl.DiscreteInfo();
		}
		Column totaltypecl = new Column(this.getRowsFromColumn(this.getColumnDimension() - 1));
		return totaltypecl.DiscreteInfo() - info;
	}
	
	/**
	 * 
	 * @param columnindex
	 * @param index
	 * @return
	 */
	public double DiscreteGainRato(int columnindex, int[] index){
		double info = 0;
		QSort sort = new QSort(this.getRowsFromColumn(columnindex,index));
		for(QSort.SubClass cls : sort.QSortClass()){
			Column typecl = new Column(this.getRowsFromColumn(this.getColumnDimension() - 1, cls.set));
			info += (double)cls.length / (double)index.length * typecl.DiscreteInfo();
		}
		Column cl = new Column(this.getRowsFromColumn(columnindex, index));
		Column totaltypecl = new Column(this.getRowsFromColumn(this.getColumnDimension() - 1));
		return (totaltypecl.DiscreteInfo() - info) / cl.DiscreteInfo();
	}
	
	/**
	 * 
	 * @param columnindex
	 * @param index
	 * @param pace
	 * @return
	 */
	public int ContinuousSeperatorEstimateByGain(int columnindex, int[] index, int pace){
		pace = pace - pace % 2;
		if(index.length > pace){
			double[] infos = new double[index.length - pace + 1];
			QSort clsort = new QSort(this.getRowsFromColumn(this.getColumnDimension() - 1, index));
			for(int i = pace / 2,j = 0; j < index.length - pace + 1; i++, j++){
				double[] former = new double[pace / 2];
				double[] latter = new double[pace / 2];
				for(int k = 0 ;k < pace / 2; k++){
					former[k] = clsort.data[j + k];
					latter[k] = clsort.data[i + k];
				}
				infos[j] = ((new Column(former)).DiscreteInfo() + (new Column(latter)).DiscreteInfo()) / (double)(2);
			}
			QSort res = new QSort(infos);
			return clsort.index[res.index[infos.length - 1]];
		} else {
			double[] infos = new double[index.length - 1];
			QSort clsort = new QSort(this.getRowsFromColumn(this.getColumnDimension() - 1, index));
			for(int i = 1; i < index.length; i++){
				infos[i -1] = ContinousGain(columnindex, i, index);
			}
			QSort res = new QSort(infos);
			for(int i = 0; i < res.index.length; i++){
				int resint = clsort.index[res.index[0]];
				if(resint != 0 ){
					return resint;
				}
			}
			return 0;
		}
	}
	
	/**
	 * 
	 * @param columnindex
	 * @param seperator
	 * @param index
	 * @return
	 */
	public double ContinousGain(int columnindex,int seperator, int[] index){
		QSort clsort = new QSort(this.getRowsFromColumn(columnindex, index));
		int sortsep = 0;

		//获取排序后划分点所在的位置
		for(int i = 0 ;i < index.length; i++){
			if(clsort.index[i] == seperator){
				sortsep = i;
				break;
			}
		}
		int[] former_rows = new int[sortsep];
		int[] latter_rows = new int[index.length - sortsep];

		//取出划分点的前后相邻点的原始序号
		for(int i = 0; i < former_rows.length; i++){
			former_rows[i] = index[clsort.index[i]];
		}
		for(int i = 0; i < latter_rows.length; i++){
			latter_rows[i] = index[clsort.index[i + sortsep]];
		}
		
		double formar_gain = (new Column(this.getRowsFromColumn(this.getColumnDimension() - 1, former_rows))).DiscreteInfo();
		double latter_gain = (new Column(this.getRowsFromColumn(this.getColumnDimension() - 1, latter_rows))).DiscreteInfo();
		
		return (new Column(this.getRowsFromColumn(this.getColumnDimension() - 1, index))).DiscreteInfo() - 
				( former_rows.length * formar_gain + latter_rows.length * latter_gain) /
				(double)(index.length);
	}
	
	/**
	 * 
	 * @param columnindex
	 * @param seperator
	 * @param index
	 * @return
	 */
	public double ContinousGainRato(int columnindex,int seperator, int[] index){
		QSort clsort = new QSort(this.getRowsFromColumn(columnindex, index));
		int sortsep = 0;
		//获取排序后划分点所在的位置
		for(int i = 0 ;i < index.length; i++){
			if(clsort.index[i] == seperator){
				sortsep = i;
				break;
			}
		}
		int[] former_rows = new int[sortsep];
		int[] latter_rows = new int[index.length - sortsep];
		
		//取出划分点的前后相邻点的原始序号
		for(int i = 0; i < former_rows.length; i++){
			former_rows[i] = index[clsort.index[i]];
		}
		for(int i = 0; i < latter_rows.length; i++){
			latter_rows[i] = index[clsort.index[i + sortsep]];
		}
		double formar_gain = (new Column(this.getRowsFromColumn(this.getColumnDimension() - 1, former_rows))).DiscreteInfo();
		double latter_gain = (new Column(this.getRowsFromColumn(this.getColumnDimension() - 1, latter_rows))).DiscreteInfo();
		return ((new Column(this.getRowsFromColumn(this.getColumnDimension() - 1, index))).DiscreteInfo() - 
				( former_rows.length * formar_gain + latter_rows.length * latter_gain) / (double)(index.length))
				/(new Column(this.getRowsFromColumn(columnindex, index))).DiscreteInfo();
	}
	
}
