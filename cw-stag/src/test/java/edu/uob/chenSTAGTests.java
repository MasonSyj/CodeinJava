package edu.uob;

import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.nio.file.Paths;
import java.time.Duration;

class chenSTAGTests {

  private GameServer server;

  // Create a new server _before_ every @Test
  @BeforeEach
  void setup() throws Exception {
//      File entitiesFile = Paths.get("config" + File.separator + "basic-entities.dot").toAbsolutePath().toFile();
//      File actionsFile = Paths.get("config" + File.separator + "basic-actions.xml").toAbsolutePath().toFile();
      File entitiesFile = Paths.get("config" + File.separator + "extended-entities.dot").toAbsolutePath().toFile();
      File actionsFile = Paths.get("config" + File.separator + "extended-actions.xml").toAbsolutePath().toFile();
      server = new GameServer(entitiesFile, actionsFile);
  }

  String sendCommandToServer(String command) {
      // Try to send a command to the server - this call will timeout if it takes too long (in case the server enters an infinite loop)
//      return assertTimeoutPreemptively(Duration.ofMillis(1000), () -> {
          return server.handleCommand(command);
//          },
//      "Server took too long to respond (probably stuck in an infinite loop)");
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

    // Test that we can goto a different location (we won't get very far if we can't move around the game !)
    @Test
    void testGoto()
    {
        sendCommandToServer("simon: goto forest");
        String response = sendCommandToServer("simon: look");
        response = response.toLowerCase();
        assertTrue(response.contains("key"), "Failed attempt to use 'goto' command to move to the forest - there is no key in the current location");
    }

    // Add more unit tests or integration tests here.

    @Test
    void testHealth() {
        String response = sendCommandToServer("simon: health");
        response = response.toLowerCase();
        assertTrue(response.contains("3"), "Did not find the correct health.");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: open key");
        sendCommandToServer("simon: goto cellar");
        sendCommandToServer("simon: hit elf");
        String response2 = sendCommandToServer("simon: health");
        assertTrue(response2.contains("2"), "Did not find the correct health.");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: drink potion");
        String response3 = sendCommandToServer("simon: health");
        assertTrue(response3.contains("3"), "Did not find the correct health.");
    }

    @Test
    void testMaxHealth() {
        String response = sendCommandToServer("simon: health");
        response = response.toLowerCase();
        assertTrue(response.contains("3"), "Did not find the correct health.");
        sendCommandToServer("simon: drink potion");
        String response1 = sendCommandToServer("simon: health");
        assertTrue(response1.contains("3"), "Did not find the correct health.");
        assertFalse(response1.contains("4"), "Did not find the correct health.");
    }

    @Test
    void testDeath() {
        String response = sendCommandToServer("simon: health");
        response = response.toLowerCase();
        assertTrue(response.contains("3"), "Did not find the correct health.");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: open key");
        sendCommandToServer("simon: goto cellar");
        sendCommandToServer("simon: hit elf");
        sendCommandToServer("simon: hit elf");
        String response1 = sendCommandToServer("simon: hit elf");
        System.out.println(response1);
//        assertTrue(response1.contains("You died and lost all of your items"), "Did not find the death return.");
        String response2 = sendCommandToServer("simon: look");
        assertTrue(response2.contains("cabin"), "Did not find the correct place.");
        String response3 = sendCommandToServer("simon: health");
        assertTrue(response3.contains("3"), "Did not find the correct health.");
        String response4 = sendCommandToServer("simon: goto cellar");
        assertTrue(response4.contains("A dusty cellar"), "Did not find the correct location.");
        String response5 = sendCommandToServer("simon: look");
        assertTrue(response5.contains("potion"), "Did not find the correct dropped artefact.");
    }

    @Test
    void testDeathWithMultiPlayers() {
        String response = sendCommandToServer("simon: health");
        response = response.toLowerCase();
        assertTrue(response.contains("3"), "Did not find the correct health.");
        sendCommandToServer("andy: look");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: open key");
        sendCommandToServer("simon: goto cellar");
        sendCommandToServer("andy: goto cellar");
        sendCommandToServer("simon: hit elf");
        sendCommandToServer("simon: hit elf");
        String response1 = sendCommandToServer("simon: hit elf");
        System.out.println(response1);
//        assertTrue(response1.contains("You died and lost all of your items"), "Did not find the death return.");
        String responseAfterOneDead = sendCommandToServer("andy: look");
        assertTrue(responseAfterOneDead.contains("potion"), "Did not find the correct artefact.");
        String secondPlayerHealth = sendCommandToServer("andy: health");
        assertTrue(secondPlayerHealth.contains("3"), "Did not find the correct health.");
        String response2 = sendCommandToServer("simon: look");
        assertTrue(response2.contains("cabin"), "Did not find the correct place.");
        String response3 = sendCommandToServer("simon: health");
        assertTrue(response3.contains("3"), "Did not find the correct health.");
        String response4 = sendCommandToServer("simon: goto cellar");
        assertTrue(response4.contains("A dusty cellar"), "Did not find the correct location.");
        String response5 = sendCommandToServer("simon: look");
        assertTrue(response5.contains("potion"), "Did not find the correct dropped artefact.");
    }
  @Test
    void testWrongCommandDoubleCmd() {
      String response = sendCommandToServer("simon: look look");
      System.out.println(response);
      assertTrue(response.contains("error"), "Did not see the name of the current room in response to look");
    }

    @Test
    void testDecorationWord1() {
        sendCommandToServer("simon: get axe");
        String response = sendCommandToServer("simon: inv");
        assertTrue(response.contains("axe"), "Did not find the correct dropped artefact.");
        sendCommandToServer("simon: Please goto forest");
        sendCommandToServer("simon: please chop the tree using the axe");
        String response1 = sendCommandToServer("simon: look");
        assertTrue(response1.contains("log"), "Did not find the correct dropped artefact.");
    }

    @Test
    void testDecorationWord2() {
        sendCommandToServer("simon: get axe");
        String response = sendCommandToServer("simon: inv");
        assertTrue(response.contains("axe"), "Did not find the correct dropped artefact.");
        sendCommandToServer("simon: Please goto forest");
        sendCommandToServer("simon: use axe to chop tree");
        String response1 = sendCommandToServer("simon: look");
        assertTrue(response1.contains("log"), "Did not find the correct dropped artefact.");
    }

    @Test
    void testWrongBuiltInMatch() {
        String response = sendCommandToServer("simon: look lumberjack");
        assertTrue(response.contains("error"));
    }

    @Test
    void testWrongBuiltInWithMultiEntity() {
        String response;
        sendCommandToServer("simon: get axe axe");
        response = sendCommandToServer("simon: inv");
        assertFalse(response.contains("axe"));
    }

    @Test
    void testWrongBuiltInWithMultiEntity1() {
        String response;
        sendCommandToServer("simon: goto forest forest");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"));
    }

    @Test
    void testMultiPlayers() {
        sendCommandToServer("simon: look");
        sendCommandToServer("simon: goto forest");
        String response = sendCommandToServer("andy: look");
        sendCommandToServer("andy: goto forest");
        assertTrue(response.contains("cabin"), "Did not find the correct location.");
    }

    @Test
    void testMultiPlayersInv() {
        String response;
        sendCommandToServer("simon: look");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: goto riverbank");
        sendCommandToServer("simon: get horn");
        sendCommandToServer("andy: goto forest");
        sendCommandToServer("andy: goto riverbank");
        sendCommandToServer("andy: blow horn");
        response = sendCommandToServer("andy: look");
        assertFalse(response.contains("wood cutter"));
        sendCommandToServer("simon: blow horn");
        response = sendCommandToServer("andy: look");
        assertTrue(response.contains("lumberjack"));
    }
    @Test
    void testWrongCommandBuiltInWithActions() {
        String response = sendCommandToServer("simon: look chop");
        response = response.toLowerCase();
        assertFalse(response.contains("cabin"));
//        assertTrue(response.contains("log cabin"), "Did not see a description of the room in response to look");
//        assertTrue(response.contains("magic potion"), "Did not see a description of artifacts in response to look");
//        assertTrue(response.contains("wooden trapdoor"), "Did not see description of furniture in response to look");
//        assertTrue(response.contains("forest"), "Did not see available paths in response to look");
    }

    @Test
    void testShortCutAction() {
        sendCommandToServer("simon: look");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: drop key");
        sendCommandToServer("simon: open trapdoor");
        String response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cellar"), "Did not see the correct location.");
    }

    @Test
    void testPartialCommand() {
        sendCommandToServer("simon: look");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: unlock trapdoor with key");
        String response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cellar"), "Did not see the correct location.");
    }
    @Test
    void testDifferentCommandOrder() {
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: goto forest");
        String response = sendCommandToServer("simon: axe chop tree");
        assertTrue(response.contains("log"), "Did not see the correct location.");
    }
    @Test
    void testDifferentCommandOrder1() {
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: goto forest");
        String response = sendCommandToServer("simon: tree chop axe");
        assertTrue(response.contains("log"), "Did not see the correct location.");
    }
    @Test
    void testPartialCommand1() {
        sendCommandToServer("simon: look");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: unlock trapdoor");
        String response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cellar"), "Did not see the correct location.");
    }
    @Test
    void testPartialCommand2() {
        sendCommandToServer("simon: look");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: unlock with key");
        String response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cellar"), "Did not see the correct location.");
    }
    @Test
    void testWrongTriggerCommand() {
        String response;
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: goto forest");
        response = sendCommandToServer("simon: axe tree");
        response = sendCommandToServer("simon: look");
        assertFalse(response.contains("log"));
        sendCommandToServer("simon: cut tree with axe look");
        response = sendCommandToServer("simon: look");
        assertFalse(response.contains("log"));
        response = sendCommandToServer("simon: axe cut down");
        System.out.println(response);
        response = sendCommandToServer("simon: look");
        System.out.println(response);
        assertTrue(response.contains("log"));

    }
    @Test
    void testWrongTriggerCommand1() {
        String response;
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: goto forest");
        response = sendCommandToServer("simon: cut down");
        response = sendCommandToServer("simon: look");
        System.out.println(response);
        assertFalse(response.contains("log"));
    }
    @Test
    void testWrongPartialCommand() {
        String response;
        sendCommandToServer("simon: look");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: unlock");
        response = sendCommandToServer("simon: look");
        assertFalse(response.contains("cellar"));
    }
    @Test
    void testWrongMultiEntitiesCommand() {
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: inv");
        sendCommandToServer("simon: Please goto forest");
        sendCommandToServer("simon: please chop the tree using the axe and potion");
        String response = sendCommandToServer("simon: look");
        System.out.println(response);
        assertFalse(response.contains("log"), "Did not find the correct return message");
    }
    @Test
    void testMultiEntitiesCommand() {
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: inv");
        sendCommandToServer("simon: Please goto forest");
        sendCommandToServer("simon: cut down please chop the tree with axe");
        String response = sendCommandToServer("simon: look");
        assertTrue(response.contains("log"), "Did not find the correct return message");
    }
    @Test
    void testMultiEntitiesCommand2() {
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: inv");
        sendCommandToServer("simon: Please goto forest");
        sendCommandToServer("simon: cut tree tree");
        String response = sendCommandToServer("simon: look");
        assertTrue(response.contains("log"), "Did not find the correct return message");
    }
    @Test
    void testMultiTriggerCommand() {
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: inv");
        sendCommandToServer("simon: Please goto forest");
        sendCommandToServer("simon: chop tree with axe to cut it down");
        String response = sendCommandToServer("simon: look");
        assertTrue(response.contains("log"));
    }
    @Test
    void testMultiTriggerCommand1() {
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: inv");
        sendCommandToServer("simon: Please goto forest");
        sendCommandToServer("simon: cut tree and cut tree");
        String response = sendCommandToServer("simon: look");
        assertTrue(response.contains("log"));
    }
    @Test
    void testMultiTriggerCommand3() {
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: inv");
        sendCommandToServer("simon: Please goto forest");
        sendCommandToServer("simon: cut tree from forest");
        String response = sendCommandToServer("simon: look");
        assertFalse(response.contains("log"));
    }
    @Test
    void testMultiTriggerCommand2() {
        String response = sendCommandToServer("simon: get axe");
        System.out.println(response);
        sendCommandToServer("simon: inv");
        sendCommandToServer("simon: Please goto forest");
        response = sendCommandToServer("simon: chop with axe to cut down tree");
        System.out.println(response);
        response = sendCommandToServer("simon: look");
        System.out.println(response);
        assertTrue(response.contains("log"));
    }
    @Test
    void testWrongCompositeCommand() {
        String response;
        sendCommandToServer("simon: get axe and potion");
        response = sendCommandToServer("simon: inv");
        assertFalse(response.contains("axe"));
        assertFalse(response.contains("potion"));
    }
    @Test
    void testWrongMultiPlayerCommand() {
        String response;
        sendCommandToServer("simon: andy:goto forest");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"));
        assertFalse(response.contains("andy"));
    }
    @Test
    void testWrongCompositeCommand1() {
        String response;
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: open key and potion");
        response = sendCommandToServer("simon: look");
        assertFalse(response.contains("elf"), "Did not find the correct return message");
        response = sendCommandToServer("simon: inv");
        assertTrue(response.contains("key"), "Did not find the correct return message");
        assertTrue(response.contains("potion"), "Did not find the correct return message");
    }

    @Test
    void testWrongBuiltIn() {
        String response = sendCommandToServer("simon: get");
        System.out.println(response);
        assertTrue(response.contains("error"), "Did not find the correct return message");
    }
    @Test
    void testWrongBuiltIn1() {
        String response = sendCommandToServer("simon: get position");
        System.out.println(response);
        assertTrue(response.contains("error"), "Did not find the correct return message");
    }
    @Test
    void testWrongBuiltIn2() {
        String response = sendCommandToServer("simon: look0");
        System.out.println(response);
        assertTrue(response.contains("error"));
    }
    @Test
    void testWrongBuiltIn4() {
        String response = sendCommandToServer("simon: look axe");
        System.out.println(response);
        assertTrue(response.contains("error"));
    }
    @Test
    void testWrongBuiltIn3() {
        String response = sendCommandToServer("simon: get get axe ");
        System.out.println(response);
        assertTrue(response.contains("error"), "Did not find the correct return message");
    }
    @Test
    void testMultiEntitiesCommand1() {
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: inv");
        sendCommandToServer("simon: Please goto forest");
        String response = sendCommandToServer("simon: cut down please chop the tree with axe log");
        System.out.println(response);
        assertFalse(response.contains("log"), "Did not find the correct return message");
    }
    @Test
    void testLumberjack() {
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: goto riverbank");
        sendCommandToServer("simon: blow horn");
        String response = sendCommandToServer("simon: look");
        assertTrue(response.contains("wood cutter"), "Did not find the correct return message");
        sendCommandToServer("simon: get horn");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: blow horn");
        String response1 = sendCommandToServer("simon: look");
        assertTrue(response1.contains("wood cutter"), "Did not find the correct return message");
    }

    @Test
    void testLumberjackInOtherLocation(){
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: goto riverbank");
        response = sendCommandToServer("simon: look");
        assertFalse(response.contains("wood cutter"));
        response = sendCommandToServer("simon: blow horn");
        response = sendCommandToServer("simon: inventory");
        assertFalse(response.contains("horn"));
        sendCommandToServer("simon: get horn");
        sendCommandToServer("simon: goto forest");
        response = sendCommandToServer("simon: look");
        assertFalse(response.contains("wood cutter"));
        response = sendCommandToServer("simon: blow horn");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("wood cutter"));
    }

    @Test
    void testEntityNotInInventory(){
        String response;
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: goto riverbank");
        response = sendCommandToServer("simon: look");
        assertFalse(response.contains("wood cutter"));
        response = sendCommandToServer("simon: blow horn");
        response = sendCommandToServer("simon: inventory");
        assertFalse(response.contains("horn"));
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: blow horn");
        response = sendCommandToServer("simon: look");
        assertFalse(response.contains("lumberjack"));
    }

    @Test
    void testProductioninOtherPlayerInv(){
        String response;
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: cut down tree");
        sendCommandToServer("simon: get log");
        sendCommandToServer("jack: goto forest");
        sendCommandToServer("jack: look");
        sendCommandToServer("jack: goto riverbank");
        response = sendCommandToServer("jack: look");
        assertFalse(response.contains("clearing"));
        response = sendCommandToServer("jack: get log");
        response = sendCommandToServer("jack: bridge river");
        response = sendCommandToServer("jack: look");
        assertFalse(response.contains("clearing"));
    }

    @Test
    void testProductioninOtherPlayerInv1(){
        String response;
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("neill: get potion");
        sendCommandToServer("neill: goto forest");
        response = sendCommandToServer("neill: cut tree with axe");
        response = sendCommandToServer("simon: get potion");
        sendCommandToServer("sam: goto forest");
        sendCommandToServer("sam: look");
        sendCommandToServer("sam: goto riverbank");
        response = sendCommandToServer("sam: look");
        assertFalse(response.contains("clearing"));
        response = sendCommandToServer("sam: bridge river");
        response = sendCommandToServer("sam: look");
        assertFalse(response.contains("clearing"));
    }

//    @Test
//    void testProductionInOtherLocation() {
//        String response;
//        sendCommandToServer("simon: get axe");
//        sendCommandToServer("simon: goto forest");
//        sendCommandToServer("simon: cut down tree");
//        sendCommandToServer("sam: goto forest");
//        response = sendCommandToServer("sam: look");
//        assertTrue(response.contains("log"));
//        sendCommandToServer("sam: goto riverbank");
//        response = sendCommandToServer("sam: look");
//        assertFalse(response.contains("clearing"));
//        response = sendCommandToServer("sam: bridge river");
//        response = sendCommandToServer("sam: look");
//        assertTrue(response.contains("clearing"));
//        response = sendCommandToServer("simon: look");
//        assertFalse(response.contains("log"));
//    }
}
