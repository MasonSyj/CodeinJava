package edu.uob;

import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.nio.file.Paths;
import java.io.IOException;
import java.time.Duration;

class OriginTest {

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
        return assertTimeoutPreemptively(Duration.ofMillis(1000), () -> { return server.handleCommand(command);},
                "Server took too long to respond (probably stuck in an infinite loop)");
    }

    // A lot of tests will probably check the game state using 'look' - so we better make sure 'look' works well !
    @Test
    void testLook() {
        String response = sendCommandToServer("simon: look");
        response = response.toLowerCase();
        assertTrue(response.contains("cabin"), "Did not see the name of the current room in response to look");
        assertTrue(response.contains("log cabin"), "Did not see a description of the room in response to look");
        assertTrue(response.contains("magic potion"), "Did not see a description of artifacts in response to look");
        assertTrue(response.contains("wooden trapdoor"), "Did not see description of furniture in response to look");
        assertTrue(response.contains("forest"), "Did not see available paths in response to look");

        response = sendCommandToServer("simon: look forest");
        assertFalse(response.contains("cabin"));
        assertFalse(response.contains("log cabin"));
        assertFalse(response.contains("magic potion"));
        assertFalse(response.contains("wooden trapdoor"));
        assertFalse(response.contains("forest"));
    }

    @Test
    void testLookOtherPlayer() {
        String response;
        sendCommandToServer("simon: look");
        response = sendCommandToServer("andy: look");
        assertTrue(response.contains("simon"));
        response = sendCommandToServer("mason: look");
        assertTrue(response.contains("simon"));
        assertTrue(response.contains("andy"));
    }

    // Test that we can pick something up and that it appears in our inventory
    @Test
    void testGet()
    {
        String response;
        sendCommandToServer("simon: get potion");
        response = sendCommandToServer("simon: inv");
        response = response.toLowerCase();
        assertTrue(response.contains("potion"), "Did not see the potion in the inventory after an attempt was made to get it");
        response = sendCommandToServer("simon: look");
        response = response.toLowerCase();
        assertFalse(response.contains("potion"), "Potion is still present in the room after an attempt was made to get it");
    }

    @Test
    void testGetFailed(){
        String response;
        sendCommandToServer("simon: get key");
        response = sendCommandToServer("simon: inv");
        assertFalse(response.contains("key"));

        sendCommandToServer("simon: get ice-cream");
        response = sendCommandToServer("simon: inv");
        assertFalse(response.contains("ice-cream"));
    }
    // Test that we can goto a different location (we won't get very far if we can't move around the game !)
    @Test
    void testGoto()
    {
        sendCommandToServer("simon: goto forest");
        String response = sendCommandToServer("simon: look");
        response = response.toLowerCase();
        assertTrue(response.contains("key"), "Failed attempt to use 'goto' command to move to the forest - there is no key in the current location");
    }

    @Test
    void testGotoFailed() {
        sendCommandToServer("simon: goto");
        String response = sendCommandToServer("simon: look");
        assertFalse(response.contains("key"));
        assertTrue(response.contains("axe"));
        assertTrue(response.contains("trapdoor"));

        sendCommandToServer("simon: goto cellar");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("axe"));
        assertTrue(response.contains("trapdoor"));

        sendCommandToServer("simon: goto library");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("axe"));
        assertTrue(response.contains("trapdoor"));
    }

    @Test
    void testDrop() {
        String response;
        sendCommandToServer("simon: get potion");
        response = sendCommandToServer("simon: look");
        assertFalse(response.contains("potion"));
        response = sendCommandToServer("simon: inv");
        assertTrue(response.contains("potion"));
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: drop potion");
        response = sendCommandToServer("simon: inv");
        assertFalse(response.contains("potion"));
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("potion"));
    }

    @Test
    void testInv() {
        String response;
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: get coin");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        response = sendCommandToServer("simon: inv");
        assertTrue(response.contains("key"));
        assertTrue(response.contains("potion"));
        assertTrue(response.contains("coin"));
        assertFalse(response.contains("axe"));

        response = sendCommandToServer("simon: inv forest");
        assertFalse(response.contains("key"));
        assertFalse(response.contains("potion"));
        assertFalse(response.contains("coin"));
        assertFalse(response.contains("axe"));
    }

    @Test
    void testHealth() {
        String response;
        response = sendCommandToServer("simon: health");
        assertTrue(response.contains("health"));
        assertTrue(response.contains("3"));
        response = sendCommandToServer("simon: health elf");
        assertFalse(response.contains("3"));
    }
    @Test
    void testHealthAndDeath() {
        String response;
        sendCommandToServer("simon: get potion");
        response = sendCommandToServer("simon: inventory");
        assertTrue(response.contains("potion"));
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: open key");
        sendCommandToServer("simon: goto cellar");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("elf"));
        sendCommandToServer("simon: hit elf");
        sendCommandToServer("simon: hit elf");
        response = sendCommandToServer("simon: health");
        assertTrue(response.contains("1"));
        sendCommandToServer("simon: hit elf");
        response = sendCommandToServer("simon: look");
        assertFalse(response.contains("elf"));
        response = sendCommandToServer("simon: inv");
        assertFalse(response.contains("potion"));
        sendCommandToServer("simon: goto cellar");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("potion"));
    }

    @Test
    void blankInput(){
        String response = sendCommandToServer("");
        assertTrue(response.equals(""));
    }

    @Test
    void testBasicCommandWithWrongEntityNumber() {
        String response;
        sendCommandToServer("simon: gotoforest");
        response = sendCommandToServer("simon: look");
        assertFalse(response.contains("key"));
        assertFalse(response.contains("tree"));
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: getkey");
        response = sendCommandToServer("simon: inv");
        assertFalse(response.contains("key"));
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: goto forest cellar");
        response = sendCommandToServer("simon: look");
        assertFalse(response.contains("tree"));
        sendCommandToServer("simon: goto forest tree");
        response = sendCommandToServer("simon: look");
        assertFalse(response.contains("tree"));
    }

    @Test
    void testGameActionDuplicateBasicCommands() {
        String response;
        sendCommandToServer("Simon: look look");
        sendCommandToServer("Simon: goto forest goto forest");
        response = sendCommandToServer("Simon: look");
        assertFalse(response.contains("key"));
        assertFalse(response.contains("tree"));
    }

    @Test
    void eatIcecream() {
        String response;
        response = sendCommandToServer("simon: eat ice-cream");
        assertTrue(response.contains("failed to execute"));
    }
}