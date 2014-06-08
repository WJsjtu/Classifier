package decision_tree;


import java.util.List;

import decision_tree.TreeNode.NodeType;
import Jama.Matrix;

/**
 * 
 * @author wjbnys
 *
 */
public class ID3 {
	
	/**
	 * The matrix data
	 */
	public DataMatrix matrix;
	
	/**
	 * The model stored in the tree
	 */
	public Tree Root;
	
	/**
	 * describe the feature type
	 */
	public boolean[] InDiscrete;
	
	/**
	 * 
	 * @param m
	 * @param InDiscrete
	 */
	public ID3(Matrix m, int[] Discrete){
		this.Root = new Tree();
		this.matrix = new DataMatrix(m);
		this.InDiscrete = new boolean[m.getColumnDimension() - 1];
		for(int i = 0; i < InDiscrete.length; i++){
			this.InDiscrete[i] = false;
		}
		if(Discrete != null){
			for(int i = 0; i < Discrete.length; i++){
				this.InDiscrete[Discrete[i]] = true;
			}
		}
		int[] rows = new int[m.getRowDimension()];
		for(int i =0; i< rows.length;i++){
			rows[i] = i;
		}
		_ID3(rows, this.Root);
	}
	
	
	/**
	 * 
	 * @param dm
	 * @param InDiscrete
	 */
	public ID3(DataMatrix dm, boolean[] InDiscrete){
		this.Root = new Tree();
		this.matrix = dm;
		this.InDiscrete = InDiscrete;
		int[] rows = new int[dm.getRowDimension()];
		for(int i =0; i< rows.length;i++){
			rows[i] = i;
		}
		_ID3(rows, this.Root);
	}
	
	/**
	 * 
	 * @param row
	 * @return
	 */
	public int GetType(double[] row){
		return _GetType(row, this.Root);
	}
	
	/**
	 * 
	 * @param row
	 * @param node
	 * @return
	 */
	private int _GetType(double[] row, TreeNode node){
		if(node.type == NodeType.LEAF){
			return (int)node.data;
		}
		double value = row[node.character];
		for(TreeNode son : node.nodes){
			if(son.data == value){
				return _GetType(row, son);
			}
		}
		return -1;
	}
	
	/**
	 * 
	 * @return
	 */
	public String TreeString(){
		return this.Root.toString();
	}
	
	/**
	 * 
	 * @param rows
	 * @param node
	 */
	private void _ID3(int[] rows, TreeNode node){
		if(rows.length == 0){
			return;
		}
		int columncnt = this.matrix.getColumnDimension();
		double[] compare = new double[columncnt - 1];
		int[] continuous = new int[columncnt - 1];
		for(int i = 0; i < compare.length ;i++){
			continuous[i] = -1;
			//The gain info is listed
			if(this.InDiscrete[i]){
				compare[i] = this.matrix.DiscreteGain(i,rows);
			} else {
				continuous[i] = this.matrix.ContinuousSeperatorEstimateByGain(i, rows, 100);
				compare[i] = this.matrix.ContinousGain(i, continuous[i], rows);
			}
		}
		QSort sort = new QSort(compare);
		int index = sort.index[compare.length - 1];
		//System.out.println("特征选取" + index + ",信息增益" + sort.data[compare.length - 1] + ".");
		node.character = index;
		//if the feature is discrete
		if(this.InDiscrete[index]){
			QSort clsort = new QSort(this.matrix.getRowsFromColumn(index, rows));
			if(node.parent != null && node.parent.character == index){
				//System.out.println("特征选取" + index + ",检测到重复将陷入循环,且无法排除该特性，选取最大概率值，退出。");
				QSort _clsort = new QSort(this.matrix.getRowsFromColumn(this.matrix.getColumnDimension() - 1, rows));
				node.parent.type = NodeType.LEAF;
				int len = -1;
				for(QSort.SubClass cls : _clsort.QSortClass()){
					if(cls.length > len){
						len = cls.length;
						node.parent.leaftype = (int)cls.data;
					}
				}
				node = null;
				return;
			}
			for(QSort.SubClass cls : clsort.QSortClass()){
				int[] rawindex = new int[cls.set.length];
				for(int i = 0;i < rawindex.length; i++){
					rawindex[i] = rows[cls.set[i]];
				}
				QSort typesort = new QSort(this.matrix.getRowsFromColumn(columncnt - 1, rawindex));
				List<QSort.SubClass> classes = typesort.QSortClass();
				if(classes.size() == 1){
					//System.out.println("特征选取" + index + "\t产生叶子节点" + cls.data  + ".");
					TreeNode trnode = new TreeNode(-1, 0, NodeType.LEAF, cls.data , (int)classes.get(0).data, true);
					node.Add(trnode);
				} else {
					//System.out.println("特征选取" + index + "\t产生子节点" + cls.data  + ".");
					TreeNode trnode = new TreeNode(-1, 0, NodeType.SUBROOT, cls.data, -1, true);
					node.Add(trnode);
					_ID3(rawindex,trnode);
				}
			}
		} else { //if the feature is continuous
			QSort clsort = new QSort(this.matrix.getRowsFromColumn(index, rows));
			int sortsep = 0;
			for(int i = 0 ;i < rows.length; i++){
				if(clsort.index[i] == continuous[index]){
					sortsep = i;
					break;
				}
			}
			//System.out.println("连续特征划分点:" + this.matrix.get(rows[continuous[index]], index) + ".(" + sortsep + ":" + (rows.length - sortsep) + ")");
			if(sortsep == 0 || rows.length - sortsep == 0){
				//System.out.println("连续特征划分点:" + this.matrix.get(rows[continuous[index]], index) + "无法继续划分将进入循环！（退出）");
				QSort _clsort = new QSort(this.matrix.getRowsFromColumn(this.matrix.getColumnDimension() - 1, rows));
				int len = -1;
				for(QSort.SubClass cls : _clsort.QSortClass()){
					if(cls.length > len){
						len = cls.length;
						node.type = NodeType.LEAF;
						node.leaftype = (int)cls.data;
					}
				}
				return;
			}
			int[] former_rows = new int[sortsep];
			int[] latter_rows = new int[rows.length - sortsep];
			for(int i = 0; i < former_rows.length; i++){
				former_rows[i] = rows[clsort.index[i]];
			}
			for(int i = 0; i < latter_rows.length; i++){
				latter_rows[i] = rows[clsort.index[i + sortsep]];
			}
			double nodedata = this.matrix.get(rows[continuous[index]], index);
			
			QSort ftypesort = new QSort(this.matrix.getRowsFromColumn(columncnt - 1, former_rows));
			List<QSort.SubClass> fclasses = ftypesort.QSortClass();
			if(fclasses.size() == 1){
				//System.out.println("连续特征划分点:<" + this.matrix.get(rows[continuous[index]], index) + "\t产生叶子节点.");
				TreeNode trnode = new TreeNode(-1, 0, NodeType.LEAF, nodedata , (int)fclasses.get(0).data, false);
				node.Add(trnode);
			} else {
				//System.out.println("连续特征划分点:<" + this.matrix.get(rows[continuous[index]], index) + "\t产生子节点.");
				TreeNode trnode = new TreeNode(-1, 0, NodeType.SUBROOT, nodedata, -1, false);
				node.Add(trnode);
				_ID3(former_rows,trnode);
			}
			QSort ltypesort = new QSort(this.matrix.getRowsFromColumn(columncnt - 1, latter_rows));
			List<QSort.SubClass> lclasses = ltypesort.QSortClass();
			if(lclasses.size() == 1){
				//System.out.println("连续特征划分点:>=" + this.matrix.get(rows[continuous[index]], index) + "\t产生叶子节点.");
				TreeNode trnode = new TreeNode(-1, 0, NodeType.LEAF, nodedata , (int)lclasses.get(0).data, false);
				node.Add(trnode);
			} else {
				//System.out.println("连续特征划分点:>=" + this.matrix.get(rows[continuous[index]], index) + "\t产生子节点.");
				TreeNode trnode = new TreeNode(-1, 0, NodeType.SUBROOT, nodedata, -1, false);
				node.Add(trnode);
				_ID3(latter_rows,trnode);
			}
		}
	}
}
