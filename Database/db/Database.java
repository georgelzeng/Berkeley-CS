package db;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import edu.princeton.cs.introcs.In;

import java.util.StringJoiner;

public class Database {
    static HashMap<String, Table> db;

    public Database() {
        db = new HashMap<>();
    }

    public String transact(String query) {
        Matcher m;
        if ((m = CREATE_CMD.matcher(query)).matches()) {
            try {
                createTable(m.group(1));
            } catch (NullPointerException e) {
                return "ERROR: Cannot create table with no columns.";
            } catch (RuntimeException e) {
                return "ERROR: Table already exists.";
            }
        } else if ((m = LOAD_CMD.matcher(query)).matches()) {
            try {
                loadTable(m.group(1));
            } catch (IllegalArgumentException e) {
                return "ERROR: File does not exist.";
            } catch (RuntimeException e) {
                return "ERROR: Malformed table.";
            }
        } else if ((m = STORE_CMD.matcher(query)).matches()) {
            try {
                storeTable(m.group(1));
            } catch (NullPointerException e) {
                return "ERROR: Table does not exist.";
            } catch (FileNotFoundException e) {
                return "ERROR: Table does not exist.";
            }
        } else if ((m = DROP_CMD.matcher(query)).matches()) {
            try {
                dropTable(m.group(1));
            } catch (RuntimeException e) {
                return "ERROR: Table does not exist.";
            }
        } else if ((m = INSERT_CMD.matcher(query)).matches()) {
            try {
                insertRow(m.group(1));
            } catch (IllegalArgumentException e) {
                return "ERROR: Type Error.";
            } catch (RuntimeException e) {
                return "ERROR: Values does not match number of columns.";
            }
        } else if ((m = PRINT_CMD.matcher(query)).matches()) {
            try {
                return printTable(m.group(1));
            } catch (NullPointerException e) {
                return "ERROR: File does not exist.";
            } catch (RuntimeException e) {
                return "ERROR: File does not exist.";
            }
        } else if ((m = SELECT_CMD.matcher(query)).matches()) {
            return select(m.group(1));
        } else {
            System.err.printf("Malformed query: %s\n", query);
        }
        return "";
    }

    private static final String REST = "\\s*(.*)\\s*",
            COMMA = "\\s*,\\s*",
            AND = "\\s+and\\s+";

    // Stage 1 syntax, contains the command name.
    private static final Pattern CREATE_CMD = Pattern.compile("create table " + REST),
            LOAD_CMD = Pattern.compile("load " + REST),
            STORE_CMD = Pattern.compile("store " + REST),
            DROP_CMD = Pattern.compile("drop table " + REST),
            INSERT_CMD = Pattern.compile("insert into " + REST),
            PRINT_CMD = Pattern.compile("print " + REST),
            SELECT_CMD = Pattern.compile("select " + REST);

    // Stage 2 syntax, contains the clauses of commands.
    private static final Pattern CREATE_NEW = Pattern.compile("(\\S+)\\s+\\(\\s*(\\S+\\s+\\S+\\s*" +
            "(?:,\\s*\\S+\\s+\\S+\\s*)*)\\)"),
            SELECT_CLS = Pattern.compile("([^,]+?(?:,[^,]+?)*)\\s+from\\s+" +
                    "(\\S+\\s*(?:,\\s*\\S+\\s*)*)(?:\\s+where\\s+" +
                    "([\\w\\s+\\-*/'<>=!.]+?(?:\\s+and\\s+" +
                    "[\\w\\s+\\-*/'<>=!.]+?)*))?"),
            CREATE_SEL = Pattern.compile("(\\S+)\\s+as select\\s+" +
                    SELECT_CLS.pattern()),
            INSERT_CLS = Pattern.compile("(\\S+)\\s+values\\s+(.+?" +
                    "\\s*(?:,\\s*.+?\\s*)*)");

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Expected a single query argument");
            return;
        }

        //eval(args[0]);
    }


    private static void createTable(String expr) {
        Matcher m;
        if ((m = CREATE_NEW.matcher(expr)).matches()) {
            createNewTable(m.group(1), m.group(2).split(COMMA));
        } else if ((m = CREATE_SEL.matcher(expr)).matches()) {
            createSelectedTable(m.group(1), m.group(2), m.group(3), m.group(4));
        } else {
            System.err.printf("Malformed create: %s\n", expr);
        }
    }

    private static void createNewTable(String name, String[] cols) {
        ArrayList<String> colTitles = new ArrayList<>();
        ArrayList<String> colTypes = new ArrayList<>();

        if (db.get(name) != null) {
            throw new RuntimeException();
        }

        if (cols == null) {
            throw new NullPointerException();
        }

        for (int i = 0; i < cols.length; i++) {
            String[] temp = cols[i].trim().split(" ");
            colTitles.add(temp[0]);
            colTypes.add(temp[1]);
        }

        Table newTable = new Table(name, colTitles.size());
        newTable.addColumns(colTitles);
        newTable.addColumnTypes(colTypes);

        db.put(name, newTable);
    }

    private static void createSelectedTable(String name, String exprs, String tables, String conds) {
        ArrayList<String> colTitles = new ArrayList<>();
        ArrayList<String> colTypes = new ArrayList<>();


        if (db.get(name) != null) {
            throw new RuntimeException();
        }

        if (cols == null) {
            throw new NullPointerException();
        }

        for (int i = 0; i < cols.length; i++) {
            String[] temp = cols[i].trim().split(" ");
            colTitles.add(temp[0]);
            colTypes.add(temp[1]);
        }

        Table newTable = new Table(name, colTitles.size());
        newTable.addColumns(colTitles);
        newTable.addColumnTypes(colTypes);

        db.put(name, newTable);
        System.out.printf("You are trying to create a table named %s by selecting these expressions:" +
                " '%s' from the join of these tables: '%s', filtered by these conditions: '%s'\n", name, exprs, tables, conds);
    }

    private static void loadTable(String name) throws IllegalArgumentException {
        ArrayList<String> colTitles = new ArrayList<>();
        ArrayList<String> colTypes = new ArrayList<>();
        ArrayList<Row> zeRows = new ArrayList<>();
        String[] daRows;

        In in = new In(name + ".tbl");

        String cols = in.readLine();
        String[] columnsandtypes = cols.trim().split(",");
        Pattern p = Pattern.compile("[^a-zA-Z]");

        for (String s : columnsandtypes) {
            String[] temp = s.trim().split(" ");
            Matcher m = p.matcher(temp[0]);
            if (m.find()) {
                throw new RuntimeException();
            }
            colTitles.add(temp[0]);
            colTypes.add(temp[1]);
        }

        Table newTable = new Table(name.split("\\.")[0], colTitles.size());
        newTable.addColumns(colTitles);
        newTable.addColumnTypes(colTypes);
        for (String s : colTypes) {
            if (!s.equals("int") && !s.equals("float") && !s.equals("string")) {
                throw new RuntimeException();
            }
        }

        while (!in.isEmpty()) {
            daRows = in.readLine().trim().split(",");
            if (daRows.length != newTable.numCols()) {
                throw new RuntimeException();
            }
            Row newRow = new Row(daRows);
            zeRows.add(newRow);
        }
        newTable.addRows(zeRows);

        db.put(name, newTable);


        //System.out.printf("You are trying to load the table named %s\n", name);
    }

    private static void storeTable(String name) throws FileNotFoundException, NullPointerException {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(name, "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Table table = db.get(name);
        for (int i = 0; i < table.numCols(); i += 1) {
            if (i == table.numCols() - 1) {
                writer.print(table.colTitles.get(i) + " " + table.colTypes.get(i));
            } else {
                writer.print(table.colTitles.get(i) + " " + table.colTypes.get(i) + ",");
            }
        }
        writer.println();

        for (int i = 0; i < table.numRows(); i += 1) {
            for (int j = 0; j < table.numCols(); j += 1) {
                Row daRow = (Row) table.rows.get(i);
                if (table.colTypes.get(j) == "String") {
                    if (j == table.numCols() - 1) {
                        writer.print("'" + daRow.get(j) + "'");
                    } else {
                        writer.print("'" + daRow.get(j) + "'" + ",");
                    }
                } else {
                    if (j == table.numCols() - 1) {
                        writer.print(daRow.get(j));
                    } else {
                        writer.print(daRow.get(j) + ",");
                    }
                }
            }
            writer.println();
        }
        writer.close();
        //System.out.printf("You are trying to store the table named %s\n", name);
    }

    private static void dropTable(String name) throws RuntimeException {
        Table dropped = db.remove(name);
        if (dropped == null) {
            throw new RuntimeException("ERROR: Table does not exist.");
        }
    }

    private static void insertRow(String expr) {
        Matcher m = INSERT_CLS.matcher(expr);
        if (!m.matches()) {
            System.err.printf("Malformed insert: %s\n", expr);
            return;
        }

        Table table = db.get(m.group(1));
        String[] daRow = m.group(2).split(",");
        if (table == null || daRow.length != table.numCols()) {
            throw new RuntimeException();
        }

        for (int i = 0; i < table.numCols(); i++) {
            daRow[i] = daRow[i].trim();
        }

        for (int i = 0; i < table.numCols(); i++) {
            if (table.colTypes.get(i).equals("int")) {
                try {
                    Integer.valueOf(daRow[i]);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException();
                }
            } else if (table.colTypes.get(i).equals("float")) {
                try {
                    Float.valueOf(daRow[i]);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException();
                }
            } else if (table.colTypes.get(i).equals("string")) {
                Pattern p = Pattern.compile("[^a-zA-Z]");
                Matcher check = p.matcher(daRow[i]);
                if (check.find()) {
                    throw new IllegalArgumentException();
                }
            }
        }
        Row newRow = new Row(daRow);
        table.addRow(newRow);

        //System.out.printf("You are trying to insert the row \"%s\" into the table %s\n", m.group(2), m.group(1));
    }

    private static String printTable(String name) throws NullPointerException {
        Table table = db.get(name);
        if (table == null) {
            throw new RuntimeException();
        }
        StringBuilder returnString = new StringBuilder();
        for (int i = 0; i < table.numCols(); i += 1) {
            if (i == table.numCols() - 1) {
                returnString.append(table.colTitles.get(i) + " " + table.colTypes.get(i));
            } else {
                returnString.append(table.colTitles.get(i) + " " + table.colTypes.get(i) + ",");
            }
        }
        returnString.append("\n");

        for (int i = 0; i < table.numRows(); i += 1) {
            for (int j = 0; j < table.numCols(); j += 1) {
                Row daRow = (Row) table.rows.get(i);
                if (table.colTypes.get(j) == "String") {
                    if (j == table.numCols() - 1) {
                        returnString.append("'" + daRow.get(j) + "'");
                    } else {
                        returnString.append("'" + daRow.get(j) + "'" + ",");
                    }
                } else {
                    if (j == table.numCols() - 1) {
                        returnString.append(daRow.get(j));
                    } else {
                        returnString.append(daRow.get(j) + ",");
                    }
                }
            }
            if (i == table.numRows() - 1) {
                break;
            } else {
                returnString.append("\n");
            }
        }
        return returnString.toString();
    }

    private static String select(String expr) {
        Matcher m = SELECT_CLS.matcher(expr);
        if (!m.matches()) {
            System.err.printf("Malformed select: %s\n", expr);
            return "";
        }

        return select(m.group(1), m.group(2), m.group(3));
    }

    private static String select(String exprs, String tables, String conds) {
        ArrayList<String> whichCols = new ArrayList<>();
        ArrayList<Table> whichTables = new ArrayList<>();
        ArrayList<String> whichConds = new ArrayList<>();

        String[] cols = exprs.split(",");
        for (String s : cols) {
            whichCols.add(s.trim());
        }

        String[] tabls = tables.split(",");
        for (String s : tabls) {
            whichTables.add(db.get(s.trim()));
        }

        if (conds != null) {
            String[] zeConds = conds.split(",");
            for (String s : zeConds) {
                whichConds.add(s.trim());
            }
        }

        ArrayList<String> realColTitles = new ArrayList<>();
        if (whichCols.get(0).equals("*")) {
            Table newTable = whichTables.get(0).join(whichTables.get(1));
            db.put(whichTables.get(0).tableName + " and " + whichTables.get(1).tableName, newTable);
            return printTable(whichTables.get(0).tableName + " and " + whichTables.get(1).tableName);
        }
        System.out.printf("You are trying to select these expressions:" +
                " '%s' from the join of these tables: '%s', filtered by these conditions: '%s'\n", exprs, tables, conds);
        return "";
    }

    /**public static class testParse {
    @Test public void testParse() throws FileNotFoundException {
    String[] expected = {"Patriots", "2015", "12", "4", "0"};
    Database test = new Database();
    test.loadTable("t1.tbl");
    String[] actual = test.db.get("t1").getRow(10);


    assertArrayEquals(expected, actual);
    }
    }*/
}

