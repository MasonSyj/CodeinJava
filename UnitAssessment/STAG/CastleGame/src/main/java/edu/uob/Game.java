package edu.uob;
import java.util.Scanner;
import java.util.Arrays;
public class Game {
    private Room currentRoom;
    
    public Game(){
        createRooms(); 
    }
    
    private void createRooms(){
        Room outside, lobby, pub, study, bedroom;
        
        outside = new Room("outside");
        lobby = new Room("lobby");
        pub = new Room("pub");
        study = new Room("study");
        bedroom = new Room("bedroom");
/*        
        outside.setExits(null, lobby, study, pub);
        lobby.setExits(null, null, null, outside);
        pub.setExits(null, outside, null, null);
        study.setExits(outside, bedroom, null, null); 
        bedroom.setExits(null, null, null, study);
*/        
        outside.setExits(Arrays.asList(lobby, study, pub));
        lobby.setExits(Arrays.asList(outside));
        pub.setExits(Arrays.asList(outside));
        study.setExits(Arrays.asList(outside, bedroom));
        bedroom.setExits(Arrays.asList(study));

        currentRoom = outside;
    }

    public void printWelcome(){
        qSystem.out.println();
        System.out.println("Welcome to the castle");
        System.out.println("This is a super boring game");
        System.out.println("if you need type, type 'help'");
        System.out.println();
        System.out.println("Now you are at: " + currentRoom);
        System.out.print("Your exits available: ");
        System.out.println(currentRoom.displayExits());
        System.out.println();
    }

    public void goRoom(String direction){
        Room nextRoom = currentRoom.goRoom(direction);
        
        if (nextRoom == null){
            System.out.println("There's no room here");
        } else {
            currentRoom = nextRoom;
            System.out.println("Now you are at: " + currentRoom);
            System.out.print("Your exits available: ");
            System.out.println(currentRoom.displayExits());
            System.out.println();
        }
    }

    public void printHelp(){
        System.out.println("Are you lost? Commands you can type: go bye help");
        System.out.println("e.g. go east");
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Game game = new Game();
        game.printWelcome();

        while (true) {
            String line = in.nextLine();
            String[] words = line.split(" ");
            if (words[0].equals("help")){
                game.printHelp();
            } else if (words[0].equals("bye")){
                break;
            } else if (words[0].equals("go")){
                if (words.length == 1){
                    System.out.println("You don't know where to go.");
                } else {
                    game.goRoom(words[1]);
                }
            } else {
                System.out.println("Unknown Command");
            }
        }

        System.out.println("thank you for your time");
        in.close();
    }
}
