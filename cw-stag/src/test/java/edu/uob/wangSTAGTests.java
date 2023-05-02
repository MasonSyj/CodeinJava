package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class wangSTAGTests {

  private GameServer server;

  // Create a new server _before_ every @Test
  @BeforeEach
  void setup() throws FileNotFoundException {
      File entitiesFile = Paths.get("config" + File.separator + "extended-entities.dot").toAbsolutePath().toFile();
      File actionsFile = Paths.get("config" + File.separator + "extended-actions.xml").toAbsolutePath().toFile();
      server = new GameServer(entitiesFile, actionsFile);
  }

  String sendCommandToServer(String command) {
      // Try to send a command to the server - this call will timeout if it takes too long (in case the server enters an infinite loop)
      //return assertTimeoutPreemptively(Duration.ofMillis(1000), () -> {
          return server.handleCommand(command);
          //},"Server took too long to respond (probably stuck in an infinite loop)");
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


  @Test
  void testExtended() {
    String response = sendCommandToServer("simon: Look");
    System.out.println(response);
    assertTrue(response.contains("potion"), "Did not see a description of the current room in response to look");
    assertTrue(response.contains("forest"), "Did not see available paths in response to look");
    response = sendCommandToServer("simon: GET axe");
    response = sendCommandToServer("simon: INV");
    assertTrue(response.contains("axe"), "Did not see the axe in the inventory after an attempt was made to get it");

    response = sendCommandToServer("simon: goto forest");
    assertTrue(response.contains("forest"), "Did not see the name of the current room in response to goto");
    assertTrue(response.contains("key"), "Did not see a description of the current room in response to goto");
    assertTrue(response.contains("riverbank"), "Did not see available paths in response to goto");
    response = sendCommandToServer("simon: get key");
    response = sendCommandToServer("simon: inv");
    assertTrue(response.contains("key"), "Did not see the key in the inventory after an attempt was made to get it");
    response = sendCommandToServer("simon: cut tree");
    response = sendCommandToServer("simon: look");
    assertTrue(response.contains("log"), "Did not see a description of the current room in response to look");
    response = sendCommandToServer("simon: get log");
    response = sendCommandToServer("simon: inv");
    assertTrue(response.contains("log"), "Did not see the log in the inventory after an attempt was made to get it");

    response = sendCommandToServer("simon: goto cabin");
    assertFalse(response.contains("axe"), "See descriptions that shouldn't exist of the current room in response to goto");
    response = sendCommandToServer("simon: get potion");
    response = sendCommandToServer("simon: get coin");
    response = sendCommandToServer("simon: inv");
    assertTrue(response.contains("potion"), "Did not see the potion in the inventory after an attempt was made to get it");
    assertTrue(response.contains("coin"), "Did not see the coin in the inventory after an attempt was made to get it");
    response = sendCommandToServer("simon: open key");
    response = sendCommandToServer("simon: look");
    assertTrue(response.contains("cellar"), "Did not see available paths in response to look");

    response = sendCommandToServer("simon: goto cellar");
    assertTrue(response.contains("elf"), "Did not see a description of the current room in response to goto");
    response = sendCommandToServer("simon: pay elf");
    response = sendCommandToServer("simon: look");
    assertTrue(response.contains("shovel"), "Did not see a description of the current room in response to look");
    response = sendCommandToServer("simon: get shovel");
    response = sendCommandToServer("simon: fight elf");
    assertTrue(response.contains("health"), "Did not see the interaction");

    response = sendCommandToServer("simon: goto cabin");
    assertTrue(response.contains("cabin"), "Did not see the name of the current room in response to goto");
    response = sendCommandToServer("simon: goto forest");
    assertTrue(response.contains("forest"), "Did not see the name of the current room in response to goto");
    response = sendCommandToServer("simon: goto riverbank");
    assertTrue(response.contains("riverbank"), "Did not see the name of the current room in response to goto");
    response = sendCommandToServer("simon: get horn");
    response = sendCommandToServer("simon: inv");
    assertTrue(response.contains("horn"), "Did not see the horn in the inventory after an attempt was made to get it");
    response = sendCommandToServer("simon: river bridge");
    response = sendCommandToServer("simon: look");
    assertTrue(response.contains("clearing"), "Did not see available paths in response to look");

    response = sendCommandToServer("simon: goto clearing");
    assertTrue(response.contains("soil"), "Did not see a description of the current room in response to goto");
    response = sendCommandToServer("simon: blow horn");
    response = sendCommandToServer("simon: look");
    assertTrue(response.contains("cutter"), "Did not see a description of the current room in response to look");
    response = sendCommandToServer("simon: dig ground");
    response = sendCommandToServer("simon: look");
    assertTrue(response.contains("gold"), "Did not see a description of the current room in response to look");
    assertTrue(response.contains("hole"), "Did not see a description of the current room in response to look");
    response = sendCommandToServer("simon: drink potion");
    assertTrue(response.contains("health"), "Did not see the interaction");
  }

  @Test
  void testHealth() {
    String response = sendCommandToServer("simon: look");
    assertTrue(response.contains("cabin"), "Did not see the name of the current room in response to look");
    response = sendCommandToServer("simon: get potion");
    response = sendCommandToServer("simon: get axe");
    response = sendCommandToServer("simon: goto forest");
    response = sendCommandToServer("simon: get key");
    response = sendCommandToServer("simon: goto cabin");
    response = sendCommandToServer("simon: open key");
    response = sendCommandToServer("simon: look");
    assertTrue(response.contains("cellar"), "Did not see available paths in response to look");

    response = sendCommandToServer("simon: goto cellar");
    response = sendCommandToServer("simon: health");
    assertTrue(response.contains("3"), "Did not see current health or wrong health value");
    response = sendCommandToServer("simon: fight elf");
    response = sendCommandToServer("simon: health");
    assertTrue(response.contains("2"), "Did not see current health or wrong health value");
    response = sendCommandToServer("simon: fight elf");
    response = sendCommandToServer("simon: health");
    assertTrue(response.contains("1"), "Did not see current health or wrong health value");
    response = sendCommandToServer("simon: drink potion");
    response = sendCommandToServer("simon: health");
    assertTrue(response.contains("2"), "Did not see current health or wrong health value");
    response = sendCommandToServer("simon: fight elf");
    response = sendCommandToServer("simon: health");
    assertTrue(response.contains("1"), "Did not see current health or wrong health value");
    response = sendCommandToServer("simon: fight elf");
    assertTrue(response.contains("died"), "Why are you still alive?");

    response = sendCommandToServer("simon: look");
    assertTrue(response.contains("cabin"), "Did not see the name of the current room in response to look");
    response = sendCommandToServer("simon: inv");
    assertFalse(response.contains("axe"), "see the axe in the inventory after an attempt was made to get it");
    response = sendCommandToServer("simon: goto cellar");
    assertTrue(response.contains("axe"), "Did not see a description of the current room in response to look");
  }

  @Test
  void testMultiplePlayers() {
    String response = sendCommandToServer("simon: look");
    assertTrue(response.contains("cabin"), "Did not see the name of the current room in response to look");
    response = sendCommandToServer("sarah: look");
    assertTrue(response.contains("cabin"), "Did not see the name of the current room in response to look");
    assertTrue(response.contains("simon"), "Did not see other players in response to look");
    response = sendCommandToServer("simon: look");
    assertTrue(response.contains("sarah"), "Did not see other players in response to look");

    response = sendCommandToServer("simon: get axe");
    response = sendCommandToServer("sarah: look");
    assertFalse(response.contains("axe"), "see the axe in the inventory after another player get it");
    response = sendCommandToServer("sarah: get axe");
    assertTrue(response.contains("error"), "No matched entity");

    response = sendCommandToServer("simon: goto forest");
    assertTrue(response.contains("forest"), "Did not see the name of the current room in response to goto");
    response = sendCommandToServer("sarah: look");
    assertFalse(response.contains("simon"), "see other players in response to look");
    response = sendCommandToServer("simon: cut tree");
    response = sendCommandToServer("sarah: goto forest");
    System.out.println(response);
    assertTrue(response.contains("forest"), "Did not see the name of the current room in response to goto");
    assertTrue(response.contains("simon"), "Did not see other players in response to goto");
    assertTrue(response.contains("log"), "Did not see the name of the current room in response to goto");
    assertFalse(response.contains("tree"), "Tree has been cut down by simon");
    response = sendCommandToServer("sarah: get log");
    response = sendCommandToServer("sarah: inv");
    assertTrue(response.contains("log"), "Did not see the log in the inventory after an attempt was made to get it");
    response = sendCommandToServer("simon: look");
    assertTrue(response.contains("sarah"), "Did not see other players in response to look");
    assertFalse(response.contains("log"), "log has been picked up by sarah");

    response = sendCommandToServer("simon simon: look");
    response = sendCommandToServer("simon: goto cabin");
    assertTrue(response.contains("simon simon"), "Did not see other players in response to goto");
    response = sendCommandToServer("simon'simon: look");
    response = sendCommandToServer("simon: look");
    assertTrue(response.contains("simon'simon"), "Did not see other players in response to look");
    response = sendCommandToServer("simon-simon: look");
    response = sendCommandToServer("simon: look");
    assertTrue(response.contains("simon-simon"), "Did not see other players in response to look");
//    response = sendCommandToServer("111simon: look");
//    assertTrue(response.contains("[error]"), "Invalid player name with number");
  }

  @Test
  void testBuiltIn() {
    String response = sendCommandToServer("simon: look");
    assertTrue(response.contains("cabin"), "Did not see the name of the current room in response to look");
    response = sendCommandToServer("simon: look look");
    assertTrue(response.contains("[error]"), "Multiple built-in commands");
    response = sendCommandToServer("simon: look forest forest");
    assertTrue(response.contains("[error]"), "nvalid built-in command");
    response = sendCommandToServer("simon: please look");
    assertTrue(response.contains("cabin"), "Did not see the name of the current room in response to look");

    response = sendCommandToServer("simon: get axe");
    response = sendCommandToServer("simon: inv");
    assertTrue(response.contains("axe"), "see the axe in the inventory after an attempt was made to get it");
    response = sendCommandToServer("simon: inventory");
    assertTrue(response.contains("axe"), "see the axe in the inventory after an attempt was made to get it");
    response = sendCommandToServer("simon: look inv");
    assertTrue(response.contains("[error]"), "Multiple built-in commands");
    response = sendCommandToServer("simon: open inv");
    assertTrue(response.contains("[error]"), "Multiple actions");

    response = sendCommandToServer("simon: get tree from forest");
    assertTrue(response.contains("[error]"), "Multiple entities");
    response = sendCommandToServer("simon: get axe and drop");
    assertTrue(response.contains("[error]"), "Multiple built-in commands");
    response = sendCommandToServer("simon: get key and potion");
    assertTrue(response.contains("[error]"), "Multiple entities");
    response = sendCommandToServer("simon: potion get");
    assertTrue(response.contains("[error]"), "Built-in command must be ordered, have command first and then subject entity");
    response = sendCommandToServer("simon: get potion");
    response = sendCommandToServer("simon: inv");
    assertTrue(response.contains("potion"), "see the potion in the inventory after an attempt was made to get it");

    response = sendCommandToServer("simon: drop potion");
    response = sendCommandToServer("simon: inv");
    assertFalse(response.contains("potion"), "see the potion in the inventory after an attempt was made to get it");

    response = sendCommandToServer("simon: goto goto forest");
    assertTrue(response.contains("[error]"), "Multiple built-in commands");
    response = sendCommandToServer("simon: goto");
    assertTrue(response.contains("[error]"), "No matched entity in the command");
    response = sendCommandToServer("simon: goto ri");
    assertTrue(response.contains("[error]"), "No matched entity in the command");
    response = sendCommandToServer("simon: goto forest goto forest");
    assertTrue(response.contains("[error]"), "Multiple built-in commands");
    response = sendCommandToServer("simon: forest goto");
    assertTrue(response.contains("[error]"), "Built-in command must be ordered, have command first and then subject entity");
  }

  @Test
  void testTrigger() {
    String response = sendCommandToServer("simon: coin");
    assertTrue(response.contains("[error]"), "No action in the command");
    response = sendCommandToServer("simon: get axe");
    response = sendCommandToServer("simon: goto forest");
    response = sendCommandToServer("simon: cut tree and forest");
    assertTrue(response.contains("[error]"), "No performable action in the command");
    response = sendCommandToServer("simon: cutdown tree");
    assertTrue(response.contains("[error]"), "No action in the command");
    response = sendCommandToServer("simon: cut tree down");
    System.out.println(response);
    response = sendCommandToServer("simon: look");
    System.out.println(response);
    assertTrue(response.contains("log"), "Did not see a description of the current room in response to look");
    response = sendCommandToServer("simon: cut tree");
    assertTrue(response.contains("[error]"), "Entity does not exist in this location");
    response = sendCommandToServer("simon: get key");

    response = sendCommandToServer("simon: goto cabin");
    response = sendCommandToServer("simon: key open unlock");
    response = sendCommandToServer("simon: look");
    assertTrue(response.contains("cellar"), "Did not see available paths in response to look");
    response = sendCommandToServer("simon: open key");
    assertTrue(response.contains("[error]"), "No performable action in the command");
    response = sendCommandToServer("simon: get coin");

    response = sendCommandToServer("simon: goto cellar");
    response = sendCommandToServer("simon: pay elf fight");
    assertTrue(response.contains("[error]"), "Multiple actions");
    response = sendCommandToServer("simon: pay pay elf");
    response = sendCommandToServer("simon: look");
    assertTrue(response.contains("shovel"), "Did not see a description of the current room in response to look");
    response = sendCommandToServer("simon: health fight");
    assertTrue(response.contains("[error]"), "Multiple actions");
    response = sendCommandToServer("simon: fight elf and axe");
    assertTrue(response.contains("[error]"), "No performable action in the command");
    response = sendCommandToServer("simon: cut elf");
    assertTrue(response.contains("[error]"), "No performable action in the command");
    response = sendCommandToServer("simon: elf hit fight health");
    assertTrue(response.contains("[error]"), "Multiple actions");
    response = sendCommandToServer("simon: elf hit fight");
    assertTrue(response.contains("health"), "Did not see the interaction");
  }

  @Test
  void testEntity() {
    String response = sendCommandToServer("simon: get");
    assertTrue(response.contains("[error]"), "No matched entity in the command");
    response = sendCommandToServer("simon: get coin and potion");
    assertTrue(response.contains("[error]"), "Multiple entities");
    response = sendCommandToServer("simon: get horn");
    assertTrue(response.contains("[error]"), "No matched entity in the command");
    response = sendCommandToServer("simon: goto forest");
    response = sendCommandToServer("simon: get tree ");
    assertTrue(response.contains("[error]"), "No matched entity in the command");
    response = sendCommandToServer("simon: drop coin ");
    assertTrue(response.contains("[error]"), "No matched entity in the command");

    response = sendCommandToServer("simon: goto riverbank");
    response = sendCommandToServer("simon: get horn");
    response = sendCommandToServer("simon: blow horn");
    response = sendCommandToServer("simon: look");
    assertTrue(response.contains("cutter"), "Did not see a description of the current room in response to look");
    response = sendCommandToServer("simon: inv");
    assertTrue(response.contains("horn"), "see the horn in the inventory after an attempt was made to get it");
    response = sendCommandToServer("simon: goto forest");
    response = sendCommandToServer("simon: blow horn");
    response = sendCommandToServer("simon: look");
    assertTrue(response.contains("cutter"), "Did not see a description of the current room in response to look");
    response = sendCommandToServer("simon: goto riverbank");
    assertFalse(response.contains("cutter"), "Did not see a description of the current room in response to goto");
  }

  @Test
  void testPath() {
    String response = sendCommandToServer("simon: look");
    assertFalse(response.contains("cellar"), "No path to cellar");
    response = sendCommandToServer("simon: goto forest");
    response = sendCommandToServer("simon: get key");
    response = sendCommandToServer("simon: goto cabin");
    response = sendCommandToServer("simon: key open");
    response = sendCommandToServer("simon: look");
    assertTrue(response.contains("cellar"), "Did not see the path of the current room in response to look");

    response = sendCommandToServer("simon: get axe");
    response = sendCommandToServer("simon: goto forest");
    response = sendCommandToServer("simon: cut tree");
    response = sendCommandToServer("simon: get log");
    response = sendCommandToServer("simon: goto riverbank");
    response = sendCommandToServer("simon: bridge river");
    response = sendCommandToServer("simon: look");
    assertTrue(response.contains("clearing"), "Did not see the path of the current room in response to look");
    response = sendCommandToServer("simon: destroy axe");
    System.out.println(response);
    response = sendCommandToServer("simon: look");
    System.out.println(response);
    assertFalse(response.contains("clearing"), "Already moved the bridge to clearing");
  }

  @Test
  void testOthers() {
//    String response = sendCommandToServer("simon: look");
//    System.out.println(response);
//    assertFalse(response.contains("[error]"), "No action in the command");
    String response;
    response = sendCommandToServer("simon: look ,");
    assertTrue(response.contains("cabin"), "Did not see the name of the current room in response to look");
    response = sendCommandToServer("simon: l00k");
    assertTrue(response.contains("[error]"), "No action in the command");
    response = sendCommandToServer("simon: lo,ok");
    assertTrue(response.contains("[error]"), "No action in the command");
  }
}
