package edu.uob;

import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.nio.file.Paths;
import java.time.Duration;

class BasicTest {

    private GameServer server;

    // Create a new server _before_ every @Test
    @BeforeEach
    void setup() {
        File entitiesFile = Paths.get("config" + File.separator + "myConfig" + File.separator + "basic-entities.dot").toAbsolutePath().toFile();
        File actionsFile = Paths.get("config" + File.separator + "myConfig" + File.separator + "basic-actions.xml").toAbsolutePath().toFile();
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
    void testGameActionInsensitive() {
        String response;
        sendCommandToServer("simon: GOTO FOREST");
        sendCommandToServer("simon: CHOP TREE");
        response = sendCommandToServer("simon: LOOK");
        assertFalse(response.contains("tree"));
    }

    @Test
    void testGameActionInsensitive2() {
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: Cut dOWn TREE");
        response = sendCommandToServer("simon: lOOk");
        System.out.println(response);
        assertFalse(response.contains("tree"));
    }

    @Test
    void testGameActionDecoration() {
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: chop tree with axe would you do it please");
        response = sendCommandToServer("simon: look");
        assertFalse(response.contains("tree"));
    }

    @Test
    void testGameActionDecoration2() {
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon:use axe to chop tree now at once at whatever cost");
        response = sendCommandToServer("simon: look");
        assertFalse(response.contains("tree"));
    }

    @Test
    void testGameActionPartialOnlyTrigger() {
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: chop");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("tree"));

        sendCommandToServer("simon: cut down");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("tree"));
    }

    @Test
    void testGameActionPartialSuccessfulCommand(){
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: unlock trapdoor");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cellar"));
        assertFalse(response.contains("ring"));
    }
    @Test
    void testGameActionPartialSuccessfulCommand2(){
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: unlock chest");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("ring"));
        assertFalse(response.contains("cellar"));
    }

    @Test
    void testGameActionPartialFailedTrigger() {
        String response;
        response = sendCommandToServer("simon: goto forest");
        response = sendCommandToServer("simon: cut tree down");
        System.out.println(response);
        response = sendCommandToServer("simon: look");
        System.out.println(response);
        assertTrue(response.contains("tree"));
    }

    // only allow entity that only included in the action
    @Test
    void testGameActionExtraneousCase() {
        String response;
        sendCommandToServer("simon: goto forest");

        sendCommandToServer("simon: cut down tree elf");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("tree"));

        sendCommandToServer("simon: cut down tree forest");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("tree"));

        sendCommandToServer("simon: cut down tree storeroom");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("tree"));

        sendCommandToServer("simon: cut down tree potion");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("tree"));
    }

    @Test
    void testGameActionExtraneousSuccessifulCase() {
        String response;
        sendCommandToServer("simon: goto forest");

        // log is a produced entity, so it's allowed here
        sendCommandToServer("simon: cut down tree log");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("tree"));
    }

    @Test
    void testGameActionAmbiguousCase() {
        String response;
        sendCommandToServer("Simon: goto forest");
        sendCommandToServer("Simon: get key");
        sendCommandToServer("Simon: goto cabin");
        sendCommandToServer("Simon: open key");
        response = sendCommandToServer("Simon: look");
        assertFalse(response.contains("cellar"));

        sendCommandToServer("Simon: open trapdoor");
        response = sendCommandToServer("Simon: look");
        assertTrue(response.contains("cellar"));
    }

    @Test
    void testGameActionComposite() {
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: open key and hit elf");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("trapdoor"));
        assertFalse(response.contains("elf"));
    }

    @Test
    void testGameActionComposite2() {
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        response = sendCommandToServer("simon: open key and look");
        assertFalse(response.contains("potion"));
        assertFalse(response.contains("cellar"));
        response = sendCommandToServer("simon: inv");
        assertTrue(response.contains("key"));
        response = sendCommandToServer("simon: look");
        assertFalse(response.contains("cellar"));
    }

    @Test
    void testGameActionComposite3() {
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: open trapdoor and open chest");
        response = sendCommandToServer("simon: look");
        assertFalse(response.contains("ring"));
        sendCommandToServer("simon: open trapdoor and get potion");
        response = sendCommandToServer("simon: inv");
        assertFalse(response.contains("potion"));

        sendCommandToServer("simon: open trapdoor and drink potion");
        response = sendCommandToServer("simon: health");
        assertTrue(response.contains("3"));
        assertFalse(response.contains("2"));
        response = sendCommandToServer("simon: look");
        assertFalse(response.contains("cellar"));
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
    void testPlayerNameAsEntity(){
        String response;
        response = sendCommandToServer("simon: andy look");
        System.out.println(response);
        response = sendCommandToServer("andy: look");
        response = sendCommandToServer("simon: andy look");
        System.out.println(response);
    }


}
