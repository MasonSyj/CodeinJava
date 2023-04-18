package edu.uob;

import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.File;
import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.ParseException;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;
import com.alexmerz.graphviz.objects.Edge;

public class MethodTest {
    public static void main(String[] args) {
        try {
          Parser parser = new Parser();
          FileReader reader = new FileReader("config" + File.separator + "basic-entities.dot");
          parser.parse(reader);
          Graph wholeDocument = parser.getGraphs().get(0);
          ArrayList<Graph> sections = wholeDocument.getSubgraphs();

          for (Graph graph: sections){
              System.out.println("double getId():");
              System.out.println(graph.getId().getId());
          }
            System.out.println("-------------------Separate line----------");
          // The locations will always be in the first subgraph
          ArrayList<Graph> locations = sections.get(0).getSubgraphs();
          for (Graph graph: locations){
              ArrayList<Graph> items = graph.getSubgraphs();
              for (Graph item: items){
                  System.out.println(item.getId().getId());
                  ArrayList<Node> nodes = item.getNodes(false);
                  for (Node node: nodes){
                      System.out.print(node.getId().getId() + ":  ");
                      System.out.println(node.getAttribute("description"));
                  }
              }

          }
          Graph firstLocation = locations.get(0);
          Node locationDetails = firstLocation.getNodes(false).get(0);
          // Yes, you do need to get the ID twice !
          String locationName = locationDetails.getId().getId();

          // The paths will always be in the second subgraph
          ArrayList<Edge> paths = sections.get(1).getEdges();
          Edge firstPath = paths.get(0);
          Node fromLocation = firstPath.getSource().getNode();
          String fromName = fromLocation.getId().getId();
          Node toLocation = firstPath.getTarget().getNode();
          String toName = toLocation.getId().getId();

      } catch (FileNotFoundException fnfe) {
            System.out.println("FileNotFoundException was thrown when attempting to read basic entities file");
      } catch (ParseException pe) {
            System.out.println("ParseException was thrown when attempting to read basic entities file");
      }
    }
}
