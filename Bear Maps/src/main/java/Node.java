/**
 * Created by georgezeng on 4/18/17.
 */
import java.util.ArrayList;


public class Node {
    String name;
    long id;
    double x;
    double y;
    ArrayList<Long> adjacent;

    double priority = 1000;
    double distance = 1000;

    Node prev;
    public Node() {
        x = 0.0;
        y = 0.0;
        priority = 0;
        distance = 0;
    }
    public Node(long ID, double lon, double lat) {
        id = ID;
        x = lon;
        y = lat;
        adjacent = new ArrayList<>();
    }

    void addAdj(long adj) {
        adjacent.add(adj);
    }

    void remAdj(long adj) {
        adjacent.remove(adj);
    }

    int size() {
        return adjacent.size();
    }

    ArrayList<Long> getAdjacent() {
        return adjacent;
    }

    double pointDistance(double lon, double lat) {
        return Math.sqrt(Math.pow(x - lon, 2) +
                Math.pow(y - lat, 2));
    }

    void addName(String shit) {
        name = shit;
    }

    void updatePriority(double dist, Node p, Node d) {
        if (dist < distance) {
            distance = dist;
            priority = dist + pointDistance(d.x, d.y);
            prev = p;
        }
    }

    void updatePrev(Node p) {
        prev = p;
    }

    long getID() {
        return id;
    }


}
