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
        graph.addAdjacency(6, 3);

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
