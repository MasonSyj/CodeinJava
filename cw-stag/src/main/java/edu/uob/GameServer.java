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

    public static List<String> basicCommands = Arrays.asList("look", "goto", "drop", "get", "inv", "inventory", "health");

    private HashMap<String, Location> locationHashMap;
    private Location currentLocation;
    private Player currentPlayer;
    private Location startLocation;

    private String inputCommand;
    private HashMap<String, Player> playerHashMap;

    private HashMap<String, HashSet<GameAction>> actions;

    private Set<String> entities;

    private Set<String> triggers;

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
        List<String> listOfItems = new ArrayList<String>();

        NodeList nodeList;
        if (elementName.equals("triggers")){
            nodeList = certainType.getElementsByTagName("keyphrase");
        } else {
            nodeList = certainType.getElementsByTagName("entity");
        }

        for (int i = 0; i < nodeList.getLength(); i++){
            String elementSpecificName = nodeList.item(i).getTextContent();
            listOfItems.add(elementSpecificName);
        }
        for (String listOfItem : listOfItems) {
            switch (elementName) {
                case "triggers" -> gameAction.addTrigger(listOfItem);
                case "subjects" -> gameAction.addSubject(listOfItem);
                case "consumed" -> gameAction.addConsumable(listOfItem);
                case "produced" -> gameAction.addProduction(listOfItem);
            }
        }
    }

    public void loadGameEntity(Location location, ArrayList<Graph> graph, String name){
        List<Graph> graphs = graph.stream().filter(currentGraph -> currentGraph.getId().getId().equals(name)).toList();
        if (graphs.size() == 1){
            for (Node item: graphs.get(0).getNodes(false)){
                switch (name) {
                    case "artefacts" ->
                            location.addArtefact(new Artefact(item.getId().getId(), item.getAttribute("description")));
                    case "furniture" ->
                            location.addFurniture(new Furniture(item.getId().getId(), item.getAttribute("description")));
                    case "characters" ->
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

        //load Paths
        loadPaths(sections.get(1).getEdges());

        //load actions
        actions = loadActions(actionsFile);

        // set up a set containing all entities
        entities = buildEntities();

        // set up a set containing all triggers
        triggers = actions.keySet();

    }

    public Set<String> buildEntities() {
        Set<String> answer = new HashSet<String>();
        for (Location location: locationHashMap.values()){
            answer.addAll(location.getArtefacts().keySet());
            answer.addAll(location.getCharacters().keySet());
            answer.addAll(location.getFurnitures().keySet());
            answer.addAll(location.getExits().keySet());
            answer.add(location.getName());
        }
        return answer;
    }

    public Boolean checkDuplicateBasicCommands(String[] tokens){
        int numOfBasicCommands = 0;
        for (String token : tokens) {
            if (basicCommands.contains(token)) {
                numOfBasicCommands++;
            }
        }

        return numOfBasicCommands > 1;
    }

    public int numOfEntites(String[] tokens){
        int answer = 0;
        for (String token : tokens) {
            if (entities.contains(token)) {
                answer++;
            }
        }
        return answer;
    }

    public int lastEntityIndex(String[] tokens){
        for (int i = tokens.length - 1; i >= 0; i--){
            if (entities.contains(tokens[i])){
                return i;
            }
        }
        return -1;
    }

    public int indexBasicCommand(String[] tokens){
        int answer = -1;
        for (int i = 0; i < tokens.length; i++) {
            if (basicCommands.contains(tokens[i])){
                answer = i;
                break;
            }
        }
        return answer;
    }

    public String executeInv(int numOfEntities){
        if (numOfEntities > 0){
            return "Inv command contains extraneous entity";
        }
        return currentPlayer.displayInventory();
    }

    public String executeLook(int numOfEntites){
        if (numOfEntites > 0){
            return "look command contains extraneous entity";
        }
        StringBuilder nearbyPlayers = new StringBuilder("Nearby Players: ");
        for (Player player: playerHashMap.values()){
           if (player != currentPlayer && player.getCurrentLocation() == currentLocation){
              nearbyPlayers.append(player.getName()).append(" ");
           }
        }
        return currentPlayer.getCurrentLocation().showInformation() + nearbyPlayers;
    }

    public String executeHealth(int numOfEntites) {
        if (numOfEntites > 0){
            return "Health command contains extraneous entity";
        }
        return "health: " + currentPlayer.getHealth();
    }

    public String executeGoto(String newLocation) {
        if (currentLocation.getExits().containsKey(newLocation)) {
            currentPlayer.setCurrentLocation(currentLocation.getExits().get(newLocation));
            return "you are now at " + currentPlayer.getCurrentLocation().getName();
        } else {
            return "failed to execute goto, You can't go to " + newLocation;
        }
    }
    public String executeGet(String artefactName){
         if (currentLocation.getArtefacts().containsKey(artefactName)){
                Artefact currentArtefact = currentLocation.getArtefacts().get(artefactName);
                currentPlayer.addArtefact(currentArtefact);
                currentLocation.getArtefacts().remove(currentArtefact.getName());
                return "get " + currentArtefact.getName();
         } else {
                return "failed to execute get, you may input the wrong name or it doesn't exist at all.";
         }
    }

    public String executeDrop(String artefactName) {
        Artefact artefact = currentPlayer.removeArtefact(artefactName);
        if (artefact != null) {
            currentLocation.addArtefact(artefact);
            return "drop " + artefact.getName();
        } else {
            return "failed to drop, doesn't exist this artefact";
        }
    }

    public String handleCommand(String command) {
        if (command.equals("")){
            return "";
        }
        String username = "";
        for (int i = 0; i < command.length(); i++){
            if (command.charAt(i) == ':'){
               username = command.substring(0, i);
               this.inputCommand = command.substring(i + 1);
               break;
            }
        }
        String[] tokens = inputCommand.trim().replaceAll("\\s+", " ").split(" ");
        if (!playerHashMap.containsKey(username)){
            playerHashMap.put(username, new Player(username, ""));
            playerHashMap.get(username).setCurrentLocation(startLocation);
        }
        // does it hurt to use global variable here?
        currentPlayer = playerHashMap.get(username);
        currentLocation = currentPlayer.getCurrentLocation();

        Set<String> triggers = new HashSet<String>();
        Set<GameAction> possibleGameActions = getPossibleGameAction(triggers);
        if (possibleGameActions.size() != 0){
            return proceedGameAction(possibleGameActions, triggers);
        }
        return proceedBasicCommand(tokens);
    }

    public String proceedGameAction(Set<GameAction> possibleGameActions, Set<String> triggers){
        if (includeBasicCommand()){
            return "what the heck is wrong with you";
        }

        if (checkComposite(possibleGameActions, triggers)){
            return "You might use composite commands, you can only execute one at a time";
        }

        if (checkPartial(possibleGameActions)){
            return "your command needs at least one subject";
        }

        Set<String> availableEntities = getAvailableEntities();

        if (checkPerformable(possibleGameActions, availableEntities)){
            return "this game action miss necessary subject to execute";
        }

        if (checkExtraneous(possibleGameActions, availableEntities)){
            return "Your command contains extraneous entities";
        }

        if (possibleGameActions.size() > 1){
            return "Your command is ambiguous";
        } else if (possibleGameActions.size() == 0){
            return "No game action is matched";
        }

        GameAction action = possibleGameActions.stream().toList().get(0);
        return executeGameAction(action);
    }

    public Set<String> getAvailableEntities(){
        Set<String> availableEntities= new HashSet<String>();
        availableEntities.addAll(currentPlayer.getInventory().keySet());

        availableEntities.addAll(currentLocation.getArtefacts().keySet());

        availableEntities.addAll(currentLocation.getCharacters().keySet());

        availableEntities.addAll(currentLocation.getFurnitures().keySet());

        availableEntities.addAll(currentLocation.getExits().keySet());
        return availableEntities;
    }


    // a valid action must have at least one subject mentioned in client's command
    public boolean checkPartial(Set<GameAction> possibleGameActions) {
        Iterator<GameAction> iterator = possibleGameActions.iterator();
        while (iterator.hasNext()) {
            GameAction action = iterator.next();
            boolean matchSubject = false;
            for (String subject: action.getSubjects()){
                if (inputCommand.contains(subject)){
                    matchSubject = true;
                    break;
                }
            }
            // Command doesn't hold any subject for the current action. (needs to have at least one)
            if (!matchSubject){
                iterator.remove();
            }
        }

        return possibleGameActions.size() == 0;
    }

    // if one command contain more than one trigger (might be composite case, valid action must contain all triggers
    public boolean checkComposite(Set<GameAction> possibleGameActions, Set<String> triggers) {
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
        return possibleGameActions.size() == 0;
    }

    // even though there might be vague cases, some might be not performable
    // e.g. a key must be in the current location or in the current player's inv.
    // all productions can be anywhere except in other players' location.
    public boolean checkPerformable(Set<GameAction> possibleGameActions, Set<String> availableEntities){
        Iterator<GameAction> iterator = possibleGameActions.iterator();
        while (iterator.hasNext()) {
            GameAction action = iterator.next();
            for (String requiredSubject : action.getSubjects()) {
                // if required subject is not available
                if (!availableEntities.contains(requiredSubject)) {
                    iterator.remove();
                    break;
                }
            }

            if (!checkProductionConsumptionAvailable(action)){
                iterator.remove();
                break;
            }
        }
        return possibleGameActions.size() == 0;
    }

    // both production and consumption mustn't be in another player's inv
    public boolean checkProductionConsumptionAvailable(GameAction action){
        Set<String> availableMaterials = new HashSet<>(entities);
        for (Player player: playerHashMap.values()){
            if (player != currentPlayer){
                for (String artefact: player.getInventory().keySet()){
                    availableMaterials.remove(artefact);
                }
            }
        }

        for (String production: action.getProductions()){
            if (production.equals("health")) {
                continue;
            }
            if (!availableMaterials.contains(production)){
                return false;
            }
        }

        for (String consumption: action.getConsumables()){
            if (consumption.equals("health")) {
                continue;
            }
            if (!availableMaterials.contains(consumption)){
                return false;
            }
        }

        return true;
    }

    // make sure client doesn't type inappropriate entities
    public boolean checkExtraneous(Set<GameAction> possibleGameActions, Set<String> availableEntities){
        for (String entity: entities){
            if (!availableEntities.contains(entity) && inputCommand.contains(entity)){
                return true;
            }
        }

        Iterator<GameAction> iterator = possibleGameActions.iterator();
        while (iterator.hasNext()) {
            GameAction action = iterator.next();
            Set<String> excludeEntities = getExcludedEntities(availableEntities, action);

            for (String exclusion: excludeEntities){
                if (inputCommand.contains(exclusion)){
                    iterator.remove();
                    break;
                }
            }
        }

        return possibleGameActions.size() == 0;
    }

    // return: excludeEntities, which one command must not contain any of it.
    // e.g. In set, it's (all entities) - (available entities)
    public Set<String> getExcludedEntities(Set<String> availableEntities, GameAction action){
        Set<String> excludeEntities = new HashSet<String>(availableEntities);

        for (String subject: action.getSubjects()){
            excludeEntities.remove(subject);
        }

        for (String consumable: action.getConsumables()) {
            excludeEntities.remove(consumable);
        }
        return excludeEntities;
    }

    // avoid case like 'open key and goto' where exist game action and commands
    public boolean includeBasicCommand() {
        for (String basicCommand: basicCommands){
            if (inputCommand.contains(basicCommand)){
                return true;
            }
        }
        return false;
    }

    // based on the command, find all actions by trigger names
    private Set<GameAction> getPossibleGameAction(Set<String> triggers){
        Set<GameAction> possibleGameActions = new HashSet<GameAction>();
        for (String actionName: actions.keySet()){
            if (inputCommand.contains(actionName)){
                triggers.add(actionName);
                possibleGameActions.addAll(actions.get(actionName));
            }
        }
        return possibleGameActions;
    }

    public String executeGameAction(GameAction gameAction) {
        StringBuilder result = new StringBuilder("-----------------------------\n");

        for (String consumable : gameAction.getConsumables()) {
            result.append("Consumed: ").append(consumable).append(" \n");
            if (consumable.equals("health")) {
                currentPlayer.decreaseHealth();
                if (currentPlayer.getHealth() == 0) {
                    for (Artefact artefact: currentPlayer.getInventory().values()){
                        currentLocation.addArtefact(artefact);
                    }
                    currentPlayer.removeAllArtefact();
                    currentPlayer.resetHealth();
                    currentPlayer.setCurrentLocation(startLocation);
                    currentLocation = startLocation;

                    result.append("You died, go back to the start Location. \n");
                }
            }
            if (currentPlayer.getInventory().containsKey(consumable)) {
                Artefact artefact = currentPlayer.getInventory().remove(consumable);
                locationHashMap.get("storeroom").addArtefact(artefact);
            } else if (currentLocation.getCharacters().containsKey(consumable)) {
                Character character = currentLocation.getCharacters().remove(consumable);
                locationHashMap.get("storeroom").addCharacter(character);
            } else if (currentLocation.getFurnitures().containsKey(consumable)) {
                Furniture furniture = currentLocation.getFurnitures().remove(consumable);
                locationHashMap.get("storeroom").addFurniture(furniture);
            } else if (currentLocation.getArtefacts().containsKey(consumable)) {
                Artefact artefact = currentLocation.getArtefacts().remove(consumable);
                locationHashMap.get("storeroom").addArtefact(artefact);
            } else currentLocation.getExits().remove(consumable);
        }

        for (String production: gameAction.getProductions()){
            if (production.equals("health")){
                currentPlayer.increaseHealth();
            }
            if (locationHashMap.containsKey(production)){
                currentLocation.addExit(locationHashMap.get(production));
                result.append("new exit: ").append(production).append("\n");
            } else {
                for (Location location: locationHashMap.values()){
                    if (location.getArtefacts().containsKey(production)){
                        Artefact artefact = location.getArtefacts().remove(production);
                        currentLocation.addArtefact(artefact);
                        result.append("new artefact: ").append(artefact.getName()).append("\n");
                        break;
                    } else if (location.getFurnitures().containsKey(production)) {
                        Furniture furniture = location.getFurnitures().remove(production);
                        currentLocation.addFurniture(furniture);
                        result.append("new furniture: ").append(furniture.getName()).append("\n");
                        break;
                    } else if (location.getCharacters().containsKey(production)) {
                        Character character = location.getCharacters().remove(production);
                        currentLocation.addCharacter(character);
                        result.append("new character: ").append(character.getName()).append("\n");
                        break;
                    }
                }
            }
        }
        result.append(gameAction.getNarration()).append("\n-----------------------------\n");
        return result.toString();
    }
     public String proceedBasicCommand(String[] tokens){
        for (String basicCommand: basicCommands){
            for (String token: tokens){
                if (token.equals(basicCommand)){
                   if (checkDuplicateBasicCommands(tokens)){
                       return "What the hell is wrong with you";
                   }

                   int indexCommand = indexBasicCommand(tokens);
                   int numOfEntites = numOfEntites(tokens);
                   int lastEntityIndex = lastEntityIndex(tokens);

                   switch (basicCommand) {
                       case "inv":
                       case "inventory":
                           return executeInv(numOfEntites);
                       case "look":
                           return executeLook(numOfEntites);
                       case "health":
                           return executeHealth(numOfEntites);
                   }

                   if (numOfEntites > 1){
                       return basicCommand + " have more than two entites";
                   } else if (numOfEntites == 0){
                       return basicCommand + " requires one entity to execute";
                   }

                   if (indexCommand > lastEntityIndex){
                       return basicCommand + " is wrong in logic order";
                   }

                   if (basicCommand.equals("get")){
                       return executeGet(tokens[lastEntityIndex]);
                   } else if (basicCommand.equals("goto")){
                       return executeGoto(tokens[lastEntityIndex]);
                   } else {
                       return executeDrop(tokens[lastEntityIndex]);
                   }
                }
            }

        }
        return "failed to execute game action or basic commands";
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
            if (incomingCommand != null) {
                System.out.println("Received message from " + incomingCommand);
                String result = handleCommand(incomingCommand);
                writer.write(result);
                writer.write("\n" + END_OF_TRANSMISSION + "\n");
                writer.flush();
            }
        }
    }
}
           