import java.util.List;
import java.util.Map;

/**
 * Created by robotcator on 17-6-24.
 */
public class Util {
    public static void printTree(FreeTree freeTree){
        for (int i = 1; i <= freeTree.vCount; i ++)
            System.out.print(freeTree.vLabel.get(i) + " ");
        System.out.println();

        for (int i = 1; i <= freeTree.vCount; i ++)
            System.out.print(freeTree.parent.get(i) + " ");
        System.out.println();

        for (int i = 1; i <= freeTree.vCount; i ++) {
            System.out.print(i + " " + freeTree.adj.get(i).size() + " : ");
            for (int j = 0; j < freeTree.adj.get(i).size(); j ++) {
                System.out.print(freeTree.adj.get(i).get(j) + " ");
            }
            System.out.println();
        }
    }

    public static void printFrequentSet(Map<List<Integer>, SupportNode> Frequent2List) {
        for (Map.Entry<List<Integer>, SupportNode> entry : Frequent2List.entrySet()) {
            System.out.println(entry.getKey());
            SupportNode s = entry.getValue();
            System.out.println("the lastTid and support : " + s.lastTid + " " + s.support);
            System.out.println("the occurence");
            for (int num = 0; num < s.occList.size(); num ++) {
                Occurrence occ = s.occList.get(num);
                System.out.println("the index of occ :" + num + " the tid : " + occ.tid);
                for (int j = 0; j < occ.nodeIndex.size(); j ++) {
                    System.out.print(occ.nodeIndex.get(j) + " ");
                }
                System.out.println();
            }
        }
    }

}
