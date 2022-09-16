package Graph;

import java.util.*;

public class Graph {
    private final ArrayList<Node<?>> nodes;

    public Graph(){
        this.nodes = new ArrayList<>();
    }

    public int size(){ return this.nodes.size(); }

    public void add(Node<?> node){
        node.setGraphIndex(this.nodes.size());
        this.nodes.add(node);
    }

    public Node<?> getNode(int index){
        return this.nodes.get(index);
    }

    public void setNode(int index, Node<?> node){
        this.nodes.set(index, node);
    }

    public ArrayList<Node<?>> getNodes() {
        return nodes;
    }

    public void printAdjacencies(){
        for(int i = 0; i < this.nodes.size(); i++){
            System.out.print(this.nodes.get(i) + ": ");
            for(Node<?> nAdjacent : this.nodes.get(i).getAdjacencies()){
                System.out.print(nAdjacent + " ");
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

        protected boolean finished(){
            return this.nodeToVisitIndex.isEmpty();
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
            if(this.finished()) return null;
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
            if(this.finished()) return null;
            int next = this.nodeToVisitIndex.getLast();
            if(!removeElement) return this.graph.getNode(next);
            this.nodeToVisitIndex.remove(this.nodeToVisitIndex.getLast());
            addToNextVisitList(next, true);
            return this.graph.getNode(next);
        }
    }
}
