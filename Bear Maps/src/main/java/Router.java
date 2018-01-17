
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.HashMap;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    /**
     * Return a LinkedList of <code>Long</code>s representing the shortest path from st to dest,
     * where the longs are node IDs.
     */
    static PriorityQueue<NodeComparator> pq = new PriorityQueue<>();


    public static LinkedList<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                                double destlon, double destlat) {

        boolean solved = false;
        HashMap<Long, Double> seen = new HashMap<>();

        LinkedList<Long> path = new LinkedList<>();
        Node startTemp = g.vertices.get(g.closest(stlon, stlat));
        Node lastTemp = g.vertices.get(g.closest(destlon, destlat));

        NodeComparator start = new NodeComparator(0, g.distance(startTemp.getID(),
                lastTemp.getID()), startTemp);

//        Node answer = null;

        pq.add(start);

        while (!solved) {
            NodeComparator temp = pq.poll();
            for (long l : g.get(temp.id).getAdjacent()) {
                if (l == lastTemp.id) {
                    solved = true;
                    lastTemp.prev = temp;
                } else {
                    if (seen.containsKey(l)) {
                        double priority = seen.get(l);
                        double newPriority = g.distance(l, lastTemp.getID())
                                + temp.distanceTraveled + g.distance(l, temp.getID());
                        if (newPriority < priority) {
                            NodeComparator lNode = new NodeComparator(0.0, 0.0, g.get(l));
                            Long id = lNode.getID();
                            pq.remove(lNode);
                            NodeComparator lNodeComp = new NodeComparator(temp.distanceTraveled
                                    + g.distance(l, temp.getID()),
                                    g.distance(l, lastTemp.getID()), g.get(l));
                            lNodeComp.prev = temp;
                            pq.add(lNodeComp);
                        }
                    } else {
                        double p = temp.distanceTraveled + g.distance(l, temp.getID())
                                + g.distance(l, lastTemp.getID());
                        NodeComparator lNodeComp = new NodeComparator(temp.distanceTraveled
                                + g.distance(l, temp.getID()), g.distance(l,
                                lastTemp.getID()), g.get(l));
                        lNodeComp.prev = temp;
                        pq.add(lNodeComp);
                        seen.put(l, p);
                    }
                }
            }
        }
        Node cursor = lastTemp;
        while (cursor.id != startTemp.id) {
            path.addFirst(cursor.id);
            cursor = cursor.prev;
        }
        path.addFirst(startTemp.getID());
        seen.clear();
        pq.clear();
        return path;
    }
}
