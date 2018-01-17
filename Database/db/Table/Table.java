package Table;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by danie on 2/24/2017.
 */
public class Table<V>{
    String tableName;
    List<String> colTitles;
    HashMap<Integer, Row> rows;

    public Table(String name, int numCols) {
        tableName = name;
        colTitles  = new ArrayList<>(numCols);
        rows = new HashMap<>();
    }

    public int colIndex(String title) {
        for (int i = 0; i < colTitles.size(); i += 1) {
            if (colTitles.get(i).equals(title)) {
                return i;
            }
        }
        return -1;
    }

    public int numCols() {
        return colTitles.size();
    }

    public int numRows() {
        return rows.size();
    }

    public boolean containsCol(String title) {
        return colTitles.contains(title);
        }

    public void addColumn(String title) {
        colTitles.add(title);
        }

    public ArrayList<V> getColumn(String title) {
        int index = colIndex(title);
        ArrayList<V> column = new ArrayList<>();
        for (int i = 0; i < rows.size(); i += 1) {
            column.add((V) rows.get(i).get(index));
        }
        return column;
        }


    public void addRow(Row row) {
        rows.put(rows.size(), row);
        }


    public void join(Table t1, Table t2) {
        return;
        }
}

