package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class ExtendedTest {

    private GameServer server;

    // Create a new server _before_ every @Test
    @BeforeEach
    void setup() {
        File entitiesFile = Paths.get("config" + File.separator + "extended-entities.dot").toAbsolutePath().toFile();
        File actionsFile = Paths.get("config" + File.separator + "extended-actions.xml").toAbsolutePath().toFile();
        server = new GameServer(entitiesFile, actionsFile);
    }

    String sendCommandToServer(String command) {
        // Try to send a command to the server - this call will timeout if it takes too long (in case the server enters an infinite loop)
        return assertTimeoutPreemptively(Duration.ofMillis(1000), () -> {
                    return server.handleCommand(command);
                },
                "Server took too long to respond (probably stuck in an infinite loop)");
    }

    @Test
    void removeCharacterAndProduceHealth() {
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: open key");
        sendCommandToServer("simon: goto cellar");
        response = sendCommandToServer("simon: look");
        sendCommandToServer("simon: hit elf");
        response = sendCommandToServer("simon: health");
        assertTrue(response.contains("2"));
        response = sendCommandToServer("simon: hit spider");
        response = sendCommandToServer("simon: health");
        assertTrue(response.contains("3"));
        response = sendCommandToServer("simon: look");
        assertFalse(response.contains("spider"));
    }

    @Test
    void openCellarWhenKeyOnGround() {
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: drop key");
        sendCommandToServer("simon: open key");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cellar"));
    }
    @Test
    void basicCommandWrongOrder() {
        String response;
        response = sendCommandToServer("simon: forest goto");
        assertTrue(response.contains("wrong"));
        assertTrue(response.contains("logic order"));
    }

    @Test
    void basicCommandManyEntites(){
        String response;
        response = sendCommandToServer("simon: goto forest forest");
        System.out.println(response);
        assertTrue(response.contains("have more than two"));
    }

    @Test
    void testLumberjackInOtherLocation(){
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: goto riverbank");
        response = sendCommandToServer("simon: look");
        assertFalse(response.contains("lumberjack"));
        response = sendCommandToServer("simon: blow horn");
        sendCommandToServer("simon: get horn");
        System.out.println(response);

        sendCommandToServer("simon: goto forest");

        response = sendCommandToServer("simon: look");
        assertFalse(response.contains("lumberjack"));

        response = sendCommandToServer("simon: blow horn");
        System.out.println(response);
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("lumberjack"));
    }

    // log is no longer a subject, just a consumption
    // so that is doesn't need be in the current location
    // if in other player's inv, the action will fail
    @Test
    void testConsumptioninOtherPlayerInv(){
        String response;
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: cut down tree");
        sendCommandToServer("simon: get log");
        sendCommandToServer("sam: goto forest");
        sendCommandToServer("sam: look");
        sendCommandToServer("sam: goto riverbank");
        response = sendCommandToServer("sam: look");
        assertFalse(response.contains("clearing"));
        response = sendCommandToServer("sam: bridge river");
        response = sendCommandToServer("sam: look");
        assertFalse(response.contains("clearing"));
    }

    // if in other location, the action will succeed
    @Test
    void testConsumptionInOtherLocation() {
        String response;
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: cut down tree");
        sendCommandToServer("sam: goto forest");
        response = sendCommandToServer("sam: look");
        assertTrue(response.contains("log"));
        sendCommandToServer("sam: goto riverbank");
        response = sendCommandToServer("sam: look");
        assertFalse(response.contains("clearing"));
        response = sendCommandToServer("sam: bridge river");
        response = sendCommandToServer("sam: look");
        assertTrue(response.contains("clearing"));
    }

    @Test
    void testProductionIsFurniture() {
        String response;
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: cut down tree");
        sendCommandToServer("sam: get coin");
        sendCommandToServer("sam: goto forest");
        sendCommandToServer("sam: get key");
        sendCommandToServer("sam: goto cabin");
        sendCommandToServer("sam: open key");
        sendCommandToServer("sam: goto cellar");
        response = sendCommandToServer("sam: pay elf");
        System.out.println(response);
        response = sendCommandToServer("sam: look");
        System.out.println(response);
        assertTrue(response.contains("shovel"));
        sendCommandToServer("sam: get shovel");
        response = sendCommandToServer("sam: inv");
        String response2 = sendCommandToServer("sam: inventory");
        assertTrue(response.equals(response2));
        assertTrue(response.contains("shovel"));
        sendCommandToServer("sam: goto cabin");
        sendCommandToServer("sam: goto forest");
        sendCommandToServer("sam: goto riverbank");
        sendCommandToServer("sam: bridge river");
        sendCommandToServer("sam: goto clearing");
        response = sendCommandToServer("sam: dig ground");
        System.out.println(response);
        response = sendCommandToServer("sam: look");
        assertTrue(response.contains("hole"));
        assertTrue(response.contains("gold"));
    }

    // log as the production of the cut down tree, I put it to the cabin
    @Test
    void testProductionInOtherLocation() {
        String response;
        response = sendCommandToServer("simon: look");
        System.out.println(response);
        assertTrue(response.contains("log"));
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: cut down tree");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("log"));
        sendCommandToServer("simon: goto cabin");
        response = sendCommandToServer("simon: look");
        System.out.println(response);
        assertFalse(response.contains("log"));
    }

    @Test
    void testProductionInOtherPlayer() {
        String response;
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("log"));
        sendCommandToServer("sam: get log");
        response = sendCommandToServer("sam: inv");
        assertTrue(response.contains("log"));
        response = sendCommandToServer("simon: look");
        assertFalse(response.contains("log"));

        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: cut down tree");
        response = sendCommandToServer("simon: look");
        assertFalse(response.contains("log"));

        sendCommandToServer("simon: goto cabin");
        response = sendCommandToServer("simon: look");
        assertFalse(response.contains("log"));

        response = sendCommandToServer("sam: inv");
        assertTrue(response.contains("log"));
    }
}