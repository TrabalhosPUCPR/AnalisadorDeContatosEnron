package Graph;

import java.util.ArrayList;

public class Node<T> {

    private T label;
    private int graphIndex;
    private ArrayList<Node<?>> adjacencies;
    private ArrayList<Integer> weights;

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

    public void newAdjacency(Node<?> node, int weight){
        this.adjacencies.add(node);
        this.weights.add(weight);
    }

    public void newNonDirectedAdjacency(Node<?> node, int weight){
        this.newAdjacency(node, weight);
        node.newAdjacency(this, weight);
    }

    public ArrayList<Node<?>> getAdjacencies() {
        return adjacencies;
    }

    public void setWeight(int adjacencyIndex, int weights){
        this.weights.set(adjacencyIndex, weights);
    }
}
