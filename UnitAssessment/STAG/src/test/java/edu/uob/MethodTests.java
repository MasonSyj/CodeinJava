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
import java.util.Set;

class MethodTests {

    private GameServer server;

    // Create a new server _before_ every @Test
    @BeforeEach
    void setup() {
        File entitiesFile = Paths.get("config" + File.separator + "myConfig" + File.separator + "extended-entities.dot").toAbsolutePath().toFile();
        File actionsFile = Paths.get("config" + File.separator + "myConfig" + File.separator +  "extended-actions.xml").toAbsolutePath().toFile();
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
    void numOfEntitiesTest(){
        String[] tokens = {"tree", "axe", "key", "hole", "apple"};
        int num1 = server.numOfEntites(tokens);
        assertTrue(num1 == 4);

        tokens = new String[]{"hello", "bristol"};
        assertTrue(server.numOfEntites(tokens) == 0);

        tokens = new String[]{""};
        assertTrue(server.numOfEntites(tokens) == 0);
    }

    @Test
    void duplicateBasicCommands(){
        assertFalse(server.checkDuplicateBasicCommands(new String[]{"inv", "hello", "apple"}));
        assertTrue(server.checkDuplicateBasicCommands(new String[]{"inv", "look", "apple"}));
        assertFalse(server.checkDuplicateBasicCommands(new String[]{"bristol", "london", "shanghai"}));
        assertTrue(server.checkDuplicateBasicCommands(new String[]{"inv", "inv"}));
        assertFalse(server.checkDuplicateBasicCommands(new String[]{"health"}));
        assertFalse(server.checkDuplicateBasicCommands(new String[]{"goto forest"}));
    }

    @Test
    void lastEntityIndexTest() {
        assertTrue(server.entityIndex(new String[]{"apple", "axe", "forest"}) == 2);
        assertTrue(server.entityIndex(new String[]{"axe", "forest", "apple"}) == 1);
        assertTrue(server.entityIndex(new String[]{"axe", "banana", "apple"}) == 0);
        assertTrue(server.entityIndex(new String[]{"pear", "banana", "apple"}) == -1);
    }

    @Test
    void indexCommand() {
        // return the first one, in practice string[] only has one basic command.
        assertTrue(server.indexBasicCommand(new String[]{"goto", "goto"}) == 0);
        assertTrue(server.indexBasicCommand(new String[]{"sam", "goto"}) == 1);
        assertTrue(server.indexBasicCommand(new String[]{"goto" ,"library"}) == 0);
    }

    @Test
    void findTriggersInCommand(){
        server.handleCommand("simon: ccut down tree hit elf, cut log");
        Set<String> answer = server.getTriggersInCommand();

        assertFalse(answer.contains("cut down"));
        // contain a cut, meaning a string equals to
        assertTrue(answer.contains("cut"));
        assertTrue(answer.contains("hit"));
        assertFalse(answer.contains("elf"));
        assertFalse(answer.contains("apple"));
    }

    @Test
    void testValidTrigger() {
        server.handleCommand("simon: ccut down");
        boolean isvalid = server.isValidTrigger("cut down");
        assertFalse(isvalid);
        isvalid = server.isValidTrigger("cut");
        assertFalse(isvalid);

        server.handleCommand("simon: cut down");
        isvalid = server.isValidTrigger("cut down");
        assertTrue(isvalid);
    }

    @Test
    void isDead() {
        server.handleCommand("simon: look");
        boolean isDead = server.isDead();
        assertFalse(isDead);
        isDead = server.isDead();
        assertFalse(isDead);
        isDead = server.isDead();
        assertTrue(isDead);

        isDead = server.isDead();
        assertFalse(isDead);
    }
}

