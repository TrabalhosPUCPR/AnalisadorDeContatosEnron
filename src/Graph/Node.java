package Graph;

import java.util.ArrayList;

public class Node<T> {

    private T label;
    private int graphIndex;
    private final ArrayList<Node<?>> adjacencies;
    private final ArrayList<Integer> weights;

    public Node(T label) {
        this.label = label;
        this.adjacencies = new ArrayList<>();
        this.weights = new ArrayList<>();
    }

    protected void setGraphIndex(int graphIndex) {
        this.graphIndex = graphIndex;
    }

    public void setLabel(T label){
        this.label = label;
    }

    public T getLabel() {
        return label;
    }

    public Integer getWeight(int i) {
        return weights.get(i);
    }

    public int getGraphIndex() {
        return graphIndex;
    }

    @Override
    public String toString() {
        return this.label.toString();
    }

    protected void newAdjacency(Node<?> node, int weight){
        this.adjacencies.add(node);
        this.weights.add(weight);
    }

    public int indexOfAdjacent(Node<?> node){
        for(int i = 0; i < this.adjacencies.size(); i++){
            if(this.adjacencies.get(i).equals(node)){
                return i;
            }
        }
        return -1;
    }

    protected void newNonDirectedAdjacency(Node<?> node, int weight){
        this.newAdjacency(node, weight);
        node.newAdjacency(this, weight);
    }

    public ArrayList<Node<?>> getAdjacencies() {
        return adjacencies;
    }

    public void setWeight(int adjacencyIndex, int weights){
        this.weights.set(adjacencyIndex, weights);
    }

    public boolean equals(Node<?> n) {
        return n.getLabel().equals(this.label);
    }
}
