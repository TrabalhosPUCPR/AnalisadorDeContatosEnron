import Analyzer.Analyzer;
import Graph.*;

public class Main {
    public static void main(String[] args) {
        Analyzer analyzer = new Analyzer("Data/Amostra Enron");
        System.out.println(analyzer.getGraph());

        //Graph.debugGraph();
    }
}
