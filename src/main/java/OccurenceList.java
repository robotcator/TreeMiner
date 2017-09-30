import java.util.ArrayList;

/**
 * Created by robotcator on 17-5-5.
 */
public class OccurenceList {
    ArrayList<Pair<Integer, Integer>> location;
    int depth;
    int support;

    public OccurenceList(){
        location = new ArrayList<Pair<Integer, Integer>>();
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getDepth() {
        return depth;
    }

    public int getSupport() {
        return support;
    }

    public void setSupport(int support) {
        this.support = support;
    }
}
