package Graph;

import java.util.*;

public class Graph {
    private final ArrayList<Node<?>> nodes;

    public Graph(){
        this.nodes = new ArrayList<>();
    }

    public int size(){ return this.nodes.size(); }

    public int add(Node<?> node){
        node.setGraphIndex(this.nodes.size());
        this.nodes.add(node);
        return node.getGraphIndex();
    }
    public int add(Node<?> node, boolean repeat){
        if (!repeat) {
            for (Node<?> n : this.nodes) {
                if (n.equals(node)) {
                    return n.getGraphIndex();
                }
            }
        }
        return add(node);
    }

    public Node<?> getNode(int index){
        return this.nodes.get(index);
    }

    public void newAdjacency(int node1Index, int node2Index, int weight){
        this.nodes.get(node1Index).newAdjacency(this.nodes.get(node2Index), weight);
    }
    public void newNonDirectedAdjacency(int node1Index, int node2Index, int weight){
        this.nodes.get(node1Index).newAdjacency(this.nodes.get(node2Index), weight);
        this.nodes.get(node2Index).newAdjacency(this.nodes.get(node1Index), weight);
    }

    public void setNode(int index, Node<?> node){
        this.nodes.set(index, node);
    }

    public ArrayList<Node<?>> getNodes() {
        return nodes;
    }

    public void printAdjacencies(){
        for (Node<?> node : this.nodes) {
            System.out.print(node + ": | ");
            for (Node<?> nAdjacent : node.getAdjacencies()) {
                System.out.print(nAdjacent + " | ");
            }
            System.out.println();
        }
    }
    public boolean[][] getAdjacencyMatrix(){
        boolean[][] matrix = new boolean[this.nodes.size()][this.nodes.size()];
        for(int i = 0; i < this.nodes.size(); i++){
            ArrayList<Node<?>> adjacency = this.nodes.get(i).getAdjacencies();
            for(int k = 0, index = 0; k < adjacency.size(); k++){
                while(index < adjacency.get(k).getGraphIndex()){
                    index++;
                }
                matrix[i][index] = true;
            }
        }
        return matrix;
    }

    public void printAdjacencyMatrix(){
        boolean[][] adjacencyMatrix = getAdjacencyMatrix();
        for (boolean[] matrix : adjacencyMatrix) {
            System.out.print("[ ");
            for (boolean b : matrix) {
                if (b) System.out.print("1 ");
                else System.out.print("0 ");
            }
            System.out.println("]");
        }
    }

    public ArrayList<Node<?>> getShortestPath(int origin, int destination){
        // TODO: 9/16/22 djikstra algorithm
        return new ArrayList<>();
    }

    public boolean search(int origin, int destination){
        Graph.BfsIterator bfs = new BfsIterator(origin, this);
        while(bfs.next(false) != null){
            if(bfs.next().getGraphIndex() == destination){
                return true;
            }
        }
        return false;
    }

    public int indexOf(Node<?> node){
        // TODO: 9/21/22 ver uma maneira melhor de fazer isso
        for(Node<?> n : this.nodes){
            if(n.equals(node)){
                return n.getGraphIndex();
            }
        }
        return -1;
    }

    private abstract static class searchIterator {
        Graph graph;
        int iterator;
        LinkedList<Integer> nodeToVisitIndex;
        boolean[] visitedIndex;

        public searchIterator(int origin, Graph graph) {
            this.graph = graph;
            this.iterator = origin;
            this.nodeToVisitIndex = new LinkedList<>();
            this.nodeToVisitIndex.add(this.iterator);
            this.visitedIndex = new boolean[graph.nodes.size()];
        }

        protected boolean ready(){
            return !this.nodeToVisitIndex.isEmpty();
        }

        abstract Node<?> next(boolean removeElement);

        public Node<?> next(){
            return this.next(true);
        }
        protected void addToNextVisitList(int next, boolean rev){
            ArrayList<Node<?>> adjacency = this.graph.getNode(next).getAdjacencies();
            if (rev) Collections.reverse(adjacency);
            for(Node<?> node : adjacency){
                if(!this.visitedIndex[node.getGraphIndex()]){
                    this.nodeToVisitIndex.removeFirstOccurrence(node.getGraphIndex());
                    this.nodeToVisitIndex.add(node.getGraphIndex());
                }
            }
            this.visitedIndex[next] = true;
        }
    }
    public static class BfsIterator extends searchIterator{
        public BfsIterator(int origin, Graph graph) {
            super(origin, graph);
        }

        @Override
        public Node<?> next(boolean removeElement) {
            if(!this.ready()) return null;
            int next = this.nodeToVisitIndex.getFirst();
            if(!removeElement) return this.graph.getNode(next);
            this.nodeToVisitIndex.remove(this.nodeToVisitIndex.getFirst());
            addToNextVisitList(next, false);
            return this.graph.getNode(next);
        }
    }

    public static class DfsIterator extends searchIterator{

        public DfsIterator(int origin, Graph graph) {
            super(origin, graph);
        }

        @Override
        public Node<?> next(boolean removeElement) {
            if(!this.ready()) return null;
            int next = this.nodeToVisitIndex.getLast();
            if(!removeElement) return this.graph.getNode(next);
            this.nodeToVisitIndex.remove(this.nodeToVisitIndex.getLast());
            addToNextVisitList(next, true);
            return this.graph.getNode(next);
        }
    }

    @Override
    public String toString() {
        return this.nodes.toString();
    }
    
    public static void debugGraph(){
        Graph graph = new Graph();
        for(int i = 1; i < 8; i++){
            graph.add(new Node<>(i));
        }

        graph.newAdjacency(0, 1, 5);
        graph.newAdjacency(0, 3, 15);
        graph.newAdjacency(0, 4, 1);
        graph.newAdjacency(1, 2, 45);
        graph.newAdjacency(1, 4, 55);
        graph.newAdjacency(1, 5, 5);
        graph.newAdjacency(2, 6, 21);
        graph.newAdjacency(2, 5, 5);
        graph.newAdjacency(3, 4, 4);
        graph.newAdjacency(4, 5, 8);
        graph.newAdjacency(5, 6, 9);
        graph.newAdjacency(6, 3, 7);

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
