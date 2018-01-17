/**
 * Created by georgelzeng on 2/21/2017.
 */
package db;

import db.Node;

public class Row {
    public String[] values;

    public Row(String[] vals) {
        values = vals;
    }

    public String get(int index) {
        return values[index];

    }
}