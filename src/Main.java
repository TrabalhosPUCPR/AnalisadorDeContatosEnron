import Analyzer.Analyzer;
import Graph.*;

public class Main {
    public static void main(String[] args) {
        Analyzer analyzer = new Analyzer("Data/Amostra Enron");

        analyzer.getGraph().printAdjacencies();
    }

    public void debugGraph(){
        Graph graph = new Graph();
        for(int i = 1; i < 8; i++){
            graph.add(new Node<>(i));
        }

        graph.newAdjacency(0, 1, 5);
        graph.newAdjacency(0, 3, 5);
        graph.newAdjacency(0, 4, 5);
        graph.newAdjacency(1, 2, 5);
        graph.newAdjacency(1, 4, 5);
        graph.newAdjacency(1, 5, 5);
        graph.newAdjacency(2, 6, 5);
        graph.newAdjacency(2, 5, 5);
        graph.newAdjacency(3, 4, 5);
        graph.newAdjacency(4, 5, 5);
        graph.newAdjacency(5, 6, 5);
        graph.newAdjacency(6, 3, 5);

        graph.printAdjacencies();
        graph.printAdjacencyMatrix();

        Graph.BfsIterator bfsIterator = new Graph.BfsIterator(0, graph);
        Graph.DfsIterator dfsIterator = new Graph.DfsIterator(0, graph);

        System.out.print("BFS: ");
        while(bfsIterator.next(false) != null){
            System.out.print(bfsIterator.next() + " ");
        }
        System.out.println();
        System.out.print("DFS: ");
        while(dfsIterator.next(false) != null){
            System.out.print(dfsIterator.next() + " ");
        }
        System.out.println();

        System.out.println(graph.search(0, 5));

        System.out.println("ShortestPath: " + graph.getShortestPath(0, graph.size()));
    }
}
