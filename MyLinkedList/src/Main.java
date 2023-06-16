public class Main {
    public static void main(String[] args) {
        /*
        ListNode ln1 = new ListNode();
        ListNode ln2 = new ListNode(3);
        ListNode ln3 = new ListNode(4);
        ListNode ln4 = new ListNode(200, ln3);

        System.out.println(ln1.getVal() + " " + ln2.getVal() + " " + ln3.getVal() + " " + ln4.getVal());
        */
        MyLinkedList list = new MyLinkedList();
        list.addAtIndex(0, 3);
        list.addAtIndex(0, 4);
        System.out.println(list.toDefaultString());
        list.addAtIndex(0, 5);
        System.out.println(list.toDefaultString());
        list.addAtIndex(1, 10);
        System.out.println(list.toDefaultString());
        list.addAtIndex(2, 7);

        System.out.println(list.toDefaultString());

        list.addAtTail(8);
        list.addAtTail(9);

        System.out.println(list.toDefaultString());

        list.deleteAtIndex(0);
        list.deleteAtIndex(0);

        System.out.println(list.toDefaultString());

        System.out.println(list.get(0));
        System.out.println(list.get(1));
        System.out.println(list.get(2));

        MyLinkedList list2 = new MyLinkedList();
        list2.addAtHead(1);
        list2.addAtTail(3);
        list2.addAtIndex(1, 2);
        System.out.println(list2.toDefaultString());
        System.out.println(list2.get(1));
        list2.deleteAtIndex(1);
        System.out.println(list2.get(1));
        System.out.println(list2.toDefaultString());
    }
}
