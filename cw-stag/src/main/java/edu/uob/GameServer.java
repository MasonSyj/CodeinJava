package edu.uob;

import com.alexmerz.graphviz.ParseException;
import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;
import com.alexmerz.graphviz.objects.Edge;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.*;

/** This class implements the STAG server. */
public final class GameServer {

    private static final char END_OF_TRANSMISSION = 4;

    private HashMap<String, Location> locationHashMap;
    private Location currentLocation;

    private Location startLocation;
    private HashMap<String, Player> playerHashMap;

    private Set<GameAction> gameActionSet;
    private String gameActionResult;

    public static void main(String[] args) throws IOException {
        File entitiesFile = Paths.get("config" + File.separator + "basic-entities.dot").toAbsolutePath().toFile();
        File actionsFile = Paths.get("config" + File.separator + "basic-actions.xml").toAbsolutePath().toFile();
        GameServer server = new GameServer(entitiesFile, actionsFile);
        server.blockingListenOn(8888);
    }

    /**
    * KEEP this signature (i.e. {@code edu.uob.GameServer(File, File)}) otherwise we won't be able to mark
    * your submission correctly.
    *
    * <p>You MUST use the supplied {@code entitiesFile} and {@code actionsFile}
    *
    * @param entitiesFile The game configuration file containing all game entities to use in your game
    * @param actionsFile The game configuration file containing all game actions to use in your game
    *
    */
    public GameServer(File entitiesFile, File actionsFile) {
        // TODO implement your server logic here
        playerHashMap = new HashMap<String, Player>();
        Parser parser = new Parser();
        FileReader reader = null;
        try {
            reader = new FileReader("config" + File.separator + "basic-entities.dot");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            parser.parse(reader);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Graph wholeDocument = parser.getGraphs().get(0);
        ArrayList<Graph> sections = wholeDocument.getSubgraphs();

        locationHashMap = new HashMap<String, Location>();

        // locations
        ArrayList<Graph> locations = sections.get(0).getSubgraphs();
        for (Graph location: locations){
            Node locationDetails = location.getNodes(false).get(0);
            String locationName = locationDetails.getId().getId();
            currentLocation = new Location(locationName, locationDetails.getAttribute("description"));
            if (startLocation == null) {
                startLocation = currentLocation;
            }

            ArrayList<Graph> others = location.getSubgraphs();

            loadGameEntity(currentLocation, others, "artefacts");
            loadGameEntity(currentLocation, others, "furniture");
            loadGameEntity(currentLocation, others, "characters");
            locationHashMap.put(currentLocation.getName(), currentLocation);

            System.out.println(currentLocation.toString());
            System.out.println(currentLocation.showInformation());
        }

        currentLocation = locationHashMap.values().stream().toList().get(0);

        //load Path
        ArrayList<Edge> paths = sections.get(1).getEdges();
        for (Edge edge: paths){
            Node fromLocation = edge.getSource().getNode();
            Node toLocation = edge.getTarget().getNode();
            String fromLocationName = fromLocation.getId().getId();
            String toLocationName = toLocation.getId().getId();
            if (locationHashMap.containsKey(fromLocationName)
                    && locationHashMap.containsKey(toLocationName)){
                locationHashMap.get(fromLocationName).addExit(locationHashMap.get(toLocationName));
            }
            System.out.println(fromLocationName + " -> " + toLocationName);
        }
        gameActionSet = new HashSet<GameAction>();
        //load actions
        try {
          DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
          Document document = builder.parse("config" + File.separator + "basic-actions.xml");
          Element root = document.getDocumentElement();
          NodeList actions = root.getChildNodes();
          // only the odd items are actually actions - 1, 3, 5 etc.
          for (int i = 1; i < actions.getLength(); i += 2){
              GameAction currentGameAction = new GameAction();
              Element currentActionElement = (Element) actions.item(i);
              System.out.println("-----------------------------");
              loadActionItem(currentGameAction, currentActionElement, "triggers");
              loadActionItem(currentGameAction, currentActionElement, "subjects");
              loadActionItem(currentGameAction, currentActionElement, "consumed");
              loadActionItem(currentGameAction, currentActionElement, "produced");
//              System.out.println("-----------------------------");
              System.out.println(currentGameAction.toString());
              gameActionSet.add(currentGameAction);
          }
      } catch(ParserConfigurationException pce) {
            System.out.println("ParserConfigurationException was thrown when attempting to read basic actions file");
      } catch(SAXException saxe) {
            System.out.println("SAXException was thrown when attempting to read basic actions file");
      } catch(IOException ioe) {
            System.out.println("IOException was thrown when attempting to read basic actions file");
      }

        currentLocation = startLocation;
    }

    public void loadActionItem(GameAction gameAction, Element gameActionElement, String elementName){
//        System.out.println("+++++++" + elementName + "++++++");
        Element certainType = (Element) gameActionElement.getElementsByTagName(elementName).item(0);
        List<String> listofItems = new ArrayList<String>();
        NodeList nodeList;
        if (elementName.equals("triggers")){
            nodeList = certainType.getElementsByTagName("keyphrase");
        } else {
            nodeList = certainType.getElementsByTagName("entity");
        }
        int len = nodeList.getLength();
        for (int i = 0; i < len; i++){
            String elementSpecificName = nodeList.item(i).getTextContent();
//            System.out.println(elementSpecificName);
            listofItems.add(elementSpecificName);
        }
        for (int i = 0; i < listofItems.size(); i++) {
            if (elementName.equals("triggers")) {
                gameAction.addTrigger(listofItems.get(i));
            } else if (elementName.equals("subjects")) {
                gameAction.addSubject(listofItems.get(i));
            } else if (elementName.equals("consumed")) {
                gameAction.addConsumable(listofItems.get(i));
            } else if (elementName.equals("produced")) {
                gameAction.addProduction(listofItems.get(i));
            }
        }
    }

    public void loadGameEntity(Location location, ArrayList<Graph> graph, String name){
        List<Graph> graphs = graph.stream().filter(currentGraph -> currentGraph.getId().getId().equals(name)).toList();
        if (graphs.size() == 1){
            for (Node item: graphs.get(0).getNodes(false)){
                if (name.equals("artefacts")){
                    location.addArtefact(new Artefact(item.getId().getId(), item.getAttribute("description")));
                } else if (name.equals("furniture")){
                    location.addFurniture(new Furniture(item.getId().getId(), item.getAttribute("description")));
                } else if (name.equals("characters")){
                    location.addCharacter(new Character(item.getId().getId(), item.getAttribute("description")));
                }
            }
        }
    }

    /**
    * KEEP this signature (i.e. {@code edu.uob.GameServer.handleCommand(String)}) otherwise we won't be
    * able to mark your submission correctly.
    *
    * <p>This method handles all incoming game commands and carries out the corresponding actions.
    */
    public String handleCommand(String command) {
        // TODO implement your server logic here
        String username = "";
        for (int i = 0; i < command.length(); i++){
            if (command.charAt(i) == ':'){
               username = command.substring(0, i);
               command = command.substring(i + 1, command.length());
            }
        }
        String[] tokens = command.trim().replaceAll("\\s+", " ").split(" ");
        if (!playerHashMap.containsKey(username)){
            playerHashMap.put(username, new Player(username, ""));
            playerHashMap.get(username).setCurrentLocation(startLocation);
        }

        Player currentPlayer = playerHashMap.get(username);

        System.out.println("Command: " + command);
        for (String str: tokens){
            System.out.println(str);
        }
        if (tokens.length == 0){
            return "";
        } else if (tokens[0].equals("inventory") || tokens[0].equals("inv")){
            return currentPlayer.displayInventory();
        } else if (tokens[0].equals("get") && tokens.length == 2){
            if (currentLocation.getArtefacts().containsKey(tokens[1])){
                Artefact currentArtefact = currentLocation.getArtefacts().get(tokens[1]);
                currentPlayer.addArtefact(currentArtefact);
                currentLocation.getArtefacts().remove(currentArtefact.getName());
                return "get " + currentArtefact.getName();
            } else{
                return "failed to get, you may input the wrong name or it doesn't exist at all.";
            }
        } else if (tokens[0].equals("drop") && tokens.length == 2){
            Artefact artefact = currentPlayer.removeArtefact(tokens[1]);
            if (artefact != null){
                currentLocation.addArtefact(artefact);
                return "drop " + artefact.getName();
            } else{
                return "doesn't exist this artefact";
            }
        } else if (tokens[0].equals("goto") && tokens.length == 2){
            if (currentLocation.getExits().containsKey(tokens[1])){
                currentLocation = currentLocation.getExits().get(tokens[1]);
                return "you are now at " + currentLocation.getName();
            } else {
                return "You can't go to " + tokens[1];
            }
        } else if (tokens[0].equals("look")){
            return currentPlayer.getCurrentLocation().showInformation();
        } else if (tokens.length >= 2){
            gameActionResult = null;
            parseGameAction(tokens, username);
            if (gameActionResult != null){
                return gameActionResult;
            } else {
                return "Failed to execute basic command";
            }
        } else{
            return "Failed to execute basic command";
        }
    }

    public void parseGameAction(String[] tokens, String currentPlayerName) {
        System.out.println("Begin parsing GameAction: ");
        Set<String> subjects = new HashSet<String>();
        for (int i = 0; i < tokens.length; i++){
           subjects.add(tokens[i]);
        }
        String subjectInString = subjects.stream().sorted((a, b) -> a.compareTo(b)).toList().toString();
        System.out.println("subjectInString" + subjectInString);
        for (GameAction gameAction: gameActionSet){
            if (matchedGameAction(gameAction, subjects)){
                System.out.println("following GameAction matched: ");
                System.out.println(gameAction.getTriggers().toString() + " " + gameAction.getSubjects().toString());
                executeMatchedGameAction(gameAction, currentPlayerName);
                return;
            }else{
                System.out.println("didn't match the game Action");
                boolean result = executeGameAction(gameAction, subjects, currentPlayerName);
                if (result == true){
                    System.out.println("correctgameAction: " + gameAction.printSubject());
                    return;
                }
            }

        }
    }

    public boolean matchedGameAction(GameAction gameAction, Set<String> subjects){
        boolean hasTrigger = false;
        for (String trigger: subjects){
            if (gameAction.getTriggers().contains(trigger)){
                hasTrigger = true;
                break;
            }
        }
        if (hasTrigger == false){
            return false;
        }

        for (String subject: subjects){
           if (gameAction.getSubjects().contains(subject)) {
               return true;
           }
        }
        return false;
    }

    public void executeMatchedGameAction(GameAction gameAction, String playerName){
        Map<String, GameEntity> searchingPool = buildSearchingPool(gameAction, playerName);
        for (String subject: gameAction.getSubjects()){
            if (!searchingPool.containsKey(subject)){
                return;
            }
        }

        StringBuilder result = new StringBuilder();

        Player currentPlayer = playerHashMap.get(playerName);

        for (String consumable: gameAction.getConsumables()){
            result.append("Consumed: " + consumable);
            result.append("  \n");
            if (consumable.equals("health")){
                currentPlayer.decreaseHealth();
                if (currentPlayer.getHealth() == 0){
                    currentPlayer.resetHealth();
                    currentPlayer.setCurrentLocation(startLocation);
                    currentLocation = startLocation;
                    result.append("You died, go back to the start Location. \n");
                }
            }
            if (currentPlayer.getInventory().containsKey(consumable)){
                currentPlayer.getInventory().remove(consumable);
            } else if (currentLocation.getCharacters().containsKey(consumable)){
                currentLocation.getCharacters().remove(consumable);
            } else if (currentLocation.getFurnitures().containsKey(consumable)){
                currentLocation.getFurnitures().remove(consumable);
            } else if (currentLocation.getArtefacts().containsKey(consumable)){
                currentLocation.getArtefacts().remove(consumable);
            } else if (currentLocation.getName().equals(consumable)){
                currentLocation = null;
            }
        }

        for (String production: gameAction.getProductions()){
            if (locationHashMap.containsKey(production)){
                currentLocation.addExit(locationHashMap.get(production));
                result.append("\nnew exit: ");
                result.append(production);
            } else {
                locationHashMap.get("storeroom").getArtefacts().put(production, new Artefact(production, ""));
                result.append("\nnew Artefacts at storeroom");
                result.append(production);
            }
        }
        System.out.println("--------------------------------");
        System.out.println("executeMatchedGameAction: ");
        System.out.println(result.toString());
        System.out.println("--------------------------------");
        gameActionResult = result.toString();
    }

    public Map<String, GameEntity> buildSearchingPool(GameAction gameAction, String playerName){
        Map<String, GameEntity> searchingPool = new HashMap<String, GameEntity>();
        for (String str: gameAction.getTriggers()){
            searchingPool.put(str, null);
        }

        for (String str: playerHashMap.get(playerName).getInventory().keySet()){
            searchingPool.put(str, playerHashMap.get(playerName).getInventory().get(str));
        }

        for (String str: currentLocation.getFurnitures().keySet()){
            searchingPool.put(str, currentLocation.getFurnitures().get(str));
        }

        for (String str: currentLocation.getArtefacts().keySet()){
            searchingPool.put(str, currentLocation.getArtefacts().get(str));
        }

        for (String str: currentLocation.getCharacters().keySet()){
            searchingPool.put(str, currentLocation.getCharacters().get(str));
        }

        searchingPool.put(currentLocation.getName(), currentLocation);
        return searchingPool;

    }

    public boolean executeGameAction(GameAction gameAction, Set<String> subjects, String playerName){
        Map<String, GameEntity> searchingPool = buildSearchingPool(gameAction, playerName);

        int cnt = 0;
        for (String subject: subjects){
            System.out.println("current subject / trigger: " + subject);
            if (searchingPool.containsKey(subject)){
                System.out.println("subject existed in the searchingPool:" + subject);
                cnt++;
            }
        }

        if (cnt != gameAction.getSubjects().size() + 1){
            return false;
        }
        System.out.println("succeed to match");
        StringBuilder result = new StringBuilder();

        for (String consumable: gameAction.getConsumables()){
            result.append("Consumed: " + consumable);
            result.append("  \n");
            if (playerHashMap.get(playerName).getInventory().containsKey(consumable)){
                playerHashMap.get(playerName).getInventory().remove(consumable);
            } else if (currentLocation.getCharacters().containsKey(consumable)){
                currentLocation.getCharacters().remove(consumable);
            } else if (currentLocation.getFurnitures().containsKey(consumable)){
                currentLocation.getFurnitures().remove(consumable);
            } else if (currentLocation.getArtefacts().containsKey(consumable)){
                currentLocation.getArtefacts().remove(consumable);
            } else if (currentLocation.getName().equals(consumable)){
                currentLocation = null;
            }
        }

        for (String production: gameAction.getProductions()){
            if (locationHashMap.containsKey(production)){
                currentLocation.addExit(locationHashMap.get(production));
                result.append("\nnew exit: ");
                result.append(production);
            } else {
                locationHashMap.get("storeroom").getArtefacts().put(production, new Artefact(production, ""));
                result.append("\nnew Artefacts at storeroom");
                result.append(production);
            }
        }
        gameActionResult = result.toString();
        return true;
    }
    //  === Methods below are there to facilitate server related operations. ===

    /**
    * Starts a *blocking* socket server listening for new connections. This method blocks until the
    * current thread is interrupted.
    *
    * <p>This method isn't used for marking. You shouldn't have to modify this method, but you can if
    * you want to.
    *
    * @param portNumber The port to listen on.
    * @throws IOException If any IO related operation fails.
    */
    public void blockingListenOn(int portNumber) throws IOException {
        try (ServerSocket s = new ServerSocket(portNumber)) {
            System.out.println("Server listening on port " + portNumber);
            while (!Thread.interrupted()) {
                try {
                    blockingHandleConnection(s);
                } catch (IOException e) {
                    System.out.println("Connection closed");
                }
            }
        }
    }

    /**
    * Handles an incoming connection from the socket server.
    *
    * <p>This method isn't used for marking. You shouldn't have to modify this method, but you can if
    * * you want to.
    *
    * @param serverSocket The client socket to read/write from.
    * @throws IOException If any IO related operation fails.
    */
    private void blockingHandleConnection(ServerSocket serverSocket) throws IOException {
        try (Socket s = serverSocket.accept();
        BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()))) {
            System.out.println("Connection established");
            String incomingCommand = reader.readLine();
            if(incomingCommand != null) {
                System.out.println("Received message from " + incomingCommand);
                String result = handleCommand(incomingCommand);
                writer.write(result);
                writer.write("\n" + END_OF_TRANSMISSION + "\n");
                writer.flush();
            }
        }
    }
}
