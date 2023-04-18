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
    private HashMap<String, Player> playerHashMap;

    private Set<GameAction> gameActionSet;

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

            ArrayList<Graph> others = location.getSubgraphs();

            loadGameEntity(currentLocation, others, "artefacts");
            loadGameEntity(currentLocation, others, "furniture");
            loadGameEntity(currentLocation, others, "characters");
            loadGameEntity(currentLocation, others, "players");
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
              loadActionItem(currentGameAction, currentActionElement, "triggers");
              loadActionItem(currentGameAction, currentActionElement, "subjects");
              loadActionItem(currentGameAction, currentActionElement, "consumed");
              loadActionItem(currentGameAction, currentActionElement, "produced");
              gameActionSet.add(currentGameAction);
          }
      } catch(ParserConfigurationException pce) {
            System.out.println("ParserConfigurationException was thrown when attempting to read basic actions file");
      } catch(SAXException saxe) {
            System.out.println("SAXException was thrown when attempting to read basic actions file");
      } catch(IOException ioe) {
            System.out.println("IOException was thrown when attempting to read basic actions file");
      }

        for (GameAction gameAction: gameActionSet){
            System.out.println(gameAction.toString());
        }
    }

    public void loadActionItem(GameAction gameAction, Element gameActionElement, String elementName){
        NodeList items = gameActionElement.getElementsByTagName(elementName);
        List<String> listofItems = new ArrayList<String>();
        for (int i = 0; i < items.; i++){
            Element element = (Element) gameActionElement.getElementsByTagName(elementName).item(0);
            String elementSpecificName = element.getElementsByTagName("keyphrase").item(0).getTextContent();
            System.out.println(elementSpecificName);
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
        String[] tokens = command.split(" ");
        tokens[0] = tokens[0].substring(0, tokens[0].length() - 1);
        if (!playerHashMap.containsKey(tokens[0])){
            playerHashMap.put(tokens[0], new Player(tokens[0], ""));
        }

        System.out.println("Command: " + command);
        for (String str: tokens){
            System.out.println(str);
        }
        if (tokens.length == 1){
            return "";
        } else if (tokens[1].equals("inventory") || tokens[1].equals("inv")){
            return playerHashMap.get(tokens[0]).displayInventory();
        } else if (tokens[1].equals("get") && tokens.length == 3){
            if (currentLocation.getArtefacts().containsKey(tokens[2])){
                Artefact currentArtefact = currentLocation.getArtefacts().get(tokens[2]);
                playerHashMap.get(tokens[0]).addArtefact(currentArtefact);
                currentLocation.getArtefacts().remove(currentArtefact.getName());
                return "get " + currentArtefact.getName();
            } else{
                return "failed to get, you may input the wrong name or it doesn't exist at all.";
            }
        } else if (tokens[1].equals("drop") && tokens.length == 3){
            Artefact artefact = playerHashMap.get(tokens[0]).removeArtefact(tokens[2]);
            if (artefact != null){
                currentLocation.addArtefact(artefact);
                return "drop " + artefact.getName();
            } else{
                return "doesn't exist this artefact";
            }
        } else if (tokens[1].equals("goto") && tokens.length == 3){
            if (currentLocation.getExits().containsKey(tokens[2])){
                currentLocation = currentLocation.getExits().get(tokens[2]);
                return "you are now at " + currentLocation.getName();
            } else {
                return "You can't go to " + tokens[2];
            }
        } else if (tokens[1].equals("look")){
            return currentLocation.showInformation();
        }
        return "Failed to execute basic command";
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
