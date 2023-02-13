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




}