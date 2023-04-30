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

    // A lot of tests will probably check the game state using 'look' - so we better make sure 'look' works well !
    @Test
    void testLook() {
        String response = sendCommandToServer("simon: look");
        response = response.toLowerCase();
        System.out.println(response);
        assertTrue(response.contains("cabin"), "Did not see the name of the current room in response to look");
        assertTrue(response.contains("log cabin"), "Did not see a description of the room in response to look");
        assertTrue(response.contains("magic potion"), "Did not see a description of artifacts in response to look");
        assertTrue(response.contains("wooden trapdoor"), "Did not see description of furniture in response to look");
        assertTrue(response.contains("forest"), "Did not see available paths in response to look");
    }

    // Test that we can pick something up and that it appears in our inventory
    @Test
    void testGet() {
        String response;
        sendCommandToServer("simon: get potion");
        response = sendCommandToServer("simon: inv");
        response = response.toLowerCase();
        assertTrue(response.contains("potion"), "Did not see the potion in the inventory after an attempt was made to get it");
        response = sendCommandToServer("simon: look");
        response = response.toLowerCase();
        assertFalse(response.contains("potion"), "Potion is still present in the room after an attempt was made to get it");
    }

    // Test that we can goto a different location (we won't get very far if we can't move around the game !)
    @Test
    void testGoto() {
        String response;
        response = sendCommandToServer("simon: goto forest");
        System.out.println(response);
        response = sendCommandToServer("simon: look");
        response = response.toLowerCase();
        System.out.println("--------------");
        System.out.println(response);
        System.out.println("--------------");
        assertTrue(response.contains("key"), "Failed attempt to use 'goto' command to move to the forest - there is no key in the current location");
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

    @Test
    void testProductioninOtherPlayerInv(){
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

    @Test
    void testProductionInOtherLocation() {
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
}