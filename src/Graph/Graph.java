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

    public List<Node<?>> getLongestPath(Object originKey, Object destinationKey){
        return this.getShortLongPath(this.getNode(originKey), this.getNode(destinationKey), false);
    }

    public List<Node<?>> getShortestPath(Object originKey, Object destinationKey){
        return this.getShortLongPath(this.getNode(originKey), this.getNode(destinationKey), true);
    }

    private List<Node<?>> getShortLongPath(Node<?> origin, Node<?> destination, boolean shortest){
        if(!this.search(origin.toString(), destination.toString())){
            return new ArrayList<>();
        }
        Map<String, Double> distances = new HashMap<>();
        distances.put(origin.toString(), 0.0);

        Set<Node<?>> nodesPassed = new HashSet<>();
        List<Node<?>> nodeToVisit = new ArrayList<>();
        nodeToVisit.add(origin);

        Map<String, Node<?>> previousNode = new HashMap<>();
        while(!nodeToVisit.isEmpty()){
            Node<?> current = getUnvisitedNodeWithMinDistance(nodeToVisit, distances);
            Node<?>[] adjacencies = current.getAdjacencies();
            for(Node<?> n : adjacencies){
                if(!nodesPassed.contains(n)){
                    Double currentsDistance = distances.get(current.toString());
                    double newDistance = Math.pow((current.getWeight(n) + (currentsDistance == null ? 0 : currentsDistance)), (shortest ? 1 : -1));
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

    private Node<?> getUnvisitedNodeWithMinDistance(List<Node<?>> nodesToVisit, Map<String, Double> distance){
        int lowestNodeIndex = 0;
        Double lowestValue = Double.MAX_VALUE;
        for(int i = 0; i < nodesToVisit.size(); i++){
            if(!distance.containsKey(nodesToVisit.get(i).toString()) || lowestValue > distance.get(nodesToVisit.get(i).toString())){
                lowestValue = distance.get(nodesToVisit.get(i).toString());
                lowestNodeIndex = i;
            }
        }
        return nodesToVisit.remove(lowestNodeIndex);
    }

    public boolean search(Object originKey, Object destinationKey){
        BfsIterator bfs = new BfsIterator(this.getNode(originKey));
        while(bfs.ready()){
            if(bfs.next().toString().equals(destinationKey.toString())){
                return true;
            }
        }
        return false;
    }

    public List<?> adjacentNodesAtDistance(Object origin, int distance){
        return this.adjacentNodesAtDistance(this.getNode(origin.toString()), distance);
    }
    public List<?> adjacentNodesAtDistance(Node<?> origin, int distance){
        BfsIterator bfs = new BfsIterator(origin);
        List<Node<?>> nodes = new ArrayList<>();
        while(bfs.ready() && !(bfs.nextIterationLayer() == distance)){
            bfs.next();
        }
        while (bfs.ready() && bfs.nextIterationLayer() == distance){
            nodes.add(bfs.next());
        }
        return nodes;
    }

    @Override
    public String toString() {
        return Arrays.toString(this.nodes.values().toArray());
    }

    private static void createGraph(Graph graph, Object[] keys){
        for(Object key : keys) {
            graph.add(new Node<>(key));
        }
    }
    
    public static void debugGraph(){
        Graph graph = new Graph();

        int distance = 2;

        ///*
        String[] keys = {"s", "a", "b", "c", "d", "e"};

        createGraph(graph, keys);
        String iteratorStart = "s";
        String searchOrigin = "s";
        String searchEnd = "e";
        String pathStart = "s";
        String pathEnd = "e";

        https://www.gatevidyalay.com/wp-content/uploads/2018/03/Dijkstra-Algorithm-Problem-01.png
        graph.newAdjacency("s", "a", 1);
        graph.newAdjacency("s", "b", 5);
        graph.newAdjacency("a", "b", 2);
        graph.newAdjacency("a", "c", 2);
        graph.newAdjacency("a", "d", 1);
        graph.newAdjacency("b", "d", 2);
        graph.newAdjacency("c", "d", 3);
        graph.newAdjacency("c", "e", 1);
        graph.newAdjacency("d", "e", 2);
        //*/

        /*
        Object[] keys = {0, 1, 2, 3, 4, 5, 6, 7, 8};

        createGraph(graph, keys);
        int iteratorStart = 0;
        int searchOrigin = 0;
        int searchEnd = 4;
        int pathStart = 0;
        int pathEnd = 4;

        // https://www.geeksforgeeks.org/wp-content/uploads/Fig-11.jpg
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
        */
        
        graph.printAdjacencies();

        BfsIterator bfsIterator = new BfsIterator(graph.getNode(iteratorStart));
        DfsIterator dfsIterator = new DfsIterator(graph.getNode(iteratorStart));

        System.out.print("BFS: ");
        while(bfsIterator.ready()){
            System.out.print(bfsIterator.next() + " ");
        }
        System.out.println();
        System.out.print("DFS: ");
        while(dfsIterator.ready()){
            System.out.print(dfsIterator.next() + " ");
        }
        System.out.println();

        System.out.println(graph.search(searchOrigin, searchEnd));

        System.out.println("ShortestPath: " + graph.getShortestPath(pathStart, pathEnd));
        System.out.println("LongestPath: " + graph.getLongestPath(pathStart, pathEnd));

        System.out.println("Nodes at distance 3 from node: " + graph.adjacentNodesAtDistance(pathStart, distance));
    }
}
