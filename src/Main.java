import Analyzer.Analyzer;

import Graph.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        /*
        Analyzer analyzer = new Analyzer("Data/Amostra Enron", "sent");
        //Analyzer analyzer = new Analyzer("Data/mockData", "sent");
        System.out.println(analyzer.getGraph());

        System.out.print("Top senders: ");
        List<?> topSenders = analyzer.getTopSenders(20);
        System.out.println(topSenders);

        System.out.print("Top receivers: ");
        List<?> topReceivers = analyzer.getTopReceivers(20);
        System.out.println(topReceivers);

         */

        Graph.debugGraph();
    }
}
