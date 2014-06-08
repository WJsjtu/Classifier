package decision_tree;

import Jama.Matrix;

public class Tree extends TreeNode{
	
	/**
	 * setup a tree
	 */
	public Tree(){
		super();
	}
	
	/**
	 * Construct from a matrix
	 * @param root The root node
	 */
	public Tree(TreeNode root){
		super(root);
	}
	
	/**
	 * private function to map the tree
	 * @param str
	 * @param nd
	 * @param level
	 * @return
	 */
	private String _toString(String str, TreeNode nd, int level){
		String prefix = "";
		for(int i = 0; i < level - 1 ; i++){
			prefix += "        |";
		}
		prefix += "\n" + prefix;
		if(nd.type == NodeType.LEAF){
			return  str + prefix + "__" + nd.data + " => " + nd.leaftype + "\n";
		} else {
			if(level == 1){
				prefix += "Root:Attr(" + nd.character + ")\n";
			} else {
				prefix += "__" + nd.data + "__Attr(" + nd.character + ")\n";
			}
			for(TreeNode node : nd.nodes){
				prefix = _toString(prefix, node,level+1);
			}
		}
		return str + prefix;
	}
	
	/**
	 * 
	 * @param row
	 * @return the type of a test row
	 */
	public int GetType(double[] row){
		return _GetType(this, row);
	}
	
	/**
	 * 
	 * @param node
	 * @param row
	 * @return
	 */
	private int _GetType(TreeNode node, double[] row){
		if(node.isDecreste){
			if(node.type == NodeType.LEAF){
				return node.leaftype;
			} else {
				for(TreeNode trnd : node.nodes){
					if(trnd.data == row[trnd.character]){
						return _GetType(trnd, row);
					}
				}
			}
		} else {
			if(node.type == NodeType.LEAF){
				return node.leaftype;
			} else {
				TreeNode left = node.nodes.get(0);
				TreeNode right = node.nodes.get(1);
				if(row[node.character] < left.data){
					return _GetType(left, row);
				} else {
					return _GetType(right, row);
				}
			}
		}
		return -1;
	}
	
	/**
	 * Test a matrix
	 * @param 
	 * @return
	 */
	public double[] MatrixTest(Matrix m){
		double[] result = new double[m.getRowDimension()];
		for(int i = 0; i < m.getRowDimension(); i++){
			double[] temp = new double[m.getColumnDimension() - 1];
			for(int j = 0; j< temp.length; j++){
				temp[j] = m.get(i, j);
			}
			result[i] = this.GetType(temp);
		}
		return result;
	}
	
	/**
	 * Override the toString function
	 */
	@Override
	public String toString(){
		String str = "";
		return _toString(str, this, 1);
	}

}
