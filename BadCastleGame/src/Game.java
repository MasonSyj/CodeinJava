import java.util.Scanner;

public class Game {
    private Room currentRoom;
    
    public Game(){
        createRooms(); 
    }
    
    private void createRooms(){
        Room outside, lobby, pub, study, bedroom;
        
        outside = new Room("outside the garden");
        lobby = new Room("lobby");
        pub = new Room("pub");
        study = new Room("study");
        bedroom = new Room("bedroom");
        
        outside.setExits(null, lobby, study, pub);
        lobby.setExits(null, null, null, outside);
        pub.setExits(null, outside, null, null);
        study.setExits(outside, bedroom, null, null); 
        bedroom.setExits(null, null, null, study);
        
        currentRoom = outside;
    }

    public void printWelcome(){
        System.out.println();
        System.out.println("Welcome to the castle");
        System.out.println("This is a super boring game");
        System.out.println("if you need type, type 'help'");
        System.out.println();
        System.out.println("Now you are at: " + currentRoom);
        System.out.print("Your exits available: ");
        if (currentRoom.northExit != null){
            System.out.print("north ");
        }
        if (currentRoom.southExit != null){
            System.out.print("south ");
        }
        if (currentRoom.westExit != null){
            System.out.print("west ");
        }
        if (currentRoom.eastExit != null){
            System.out.print("east ");
        }
        System.out.println();
    }

    public void goRoom(String direction){
        Room nextRoom = null;
        if (direction.equals("north")){
            nextRoom = currentRoom.northExit;
        }
        if (direction.equals("south")){
            nextRoom = currentRoom.southExit;
        }
        if (direction.equals("west")){
            nextRoom = currentRoom.westExit;
        }
        if (direction.equals("east")){
            nextRoom = currentRoom.eastExit;
        }

        if (nextRoom == null){
            System.out.println("There's no room here");
        } else {
            currentRoom = nextRoom;
            System.out.println("Now you are at: " + currentRoom);
            System.out.print("Your exits available: ");
            if (currentRoom.northExit != null){
                System.out.print("north ");
            }
            if (currentRoom.southExit != null){
                System.out.print("south ");
            }
            if (currentRoom.westExit != null){
                System.out.print("west ");
            }
            if (currentRoom.eastExit != null){
                System.out.print("east ");
            }
            System.out.println();
        }
    }

    public void printHelp(){
        System.out.println("Are you lost? Commands you can type: go bye help");
        System.out.println("for example: go east");
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
            } else if (words[0].equals("go")){
                game.goRoom(words[1]);
            } else if (words[0].equals("bye")){
                break;
            }
        }

        System.out.println("thank you for your time");
        in.close();
    }
}
