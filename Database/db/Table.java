package db;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by georgelzeng on 2/24/2017.
 */
public class Table<V> {
    public String tableName;
    public ArrayList<String> colTitles;
    public ArrayList<String> colTypes;
    public ArrayList<Row> rows;

    public Table(String name, int numCols) {
        tableName = name;
        colTitles = new ArrayList<>(numCols);
        colTypes = new ArrayList<>(numCols);
        rows = new ArrayList<>();
    }

    public int colIndex(String title) {
        for (int i = 0; i < colTitles.size(); i += 1) {
            if (colTitles.get(i).equals(title)) {
                return i;
            }
        }
        return -1;
    }

    public void changeName(String name) {
        tableName = name;
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

    public void addColumns(ArrayList<String> titles) {
        colTitles = titles;
    }

    public void addColumnTypes(ArrayList<String> types) {
        colTypes = types;
    }

    public ArrayList<String> getColumn(String title) {
        int index = colIndex(title);
        ArrayList<String> column = new ArrayList<>();
        for (int i = 0; i < rows.size(); i += 1) {
            column.add(rows.get(i).get(index));
        }
        return column;
    }


    public void addRow(Row row) {
        rows.add(row);
    }

    public void addRows(ArrayList<Row> daRows) {
        rows = daRows;
    }

    public String[] getRow(int index) {
        return rows.get(index).values;
    }


    public Table join(Table t2) {
        ArrayList<String> shared = new ArrayList<>();
        ArrayList<String> newCols = new ArrayList<>(this.numCols() + t2.numCols() - shared.size());
        ArrayList<String> newTypes = new ArrayList<>(this.numCols() + t2.numCols() - shared.size());
        for (int i = 0; i < this.numCols(); i += 1) {
            if (t2.containsCol((String) this.colTitles.get(i))) {
                shared.add((String) this.colTitles.get(i));
            }
        }

        Table joinedTable = new Table(this.tableName + " and " + t2.tableName, this.numCols() + t2.numCols() - shared.size());
        for (int i = 0; i < shared.size(); i += 1) {
            newCols.add(this.colTitles.get(this.colIndex(shared.get(i))));
            newTypes.add(this.colTypes.get(this.colIndex(shared.get(i))));
        }
        for (int i = 0; i < this.numCols(); i += 1) {
            if (!shared.contains(this.colTitles.get(i))) {
                newCols.add(this.colTitles.get(i));
                newTypes.add(this.colTypes.get(i));
            }
        }
        for (int i = 0; i < t2.numCols(); i += 1) {
            if (!shared.contains(t2.colTitles.get(i))) {
                newCols.add((String) t2.colTitles.get(i));
                newTypes.add((String) t2.colTypes.get(i));
            }
        }
        joinedTable.addColumns(newCols);
        joinedTable.addColumnTypes(newTypes);

        ArrayList<Integer> nonMatchingRows = new ArrayList<>();
        for (int i = 0; i < shared.size(); i += 1) { //which column of the shared column
            for (int j = 0; j < this.numRows(); j += 1) { //which row
                if (this.rows.get(j).get(this.colIndex(shared.get(i))) != ((Row) t2.rows.get(j)).get(this.colIndex(shared.get(i)))) {
                    nonMatchingRows.add(j);
                }
            }
        }

        for (int j = 0; j < this.numRows(); j += 1) { //which row it is on
            if (nonMatchingRows.contains(j)) {
                continue;
            }
            String[] daRow = new String[joinedTable.numCols()];
            for (int i = 0; i < joinedTable.numCols(); i += 1) { //which column of the row
                if (i < shared.size()) {
                    daRow[i] = ((Row) this.rows.get(j)).get(this.colIndex(shared.get(i)));
                } else if (i < this.numCols()) {
                    if (shared.contains(this.colTitles.get(i))) {
                        continue;
                    } else {
                        daRow[i] = ((Row) this.rows.get(j)).get(i);
                    }
                } else if (i < this.numCols() + t2.numCols() - shared.size()) {
                    if (shared.contains(t2.colTitles.get(i - 1))) {
                        continue;
                    } else {
                        daRow[i] = ((Row) t2.rows.get(j)).get(i - 1);
                    }
                }
            }
            Row newRow = new Row(daRow);
            joinedTable.addRow(newRow);
        }
        return joinedTable;
    }
}


