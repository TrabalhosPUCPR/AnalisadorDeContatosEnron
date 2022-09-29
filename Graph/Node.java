package Graph;


import java.util.LinkedHashMap;

public class Node<T> {

    private static class AdjacencyHolder {
        Node<?> node;
        Integer weight;

        //construtor
        public AdjacencyHolder(Node<?> node, Integer weight) {
            this.node = node;
            this.weight = weight;
        }

        //retorna o node
        public Node<?> getNode() {
            return node;
        }
        //retorna o peso
        public Integer getWeight() {
            return weight;
        }
        //para setar o node
        public void setNode(Node<?> node) {
            this.node = node;
        }
        // para setar o peso
        public void setWeight(Integer weight) {
            this.weight = weight;
        }
    }

    //Por causa que o grafo é um grafo rotulado
    private T label;
    private final LinkedHashMap<Object, AdjacencyHolder> adjacencies;

    //Construtor do node.
    public Node(T label) {
        this.label = label;
        this.adjacencies = new LinkedHashMap<>();
    }

    //para setar o label
    public void setLabel(T label){
        this.label = label;
    }

    //para retornar o label.
    public T getLabel() {
        return label;
    }

    //para retornar o peso de um node rotulado.
    public Integer getWeight(Object adjacentNode) {
        return adjacencies.get(adjacentNode.toString()).getWeight();
    }

    @Override
    //retorna o label em string
    public String toString() {
        return this.label.toString();
    }

    //para criar uma nova adjacencia com novo peso e node rotulado
    protected void newAdjacency(Node<?> node, int weight){
        this.adjacencies.put(node.toString(), new AdjacencyHolder(node, weight));
    }
    //aqui se a adjacencia for diferente de nulo ele retorna o node com base na key
    public Node<?> getAdjacency(String key){
        AdjacencyHolder adjacent = this.adjacencies.get(key);
        if(adjacent != null){
            return adjacent.getNode();
        }
        return null;
    }
    //aqui é para retornar uma lista dos nodes do grafo
    public Node<?>[] getAdjacencies() {
        AdjacencyHolder[] adjacencyHolders = new AdjacencyHolder[0];
        adjacencyHolders = this.adjacencies.values().toArray(adjacencyHolders);
        Node<?>[] nodesList = new Node<?>[adjacencyHolders.length];
        for(int i = 0; i < adjacencyHolders.length; i++){
            nodesList[i] = adjacencyHolders[i].getNode();
        }
        return nodesList;
    }

    //Aqui é para retornar os valores
    public AdjacencyHolder[] getAdjacencyHolders(){
        AdjacencyHolder[] adjacencyHolders = new AdjacencyHolder[0];
        return this.adjacencies.values().toArray(adjacencyHolders);
    }

    //para retornar a key das adjacencias
    public AdjacencyHolder getAdjacencyHolder(String key){
        return this.adjacencies.get(key);
    }

    //para setar o peso das adjacencias
    public void setWeight(Object adjacentNode, int weights){
        this.adjacencies.get(adjacentNode.toString()).setWeight(weights);
    }

    //para somar o peso
    public int sumWeights(){
        int sum = 0;
        for(AdjacencyHolder adH : this.getAdjacencyHolders()){
            sum += adH.weight;
        }
        return sum;
    }

    //para verificar igualdade do rotulo de um node
    public boolean equals(Node<?> n) {
        return n.getLabel().equals(this.label);
    }
}