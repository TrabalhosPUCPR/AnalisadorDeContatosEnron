package Graph;

import java.util.LinkedHashMap;

public class Node<T> {

    private static class AdjancencyHolder {
        Node<?> node;
        Integer weight;

        public AdjancencyHolder(Node<?> node, Integer weight) {
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
    private final LinkedHashMap<Object, Node.AdjancencyHolder> adjacencies;

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
        this.adjacencies.put(node.toString(), new AdjancencyHolder(node, weight));
    }
    public Node<?> getAdjacency(String key){
        AdjancencyHolder adjacent = this.adjacencies.get(key);
        if(adjacent != null){
            return adjacent.getNode();
        }
        return null;
    }

    public Node<?>[] getAdjacencies() {
        AdjancencyHolder[] adjancencyHolders = new AdjancencyHolder[0];
        adjancencyHolders = this.adjacencies.values().toArray(adjancencyHolders);
        Node<?>[] nodesList = new Node<?>[adjancencyHolders.length];
        for(int i = 0; i < adjancencyHolders.length; i++){
            nodesList[i] = adjancencyHolders[i].getNode();
        }
        return nodesList;
    }

    public void setWeight(Object adjacentNode, int weights){
        this.adjacencies.get(adjacentNode.toString()).setWeight(weights);
    }



    public boolean equals(Node<?> n) {
        return n.getLabel().equals(this.label);
    }
}
