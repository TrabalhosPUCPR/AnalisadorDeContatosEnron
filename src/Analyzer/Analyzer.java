package Analyzer;

import Graph.Graph;
import Graph.Node;

import java.io.*;
import java.util.*;

public class Analyzer {
    final Graph graph;
    final String dataPath, sentEmailFolder;

    public Analyzer(String dataPath, String emailFolderName) {
        this.dataPath = dataPath;
        this.graph = new Graph();
        this.sentEmailFolder = emailFolderName;
        createGraph();
    }

    public Graph getGraph() {
        return graph;
    }

    private void addAdjacency(String userNode, String line){
        String[] emails = line.split(", ");
        try {
            for(String email : emails){
                exists : {
                    Node<?> newNode = new Node<>(email);
                    Node<?> adjacentNode = this.graph.getNode(userNode).getAdjacency(email);
                    if(adjacentNode != null){
                        this.graph.getNode(userNode).setWeight(email, this.graph.getNode(userNode).getWeight(email) + 1);
                        break exists;
                    }
                    this.graph.add(newNode);
                    this.graph.newAdjacency(userNode, newNode, 1);
                }
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    private void createGraph(){
        File folder = new File(this.dataPath);
        for(File userFolder : Objects.requireNonNull(folder.listFiles())){
            try{
                File userSentEmailsFolder = new File(userFolder.getPath() + "/" + this.sentEmailFolder);
                if(!userSentEmailsFolder.isDirectory()){
                    continue;
                }
                System.out.println(userFolder.getName());
                for(File userSentEmails : Objects.requireNonNull(userSentEmailsFolder.listFiles())){
                    BufferedReader reader = new BufferedReader(new FileReader(userSentEmails));

                    // os destinatarios vao sempre estar na terceira linha, e melhor fazer assim doq um while ate encontrar um "To:" pq pode ter outras linhas q comecam com isso tb,
                    // entao teria q fazer um contador pra ter algum limite, q e relativo a so fazer esse for
                    String line = reader.readLine();
                    while(!line.startsWith("From: ")){
                        line = reader.readLine();
                    }

                    Node<String> userNode = new Node<>(line.substring(6));
                    this.graph.add(userNode);

                    line = reader.readLine();
                    if(line.startsWith("To:")){ // ai so verifica se a linha comeca com o "To:" mesmo
                        line = line.substring(4);
                        addAdjacency(userNode.toString(), line);
                        line = reader.readLine();
                        while (line.startsWith("\t")){
                            addAdjacency(userNode.toString(), line.substring(1));
                            line = reader.readLine();
                        }
                    }
                }
            }catch (Exception e){
                System.err.println(userFolder.getName() + ": " + e);
            }
        }
    }

    public List<?> getTopReceivers(int number){
        HashMap<String, Integer> hashMap = new HashMap<>();
        for(Node<?> n : this.graph.getNodes()){
            hashMap.put(n.toString(), 0);
        }
        for(Node<?> n : this.graph.getNodes()){
            for(Node<?> ad : n.getAdjacencies()){
                String adjacentLabel = ad.toString();
                hashMap.put(adjacentLabel, hashMap.get(adjacentLabel) + 1);
            }
        }

        String[] keys = hashMap.keySet().toArray(new String[0]);
        Integer[] numbers = hashMap.values().toArray(new Integer[0]);

        heapReverseSort(keys, numbers);

        Node<?>[] nodes = new Node<?>[keys.length];

        for(int i = 0; i < keys.length; i ++){
            nodes[i] = this.graph.getNode(keys[i]);
        }

        List<List<?>> result = new ArrayList<>();

        result.add(Arrays.asList(nodes).subList(0, number));
        result.add(Arrays.asList(numbers).subList(0, number));
        return result;
    }

    public List<?> getTopSenders(int number){
        List<List<?>> result = new ArrayList<>();
        try {
            Node<?>[] senders = this.graph.getNodes();
            Integer[] messages = new Integer[senders.length];
            for (int i = 0; i < senders.length; i++) {
                messages[i] = senders[i].getAdjacencies().length;
            }
            heapReverseSort(senders, messages);
            result.add(Arrays.asList(senders).subList(0, number));
            result.add(Arrays.asList(messages).subList(0, number));
        }catch (Exception e){
            System.err.println("The number is too big!");
        }
        return result;
    }

    private static void heapReverseSort(Object[] nodes, Integer[] weights) {
        int n = weights.length;
        for (int i = n / 2 - 1; i >= 0; i--) MinHeapify(nodes, weights, n, i);
        for (int i = n - 1; i > 0; i--) {
            int temp = weights[0];
            Object tempNode = nodes[0];
            weights[0] = weights[i];
            nodes[0] = nodes[i];
            weights[i] = temp;
            nodes[i] = tempNode;
            MinHeapify(nodes, weights, i, 0);
        }
    }

    private static void MinHeapify(Object[] nodes, Integer[] weights, int n, int i) {
        int smallest = i;
        int l = 2 * i + 1;
        int r = 2 * i + 2;
        if (l < n && weights[l] < weights[smallest]) smallest = l;
        if (r < n && weights[r] < weights[smallest]) smallest = r;
        if (smallest != i) {
            int swap = weights[i];
            Object swapNode = nodes[i];
            weights[i] = weights[smallest];
            nodes[i] = nodes[smallest];
            weights[smallest] = swap;
            nodes[smallest] = swapNode;
            MinHeapify(nodes, weights, n, smallest);
        }
    }
}
