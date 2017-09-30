import java.util.ArrayList;
import java.util.List;

/**
 * Created by robotcator on 17-5-25.
 */
public class SupportNode {

    int support;
    int lastTid;
    List<Occurrence> occList;

    SupportNode() {
        support = 0;
        lastTid = -1;
        occList = new ArrayList<Occurrence>();
    }
}
