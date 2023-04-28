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

    public static List<String> basicCommands = Arrays.asList("look", "goto", "drop", "get", "inv", "inventory");

    private HashMap<String, Location> locationHashMap;
    private Location currentLocation;
    private Player currentPlayer;
    private Location startLocation;
    private HashMap<String, Player> playerHashMap;

    private HashMap<String, HashSet<GameAction>> actions;

    private String gameActionResult;
    
    private Set<String> entities;

    public static void main(String[] args) throws IOException {
        File entitiesFile = Paths.get("config" + File.separator + "basic-entities.dot").toAbsolutePath().toFile();
        File actionsFile = Paths.get("config" + File.separator + "basic-actions.xml").toAbsolutePath().toFile();
        GameServer server = new GameServer(entitiesFile, actionsFile);
        server.blockingListenOn(8888);
    }

    public ArrayList<Graph> getSections(File entitiesFile) {
        Parser parser = new Parser();
        FileReader reader;

        try {
            reader = new FileReader(entitiesFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            parser.parse(reader);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Graph wholeDocument = parser.getGraphs().get(0);
        return wholeDocument.getSubgraphs();
    }

    public HashMap<String, Location> loadLocations(ArrayList<Graph> locations){
        HashMap<String, Location> answer = new HashMap<String, Location>();
        for (Graph location: locations){
            Node locationDetails = location.getNodes(false).get(0);
            String locationName = locationDetails.getId().getId();
            currentLocation = new Location(locationName, locationDetails.getAttribute("description"));

            // set the start location
            if (startLocation == null) {
                startLocation = currentLocation;
            }

            ArrayList<Graph> locationEntities = location.getSubgraphs();
            loadGameEntity(currentLocation, locationEntities, "artefacts");
            loadGameEntity(currentLocation, locationEntities, "furniture");
            loadGameEntity(currentLocation, locationEntities, "characters");
            answer.put(currentLocation.getName(), currentLocation);
        }
        return answer;
    }

    public void loadPaths(ArrayList<Edge> paths){
        for (Edge edge: paths){
            String fromLocationName = edge.getSource().getNode().getId().getId();
            String toLocationName = edge.getTarget().getNode().getId().getId();
            if (locationHashMap.containsKey(fromLocationName)
                    && locationHashMap.containsKey(toLocationName)){
                locationHashMap.get(fromLocationName).addExit(locationHashMap.get(toLocationName));
            }
        }
    }

    public HashMap<String, HashSet<GameAction>> loadActions(File actionsFile) {
        HashMap<String, HashSet<GameAction>> answer = new HashMap<String, HashSet<GameAction>>();

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(actionsFile);
            Element root = document.getDocumentElement();
            NodeList actionList = root.getChildNodes();
            // only the odd items are actually actions - 1, 3, 5 etc.
            for (int i = 1; i < actionList.getLength(); i += 2){
                GameAction currentGameAction = new GameAction();
                Element currentActionElement = (Element) actionList.item(i);
                loadActionItem(currentGameAction, currentActionElement, "triggers");
                loadActionItem(currentGameAction, currentActionElement, "subjects");
                loadActionItem(currentGameAction, currentActionElement, "consumed");
                loadActionItem(currentGameAction, currentActionElement, "produced");
                currentGameAction.setNarration(currentActionElement.getElementsByTagName("narration").item(0).getTextContent());
                for (String trigger: currentGameAction.getTriggers()){
                    if (!answer.containsKey(trigger)){
                        answer.put(trigger, new HashSet<GameAction>());
                    }
                    answer.get(trigger).add(currentGameAction);
                }
            }
        } catch(ParserConfigurationException pce) {
            System.out.println("ParserConfigurationException was thrown when attempting to read basic actions file");
        } catch(SAXException saxe) {
            System.out.println("SAXException was thrown when attempting to read basic actions file");
        } catch(IOException ioe) {
            System.out.println("IOException was thrown when attempting to read basic actions file");
        }

        return answer;
    }
    public void loadActionItem(GameAction gameAction, Element gameActionElement, String elementName){
        Element certainType = (Element) gameActionElement.getElementsByTagName(elementName).item(0);
        List<String> listofItems = new ArrayList<String>();

        NodeList nodeList;
        if (elementName.equals("triggers")){
            nodeList = certainType.getElementsByTagName("keyphrase");
        } else {
            nodeList = certainType.getElementsByTagName("entity");
        }

        for (int i = 0; i < nodeList.getLength(); i++){
            String elementSpecificName = nodeList.item(i).getTextContent();
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

    // @param entitiesFile The game configuration file containing all game entities to use in your game
    // @param actionsFile The game configuration file containing all game actions to use in your game
    public GameServer(File entitiesFile, File actionsFile) {
        playerHashMap = new HashMap<String, Player>();

        ArrayList<Graph> sections = getSections(entitiesFile);

        // load locations
        locationHashMap = loadLocations(sections.get(0).getSubgraphs());
        
        // set up a set containning all entities
        entities = buildEntites();

        //load Paths
        loadPaths(sections.get(1).getEdges());

        //load actions
        actions = loadActions(actionsFile);
    }

    private Set<String> buildEntites() {
        Set<String> answer = new HashSet<String>();
        for (Location location: locationHashMap.values()){
            for (String artefact: location.getArtefacts().keySet()) {
                answer.add(artefact);
            }

            for (String character: location.getCharacters().keySet()) {
                answer.add(character);
            }
            for (String furniture: location.getFurnitures().keySet()) {
                answer.add(furniture);
            }
        }
        return answer;
    }

    public Boolean checkDuplicateBasicCommands(String[] tokens){
        int numOfBasicCommmands = 0;
        for (int i = 0; i < tokens.length; i++){
            if (basicCommands.contains(tokens[i])){
                numOfBasicCommmands++;
            }
        }

        if (numOfBasicCommmands > 1){
            return true;
        } else {
            return false;
        }
    }

    public int indexCommand(String[] tokens){
        for (int i = 0; i < tokens.length; i++) {
            if (basicCommands.contains(tokens[i])){
                return i;
            }
        }
        return -1;
    }

    private String executeInv(Player currentPlayer) {
        return currentPlayer.displayInventory();
    }

    private String executeLook(Player currentPlayer){
        return currentPlayer.getCurrentLocation().showInformation();
    }

    private String executeGoto(Player currentPlayer, String newLocation) {
        if (currentLocation.getExits().containsKey(newLocation)) {
            currentPlayer.setCurrentLocation(currentLocation.getExits().get(newLocation));
            return "you are now at " + currentPlayer.getCurrentLocation().getName();
        } else {
            return "You can't go to " + newLocation;
        }
    }
    private String executeGet(Player currentPlayer, String artefactName){
         if (currentLocation.getArtefacts().containsKey(artefactName)){
                Artefact currentArtefact = currentLocation.getArtefacts().get(artefactName);
                currentPlayer.addArtefact(currentArtefact);
                currentLocation.getArtefacts().remove(currentArtefact.getName());
                return "get " + currentArtefact.getName();
            } else{
                return "failed to get, you may input the wrong name or it doesn't exist at all.";
         }
    }

    private String executeDrop(Player currentPlayer, String artefactName) {
        Artefact artefact = currentPlayer.removeArtefact(artefactName);
        if (artefact != null) {
            currentLocation.addArtefact(artefact);
            return "drop " + artefact.getName();
        } else {
            return "doesn't exist this artefact";
        }
    }

    public String handleCommand(String command) {
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
        // does it hurt to use global varibale here?
        currentPlayer = playerHashMap.get(username);
        currentLocation = currentPlayer.getCurrentLocation();
        if (tokens.length == 0){
            return "";
        }

        String str = proceedGameAction(command, tokens);
        if (!str.equals("")){
            return str;
        }
        return proceedBasicCommand(command, tokens);
    }

    public String proceedBasicCommand(String command, String[] tokens){
        for (String basicCommand: basicCommands){
            for (String token: tokens){
                if (token.equals(basicCommand)){
                    if (checkDuplicateBasicCommands(tokens)){
                        return "What the hell is wrong with you";
                    }
                    if (basicCommand.equals("inv") || basicCommand.equals("inventory")){
                        return executeInv(currentPlayer);
                    } else if (basicCommand.equals("look")){
                        return executeLook(currentPlayer);
                    }
                    int indexCommand = indexCommand(tokens);
                    if (indexCommand + 1 >= tokens.length){
                        return basicCommand + " requires further object";
                    }
                    if (basicCommand.equals("get")){
                        return executeGet(currentPlayer, tokens[indexCommand + 1]);
                    } else if (basicCommand.equals("goto")){
                        return executeGoto(currentPlayer, tokens[indexCommand + 1]);
                    } else {
                        return executeDrop(currentPlayer, tokens[indexCommand(tokens) + 1]);
                    }
                }
            }

        }
        return "failed to execute game action or basic commands";
    }

    private String proceedGameAction(String command, String[] tokens) {
        Set<GameAction> possibleGameActions = new HashSet<GameAction>();
        Set<String> triggers = new HashSet<String>();
        for (String actionName: actions.keySet()){
            if (command.contains(actionName)){
                triggers.add(actionName);
                for (GameAction action: actions.get(actionName)){
                    possibleGameActions.add(action);
                }
            }
        }

        if (possibleGameActions.size() == 0){
            return "";
        } else {
            for (String basicCommand: basicCommands){
                if (command.contains(basicCommand)){
                    return "what the heck is wrong with you";
                }
            }
        }

        // if one command contain more than one trigger
        // then valid action must contain all triggers
        Iterator<GameAction> iterator = possibleGameActions.iterator();
        if (triggers.size() > 1){
            while (iterator.hasNext()) {
                GameAction action = iterator.next();
                for (String trigger: triggers){
                    if (!action.getTriggers().contains(trigger)){
                        iterator.remove();
                        break;
                    }
                }
            }
        }
        if (possibleGameActions.size() == 0){
            return "You might use composite commands, you can only execute one at a time";
        }

        // a valid performable action must have at least one subject mentioned in client's command
        iterator = possibleGameActions.iterator();
        while (iterator.hasNext()) {
            GameAction action = iterator.next();
            boolean matchSubject = false;
            for (String subject: action.getSubjects()){
                if (command.contains(subject)){
                    matchSubject = true;
                    break;
                }
            }
            // Command doesn't hold any subject for the current action. (needs to have at least one)
            if (matchSubject == false){
                iterator.remove();
            }
        }

        if (possibleGameActions.size() == 0){
            return "your command needs at least one subject";
        }

        // build a set which contains all available entities so that can be used as one action's subject
        Set<String> availableSubjects = new HashSet<String>();
        for (String invArtefact: currentPlayer.getInventory().keySet()){
            availableSubjects.add(invArtefact);
        }

        for (String locationArtefact: currentLocation.getArtefacts().keySet()){
            availableSubjects.add(locationArtefact);
        }
        
        for (String locationCharacter: currentLocation.getCharacters().keySet()){
            availableSubjects.add(locationCharacter);
        } 
        
        for (String locationFurniture: currentLocation.getFurnitures().keySet()){
            availableSubjects.add(locationFurniture);
        }

        // check the action's subject are all satisfied, either in player's inv or in current location
        iterator = possibleGameActions.iterator();
        while (iterator.hasNext()) {
            GameAction action = iterator.next();
            for (String requiredSubject : action.getSubjects()) {
                if (!availableSubjects.contains(requiredSubject)) {
                    iterator.remove();
                    break;
                }
            }
        }
        if (possibleGameActions.size() == 0){
            return "this game action miss necessary subject to execute";
        }

        for (String entity: entities){
            if (!availableSubjects.contains(entity) && command.contains(entity)){
                return "Your command contains extraneous entities";
            }
        }

        if (possibleGameActions.size() > 1){
            return "Your command is ambiguous";
        } else if (possibleGameActions.size() == 0){
            return "No game action is matched";
        }

        GameAction action = possibleGameActions.stream().toList().get(0);
        return executeMatchedGameAction(action, currentPlayer.getName());
    }

    public String executeMatchedGameAction(GameAction gameAction, String playerName) {
        StringBuilder result = new StringBuilder();
        result.append("-----------------------------\n");

        for (String consumable : gameAction.getConsumables()) {
            result.append("Consumed: " + consumable);
            result.append("  \n");
            if (consumable.equals("health")) {
                currentPlayer.decreaseHealth();
                if (currentPlayer.getHealth() == 0) {
                    currentPlayer.resetHealth();
                    currentPlayer.setCurrentLocation(startLocation);
                    currentLocation = startLocation;
                    result.append("You died, go back to the start Location. \n");
                }
            }
            if (currentPlayer.getInventory().containsKey(consumable)) {
                currentPlayer.getInventory().remove(consumable);
            } else if (currentLocation.getCharacters().containsKey(consumable)) {
                currentLocation.getCharacters().remove(consumable);
            } else if (currentLocation.getFurnitures().containsKey(consumable)) {
                currentLocation.getFurnitures().remove(consumable);
            } else if (currentLocation.getArtefacts().containsKey(consumable)) {
                currentLocation.getArtefacts().remove(consumable);
            } else if (currentLocation.getName().equals(consumable)) {
                currentLocation = null;
            }
        }

        for (String production: gameAction.getProductions()){
            if (locationHashMap.containsKey(production)){
                currentLocation.addExit(locationHashMap.get(production));
                result.append("\nnew exit: ");
                result.append(production + "\n");
            } else {
                locationHashMap.get("storeroom").getArtefacts().put(production, new Artefact(production, ""));
                result.append("\nnew Artefacts (at storeroom): ");
                result.append(production + "\n");
            }
        }
        result.append(gameAction.getNarration());
        result.append("\n-----------------------------\n");
        return result.toString();
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
