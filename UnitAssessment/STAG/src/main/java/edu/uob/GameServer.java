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
        File entitiesFile = Paths.get("config" + File.separator + "extended-entities.dot").toAbsolutePath().toFile();
        File actionsFile = Paths.get("config" + File.separator + "extended-actions.xml").toAbsolutePath().toFile();
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
            currentLocation = new Location(locationName.toLowerCase(), locationDetails.getAttribute("description"));

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
            String fromLocationName = edge.getSource().getNode().getId().getId().toLowerCase();
            String toLocationName = edge.getTarget().getNode().getId().getId().toLowerCase();
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

    // load different part of one action
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
            String elementSpecificName = nodeList.item(i).getTextContent().toLowerCase();
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

    public void loadGameEntity(Location location, ArrayList<Graph> graph, String name) {
        List<Graph> graphs = graph.stream().filter(currentGraph -> currentGraph.getId().getId().equals(name)).toList();
        if (graphs.size() == 1) {
            for (Node item : graphs.get(0).getNodes(false)) {
                String itemName = item.getId().getId().toLowerCase();
                GameEntity entity;
                switch (name) {
                    case "artefacts" -> entity = new Artefact(itemName, item.getAttribute("description"));
                    case "furniture" -> entity = new Furniture(itemName, item.getAttribute("description"));
                    default -> entity = new CharacterStag(itemName, item.getAttribute("description"));
                }
                entity.add(location);
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

    // build a set containing all entities in the game
    public Set<String> buildEntities() {
        Set<String> answer = new HashSet<>();
        for (Location location: locationHashMap.values()){
            answer.addAll(location.getArtefacts().keySet());
            answer.addAll(location.getCharacters().keySet());
            answer.addAll(location.getFurnitures().keySet());
            answer.addAll(location.getExits().keySet());
            answer.add(location.getName());
        }
        return answer;
    }

    // reject case like look look
    public Boolean checkDuplicateBasicCommands(String[] tokens){
        int numOfBasicCommands = 0;
        for (String token : tokens) {
            if (basicCommands.contains(token)) {
                numOfBasicCommands++;
            }
        }
        return numOfBasicCommands > 1;
    }

    // return num of entities appeared in the command
    public int numOfEntites(String[] tokens){
        int answer = 0;
        for (String token : tokens) {
            if (entities.contains(token)) {
                answer++;
            }
        }
        return answer;
    }

    // return index of the last entity
    public int entityIndex(String[] tokens){
        int answer = -1;
        for (int i = tokens.length - 1; i >= 0; i--){
            if (entities.contains(tokens[i])){
                answer = i;
                break;
            }
        }
        return answer;
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

    // return current player's inventory information
    public String executeInv(){
        return currentPlayer.displayInventory();
    }

    // return current player's current location's information
    public String executeLook(){
        StringBuilder nearbyPlayers = new StringBuilder("Nearby Players: ");
        for (Player player: playerHashMap.values()){
           if (!player.equals(currentPlayer) && player.getCurrentLocation().equals(currentLocation)){
              nearbyPlayers.append(player.getName()).append(", ");
           }
        }
        return currentPlayer.getCurrentLocation().showInformation() + nearbyPlayers;
    }

    // return current player's health
    public String executeHealth() {
        return "health: " + currentPlayer.getHealth();
    }

    // change current player's current location
    public String executeGoto(String newLocation) {
        if (currentLocation.getExits().containsKey(newLocation)) {
            currentLocation = currentLocation.getExits().get(newLocation);
            currentPlayer.setCurrentLocation(currentLocation);
            return executeLook();
        } else {
            return "[error] " + "failed to execute goto, You can't go to " + newLocation;
        }
    }

    // only can get artefact
    public String executeGet(String artefactName){
         if (currentLocation.getArtefacts().containsKey(artefactName)){
                Artefact currentArtefact = currentLocation.getArtefacts().get(artefactName);
                currentPlayer.addArtefact(currentArtefact);
                currentArtefact.remove(currentLocation);
                return "get " + currentArtefact.getName();
         } else {
                return "[error] " + "failed to execute get, you may input the wrong name or it doesn't exist at all.";
         }
    }

    // only can drop when exist in current player's inv
    public String executeDrop(String artefactName) {
        Artefact artefact = currentPlayer.removeArtefact(artefactName);
        if (artefact != null) {
            currentLocation.addArtefact(artefact);
            return "drop " + artefact.getName();
        } else {
            return "[error] " + "failed to drop, doesn't exist this artefact";
        }
    }

    public String handleCommand(String command) {
        if (command.equals("")){ return ""; }

        int colonIndex = command.indexOf(':');
        String username = command.substring(0, colonIndex).toLowerCase();
        this.inputCommand = command.substring(colonIndex + 1).toLowerCase();
        String[] tokens = inputCommand.trim().replaceAll("\\s+", " ").split(" ");

        if (!playerHashMap.containsKey(username)){
            entities.add(username);
            playerHashMap.put(username, new Player(username, "", startLocation));
        }

        currentPlayer = playerHashMap.get(username);
        currentLocation = currentPlayer.getCurrentLocation();

        Set<GameAction> possibleGameActions = getPossibleGameAction();
        if (possibleGameActions.size() > 0){
            return proceedGameAction(possibleGameActions);
        }
        return proceedBasicCommand(tokens);
    }

    // find all triggers contained in the command
    public Set<String> getTriggersInCommand() {
        Set<String> answer = new HashSet<>();
        for (String trigger: triggers){
            if (inputCommand.contains(trigger) && isValidTrigger(trigger)){
               answer.add(trigger);
            }
        }
        return answer;
    }

    // might be case like : ccut dowm, chopped
    // so that the trigger must be in the form of ' ${trigger} ' with space before and after
    public boolean isValidTrigger(String trigger){
        int triggerIndex = inputCommand.indexOf(trigger);
        int triggerLen = trigger.length();
        boolean isValid = false;
        while (triggerIndex >= 0){
           if (isWhiteSpaceBeforeTrigger(triggerIndex) && isWhiteSpaceAfterTrigger(triggerIndex, triggerLen)){
               isValid = true;
               break;
           }
           triggerIndex = inputCommand.indexOf(trigger, triggerIndex + 1);
        }
        return isValid;
    }

    // possible game action needs to check by several procedures
    // and when only one left, then execute that one.
    public String proceedGameAction(Set<GameAction> possibleGameActions){
        if (includeBasicCommand()){
            return "[error] what the heck is wrong with you";
        }

        if (checkComposite(possibleGameActions)){
            return "[error] You might use composite commands, you can only execute one at a time";
        }

        if (checkPartial(possibleGameActions)){
            return "[error] your command needs at least one subject";
        }

        if (checkPerformable(possibleGameActions)){
            return "[error] this game action miss necessary subject to execute";
        }

        if (checkExtraneous(possibleGameActions)){
            return "[error] Your command contains extraneous entities";
        }

        if (possibleGameActions.size() > 1){
            return "[error] Your command is ambiguous";
        } else if (possibleGameActions.size() == 0){
            return "[error] No game action is matched";
        }

        GameAction action = possibleGameActions.stream().toList().get(0);
        return executeGameAction(action);
    }

    // available entities means: eneties in the current location and in the current player's inv
    public Set<String> getAvailableEntities(){
        Set<String> availableEntities= new HashSet<String>();
        availableEntities.addAll(currentPlayer.getInventory().keySet());

        availableEntities.addAll(currentLocation.getArtefacts().keySet());

        availableEntities.addAll(currentLocation.getCharacters().keySet());

        availableEntities.addAll(currentLocation.getFurnitures().keySet());

        availableEntities.addAll(currentLocation.getExits().keySet());

        availableEntities.add(currentLocation.getName());

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

    // valid action must contain all triggers mentioned in the command
    public boolean checkComposite(Set<GameAction> possibleGameActions) {
        Iterator<GameAction> iterator = possibleGameActions.iterator();
        Set<String> triggersInCommand = getTriggersInCommand();
        if (triggersInCommand.size() > 1){
            while (iterator.hasNext()) {
                GameAction action = iterator.next();
                Set<String> triggersInAction = action.getTriggers();
                if (!triggersInAction.containsAll(triggersInCommand)){
                    iterator.remove();
                }
            }
        }
        return possibleGameActions.size() == 0;
    }

    // even though there might be vague cases, some might be not performable
    // e.g. a key must be in the current location or in the current player's inv.
    // all consumptions and productions can be anywhere except in other players' location.
    public boolean checkPerformable(Set<GameAction> possibleGameActions){
        Set<String> availableEntities = getAvailableEntities();
        Iterator<GameAction> iterator = possibleGameActions.iterator();
        while (iterator.hasNext()) {
            GameAction action = iterator.next();
            if (!availableEntities.containsAll(action.getSubjects())){
               iterator.remove();
               continue;
            }

            if (!checkProductionConsumptionAvailable(action)){
                iterator.remove();
            }
        }
        return possibleGameActions.size() == 0;
    }

    // both production and consumption mustn't be in another player's inv
    public boolean checkProductionConsumptionAvailable(GameAction action){
        Set<String> availableMaterials = new HashSet<>(entities);
        availableMaterials.add("health");
        boolean result = true;
        for (Player player: playerHashMap.values()){
            if (!player.equals(currentPlayer)){
                availableMaterials.removeAll(player.getInventory().keySet());
            }
        }

        for (String production: action.getProductions()){
            if (!availableMaterials.contains(production)){
                result = false;
                break;
            }
        }

        for (String consumption: action.getConsumables()){
            if (!availableMaterials.contains(consumption)){
                result = false;
                break;
            }
        }

        return result;
    }

    // make sure client doesn't type inappropriate entities
    public boolean checkExtraneous(Set<GameAction> possibleGameActions){
        Iterator<GameAction> iterator = possibleGameActions.iterator();
        while (iterator.hasNext()) {
            GameAction action = iterator.next();
            Set<String> extraneousEntities = getExtraneousEntities(action);

            for (String exclusion: extraneousEntities){
                if (inputCommand.contains(exclusion)){
                    iterator.remove();
                    break;
                }
            }
        }

        return possibleGameActions.size() == 0;
    }

    // return: excludeEntities, which one command must not contain any of it.
    // e.g. In set, it's (all entities) - (entities in the subject)
    public Set<String> getExtraneousEntities(GameAction action){
        Set<String> extraneousEntities = new HashSet<String>(entities);

        for (String subject: action.getSubjects()){
            extraneousEntities.remove(subject);
        }

        return extraneousEntities;
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
    private Set<GameAction> getPossibleGameAction(){
        Set<GameAction> possibleGameActions = new HashSet<GameAction>();
        for (String trigger: actions.keySet()){
            if (inputCommand.contains(trigger) && isValidTrigger(trigger)){
                possibleGameActions.addAll(actions.get(trigger));
            }
        }
        return possibleGameActions;
    }


    private boolean isWhiteSpaceAfterTrigger(int triggerIndex, int triggerLen) {
        return triggerIndex + triggerLen == inputCommand.length() || inputCommand.charAt(triggerIndex + triggerLen) == ' ';
    }

    private boolean isWhiteSpaceBeforeTrigger(int triggerIndex) {
        return triggerIndex - 1 == -1 || inputCommand.charAt(triggerIndex- 1) == ' ';
    }

    // check if the current player is dead
    public boolean isDead() {
        currentPlayer.decreaseHealth();
        if (currentPlayer.getHealth() == 0) {
            for (Artefact artefact: currentPlayer.getInventory().values()){
                currentLocation.addArtefact(artefact);
            }
            currentPlayer.removeAllArtefact();
            currentPlayer.resetHealth();
            currentPlayer.setCurrentLocation(startLocation);
            currentLocation = startLocation;
            return true;
        } else {
            return false;
        }
    }

    // consume entity in the game action, move to storeroom or consume health, or remove path
    public String consumeEntity(GameAction gameAction){
        StringBuilder result = new StringBuilder();

        for (String consumable : gameAction.getConsumables()) {
            result.append("Consumed: ").append(consumable).append(" \n");
            if (consumable.equals("health")) {
                result.append(isDead() ? "You died, go back to the start location \n": "You lost one health\n");
            } else if (currentPlayer.getInventory().containsKey(consumable)) {
                Artefact artefact = currentPlayer.getInventory().remove(consumable);
                locationHashMap.get("storeroom").addArtefact(artefact);
            } else {
                for (Location location: locationHashMap.values()) {
                    if (location.getGameEntity(consumable) != null) {
                        GameEntity consumption = location.getGameEntity(consumable);
                        consumption.remove(location);
                        consumption.add(locationHashMap.get("storeroom"));
                    }
                }
            }
        }
        return result.toString();
    }

    // execute game action needs to consume entity and produce entity
    public String executeGameAction(GameAction gameAction) {
         return   "-------------------------------\n"
                + consumeEntity(gameAction)
                + produceEntity(gameAction)
                + gameAction.getNarration()
                + "\n-----------------------------\n";
    }

    // produce game entity, including gain health, put entity in the current location or have new exits
    private String produceEntity(GameAction gameAction) {
        StringBuilder result = new StringBuilder();
        for (String production: gameAction.getProductions()){
            result.append("produced:  \n");
            if (production.equals("health")){
                currentPlayer.increaseHealth();
                result.append("You gain one unit of health \n");
            } else if (locationHashMap.containsKey(production)){
                locationHashMap.get(production).add(currentLocation);
                result.append("new exit: ").append(production).append(" \n");
            } else {
                result.append(production).append(" \n");
                for (Location location: locationHashMap.values()){
                    if (location.getGameEntity(production) != null) {
                        GameEntity productionName = location.getGameEntity(production);
                        productionName.remove(location);
                        productionName.add(currentLocation);
                    }
                }
            }
        }
        return result.toString();
    }

    public String proceedBasicCommand(String[] tokens){
        // if contain several basic commands
        if (checkDuplicateBasicCommands(tokens)){ return "[error] What the hell is wrong with you"; }

        // if it doesn't contain any basic command
        int indexCommand = indexBasicCommand(tokens);
        if (indexCommand == -1){ return "[error] failed to execute game action or basic commands"; }

        String basicCommand = tokens[indexCommand];
        boolean basicCommandWithoutEntity = basicCommand.equals("inv") || basicCommand.equals("inventory")
                || basicCommand.equals("look") || basicCommand.equals("health");
        int numOfEntities = numOfEntites(tokens);
        // num of entities is strictly restricted
        if (numOfEntities > 1){
            return "[error] " + basicCommand + " have more than two entites";
        } else if (numOfEntities == 0 && !basicCommandWithoutEntity){
            return "[error] " + basicCommand + " requires one entity to execute";
        } else if (numOfEntities == 1 && basicCommandWithoutEntity){
            return "[error] " + basicCommand + " doesn't need entity";
        }

        int entityIndex = entityIndex(tokens);
        if (indexCommand > entityIndex && !basicCommandWithoutEntity){
            return "[error] " + basicCommand + " is wrong in logic order";
        }

        return executeBasicCommand(basicCommand, tokens, entityIndex);
    }

    public String executeBasicCommand(String basicCommand, String[] tokens, int entityIndex){
        return switch (basicCommand) {
            case "inv", "inventory" -> executeInv();
            case "look" -> executeLook();
            case "health" -> executeHealth();
            case "get" -> executeGet(tokens[entityIndex]);
            case "goto" -> executeGoto(tokens[entityIndex]);
            default -> executeDrop(tokens[entityIndex]);
        };
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
           