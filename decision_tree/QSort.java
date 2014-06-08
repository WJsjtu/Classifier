package decision_tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author wjbnys
 *
 */
public class QSort {

	public double[] data;
	public int[] index;
	
	/**
	 * The class is used to sort the item efficiently
	 * @author wjbnys
	 *
	 */
	public class SortItem implements Comparable<SortItem>{
		int index;
		double data;
		public SortItem(int index, double data){
			this.index = index;
			this.data = data;
		}
		@Override
		public int compareTo(SortItem item){
			if(this.data < item.data){
				return -1;
			} else if(this.data > item.data){
				return 1;
			}
			return 0;
		}
	}
	
	/**
	 * The class describe the classes in a feature
	 * @author wjbnys
	 *
	 */
	public class SubClass{
		public double data;
		public int index;
		public int length;
		public int[] set;
		
		public SubClass(double data, int index){
			this.data = data;
			this.index = index;
			this.length = 1;
		}
	}
	
	/**
	 * Constructor
	 * @param _data
	 */
	public QSort(double[] _data){
		this.data = new double[_data.length];
		this.index = new int[_data.length];
		SortItem[] list = new SortItem[_data.length];
		for(int i = 0;i< data.length; i++){
			list[i] = new SortItem(i, _data[i]);
		}
		Arrays.sort(list);
		for(int i = 0; i< _data.length; i++){
			this.data[i] = list[i].data;
			this.index[i] = list[i].index;
		}
	}

	/**
	 * Get the classes of a feature
	 * @return
	 */
	public List<SubClass> QSortClass(){
		List<SubClass> result = new ArrayList<SubClass>();
		if(this.data.length == 0){
			return result;
		}
		SubClass current = new SubClass(this.data[0], 0);
		for(int i = 1; i< this.data.length; i++){
			if(this.data[i] == current.data){
				current.length++;
			} else {
				current.set = new int[current.length];
				for(int iterator = current.index, j=0; j < current.length; iterator++,j++){
					current.set[j] = this.index[iterator];
				}
				result.add(current);
				current = new SubClass(this.data[i], i);
			}
		}
		current.set = new int[current.length];
		for(int iterator = current.index, j=0; j < current.length; iterator++,j++){
			current.set[j] = this.index[iterator];
		}
		result.add(current);
		return result;
	}
	
}
