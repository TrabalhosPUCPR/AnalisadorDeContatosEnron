import Analyzer.Analyzer;
import Graph.*;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Analyzer analyzer = new Analyzer("Data/Amostra Enron");
        System.out.println(analyzer.getGraph());

        Node<?>[] topSenders = analyzer.getTopSenders(20);
        System.out.println(Arrays.toString(topSenders));

        //Graph.debugGraph();
    }
}
