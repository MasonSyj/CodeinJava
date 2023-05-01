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
    void testGameActionOnlyTrigger() {
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
    void testGameActionSuccessful() {
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: chop tree");
        response = sendCommandToServer("simon: look");
        assertFalse(response.contains("tree"));
    }


    @Test
    void testGameActionSuccessful2() {
        String response;
        sendCommandToServer("simon: cut down tree");
        sendCommandToServer("simon: chop tree");
        response = sendCommandToServer("simon: look");
        assertFalse(response.contains("tree"));
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
        sendCommandToServer("Simon: open key");
        response = sendCommandToServer("Simon: look");
        assertFalse(response.contains("cellar"));

        sendCommandToServer("Simon: open trapdoor");
        response = sendCommandToServer("Simon: look");
        assertTrue(response.contains("cellar"));
    }

    @Test
    void testGameActionCompositeCase() {
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
    void testGameActionCompositeCase2() {
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
        response = sendCommandToServer("simon: open trapdoor and open chest");
        System.out.println(response);
        response = sendCommandToServer("simon: open trapdoor and drink potion");
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
    void eatIcecream() {
        String response;
        response = sendCommandToServer("simon: eat ice-cream");
        assertTrue(response.contains("failed to execute"));
    }
}
