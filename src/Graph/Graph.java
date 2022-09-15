package Graph;

import java.util.ArrayList;

public class Graph {
    private final ArrayList<ArrayList<Node<?>>> adjacency;
    private final ArrayList<Node<?>> nodes;

    public Graph(){
        this.adjacency = new ArrayList<>();
        this.nodes = new ArrayList<>();
    }

    public Graph(ArrayList<ArrayList<Node<?>>> adjacency) {
        this.adjacency = adjacency;
        this.nodes = new ArrayList<>();
    }

    public void add(Node<?> node){
        this.adjacency.add(new ArrayList<>());
        node.setGraphIndex(this.nodes.size());
        this.nodes.add(node);
    }

    public void addAdjacency(int node1Index, int node2Index){
        this.adjacency.get(node1Index).add(this.nodes.get(node2Index));
        this.adjacency.get(node2Index).add(this.nodes.get(node1Index));
    }

    public Node<?> getNode(int index){
        return this.nodes.get(index);
    }

    public void setNode(int index, Node<?> node){
        this.nodes.set(index, node);
    }

    public ArrayList<Node<?>> removeAdjacency(int index){
        ArrayList<Node<?>> removed = this.adjacency.get(index);
        this.adjacency.remove(index);
        return removed;
    }

    public ArrayList<Node<?>> getAdjacency(int index){
        return this.adjacency.get(index);
    }

    public ArrayList<Node<?>> getNodes() {
        return nodes;
    }

    public void printAdjacencies(){
        for(int i = 0; i < this.nodes.size(); i++){
            System.out.print(this.nodes.get(i) + ": ");
            for(Node<?> nAdjacent : this.getAdjacency(i)){
                System.out.print(nAdjacent + " ");
            }
            System.out.println();
        }
    }

    private abstract static class searchIterator {
        Graph graph;
        int iterator;
        ArrayList<Integer> nodeToVisitIndex;
        boolean[] visitedIndex;

        public searchIterator(int origin, Graph graph) {
            this.graph = graph;
            this.iterator = origin;
            this.nodeToVisitIndex = new ArrayList<>();
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
        protected void addToNextVisitList(int next){
            ArrayList<Node<?>> adjacency = this.graph.getAdjacency(next);
            for(Node<?> node : adjacency){
                if(!this.visitedIndex[node.getGraphIndex()]){
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
            int next = this.nodeToVisitIndex.get(this.nodeToVisitIndex.get(0));
            if(!removeElement) return this.graph.getNode(next);
            this.nodeToVisitIndex.remove(this.nodeToVisitIndex.get(0));
            addToNextVisitList(next);
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
            int next = this.nodeToVisitIndex.get(this.nodeToVisitIndex.size()-1);
            if(!removeElement) return this.graph.getNode(next);
            this.nodeToVisitIndex.remove(this.nodeToVisitIndex.size()-1);
            addToNextVisitList(next);
            return this.graph.getNode(next);
        }
    }
}
