package decision_tree;

import java.util.List;

public class Column{
	
	public double[] data;
	
	public Column(double[] data){
		this.data = data;
	}

	public double DiscreteInfo(){
		double info = 0;
		QSort sort = new QSort(this.data.clone());
		List<QSort.SubClass> classes = sort.QSortClass();
		for(QSort.SubClass cls : classes){
			double pr= (double)cls.length / (double)(data.length);
			info += 0 - pr * Math.log(pr) / Math.log(2);
		}
		return info;
	}
}

