/**
 * Created by danie on 2/21/2017.
 */
package Table;

import java.util.ArrayList;

public class Row {
    public ArrayList<Object> values;

    public Row(Node[] vals) {
        for (Node n : vals) {
            values.add(n);
        }
    }

    public Object get(int index) {
        return values[index].getItem();
    }
}