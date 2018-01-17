package Table;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by danie on 2/21/2017.
 */
public class Columns<V> {
    public LinkedList colTitles;
    public LinkedList[] colValues;
    public int numCols;
    public int colLength = 0;

    public Columns(String[] titles) {
        colTitles = new LinkedList(Arrays.asList(titles)); // = titles
        numCols = titles.length;
        colValues = new LinkedList[numCols];
        //colValues = (V[][]) new Object[numCols][];

    }

    public int colIndex(String title) {
        for (int i = 0; i < numCols; i += 1) {
            if (colTitles.get(i).equals(title)) { //colTitles[i].equals(title)
                return i;
            }
        }
        return -1;
    }

    public int numCols() {
        return numCols;
    }

    public List<String> titles() {
        List<String> columnsList = new ArrayList<String>();
        for (int i = 0; i < colTitles.size(); i += 1) {
            columnsList.add((String) colTitles.get(i));
        }
        return columnsList;
    }
}
