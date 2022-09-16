package Graph;

import java.util.ArrayList;

public class Node<T> {

    private T label;
    private int graphIndex;
    ArrayList<Node<?>> adjacencies;

    public Node(T label) {
        this.label = label;
        adjacencies = new ArrayList<>();
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

    public int getGraphIndex() {
        return graphIndex;
    }

    @Override
    public String toString() {
        return this.label.toString();
    }

    public void newAdjacency(Node<?> node){
        this.adjacencies.add(node);
    }

    public void newNonDirectedAdjacency(Node<?> node){
        this.newAdjacency(node);
        node.newAdjacency(this);
    }

    public ArrayList<Node<?>> getAdjacencies() {
        return adjacencies;
    }
}
