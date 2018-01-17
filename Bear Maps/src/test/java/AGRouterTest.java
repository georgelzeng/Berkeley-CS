import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

public class AGRouterTest extends AGMapTest {
    /**
     * Test the route-finding functionality by comparing the node id list item by item.
     *
     * @throws Exception
     */
    @Test
    public void testShortestPath() throws Exception {
        for (TestParameters p : params) {
            LinkedList<Long> studentRouteResult = Router.shortestPath(graph,
                    p.routeParams.get("start_lon"), p.routeParams.get("start_lat"),
                    p.routeParams.get("end_lon"), p.routeParams.get("end_lat"));
            assertEquals("Found route differs for input: " + p.routeParams + ".\n",
                    p.routeResult, studentRouteResult);
        }
    }

    @Test
    public void testEight() throws Exception{
      LinkedList<Long> studentRouteResult = Router.shortestPath(graph, -122.26143030962002, 37.831051606162895, -122.2650498760774, 37.83796678748061);
      for (int i = 0; i < studentRouteResult.size(); i++) {
          System.out.print(studentRouteResult.get(i) + ", ");
      }
    }
}
