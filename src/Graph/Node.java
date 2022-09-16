package Graph;

public class Node<T> {

    private T label;
    private int graphIndex;

    public Node(T label) {
        this.label = label;
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
}
