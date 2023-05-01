package edu.uob;

import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.nio.file.Paths;
import java.io.IOException;
import java.sql.SQLOutput;
import java.time.Duration;

class ExampleSTAGTests {

    private GameServer server;

    // Create a new server _before_ every @Test
    @BeforeEach
    void setup() {
        File entitiesFile = Paths.get("config" + File.separator + "basic-entities.dot").toAbsolutePath().toFile();
        File actionsFile = Paths.get("config" + File.separator + "basic-actions.xml").toAbsolutePath().toFile();
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
    void usernameIsBuiltin(){
        String response;
        response = sendCommandToServer("inv: look");
        System.out.println(response);
    }

    @Test
    void invExtraneous(){
        String response = sendCommandToServer("simon: inv forest");
        assertTrue(response.contains("doesn't need entity"));
    }

    @Test
    void lookExtraneous(){
        String response = sendCommandToServer("simon: look forest");
        assertTrue(response.contains("doesn't need entity"));
    }

    @Test
    void healthExtraneous() {
        String response = sendCommandToServer("simon: health elf");
        assertTrue(response.contains("doesn't need entity"));
    }

    @Test
    void failedToGoto(){
        String response = sendCommandToServer("simon: goto cellar");
        assertTrue(response.contains("failed to execute goto, You can't go to "));
    }

    @Test
    void failedToGet(){
        String response = sendCommandToServer("simon: get key");
        assertTrue(response.contains("failed to execute get"));
    }

    @Test
    void blankInput(){
        String response = sendCommandToServer("");
        System.out.println(response);
        assertTrue(response.equals(""));
    }

    @Test
    void testdGameActionOnlyTrigger1() {
        String response;
        sendCommandToServer("simon: goto forest");
        response = sendCommandToServer("simon: chop");
        System.out.println(response);
        assertTrue(response.contains("subject"));
        response = sendCommandToServer("simon: look");
        System.out.println(response);
        assertTrue(response.contains("tree"));
    }

    @Test
    void testdGameActionOnlyTrigger2() {
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: cut down");
        response = sendCommandToServer("simon: look");
        System.out.println(response);
        assertTrue(response.contains("tree"));
    }

    @Test
    void testGameActionSuccessful() {
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: chop tree");
        response = sendCommandToServer("simon: look");
        System.out.println(response);
        assertTrue(!response.contains("tree"));
    }

    @Test
    void testGameActionSuccessful2() {
        String response;
        sendCommandToServer("simon: goto forest");
        response = sendCommandToServer("simon: cut down tree");
        System.out.println(response);
    }

    @Test
    void testRemoveExit() {
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: open trapdoor");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cellar"));
        response = sendCommandToServer("simon: close trapdoor");
        System.out.println(response);
        response = sendCommandToServer("simon: look");
        System.out.println(response);
        assertTrue(!response.contains("cellar"));
    }

    @Test
    void testGameActionFailedTrigger() {
        String response;
        response = sendCommandToServer("simon: goto forest");
        response = sendCommandToServer("simon: cut tree down");
        System.out.println(response);
        response = sendCommandToServer("simon: look");
        System.out.println(response);
        assertTrue(response.contains("tree"));
    }

    @Test
    void testGameActionExtraneousCase() {
        String response;
        response = sendCommandToServer("simon: goto forest");
        response = sendCommandToServer("simon: cut down tree elf");
        System.out.println(response);
        response = sendCommandToServer("simon: look");
        System.out.println(response);
        assertTrue(response.contains("tree"));
    }

    @Test
    void testGameActionNotPerformable() {
        String response;
        // current location is cabin and the player doesn't have key(it's in the forest)
        response = sendCommandToServer("simon: open key");
        System.out.println(response);
        response = sendCommandToServer("simon: look");
        System.out.println(response);
        assertFalse(response.contains("cellar"));
    }

    @Test
    void testGameActionAmbiguousCase() {
        String response;
        sendCommandToServer("Simon: goto forest");
        sendCommandToServer("Simon: get key");
        sendCommandToServer("Simon: goto cabin");
        response = sendCommandToServer("Simon: open key");
        System.out.println(response);
        response = sendCommandToServer("Simon: look");
        System.out.println(response);
        assertFalse(response.contains("cellar"));
    }

    @Test
    void testGameActionCompositeCase() {
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        response = sendCommandToServer("simon: open key and hit elf");
        System.out.println(response);
    }

    @Test
    void testGameActionCompositeCase2() {
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        response = sendCommandToServer("simon: open key and look");
        System.out.println(response);
        response = sendCommandToServer("simon: look");
        System.out.println(response);
        assertFalse(response.contains("cellar"));
    }

    @Test
    void testGameActionDuplicateBasicCommands() {
        String response;
        response = sendCommandToServer("Simon: look look");
        System.out.println(response);
        response = sendCommandToServer("Simon: goto forest goto forest");
        System.out.println(response);
        response = sendCommandToServer("Simon: look");
        assertFalse(response.contains("axe"));
    }

    @Test
    void testGameActionComposite3() {
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        response = sendCommandToServer("simon: open trapdoor and open chest");
        System.out.println(response);
        response = sendCommandToServer("simon: open trapdoor and drink potion");
        System.out.println(response);
        response = sendCommandToServer("simon: look");
        System.out.println(response);
        assertFalse(response.contains("cellar"));
    }

    @Test
    void testInvalidBasicCommand() {
        String response;
        sendCommandToServer("simon: gotoforest");
        response = sendCommandToServer("simon: look");
        assertFalse(response.contains("axe"));
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: getkey");
        response = sendCommandToServer("simon: inv");
        assertFalse(response.contains("key"));
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: goto forest cellar");
        response = sendCommandToServer("simon: look");
        System.out.println(response);
        assertFalse(response.contains("tree"));
        sendCommandToServer("simon: goto forest tree");
        response = sendCommandToServer("simon: look");
        assertFalse(response.contains("tree"));
    }

    @Test
    void testGameActionDecoration() {
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: chop tree with axe");
        response = sendCommandToServer("simon: look");
        System.out.println(response);
        assertFalse(response.contains("tree"));
    }

    @Test
    void testGameActionDecoration2() {
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon:use axe to chop tree");
        response = sendCommandToServer("simon: look");
        System.out.println(response);
        assertFalse(response.contains("tree"));
    }

    @Test
    void testGameActionHealth() {
        String response;
        response = sendCommandToServer("simon: health");
        System.out.println(response);
        assertTrue(response.contains("health"));
        assertTrue(response.contains("3"));
    }

    @Test
    void testDeath() {
        String response;
        sendCommandToServer("simon: get potion");
        response = sendCommandToServer("simon: inv");
        System.out.println(response);
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        response = sendCommandToServer("simon: open key");
        System.out.println(response);
        sendCommandToServer("simon: open trapdoor");
        sendCommandToServer("simon: goto cellar");
        System.out.println(sendCommandToServer("simon: look"));
        sendCommandToServer("simon: hit elf");
        response = sendCommandToServer("simon: hit elf");
        System.out.println(response);
        response = sendCommandToServer("simon: health");
        System.out.println(response);
        assertTrue(response.contains("1"));
        response = sendCommandToServer("simon: hit elf");
        System.out.println(response);
        response = sendCommandToServer("simon: look");
        assertFalse(response.contains("elf"));
        System.out.println(response);
        response = sendCommandToServer("simon: inv");
        System.out.println(response);
        assertFalse(response.contains("simon: potion"));
        sendCommandToServer("simon: goto cellar");
        response = sendCommandToServer("simon: look");
        System.out.println(response);
        assertTrue(response.contains("potion"));
    }

    @Test
    void testGoto2() {
        sendCommandToServer("simon: goto");
        String response = sendCommandToServer("simon: look");
        assertFalse(response.contains("axe"));
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
        response = sendCommandToServer("simon: drop potion");
        System.out.println(response);
    }

    @Test
    void eatIcecream() {
        String response;
        response = sendCommandToServer("simon: eat ice-cream");
        assertTrue(response.contains("failed to execute"));
    }

    @Test
    void getWrongName() {
        String response;
        response = sendCommandToServer("simon: get ice-cream");
        System.out.println("failed to execute");
    }

    @Test
    void gotoWrongLocation() {
        String response = sendCommandToServer("simon: goto library");
        System.out.println(response);
        assertTrue(response.contains("requires one entity"));
    }

    @Test
    void lookOtherPlayer() {
        String response;
        sendCommandToServer("simon: look");
        response = sendCommandToServer("andy: look");
        System.out.println(response);
        assertTrue(response.contains("simon"));
    }

}
