import java.util.List;

public class ListNode {
	int val;
	ListNode next;

	public ListNode(int val, ListNode next){
		this.val = val;
		this.next = next;
	}

	public ListNode(){
		this(0, null);
	}

	public ListNode(int val){
		this(val, null);
	}

	public ListNode getNext(){
		return this.next;
	}

	public int getVal(){
		return this.val;
	}

/*
	public static void main(String[] args){
		ListNode ln1 = new ListNode();
		ListNode ln2 = new ListNode(3);
		ListNode ln3 = new ListNode(4);
		ListNode ln4 = new ListNode(200, ln3);

		System.out.println(ln1.getVal() + " "+ ln2.getVal() +  " "+ ln3.getVal() +  " "+ ln4.getVal());


	}
*/

}