package decision_tree;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author wjbnys
 *
 */
public class TreeNode {
	
	public enum NodeType { ROOT, SUBROOT ,LEAF };
	
	//the character used to build sub root
	public int character;
	
	//the reserved param
	public double seperator;
	
	public NodeType type;
	
	public TreeNode parent;
	
	public List<TreeNode> nodes;
	
	public double data;
	
	public int leaftype;
	
	public int index;
	
	public boolean isDecreste;
	
	/**
	 * 
	 */
	public TreeNode(){
		this.character = 0;
		this.seperator = 0;
		this.type = NodeType.ROOT;
		this.data = 0;
		this.parent = null;
		this.nodes = new ArrayList<TreeNode>();
		this.index = -1;
		this.leaftype = -1;
	}
	
	/**
	 * 
	 * @param character
	 * @param seperator
	 * @param type
	 * @param data
	 * @param leaftype
	 * @param isDecreste
	 */
	public TreeNode(int character, double seperator, NodeType type, double data, int leaftype, boolean isDecreste){
		this.character = character;
		this.seperator = seperator;
		this.type = type;
		this.data = data;
		this.parent = null;
		this.nodes = new ArrayList<TreeNode>();
		this.index = -1;
		this.leaftype = leaftype;
		this.isDecreste = isDecreste;
	}
	
	/**
	 * 
	 * @param node
	 */
	public TreeNode(TreeNode node){
		this.character = node.character;
		this.seperator = node.seperator;
		this.type = node.type;
		this.parent = node.parent;
		this.nodes = node.nodes;
		this.data = node.data;
		this.leaftype = node.leaftype;
		this.isDecreste = node.isDecreste;
	}
	
	/**
	 * 
	 * @param node
	 */
	public void Add(TreeNode node){
		if(this.type == NodeType.LEAF){
			this.type = NodeType.SUBROOT;
		}
		node.type = node.type == NodeType.ROOT ? NodeType.LEAF : node.type ;
		this.nodes.add(node);
		node.index = this.nodes.size() - 1;
		node.parent = this;
	}
}
