package Table; /**
 * Created by danie on 2/21/2017.
 */
import java.util.List;

/*taken from syntax 1 lecture code*/
public interface Map<K, V> {
    /* Returns true if this map contains a mapping for the specified key. */
    boolean containsCol(String title);

    /* Returns the value to which the specified key is mapped. No defined
     * behavior if the key doesn't exist (ok to crash). */
    V[] getColumn(String title);

    /* Returns the number of key-value mappings in this map. */
    int numCols();

    /* Associates the specified value with the specified key in this map. */
    void addColumn(String title, V[] values);

    /* Returns a list of the keys in this map. */
    List<String> titles();
}