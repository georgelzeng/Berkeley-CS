/**
 * Created by danie on 2/22/2017.
 */
package Table;

import static org.junit.Assert.*;
import org.junit.Test;

public class TableTester {
    @Test
    public void tester() {
        Table powers = new Table(powers, 3);
        powers.addColumn("numbers");
        powers.addColumn("squares");
        powers.addColumn("cubes");
        Node one = new Node(1);
        Node[] first = new Node[]{one, one, one};
        Row row1 = new Row(first);
        Integer[] nums = {1, 2, 3, 4};
        Integer[] squares = {1, 4, 9, 16};
        Integer[] cubes = {1, 8, 27, 64};
        Integer[] newRow = {5, 25, 125};
        powers.addRow(newRow);
        Integer[] newRow2 = {6, 36, 216};
        powers.addRow(newRow2);
        Integer[] squares2 = {1, 4, 9, 16, 25, 36};

        assertEquals(squares2, powers.getColumn("squares"));
        assertEquals(true, powers.containsCol("cubes"));



    }
}
