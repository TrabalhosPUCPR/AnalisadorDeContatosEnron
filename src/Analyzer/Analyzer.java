package Analyzer;

import Graph.Graph;
import Graph.Node;

import java.io.*;
import java.util.ArrayList;

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
        for(String email : emails){
            exists : {
                // TODO: 9/21/22 ver uma maneira melhor de fazer isso
                Node<?> newNode = new Node<>(email);
                int adjacentIndex = userNode.indexOfAdjacent(newNode);
                if(adjacentIndex != -1){
                    userNode.setWeight(adjacentIndex, userNode.getWeight(adjacentIndex) + 1);
                    break exists;
                }

                int index = this.graph.add(newNode, false);
                this.graph.newAdjacency(userNode.getGraphIndex(), index, 1);
            }
        }
    }

    private String getUserEmail(String userSentEmailDirectoty) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(userSentEmailDirectoty + "/1"));
        while(!reader.readLine().startsWith("From:")){
            reader.readLine();
        }
        return reader.readLine().substring(7);
    }

    private void createGraph(){
        File folder = new File(this.dataPath);
        for(File userFolder : folder.listFiles()){
            try{
                File userSentEmailsFolder = new File(userFolder.getPath() + "/_sent_mail");
                Node<String> userNode = new Node<>(getUserEmail(userSentEmailsFolder.getPath()));
                this.graph.add(userNode, false);
                System.out.println(userNode.getLabel());
                for(File userSentEmails : userSentEmailsFolder.listFiles()){
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
}
