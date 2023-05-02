package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class pengExtensionTests {
    private GameServer server;
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
    @Test
    void healthTest(){
        String response;
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: open trapdoor");
        sendCommandToServer("simon: goto cellar");
        response=sendCommandToServer("simon: health");
        assertTrue(response.contains("3"),"Health should be 3 at moment");
        sendCommandToServer("simon: hit elf");
        response=sendCommandToServer("simon: health");
        assertTrue(response.contains("2"),"Health should be 2 at moment");
        sendCommandToServer("simon: drink potion");
        response=sendCommandToServer("simon: health");
        assertTrue(response.contains("3"),"Health should be 3 after drink potion");
    }
    @Test
    void deathTest(){
        String response;
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: get coin");
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: cut tree");
        sendCommandToServer("simon: get log");
        sendCommandToServer("simon: goto riverbank");
        sendCommandToServer("simon: bridge log");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: open trapdoor");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: goto riverbank");
        sendCommandToServer("simon: get horn");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: goto cellar");
        sendCommandToServer("simon: inv");
        sendCommandToServer("simon: hit elf");
        sendCommandToServer("simon: hit elf");
        sendCommandToServer("simon: hit elf");
        response=sendCommandToServer("simon: inv");
        assertFalse(response.toLowerCase().contains("horn"));
        response=sendCommandToServer("simon: goto cellar");
        assertTrue(response.toLowerCase().contains("horn"));
    }
    @Test
    void multipleTriggersTest1(){
        String response;
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: cut down tree with axe");
        response = sendCommandToServer("simon: look");
        assertFalse(response.toLowerCase().contains("tree"),"Tree didn't cut down after command");
    }
    @Test
    void multipleTriggersTest2(){
        String response;
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: axe cut tree");
        response = sendCommandToServer("simon: look");
        assertFalse(response.toLowerCase().contains("tree"),"Tree didn't cut down after command");
    }
    @Test
    void multipleTriggersTest3(){
        String response;
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: axe cut tree to get log");
        response = sendCommandToServer("simon: look");
        assertTrue(response.toLowerCase().contains("tree"),"Tree cut down after invalid command");
    }
    @Test
    void multipleTriggersTest4(){
        String response;
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: axe cut tree and drink potion");
        response = sendCommandToServer("simon: look");
        assertTrue(response.toLowerCase().contains("tree"),"Tree cut down after invalid command");
    }
//    @Test
//    void multiplePlayerTest1(){
//        String response;
//        response=sendCommandToServer("simon: andy: simon: neil:look");
//        assertEquals(server.game.getCurrentPlayer().getName(),"simon","Players should be added");
//        assertFalse(response.toLowerCase().contains("cabin"),"It is not a valid look command");
//        sendCommandToServer("simon: look");
//        sendCommandToServer("simon: look neil");
//        sendCommandToServer("neil: look");
//        assertEquals(server.game.getCurrentPlayer().getName(),"neil");
//        sendCommandToServer("tim: look");
//        assertEquals(server.game.getCurrentPlayer().getName(),"tim");
//        response=sendCommandToServer("simon: look");
//        assertTrue(response.contains("neil"),"Player neil should display in current location");
//        assertTrue(response.contains("tim"),"Player tim should display in current location");
//    }
    @Test
    void multiplePlayerTest2(){
        String response;
        sendCommandToServer("simon: look");
        sendCommandToServer("simon: neil:look");
        sendCommandToServer("neil: get axe");
        response=sendCommandToServer("simon: look");
        assertFalse(response.contains("axe"),"Simon should not see axe cause neil gpt it");
        assertTrue(response.contains("neil"),"Neil should be display in current location");
        sendCommandToServer("");
    }
    @Test
    void multiplePlayerTest3(){
        String response;
        sendCommandToServer("simon: look");
        sendCommandToServer("simon: neil:look");
        sendCommandToServer("neil: get axe");
        response=sendCommandToServer("simon: look");
        assertFalse(response.contains("axe"),"Simon should not see axe cause neil got it");
        assertTrue(response.contains("neil"),"Neil should be display in current location");
        sendCommandToServer("simon: cut tree");
        response=sendCommandToServer("simon: goto forest");
        assertTrue(response.contains("tree"),"Simon can not cut the tree in other location without axe");
        sendCommandToServer("neil: goto forest");
        sendCommandToServer("simon: cut tree");
        response=sendCommandToServer("simon: look");
        assertTrue(response.contains("tree"),"Simon can not cut the tree without axe");
        sendCommandToServer("neil: drop axe");
        sendCommandToServer("simon: cut tree");
        response=sendCommandToServer("simon: look");
        assertFalse(response.contains("tree"),"Simon can cut the tree with axe");
    }
    @Test
    void validMultipleTriggersTest1(){
        String response;
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: cut tree and cut down tree");
        response = sendCommandToServer("simon: look");
        assertFalse(response.toLowerCase().contains("tree"),"Tree didn't cut down after command");
    }
    @Test
    void validMultipleTriggersTest2(){
        String response;
        sendCommandToServer("simon: open unlock trapdoor key");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: look");
        sendCommandToServer("simon: cut tree and cut down tree");
        response = sendCommandToServer("simon: look");
        assertTrue(response.toLowerCase().contains("key"),"Key was used in other location");
    }
    @Test
    void actionsInOtherLocationTest(){
        String response;
        sendCommandToServer("simon: look");
        response=sendCommandToServer("simon: cut tree");
        assertFalse(response.contains("log"),"Tree was cut in other location");
    }
    @Test
    void doActionWithoutEssentialEntity(){
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: cut tree");
        response=sendCommandToServer("simon: look");
        assertTrue(response.contains("tree"),"Tree was cut without axe");
    }
    @Test
    void extraneousCMDTest1(){
        String response;
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: get coin");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: cut tree with key");
        response=sendCommandToServer("simon: look");
        assertTrue(response.toLowerCase().contains("tree"),"Simon can not cut tree with key!!!!!");
    }
    @Test
    void extraneousCMDTest2(){
        String response;
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: get coin");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: cut tree tree");
        response=sendCommandToServer("simon: look");
        assertFalse(response.toLowerCase().contains("tree"),"Simon can not cut tree with key!!!!!");
    }
    @Test
    void extraneousCMDTest3(){
        String response;
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: get coin");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: cut cut tree");
        response=sendCommandToServer("simon: look");
        assertFalse(response.toLowerCase().contains("tree"),"Simon can not cut tree with key!!!!!");
    }
    @Test
    void fullGameTest(){
        String response;
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: get coin");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: cut tree");
        sendCommandToServer("simon: get log");
        sendCommandToServer("simon: goto riverbank");
        sendCommandToServer("simon: bridge log");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: open trapdoor");
        sendCommandToServer("simon: goto cellar");
        sendCommandToServer("simon: pay elf");
        sendCommandToServer("simon: get shovel");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: goto riverbank");
        sendCommandToServer("simon: goto clearing");
        sendCommandToServer("simon: dig ground");
        response=sendCommandToServer("simon: look");
        assertTrue(response.toLowerCase().contains("a big pot of gold"));
        assertTrue(response.toLowerCase().contains("a deep hole in the ground"));
        sendCommandToServer("simon: goto riverbank");
        sendCommandToServer("simon: blow horn");
        response=sendCommandToServer("simon: look");
        assertTrue(response.toLowerCase().contains("wood cutter"),"Lumberjack should appear after blow the horn");
        sendCommandToServer("simon: get horn");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: blow horn");
        response=sendCommandToServer("simon: look");
        assertTrue(response.toLowerCase().contains("wood cutter"),"Lumberjack should appear after blow the horn");
    }
}
