package Analyzer;

import Graph.Graph;
import Graph.Node;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

/*      EXAMPLE EMAIL TO ANALYZE

Message-ID: <28715254.1075858200633.JavaMail.evans@thyme>
Date: Mon, 31 Jul 2000 09:22:00 -0700 (PDT)
From: mike.carson@enron.com
To: john.zufferli@enron.com, matt.lorenz@enron.com, john.berger@enron.com,
	paul.broderick@enron.com, mark.davis@enron.com,
	kevin.presto@enron.com, john.suarez@enron.com,
	chad.starnes@enron.com, clint.dean@enron.com, kyle.schultz@enron.com,
	rogers.herndon@enron.com, robert.benson@enron.com,
	doug.gilbert-smith@enron.com, rlmichaelis@hormel.com,
	dttowns@swbell.net, greg.woulfe@enron.com,
	seanoneal@worldnet.att.net, ashton.soniat@enron.com
Subject: Baby Party/Shower
Mime-Version: 1.0
Content-Type: text/plain; charset=us-ascii
Content-Transfer-Encoding: 7bit
X-From: Mike Carson
X-To: John Zufferli, Matt Lorenz, John Berger, Paul J Broderick, Mark Dana Davis, Kevin M Presto, John D Suarez, Chad Starnes, Clint Dean, Kyle Schultz, Rogers Herndon, Robert Benson, Doug Gilbert-Smith, rlmichaelis@hormel.com, dttowns@swbell.net, Greg Woulfe, seanoneal@worldnet.att.net, Ashton Soniat
X-cc:
X-bcc:
X-Folder: \Mike_Carson_Dec2000\Notes Folders\'sent mail
X-Origin: Carson-M
X-FileName: mcarson2.nsf

Some friends of my wife and I are throwing a little bash on the evening of
September 9th for my upcoming baby BOY.   I would like to invite you all to
attend.  I believe there will be a margarita machine....

If you want to come,, please reply by sending me your address.  You will then
recieve an invitation by mail.

Hope to see you there,,

Mike
 */

public class Analyzer {
    final Graph graph;
    final String dataPath;

    public Analyzer(String dataPath) {
        this.dataPath = dataPath;
        this.graph = new Graph();
        createGraph();
    }

    public Graph getGraph() {
        return graph;
    }

    private void addAdjacency(Node<?> userNode, String line){
        String[] emails = line.split(", ");
        try {
            for(String email : emails){
                exists : {
                    Node<?> newNode = new Node<>(email);
                    Node<?> adjacentNode = userNode.getAdjacency(email);
                    if(adjacentNode != null){
                        userNode.setWeight(email, userNode.getWeight(email) + 1);
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

    private String getUserEmail(String userSentEmailDirectoty) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(userSentEmailDirectoty + "/1"));
        while(!reader.readLine().startsWith("Date:")){}
        return reader.readLine().substring(6);
    }

    private void createGraph(){
        File folder = new File(this.dataPath);
        for(File userFolder : Objects.requireNonNull(folder.listFiles())){
            try{
                File userSentEmailsFolder = new File(userFolder.getPath() + "/_sent_mail");
                Node<String> userNode = new Node<>(getUserEmail(userSentEmailsFolder.getPath()));
                this.graph.add(userNode);
                System.out.println(userNode.getLabel());
                for(File userSentEmails : Objects.requireNonNull(userSentEmailsFolder.listFiles())){
                    BufferedReader reader = new BufferedReader(new FileReader(userSentEmails));
                    for (int i = 0; i < 3; i ++){
                        reader.readLine();
                    }
                    String line = reader.readLine();
                    if(line.startsWith("To:")){
                        line = line.substring(4);
                        addAdjacency(userNode, line);
                        line = reader.readLine();
                        while (line.startsWith("\t")){
                            addAdjacency(userNode, line.substring(1));
                            line = reader.readLine();
                        }
                    }
                }
            }catch (Exception e){
                System.err.println(userFolder.getName() + " has no sent emails!");
            }
        }
    }

    private Node<?>[] getTopReceivers(){
        HashMap<String, Integer> hashMap = new HashMap<>();
        for(Node<?> n : this.graph.getNodes()){
            hashMap.put(n.getLabel().toString(), 1);
        }
        for(Node<?> n : this.graph.getNodes()){
            for(Node<?> ad : n.getAdjacencies()){
                String adjacentLabel = ad.getLabel().toString();
                hashMap.put(adjacentLabel, hashMap.get(adjacentLabel) + n.getWeight(adjacentLabel));
            }
        }

        String[] keys = hashMap.keySet().toArray(new String[0]);
        Integer[] numbers = hashMap.values().toArray(new Integer[0]);

        heapsort(keys, Arrays.stream(numbers).mapToInt(Integer::intValue).toArray());

        Node<?>[] nodes = new Node<?>[keys.length];

        for(int i = 0; i < keys.length; i ++){
            nodes[i] = this.graph.getNode(keys[i]);
        }

        return nodes;
    }

    public Node<?>[] getTopReceivers(int number){
        Node<?>[] result = getTopReceivers();
        return Arrays.copyOf(result, number);
    }

    private Node<?>[] getTopSenders(){
        Node<?>[] senders = this.graph.getNodes();
        int[] messages = new int[senders.length];
        for(int i = 0; i < senders.length; i++){
            messages[i] = senders[i].sumWeights();
        }
        heapsort(senders, messages);
        return senders;
    }

    public Node<?>[] getTopSenders(int number){
        Node<?>[] result = getTopSenders();
        return Arrays.copyOf(result, number);
    }

    private static void heapsort(Object[] nodes, int[] weights) {
        int n = weights.length;
        for (int i = n / 2 - 1; i >= 0; i--) heapify(nodes, weights, n, i);
        for (int i = n - 1; i > 0; i--) {
            int temp = weights[0];
            Object tempNode = nodes[0];
            weights[0] = weights[i];
            nodes[0] = nodes[i];
            weights[i] = temp;
            nodes[i] = tempNode;
            heapify(nodes, weights, i, 0);
        }
    }

    private static void heapify(Object[] nodes, int[] weights, int n, int i) {
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
            heapify(nodes, weights, n, smallest);
        }
    }
}
