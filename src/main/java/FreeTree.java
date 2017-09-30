import java.util.ArrayList;
import java.util.List;

/**
 * Created by jxh on 17-5-24.
 */
public class FreeTree {
    int tid;
    int vCount;
    List<List<Integer>> adj;
    List<Integer>[] adj_list;
    List<Integer> vLabel;
    List<Integer> parent;
    List<Integer> degree;

    FreeTree() {}

    FreeTree(int id, int v) {
        tid = id;
        vCount = v;

        adj = new ArrayList<List<Integer>>();
        for (int i = 0; i <= v; i ++)
            adj.add(new ArrayList<Integer>());

        vLabel = new ArrayList<Integer>();
        for (int i = 0; i <= v; i ++)
            vLabel.add(-1);
        parent = new ArrayList<Integer>();
        for (int i = 0; i <= v; i ++)
            parent.add(-1);
        degree = new ArrayList<Integer>();
        for (int i = 0; i <= v; i ++)
            degree.add(0);

    }
}
