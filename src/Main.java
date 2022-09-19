import Analyzer.Analyzer;
import Graph.*;

public class Main {
    public static void main(String[] args) {
        Analyzer analyzer = new Analyzer("Data/Amostra Enron");

        analyzer.getGraph().printAdjacencies();
    }
}
