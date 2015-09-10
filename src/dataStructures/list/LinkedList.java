package dataStructures.list;

import edu.utexas.testera.annotations.TestEra;

@TestEra (invariants={"all l:LinkedList | all n : l.header.*next | n !in n.^next",
					  "all l:LinkedList | #l.header.*next = l.size"} )
public class LinkedList {
	public ListNode header;
	public int size = 0;
	
	
	@TestEra (preConditions={"x >= 0", 
			  "all n: ListNode | n in LinkedList.header.*next"},
			  postConditions={"LinkedList.size` = LinkedList.size + 1"},
			  runCommand="1 LinkedList, 3 ListNode, 3 int"
	)
	public void addNode(int x) {
		ListNode n = new ListNode();
		n.value = x;
		
		n.next = header;
		
		header = n;
		
		size++;
	}
}
