package Analyzer;

import Graph.Graph;
import Graph.BfsIterator;
import Graph.DfsIterator;
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

    public List<?> findConnectionBFS(Object key, Object key2){
        BfsIterator bfsIterator = new BfsIterator(this.graph.getNode(key));
        ArrayList<Node<?>> takenSearchPath = new ArrayList<>();
        while(bfsIterator.ready()){ // enquanto ainda tem valor dentro do iterador
            takenSearchPath.add(bfsIterator.next()); // adiciona o proximo dentro da lista
            // caso for igual ao destino
            if(takenSearchPath.get(takenSearchPath.size() - 1).toString().equals(key2.toString())){
                return takenSearchPath;
            }
        }
        return null;
    }

    public List<?> findConnectionDFS(Object key, Object key2){
        DfsIterator dfsIterator = new DfsIterator(this.graph.getNode(key)); // inicia o iterador
        ArrayList<Node<?>> takenSearchPath;
        takenSearchPath = new ArrayList<>();
        while(dfsIterator.ready()){ // enquanto ainda tem valor disponivel dentro do iterador
            takenSearchPath.add(dfsIterator.next()); // adiciona o proximo dentro da lista
            // qnd for igual ao key2 (destino)
            if(takenSearchPath.get(takenSearchPath.size() - 1).toString().equals(key2.toString())){
                return takenSearchPath;
            }
        }
        // caso nao encontra o node, retorna nulo
        return null;
    }

    public Graph getGraph() {
        return graph;
    }

    private void addAdjacency(String userNode, String line){
        String[] emails = line.split(", "); // divide a linha em cada ", " e coloca dentro de um array
        try {
            for(String email : emails){ // para toda string dentro do array
                Node<?> newNode = new Node<>(email); // cria o novo node
                Node<?> adjacentNode = this.graph.getNode(userNode).getAdjacency(email); // pega o adjacente que tem a mesma chave
                if(adjacentNode != null){ // caso nao for nulo (ja e adjacente)
                    this.graph.getNode(userNode).setWeight(email, this.graph.getNode(userNode).getWeight(email) + 1); // incrementa 1 ao peso
                    continue; // passa para a proxima iteracao
                }
                this.graph.add(newNode); // caso nao tiver sido definido como adjacente ainda, adiciona no grafo
                this.graph.newAdjacency(userNode, newNode, 1); // adiciona a adjacencia com peso inicial 1
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    private void createGraph(){
        File folder = new File(this.dataPath); // abre a pasta onde ta os arquivos
        for(File userFolder : Objects.requireNonNull(folder.listFiles())){ // passa por todos os arquivos dentro do diretorio
            try{
                File userSentEmailsFolder = new File(userFolder.getPath() + "/" + this.sentEmailFolder); // abre a pasta de emails da pessoa
                if(!userSentEmailsFolder.isDirectory()){ // caso esse arquivo nao for um diretorio, passa para a proxima iteracao
                    continue;
                }
                System.out.println("Analyzing: " + userFolder.getName() + "...");
                for(File userSentEmails : Objects.requireNonNull(userSentEmailsFolder.listFiles())){ // passa por todos os arquivos que ta dentro da pasta de emails
                    BufferedReader reader = new BufferedReader(new FileReader(userSentEmails));
                    String line = reader.readLine();
                    while(!line.startsWith("From: ")){ // enquanto a linha que o reader ta nao comecar com "from", passa para a proxima linha
                        line = reader.readLine();
                    }
                    // quando chegar na linha, pega tudo que ta nela a partir da 6 posicao, pra corta o from
                    Node<String> userNode = new Node<>(line.substring(6));  // cria o novo node pra adicionar
                    this.graph.add(userNode); // adiciona o novo node dentro do grafo, ele mesmo identifica se o node ja existe

                    line = reader.readLine(); // le a proxima linha, que vai ser o To
                    if(line.startsWith("To:")){ // ai so verifica se a linha comeca com o "To:" mesmo
                        line = line.substring(4); // corta o "To: " da linha
                        addAdjacency(userNode.toString(), line); // adiciona todos os emails que tao nessa linha como adjacencia pro user node
                        line = reader.readLine(); // le a proxima linha
                        while (line.startsWith("\t")){ // enquanto ela tem um /t no inicio (tab)
                            addAdjacency(userNode.toString(), line.substring(1)); // corta esse /t e adiciona como adjacencia
                            line = reader.readLine(); // le a prox linha
                        }
                    }
                }
            }catch (Exception e){
                System.err.println(userFolder.getName() + ": " + e);
            }
        }
    }

    public List<?> getTopReceivers(int number){
        HashMap<String, Integer> hashMap = new HashMap<>(); // cria um hashmap com o rotulo como chave
        for(Node<?> n : this.graph.getNodes()){
            hashMap.put(n.toString(), 0); // coloca dentro os nodes que tao dentro do grafo
        }
        for(Node<?> n : this.graph.getNodes()){
            for(Node<?> ad : n.getAdjacencies()){ // para toda adjacencia
                String adjacentLabel = ad.toString(); // pega o label dela
                hashMap.put(adjacentLabel, hashMap.get(adjacentLabel) + 1); // adiciona 1 toda vez que ela aparece como adjacencia
            }
        }

        String[] keys = hashMap.keySet().toArray(new String[0]);
        Integer[] numbers = hashMap.values().toArray(new Integer[0]);

        heapReverseSort(keys, numbers); // faz heap sort reverso dos dois arrays juntos

        Node<?>[] nodes = new Node<?>[keys.length]; // novo node

        for(int i = 0; i < keys.length; i ++){
            nodes[i] = this.graph.getNode(keys[i]); // pega os todos os nodes que foram ordenados
        }

        List<List<?>> result = new ArrayList<>(); // e coloca dentro do arraylist com os resultados

        result.add(Arrays.asList(nodes).subList(0, number)); // guarda os rotulos e a quantidade ordenada
        result.add(Arrays.asList(numbers).subList(0, number));
        return result;
    }

    public List<?> getTopSenders(int number){
        List<List<?>> result = new ArrayList<>();
        try {
            Node<?>[] senders = this.graph.getNodes();
            Integer[] messages = new Integer[senders.length];
            // essa parte inteira so pega a qntd de adjacencias q cada um tem, coloca dentro de uma lista, ordena
            // e devolve os number primeiros
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
