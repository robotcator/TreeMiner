import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by jxh on 17-5-24.
 */
public class Main {
    public static Integer getSupport(OccurenceList occ){
        Set<Integer> set = new HashSet<Integer>();
        for (int i = 0; i < occ.location.size(); i ++) {
            set.add(occ.location.get(i).getFirst());
        }
        return set.size();
    }

    public static Map<String, OccurenceList> Prune(Map<String, OccurenceList> candidate, int support){
        Map<String, OccurenceList> newCandidate = new HashMap<String, OccurenceList>();

        for (Map.Entry<String, OccurenceList> entry: candidate.entrySet()){
            String label = entry.getKey();
            Integer count = getSupport(entry.getValue());
            if (count >= support) {
                newCandidate.put(entry.getKey(), entry.getValue());
                newCandidate.get(label).setSupport(count);
            }else{

            }
        }
        return newCandidate;
    }

    public static void main(String args[]) {
        List<FreeTree> database = new ArrayList<FreeTree>();
        try {
            File file = new File(Config.inputfile);
            BufferedReader bfReader = new BufferedReader(new FileReader(file));
            String tempString;
            while ((tempString = bfReader.readLine()) != null) {
                int tid = Integer.parseInt(tempString);
                int vCount = Integer.parseInt(bfReader.readLine());
                FreeTree freeTree = new FreeTree(tid, vCount);

                for (int i = 1; i <= vCount; i ++) {
                    int label = Integer.parseInt(bfReader.readLine());
                    freeTree.vLabel.set(i, label);
                    if (label < Config.MinVertex) Config.MinVertex = label;
                    if (label > Config.MaxVertex) Config.MaxVertex = label;
                }

                for (int i = 1; i < vCount; i ++) {
                    String[] tokens = bfReader.readLine().split(" ");
                    int from = Integer.parseInt(tokens[1]);
                    int to = Integer.parseInt(tokens[2]);
                    freeTree.adj.get(from).add(to);
                    freeTree.parent.set(to, from);
                }
                database.add(freeTree);
            }
            bfReader.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        Map<String, OccurenceList> freqt1 = new HashMap<String, OccurenceList>();
        List<Integer> frequency = new ArrayList<Integer>();
        while (frequency.size() < 1000) frequency.add(0);

        for (int i = 0; i < database.size(); i ++) {
            FreeTree freeTree = database.get(i);
            for (int j = 1; j <= freeTree.vCount; j ++) {
                String label = freeTree.vLabel.get(j).toString();
                if (!freqt1.containsKey(label)) {
                    freqt1.put(label, new OccurenceList());
                    freqt1.get(label).location.add(new Pair<Integer, Integer>(i, j));
                }else{
                    freqt1.get(label).location.add(new Pair<Integer, Integer>(i, j));
                }
            }
        }
        Map<String, OccurenceList> prunedFrqet = Prune(freqt1, Config.support);
        frequency.set(1, prunedFrqet.size());


        RFrequentTreeList freqt2 = new RFrequentTreeList();
        freqt2.populate2(database);
        freqt2.finalize2(2);
        freqt2.extensionExploreList2(database, 2, frequency);

        for ( int i = 1; i < 1000; i++ ) {
            if ( frequency.get(i) > 0 ) {
                System.out.println("frequent " + i + " tree " + frequency.get(i));
            }
            else break;
        }
    }
}
