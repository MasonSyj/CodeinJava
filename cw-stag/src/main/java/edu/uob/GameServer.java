package edu.uob;

import com.alexmerz.graphviz.ParseException;
import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;
import com.alexmerz.graphviz.objects.Edge;

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
            reader = new FileReader("config" + File.separator + entitiesFile);
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
        Location currentLocation;
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
            System.out.println(currentLocation.showInformation());
            locationHashMap.put(currentLocation.getName(), currentLocation);
            /*
            List<Graph> artefacts = others.stream().filter(graph -> graph.getId().getId().equals("artefacts")).toList();
            if (artefacts.size() == 1){
                for (Node artefact: artefacts.get(0).getNodes(false)){
                    Artefact currentArtefact = new Artefact(artefact.getId().getId(), artefact.getAttribute("description"));
                    currentLocation.addArtefact(currentArtefact);
                }
            }
            **/
        }

        //////////////////////////////////////////////////////////////////////////
        //load Commands
    }

    public void loadGameEntity(Location location, ArrayList<Graph> graph, String name){
        List<Graph> graphs = graph.stream().filter(currentGraph -> currentGraph.getId().getId().equals(name)).toList();
        if (graphs.size() == 1){
            for (Node artefact: graphs.get(0).getNodes(false)){
                GameEntity currentEntity = new Artefact(artefact.getId().getId(), artefact.getAttribute("description"));
                if (name.equals("artefacts")){
                    location.addArtefact((Artefact) currentEntity);
                } else if (name.equals("furniture")){
                    location.addFurniture((Furniture) currentEntity);
                } else if (name.equals("characters")){
                    location.addCharacter((Character) currentEntity);
                } else if (name.equals("players")){
                    ((Player) currentEntity).setCurrentLocation(location);
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
        if (tokens.length == 0){
            return "";
        } else if (tokens[0].equals("inventory") || tokens[0].equals("inv")){
            return playerHashMap.get("Simon").displayInventory();
        } else if (tokens[0].equals("get") && tokens.length == 2){
            if (currentLocation.getArtefacts().containsKey(tokens[1])){
                Artefact currentArtefact = currentLocation.getArtefacts().get(tokens[1]);
                playerHashMap.get("Simon").addArtefact(currentArtefact);
                currentLocation.getArtefacts().remove(currentArtefact);
            }
        }

        System.out.println(command);
        return "";
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
