import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.ArrayList;
import java.util.HashSet;

import java.util.HashMap;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */

    HashMap<Long, Node> vertices = new HashMap<>();

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputFile, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        HashSet<Long> temp = new HashSet<>();
        for (long key : vertices.keySet()) {
            if (vertices.get(key).size() == 0) {
                temp.add(key);
            }
        }
        for (long l : temp) {
            vertices.remove(l);
        }

    }

    /** Returns an iterable of all vertex IDs in the graph. */
    Iterable<Long> vertices() {
        ArrayList<Long> vert = new ArrayList<>();
        for (long key : vertices.keySet()) {
            vert.add(key);
        }
        return vert;
    }

    /** Returns ids of all vertices adjacent to v. */
    Iterable<Long> adjacent(long v) {
        return vertices.get(v).getAdjacent();
    }

    /** Returns the Euclidean distance between vertices v and w, where Euclidean distance
     *  is defined as sqrt( (lonV - lonV)^2 + (latV - latV)^2 ). */
    double distance(long v, long w) {
        return Math.sqrt(Math.pow((vertices.get(v).x - vertices.get(w).x), 2)
                + Math.pow((vertices.get(v).y - vertices.get(w).y), 2));
    }

    /** Returns the vertex id closest to the given longitude and latitude. */
    long closest(double lon, double lat) {
        double minDistance = 420;
        long node = 696969;
        for (long key : vertices.keySet()) {
            double temp = vertices.get(key).pointDistance(lon, lat);
            if (temp < minDistance) {
                minDistance = temp;
                node = key;
            }
        }
        return node;
    }

    /** Longitude of vertex v. */
    double lon(long v) {
        return vertices.get(v).x;
    }

    /** Latitude of vertex v. */
    double lat(long v) {
        return vertices.get(v).y;
    }

    void addNode(long name, double x, double y) {
        vertices.put(name, new Node(name, x, y));
    }

    void addNode(long name, Node n) {
        vertices.put(name, n);
    }

    void removeNode(long v) {
        ArrayList<Long> adj = vertices.get(v).getAdjacent();
        for (int i = 0; i < adj.size(); i++) {
            vertices.get(adj.get(i)).remAdj(v);
        }
        vertices.remove(v);
    }

    void addEdge(long a, long b) {
        vertices.get(a).addAdj(b);
        vertices.get(b).addAdj(a);
    }

    Node get(long l) {
        return vertices.get(l);
    }
}
