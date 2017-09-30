import java.util.*;

/**
 * Created by robotcator on 17-6-3.
 */
public class RFrequentTreeList {
    Map<List<Integer>, SupportNode> Frequent2List;

    RFrequentTreeList() {
        Frequent2List = new TreeMap<List<Integer>, SupportNode>(new Comparator<List<Integer>>() {
            public int compare(List<Integer> o1, List<Integer> o2) {
                int s1 = o1.size();
                int s2 = o2.size();
                int i = 0;
                while (i < s1 && i < s2) {
                    if (o1.get(i) > o2.get(i)) return 1;
                    else if (o1.get(i) < o2.get(i)) return -11;
                    i += 1;
                }
                if (i < s1) return -1;
                if (i < s2) return 1;
                return 0;
            }
        });
//        Frequent2List = new HashMap<List<Integer>, SupportNode>();
    }

    void populate2(List<FreeTree> database){
//        System.out.println("populate two nodes set");
        for (int t = 0; t < database.size(); t ++) {
            FreeTree freeTree = database.get(t);
//            System.out.println("enumerate tree :" + freeTree.tid);
            for (int i = 1; i <= freeTree.vCount; i ++) {
//                System.out.println("the node and the adj size :" + i + " " + freeTree.adj.get(i).size());
                for (int j = 0; j < freeTree.adj.get(i).size(); j ++) {
//                    System.out.println("adj " + freeTree.adj.get(i).get(j));
                    int adj = freeTree.adj.get(i).get(j);
                    List<Integer> NodeString = new ArrayList<Integer>();
                    while(NodeString.size() < 2) NodeString.add(0);

                    SupportNode supportNode = new SupportNode();
                    Occurrence sOccurrence = new Occurrence();

                    NodeString.set(0, freeTree.vLabel.get(i));
                    NodeString.set(1, freeTree.vLabel.get(adj));

                    supportNode.lastTid = freeTree.tid;
                    supportNode.support = 1;
                    supportNode.occList.clear();

                    sOccurrence.tid = freeTree.tid;
                    sOccurrence.nodeIndex.clear();
                    sOccurrence.nodeIndex.add(i);
                    sOccurrence.nodeIndex.add(adj);

                    supportNode.occList.add(sOccurrence);

//                    System.out.println("the new element");
//                    System.out.println(NodeString.get(0) + " " + NodeString.get(1));
//                    System.out.println("the occurence list size : " + supportNode.occList.size());
//                    System.out.println("the occurence list: " + sOccurrence.nodeIndex.get(0) + " " + sOccurrence.nodeIndex.get(1));

                    //NOTE: assuming tids are coming in increasing order

                    if (Frequent2List.containsKey(NodeString)) {
//                        System.out.println("duplicate :" + NodeString.get(0) + " " + NodeString.get(1));
                        SupportNode temp = Frequent2List.get(NodeString);
//                        System.out.println(temp.lastTid + " " + supportNode.lastTid);

                        if (temp.lastTid != supportNode.lastTid) {
                            temp.lastTid = supportNode.lastTid;
                            temp.support += 1;
                        }
                        temp.occList.add(sOccurrence);

//                        System.out.println("update the new occ: " + temp.lastTid + " " + temp.support);
//                        System.out.println("the size of occ :" + temp.occList.size());
//                        System.out.println("the node string :" + NodeString.get(0) + " " + NodeString.get(1));

                        Frequent2List.put(NodeString, temp);
                    }else{
//                        System.out.println("the node string :" + NodeString.get(0) + " " + NodeString.get(1));
                        Frequent2List.put(NodeString, supportNode);
                    }

                }
//                System.out.println();
            }
        }
    }

    void finalize2(int support){
        for(Iterator<Map.Entry<List<Integer>, SupportNode>> it = Frequent2List.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<List<Integer>, SupportNode> entry = it.next();
            if(entry.getValue().support < support) {
                it.remove();
            }
        }
    }

    void extensionExploreList2(List<FreeTree> database, int support, List<Integer> frequency) {
        System.out.println("============extensionExploreList2=======");
        for (Map.Entry<List<Integer>, SupportNode> entry : Frequent2List.entrySet()) {
            List<Integer> vString = new ArrayList<Integer>();
            while (vString.size() < 5) vString.add(0);

            vString.set(0, 2); //two vertices
            vString.set(4, Config.ENDOFTREETAG);
            vString.set(2, Config.ENDTAG);

            List<Integer> key = entry.getKey();
            SupportNode value = entry.getValue();

            vString.set(1, key.get(0));
            vString.set(3, key.get(1));

            RBFCFTree rbfcfTree = new RBFCFTree(vString);
            rbfcfTree.extensionExplore(database, value.occList, support, frequency);
        }
    }
}
