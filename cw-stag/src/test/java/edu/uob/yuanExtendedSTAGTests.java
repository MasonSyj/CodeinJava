package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class yuanExtendedSTAGTests {

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
  void testWarning() {
      String response = sendCommandToServer("simon: get");
      assertTrue(response.contains("error"));
       response = sendCommandToServer("simon: drop");
      assertTrue(response.contains("error"));
       response = sendCommandToServer("simon: potion look");
      assertTrue(response.contains("error"));
       response = sendCommandToServer("simon: axe get");
      assertTrue(response.contains("error"));
      response = sendCommandToServer("simon: goto");
      assertTrue(response.contains("error"));
      response = sendCommandToServer("simon: goto abc");
      assertTrue(response.contains("error"));
      response = sendCommandToServer("simon: ");
      assertTrue(response.contains("error"));
       response = sendCommandToServer("simon: key");
      assertTrue(response.contains("error"));
      response = sendCommandToServer("simon: get key and cut");
      assertTrue(response.contains("error"));
  }

  @Test
  void testAction(){
      String response = sendCommandToServer("simon: get potion");
      assertTrue(response.contains("picked up"));
      response = sendCommandToServer("simon: get potion");
      assertTrue(response.contains("error"));

      response = sendCommandToServer("simon: health");
      assertTrue(response.contains("3"));
      response = sendCommandToServer("simon: inv");
      assertTrue(response.contains("magic potion"));
      response = sendCommandToServer("simon: inventory");
      assertTrue(response.contains("magic potion"));
      response = sendCommandToServer("simon: drink potion");
      assertTrue(response.contains("You drink the potion and your health improves"));
      response = sendCommandToServer("simon: inv");
      assertFalse(response.contains("potion"));
      response = sendCommandToServer("simon: health");
      assertTrue(response.contains("3"));

      response = sendCommandToServer("simon: get axe axe");
      assertTrue(response.contains("error"));
      response = sendCommandToServer("simon: get axe");
      assertTrue(response.contains("picked up"));
      response = sendCommandToServer("simon: cut");
      assertTrue(response.contains("error"));

      sendCommandToServer("simon: drop axe");
      response = sendCommandToServer("simon: inv");
      assertFalse(response.contains("axe"));
      sendCommandToServer("simon: get axe");
      response = sendCommandToServer("simon: get coin");
      assertTrue(response.contains("coin"));

      response = sendCommandToServer("simon: goto forest");
      assertTrue(response.contains("forest"));

      response = sendCommandToServer("simon: cut key");
      assertTrue(response.contains("error"));
      response = sendCommandToServer("simon: cut key and chop tree and cut down tree");
      assertTrue(response.contains("error"));
      response = sendCommandToServer("simon: cut tree and drink");
      assertTrue(response.contains("error"));
      response = sendCommandToServer("simon: cut tree and chop cut down tree tree");
      assertTrue(response.contains("You cut down the tree with the axe"));

      response = sendCommandToServer("simon: please tree tree cut");
      assertTrue(response.contains("error"));

      sendCommandToServer("simon: get log");
      sendCommandToServer("simon: goto riverbank");
      response = sendCommandToServer("simon: bridge");
      assertTrue(response.contains("error"));
      response = sendCommandToServer("simon: bridge log");
      assertTrue(response.contains("You bridge the river with the log and can now reach the other side"));

      sendCommandToServer("simon: goto forest");
      sendCommandToServer("simon: get key");
      sendCommandToServer("simon: goto cabin");
      response = sendCommandToServer("simon: open trapdoor");
      assertTrue(response.contains("You unlock the door and see steps leading down into a cellar"));

      sendCommandToServer("simon: goto cellar");
      response = sendCommandToServer("simon: look");
      assertTrue(response.contains("elf"));

      sendCommandToServer("simon: hit elf");
      sendCommandToServer("simon: hit elf");
      response =sendCommandToServer("simon: health");
      assertTrue(response.contains("1"));
      response = sendCommandToServer("simon: hit elf");
      assertTrue(response.contains("You died and lost all of your items"));

      response = sendCommandToServer("simon: health");
      assertTrue(response.contains("3"));

      response = sendCommandToServer("simon: look look");
      assertTrue(response.contains("error"));

      response = sendCommandToServer("simon: look forest");
      assertTrue(response.contains("error"));

      response = sendCommandToServer("simon: look potion");
      assertTrue(response.contains("error"));

      response = sendCommandToServer("simon: look");
      assertTrue(response.contains("A log cabin in the woods"));

      sendCommandToServer("simon: goto cellar");
      response = sendCommandToServer("simon: pay elf");
      assertTrue(response.contains("shovel"));
      response = sendCommandToServer("simon: get shovel");
      assertTrue(response.contains("shovel"));

      sendCommandToServer("simon: goto cabin");
      sendCommandToServer("simon: goto forest");
      sendCommandToServer("simon: goto riverbank");
      response = sendCommandToServer("simon: dig shovel");
      assertTrue(response.contains("error"));

      sendCommandToServer("simon: goto clearing");
      response = sendCommandToServer("simon: dig shovel");
      assertTrue(response.contains("You dig into the soft ground and unearth a pot of gold !!!"));

      sendCommandToServer("simon: goto riverbank");
      sendCommandToServer("simon: get horn");
      sendCommandToServer("simon: blow horn");
      response = sendCommandToServer("simon: look");
      assertTrue(response.contains("cutter"));

      sendCommandToServer("simon: goto forest");
      sendCommandToServer("simon: goto cabin");
      response= sendCommandToServer("eric: look");
      assertTrue(response.contains("simon"));
      response= sendCommandToServer("simon: look");
      assertTrue(response.contains("eric"));

  }

}
