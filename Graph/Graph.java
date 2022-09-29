package Graph;

import java.util.*;

public class Graph {
    //para adicionar node rotulado no hash map
    private final LinkedHashMap<String, Node<?>> nodes;

    // construtor
    public Graph(){
        this.nodes = new LinkedHashMap<>();
    }
    //retorna tamanho dos nodes.
    public int verticesSize(){return this.nodes.size();}

    //para ler as adjacencias dos nodes e retorna elas.
    public int connections(){
        int con = 0;
        for(Node<?> n : this.getNodes()){
            con += n.getAdjacencies().length;
        }
        return con;
    }

    // para adicionar um node no hashmap
    public boolean add(Node<?> node){
        if(this.nodes.get(node.toString()) == null){
            this.nodes.put(node.toString(), node);
            return true;
        }
        return false;
    }


    //retorna a chave do node convertida para valor de tipo string
    public Node<?> getNode(Object key){
        return this.nodes.get(key.toString());
    }
    //para criar uma nova adjacencia entre 2 nós.
    public boolean newAdjacency(Object node1, Object node2, int weight){
        if(this.nodes.get(node2.toString()) == null){
            return false;
        }
        this.nodes.get(node1.toString()).newAdjacency(this.nodes.get(node2.toString()), weight);
        return true;
    }
    //Aqui ele cria duas adjacencias com peso ao mesmo tempo entre 2 nodes determinados
    public boolean newNonDirectedAdjacency(Object node1, Object node2, int weight){
        return newAdjacency(node1, node2, weight) && newAdjacency(node2, node1, weight);
    }
    //seta o rotulo do node para um valor de tipo string
    public void setNode(Node<?> node){
        this.nodes.put(node.toString(), node);
    }

    // aqui retorna os nodes rotulados que estão dentro do array
    public Node<?>[] getNodes() {
        Node<?>[] nodes = new Node<?>[0];
        nodes = this.nodes.values().toArray(nodes);
        return nodes;
    }

    //Para printar as adjacencias de um node.
    public void printAdjacencies(){
        for (Node<?> node : getNodes()) {
            System.out.print(node + ": | ");
            for (Node<?> nAdjacent : node.getAdjacencies()) {
                System.out.print(nAdjacent + " | ");
            }
            System.out.println();
        }
    }

    public ArrayList<Node<?>> getLongestPath(Object originKey, Object destinationKey){
        return this.getLongestPath(this.getNode(originKey), this.getNode(destinationKey));
    }
    //algoritmo de Djikstra pra retornar o menor camino em um grafo
    public ArrayList<Node<?>> getLongestPath(Node<?> origin, Node<?> destination){
        if(!this.search(origin.toString(), destination.toString())){
            return new ArrayList<>();
        }
        Map<String, Integer> distances = new HashMap<>();
        Map<String, ArrayList<Node<?>>> path = new HashMap<>();
        for(Node<?> n : this.getNodes()){
            distances.put(n.toString(), Integer.MAX_VALUE);
            path.put(n.toString(), new ArrayList<>());
        }
        distances.put(origin.toString(), 0);

        Set<Node<?>> nodesPassed = new HashSet<>();
        Queue<String> line = new PriorityQueue<>();
        line.add(origin.toString());

        while(!line.isEmpty() && !(line.peek().equals(destination.toString()))){
            Node<?> current = this.getNode(line.poll());
            Node<?>[] adjacencies = current.getAdjacencies();
            for(Node<?> n : adjacencies){
                if(!nodesPassed.contains(n)){
                    int adj_dist = distances.get(n.toString());
                    int newDistance = current.getWeight(n) + (adj_dist == Integer.MAX_VALUE ? 0 : adj_dist);
                    if(distances.get(n.toString()) > newDistance){
                        distances.put(n.toString(), newDistance);
                        ArrayList<Node<?>> currentsPath = new ArrayList<>(path.get(current.toString()));
                        currentsPath.add(current);
                        path.put(n.toString(), currentsPath);
                    }
                    if(!line.contains(n.toString())){
                        line.add(n.toString());
                    }
                }
            }
            nodesPassed.add(current);
        }

        return new ArrayList<>();
    }

    private String getUnivistedNodeKeyWithMinDistance(Set<Node<?>> visited, Map<String, Integer> distance){
        Integer[] distances = distance.values().toArray(new Integer[0]);
        String[] keys = distance.keySet().toArray(new String[0]);

        String lowestKey = "";
        Integer lowestValue = 0;
        for(int i = 0; i < distances.length; i++){
            if(!visited.contains(this.getNode(keys[i])) && distances[i] < lowestValue && distances[i] != 0){
                lowestValue = distances[i];
                lowestKey = keys[i];
            }
        }
        return lowestKey;
    }

    //para fazer a busca de um node destinatario a partir de um node de origem.
    public boolean search(Object originKey, Object destinationKey){
        Graph.BfsIterator bfs = new BfsIterator(originKey, this);
        while(bfs.next(false) != null){
            if(bfs.next().toString().equals(destinationKey)){
                return true;
            }
        }
        return false;
    }

    //iterator para fazer tipos de busca no grafo.
    private abstract static class searchIterator {
        Graph graph;
        LinkedList<String> nodeToVisitOrder;
        private final HashSet<String> visitedNodes;

        //construtor do iterator
        public searchIterator(Object originKey, Graph graph) {
            this.graph = graph;
            this.nodeToVisitOrder = new LinkedList<>();
            this.nodeToVisitOrder.add(originKey.toString());
            this.visitedNodes = new HashSet<>();
            this.visitedNodes.add(originKey.toString());
        }

        //se o node não existe ele retorna que o node esta vazio
        protected boolean ready(){
            return !this.nodeToVisitOrder.isEmpty();
        }

        abstract Node<?> next(boolean removeElement);

        private static void reverse(Node<?>[] nodeList){
            for(int i = 0; i < nodeList.length / 2; i++){
                Node<?> temp = nodeList[nodeList.length - i - 1];
                nodeList[nodeList.length - i - 1] = nodeList[i];
                nodeList[i] = temp;
            }
        }

        public Node<?> next(){
            return this.next(true);
        }
        protected void addToNextVisitList(Node<?> next, boolean rev){
            Node<?>[] adjacency = next.getAdjacencies();
            if (rev) reverse(adjacency); // TODO: 9/27/2022 melhora isso
            for(Node<?> node : adjacency){
                if(!this.visitedNodes.contains(node.toString())){
                    this.nodeToVisitOrder.removeFirstOccurrence(node.toString());
                    this.nodeToVisitOrder.add(node.toString());
                }
            }
            this.visitedNodes.add(next.toString());
        }
    }
    //iterator da busca por largura que realiza uma busca percorrendo os nodes adjacentes ao outro
    public static class BfsIterator extends searchIterator{
        public BfsIterator(Object origin, Graph graph) {
            super(origin, graph);
        }

        @Override
        public Node<?> next(boolean removeElement) {
            if(!this.ready()) return null;
            Node<?> next = this.graph.getNode(this.nodeToVisitOrder.getFirst());
            if(!removeElement) return next;
            this.nodeToVisitOrder.removeFirst();
            addToNextVisitList(next, false);
            return next;
        }
    }

    //Iterator da busca por profundidade onde o mesmo realiza a visita dos vertices até não encontrar mais nenhuma
    //adjacencia e assim retorna ao inicio e começa denovo
    public static class DfsIterator extends searchIterator{

        public DfsIterator(Object origin, Graph graph) {
            super(origin, graph);
        }

        @Override
        public Node<?> next(boolean removeElement) {
            if(!this.ready()) return null;
            Node<?> next = this.graph.getNode(this.nodeToVisitOrder.getLast());
            if(!removeElement) return next;
            this.nodeToVisitOrder.removeLast();
            addToNextVisitList(next, true);
            return next;
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(this.nodes.values().toArray());
    }

    public static void debugGraph(){
        Graph graph = new Graph();
        for(int i = 1; i < 8; i++){
            graph.add(new Node<>(i));
        }

        graph.newNonDirectedAdjacency(1, 2, 5);
        graph.newNonDirectedAdjacency(1, 4, 15);
        graph.newNonDirectedAdjacency(1, 5, 1);
        graph.newNonDirectedAdjacency(2, 3, 45);
        graph.newNonDirectedAdjacency(2, 5, 55);
        graph.newNonDirectedAdjacency(2, 6, 5);
        graph.newNonDirectedAdjacency(3, 7, 21);
        graph.newNonDirectedAdjacency(3, 6, 5);
        graph.newNonDirectedAdjacency(4, 5, 4);
        graph.newNonDirectedAdjacency(5, 6, 8);
        graph.newNonDirectedAdjacency(6, 7, 9);
        graph.newNonDirectedAdjacency(7, 4, 7);

        graph.printAdjacencies();

        Graph.BfsIterator bfsIterator = new Graph.BfsIterator(1, graph);
        Graph.DfsIterator dfsIterator = new Graph.DfsIterator(1, graph);

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

        //System.out.println("ShortestPath: " + graph.getShortestPath(0, graph.verticesSize()));
        System.out.println("LongestPath: " + graph.getLongestPath(1, 7));
    }
}