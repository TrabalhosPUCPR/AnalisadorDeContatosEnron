package Graph;

import java.util.LinkedHashMap;

public class Node<T> {

    private static class AdjacencyHolder {
        Node<?> node;
        Integer weight;

        public AdjacencyHolder(Node<?> node, Integer weight) {
            this.node = node;
            this.weight = weight;
        }

        public Node<?> getNode() {
            return node;
        }

        public Integer getWeight() {
            return weight;
        }

        public void setNode(Node<?> node) {
            this.node = node;
        }

        public void setWeight(Integer weight) {
            this.weight = weight;
        }
    }

    private T label;
    private final LinkedHashMap<Object, Graph.Node.AdjacencyHolder> adjacencies;

    public Node(T label) {
        this.label = label;
        this.adjacencies = new LinkedHashMap<>();
    }

    public void setLabel(T label){
        this.label = label;
    }

    public T getLabel() {
        return label;
    }

    public Integer getWeight(Object adjacentNode) {
        return adjacencies.get(adjacentNode.toString()).getWeight();
    }

    @Override
    public String toString() {
        return this.label.toString();
    }

    protected void newAdjacency(Node<?> node, int weight){
        this.adjacencies.put(node.toString(), new Graph.Node.AdjacencyHolder(node, weight));
    }
    public Node<?> getAdjacency(String key){
        Graph.Node.AdjacencyHolder adjacent = this.adjacencies.get(key);
        if(adjacent != null){
            return adjacent.getNode();
        }
        return null;
    }

    public Node<?>[] getAdjacencies() {
        Graph.Node.AdjacencyHolder[] adjacencyHolders = new Graph.Node.AdjacencyHolder[0];
        adjacencyHolders = this.adjacencies.values().toArray(adjacencyHolders);
        Node<?>[] nodesList = new Node<?>[adjacencyHolders.length];
        for(int i = 0; i < adjacencyHolders.length; i++){
            nodesList[i] = adjacencyHolders[i].getNode();
        }
        return nodesList;
    }

    public AdjacencyHolder[] getAdjacencyHolders(){
        Graph.Node.AdjacencyHolder[] adjacencyHolders = new Graph.Node.AdjacencyHolder[0];
        return this.adjacencies.values().toArray(adjacencyHolders);
    }

    public void setWeight(Object adjacentNode, int weights){
        this.adjacencies.get(adjacentNode.toString()).setWeight(weights);
    }

    public int sumWeights(){
        int sum = 0;
        for(AdjacencyHolder adH : this.getAdjacencyHolders()){
            sum += adH.weight;
        }
        return sum;
    }

    public boolean equals(Node<?> n) {
        return n.getLabel().equals(this.label);
    }
}
