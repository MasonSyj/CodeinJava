import java.sql.SQLOutput;
import java.util.LinkedList;

class MyLinkedList {


	private ListNode head;
	private int size;

	public MyLinkedList(int val){
		head = new ListNode(val);
		size = 1;
	}

	public MyLinkedList(){
		head = null;
		size = 0;
	}

	public int get(int index) {
		if (index < 0 || index >= this.size){
			return -1;
		}
		ListNode temp = head;
		for (int i = 0; i < index; i++){
			temp = temp.next;
		}
		return temp.val;
	}

	public void addAtHead(int val) {
		addAtIndex(0, val);
	}

	public void addAtTail(int val) {
		addAtIndex(this.size, val);
	}

	public void addAtIndex(int index, int val) {
		if (index < 0 || index > this.size){
			return;
		}

		if (index == 0){
			ListNode temp = this.head;
			this.head = new ListNode(val);
			head.next = temp;
			this.increment();
			return;
		}

		ListNode temp = this.head;
		ListNode newnode = new ListNode(val);

		for (int i = 0; i < index - 1; i++){
			temp = temp.next;
		}

		if (temp.next != null){
			newnode.next = temp.next;
			temp.next = newnode;
		}else{
			temp.next = newnode;
		}
		this.increment();
	}

	public void deleteAtIndex(int index) {
		if (index < 0 || index >= size){
			return;
		}

		if (index == 0){
			this.head = this.head.next;
			this.decrement();
			return;
		}

		ListNode temp = this.head;
		for (int i = 0; i < index - 1; i++){
			temp = temp.next;
		}

		if (temp.next.next == null){
			temp.next = null;
		}else{
			temp.next = temp.next.next;
		}
		this.decrement();
		return;

	}

	public String toDefaultString(){
		String res = new String();
		ListNode temp = this.head;
		for (int i = 0; i < this.size; i++){
			res +=  Integer.toString(temp.getVal()) + " ";
			temp = temp.next;
		}
		return res;
	}

	public void increment(){
		this.size++;
	}

	public void decrement(){
		this.size--;
	}


}

/**
 * Your MyLinkedList object will be instantiated and called as such:
 * MyLinkedList obj = new MyLinkedList();
 * int param_1 = obj.get(index);
 * obj.addAtHead(val);
 * obj.addAtTail(val);
 * obj.addAtIndex(index,val);
 * obj.deleteAtIndex(index);
 */