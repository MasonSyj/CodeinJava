import java.util.*;

class ListWorld {
    static void printList(List<Robot> list) {
        System.out.println("List is:");
        //implement by for each
        int i = 0;
        for (Robot robot: list){
            robot.toDefaultString();
        }
        System.out.println("------------------");
    }

    public static void main(String args[]) {
        //try using the arraylist's methods:add, remote, contain, addall
        List<Robot> list = new ArrayList<Robot>();
        list.add(new Robot("Chinese", 11, 99.9f));
        list.add(new Robot("Japanese", 23, 69.9f));
        list.add(new Robot("American", 15, 233f));
        printList(list);
        list.addAll(3, list);
        printList(list);
    }
}

/*
    List<Robot> list = new ArrayList<>();
    Robot c3po = new Robot("C3PO");
        list.add(c3po);
                list.add(new CarrierRobot());
                printList(list);
                list.add(1, new Robot("C4PO"));
                printList(list);
                Robot removed = list.remove(2);
                System.out.println("Removed:"+ removed.name);
                printList(list);
                System.out.println("C3PO in list?:" + list.contains(c3po));
                list.addAll(0,list);
                printList(list);
                */
