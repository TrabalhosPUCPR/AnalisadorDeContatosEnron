import Graph.*;

public class Main {
    public static void main(String[] args) {
        Graph graph = new Graph();
        for(int i = 1; i < 8; i++){
            graph.add(new Node<>(i));
        }

        graph.addAdjacency(0, 1);
        graph.addAdjacency(0, 3);
        graph.addAdjacency(0, 4);
        graph.addAdjacency(1, 2);
        graph.addAdjacency(1, 4);
        graph.addAdjacency(1, 5);
        graph.addAdjacency(2, 6);
        graph.addAdjacency(2, 5);
        graph.addAdjacency(3, 4);
        graph.addAdjacency(4, 5);
        graph.addAdjacency(5, 6);

        graph.printAdjacencies();

        Graph.BfsIterator bfsIterator = new Graph.BfsIterator(0, graph);
        while(bfsIterator.next(false) != null){
            System.out.println(bfsIterator.next());
        }
    }
}
