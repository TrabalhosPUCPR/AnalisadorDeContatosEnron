import Graph.*;

public class Main {
    public static void main(String[] args) {
        Graph graph = new Graph();
        for(int i = 1; i < 8; i++){
            graph.add(new Node<>(i));
        }

        graph.getNode(0).newAdjacency(graph.getNode(1));
        graph.getNode(0).newAdjacency(graph.getNode(3));
        graph.getNode(0).newAdjacency(graph.getNode(4));
        graph.getNode(1).newAdjacency(graph.getNode(2));
        graph.getNode(1).newAdjacency(graph.getNode(4));
        graph.getNode(1).newAdjacency(graph.getNode(5));
        graph.getNode(2).newAdjacency(graph.getNode(6));
        graph.getNode(2).newAdjacency(graph.getNode(5));
        graph.getNode(3).newAdjacency(graph.getNode(4));
        graph.getNode(4).newAdjacency(graph.getNode(5));
        graph.getNode(5).newAdjacency(graph.getNode(6));
        graph.getNode(6).newAdjacency(graph.getNode(3));

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
