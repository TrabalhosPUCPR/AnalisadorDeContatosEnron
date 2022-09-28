package Graph;

import java.util.*;

public class Graph {
    private final LinkedHashMap<String, Node<?>> nodes;

    public Graph(){
        this.nodes = new LinkedHashMap<>();
    }

    public int verticesSize(){return this.nodes.size();}
    public int connections(){
        int con = 0;
        for(Node<?> n : this.getNodes()){
            con += n.getAdjacencies().length;
        }
        return con;
    }

    public boolean add(Node<?> node){
        if(this.nodes.get(node.toString()) == null){
            this.nodes.put(node.toString(), node);
            return true;
        }
        return false;
    }

    public Node<?> getNode(Object key){
        return this.nodes.get(key.toString());
    }

    public boolean newAdjacency(Object node1, Object node2, int weight){
        if(this.nodes.get(node2.toString()) == null){
            return false;
        }
        this.nodes.get(node1.toString()).newAdjacency(this.nodes.get(node2.toString()), weight);
        return true;
    }
    public boolean newNonDirectedAdjacency(Object node1, Object node2, int weight){
        return newAdjacency(node1, node2, weight) && newAdjacency(node2, node1, weight);
    }

    public void setNode(Node<?> node){
        this.nodes.put(node.toString(), node);
    }

    public Node<?>[] getNodes() {
        Node<?>[] nodes = new Node<?>[0];
        nodes = this.nodes.values().toArray(nodes);
        return nodes;
    }

    public void printAdjacencies(){
        for (Node<?> node : getNodes()) {
            System.out.print(node + ": | ");
            for (Node<?> nAdjacent : node.getAdjacencies()) {
                System.out.print(nAdjacent + " | ");
            }
            System.out.println();
        }
    }

    public List<Node<?>> getShortestPath(Object originKey, Object destinationKey){
        return this.getShortestPath(this.getNode(originKey), this.getNode(destinationKey));
    }

    public List<Node<?>> getShortestPath(Node<?> origin, Node<?> destination){
        if(!this.search(origin.toString(), destination.toString())){
            return new ArrayList<>();
        }
        Map<String, Integer> distances = new HashMap<>();
        distances.put(origin.toString(), 0);

        Set<Node<?>> nodesPassed = new HashSet<>();
        List<Node<?>> nodeToVisit = new ArrayList<>();
        nodeToVisit.add(origin);

        Map<String, Node<?>> previousNode = new HashMap<>();
        while(!nodeToVisit.isEmpty()){
            Node<?> current = getUnvisitedNodeWithMinDistance(nodeToVisit, distances);
            Node<?>[] adjacencies = current.getAdjacencies();
            for(Node<?> n : adjacencies){
                if(!nodesPassed.contains(n)){
                    Integer currentsDistance = distances.get(current.toString());
                    int newDistance = current.getWeight(n) + (currentsDistance == null ? 0 : currentsDistance);
                    if(!distances.containsKey(n.toString()) || distances.get(n.toString()) > newDistance){
                        distances.put(n.toString(), newDistance);
                        previousNode.put(n.toString(), current);
                    }
                    if(!nodeToVisit.contains(n) && !n.equals(destination)){
                        nodeToVisit.add(n);
                    }
                }
            }
            nodesPassed.add(current);
        }
        List<Node<?>> shortestPath = new ArrayList<>();
        shortestPath.add(destination);
        Node<?> current = previousNode.get(destination.toString());
        while(!current.equals(origin)){
            shortestPath.add(current);
            current = previousNode.get(current.toString());
        }
        shortestPath.add(origin);
        Collections.reverse(shortestPath);
        return shortestPath;
    }

    private Node<?> getUnvisitedNodeWithMinDistance(List<Node<?>> nodesToVisit, Map<String, Integer> distance){
        int lowestNodeIndex = 0;
        Integer lowestValue = Integer.MAX_VALUE;
        for(int i = 0; i < nodesToVisit.size(); i++){
            if(!distance.containsKey(nodesToVisit.get(i).toString()) || lowestValue > distance.get(nodesToVisit.get(i).toString())){
                lowestValue = distance.get(nodesToVisit.get(i).toString());
                lowestNodeIndex = i;
            }
        }
        return nodesToVisit.remove(lowestNodeIndex);
    }

    public boolean search(Object originKey, Object destinationKey){
        Graph.BfsIterator bfs = new BfsIterator(originKey, this);
        while(bfs.next(false) != null){
            if(bfs.next().toString().equals(destinationKey)){
                return true;
            }
        }
        return false;
    }

    private abstract static class searchIterator {
        Graph graph;
        LinkedList<String> nodeToVisitOrder;
        private final HashSet<String> visitedNodes;

        public searchIterator(Object originKey, Graph graph) {
            this.graph = graph;
            this.nodeToVisitOrder = new LinkedList<>();
            this.nodeToVisitOrder.add(originKey.toString());
            this.visitedNodes = new HashSet<>();
            this.visitedNodes.add(originKey.toString());
        }

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

        //String[] keys = {"A", "B", "C", "D", "E", "F"};
        int[] keys = {0, 1, 2, 3, 4, 5, 6, 7, 8};
        for(Object key : keys) {
            graph.add(new Node<>(key));
        }
        /*
        graph.newNonDirectedAdjacency("A", "B", 2);
        graph.newNonDirectedAdjacency("A", "D", 8);
        graph.newNonDirectedAdjacency("B", "D", 5);
        graph.newNonDirectedAdjacency("B", "E", 6);
        graph.newNonDirectedAdjacency("D", "E", 3);
        graph.newNonDirectedAdjacency("D", "F", 2);
        graph.newNonDirectedAdjacency("E", "F", 1);
        graph.newNonDirectedAdjacency("E", "C", 9);
        graph.newNonDirectedAdjacency("F", "C", 3);
        */

        graph.newNonDirectedAdjacency(0, 1, 4);
        graph.newNonDirectedAdjacency(0, 7, 8);
        graph.newNonDirectedAdjacency(1, 7, 11);
        graph.newNonDirectedAdjacency(1, 2, 8);
        graph.newNonDirectedAdjacency(2, 8, 2);
        graph.newNonDirectedAdjacency(2, 3, 7);
        graph.newNonDirectedAdjacency(2, 5, 4);
        graph.newNonDirectedAdjacency(8, 7, 7);
        graph.newNonDirectedAdjacency(7, 6, 1);
        graph.newNonDirectedAdjacency(8, 6, 6);
        graph.newNonDirectedAdjacency(6, 5, 2);
        graph.newNonDirectedAdjacency(5, 3, 14);
        graph.newNonDirectedAdjacency(5, 4, 10);
        graph.newNonDirectedAdjacency(3, 4, 9);

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

        System.out.println(graph.search(0, 4));

        //System.out.println("ShortestPath: " + graph.getShortestPath(0, graph.verticesSize()));
        System.out.println("LongestPath: " + graph.getShortestPath(0, 4));
    }
}
