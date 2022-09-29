import Analyzer.Analyzer;

import Graph.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ///*
        Analyzer analyzer = new Analyzer("Data/Amostra Enron", "sent");
        //Analyzer analyzer = new Analyzer("Data/mockData", "sent");
        System.out.println("\nAll nodes in graph: " + analyzer.getGraph());

        System.out.println("Emails amount in data set: " + analyzer.getGraph().verticesSize());
        System.out.println("Connections amount in data set: " + analyzer.getGraph().connections());

        System.out.println();

        int topNSenders = 20;
        System.out.print("Top " + topNSenders + " receivers: ");
        List<?> topSenders = analyzer.getTopSenders(topNSenders);
        System.out.println(topSenders);

        int topNReceivers = 20;
        System.out.print("Top " + topNReceivers + " receivers: ");
        List<?> topReceivers = analyzer.getTopReceivers(topNReceivers);
        System.out.println(topReceivers);

        System.out.println();

        String connection1 = "drew.fossum@enron.com";
        String connection2 = "rockey.storie@enron.com";
        System.out.println("BFS: " + connection1 + " connection to " + connection2 + ": " + analyzer.findConnectionBFS(connection1, connection2));
        System.out.println("DFS: " + connection1 + " connection to " + connection2 + ": " + analyzer.findConnectionDFS(connection1, connection2));

        System.out.println();

        String user = "drew.fossum@enron.com";
        int distance = 2;
        System.out.println("All users with distance of " + distance + " from " + user + ": " + analyzer.getGraph().adjacentNodesAtDistance(user, distance));

        System.out.println();

        String user1 = "drew.fossum@enron.com";
        String user2 = "mark.courtney@enron.com";
        System.out.println("Farthest connection between " + user1 + " and " + user2 + ": " + analyzer.getGraph().getLongestPath(user1, user2));

         //*/

        //Graph.debugGraph();
    }
}
