/**
 * Created by robotcator on 17-5-6.
 */
public class Pair <K extends Object, V extends Object>{
    K key;
    V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getFirst() {
        return key;
    }

    public V getSecond() {
        return value;
    }
}
