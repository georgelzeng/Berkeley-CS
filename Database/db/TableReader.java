package db;

import db.Node;
import edu.princeton.cs.introcs.In;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.lang.Character;

/**
 * Created by georgelzeng on 2/28/2017.
 */
public class TableReader {
    ArrayList<String> colTitles = new ArrayList<>();
    ArrayList<String> colTypes = new ArrayList<>();
    ArrayList<String> row;
    In in;
    Table table;

    private static final String REST = "\\s*(.*)\\s*",
            COMMA = "\\s*,\\s*",
            AND = "\\s+and\\s+";

    //needs to set table name and numCols
    //will parse each row and given the type, will read the specified type
    public TableReader(String tblName) {
        in = new In(tblName);
    }

    public static ArrayList<String> makeColumns(String name) {
        ArrayList<String> Titles = new ArrayList<>();
        ArrayList<String> Types = new ArrayList<>();
        In in = new In(name);

        String cols = in.readLine();
        String[] columnsandtypes = cols.trim().split(",");
        for (String s : columnsandtypes) {
            String[] temp = s.trim().split(" ");
            Titles.add(temp[0]);
            Types.add(temp[1]);
        }
        return Titles;
    }

    public static char[] afterComma(char[] Line) {
        int i = 0;
        while (Line[i] == ',') {
            i += 1;
        }
        while (Line[i] != ',') {
            i += 1;
        }
        char[] newLine = new char[Line.length - (i + 1)];
        System.arraycopy(Line, i + 1, newLine, 0, Line.length - (i + 1));
        return newLine;

    }

    public static String readToSpace(char[] chars) {
        String string = "";
        int i = 0;
        while (chars[i] == ' ') {
            i += 1;
        }
        while (chars[i] != ' ' || i >= chars.length) {
            if (chars[i] == ',') {
                i += 1;
            } else {
                string = string + chars[i];
                i += 1;
            }
        }
        return string;
    }

    public static String readToComma(char[] chars) {
        String string = "";
        int i = 0;
        while (chars[i] == ' ') {
            i += 1;
        }
        while (chars[i] != ',') {
            string = string + chars[i];
            i += 1;

        }
        return string;
    }

    public void getColumns() {

        return;
    }

    public void getNextRow() {
        return;
    }


    public static class testReader {
        @Test
        public void testRead() {
            String[] expected = {"TeamName", "Season", "Wins", "Losses", "Ties"};

            ArrayList<String> actual = makeColumns("records.tbl");

            assertEquals(expected, actual);
        }
    }
}
