/**
 * Created by danie on 2/22/2017.
 */
package db;

import static org.junit.Assert.*;

import db.Node;
import db.Row;
import db.Table;
import org.junit.Test;

import java.util.ArrayList;

public class TableTester {
    @Test
    public void tester() {
        Table powers = new Table("powers", 3);
        powers.addColumn("integers");
        powers.addColumn("strings");
        powers.addColumn("floats");
        Node one = new Node(1);
        Node one2 = new Node("one");
        Node one3 = new Node(1.0);
        Node two = new Node(2);
        Node two2 = new Node("two");
        Node two3 = new Node(2.0);
        Node three = new Node(3);
        Node three2 = new Node("three");
        Node three3 = new Node(3.0);
        String[] first = new String[]{"one", "one2", "one3"};
        String[] second = new String[]{"two", "two2", "two3"};
        String[] third = new String[]{"three", "three2", "three3"};

        Row row1 = new Row(first);
        Row row2 = new Row(second);
        Row row3 = new Row(third);
        powers.addRow(row1);
        powers.addRow(row2);
        powers.addRow(row3);

        ArrayList<String> strs = new ArrayList<String>();
        strs.add("one");
        strs.add("two");
        strs.add("three");


        assertEquals(third, powers.getRow(2));
        assertEquals(strs, powers.getColumn("strings"));


    }
}
