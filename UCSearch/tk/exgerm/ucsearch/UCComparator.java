package tk.exgerm.ucsearch;

import java.util.Comparator;

public class UCComparator implements Comparator<UCNode> {

	@Override
	public int compare(UCNode node1, UCNode node2) {
		if(node1.getWeight() < node2.getWeight())
			 return -1;
		else if(node1.getWeight() == node2.getWeight())
			 return 0;
		else return 1;
	}

}
