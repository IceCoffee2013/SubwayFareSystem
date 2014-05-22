import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Langley on 5/21/14.
 * Manager
 */
public class Manager {

    private Map<Integer, Station> stationsMap = new HashMap<Integer, Station>();    //存储所有的车站
    private Map<Integer, Set<Integer>> graph = new HashMap<Integer, Set<Integer>>();//车站之间的连通性
    private GraphSearch sampleSearch;   //路径搜索类

    public Manager() {
        init();
    }

    private void init() {
        //初始车站
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("subway_data")));
            String nl;
            while (null != (nl = br.readLine())) {
                String[] strarray = nl.split(" ");
                if (strarray.length == 0) {
                    System.out.println("error, str is empty");
                    continue;
                }
                int stationID = Integer.parseInt(strarray[0]);
                Station station = new Station(stationID);
                for (int i = 1; i < strarray.length; i++) {
                    int neighbourID = Integer.parseInt(strarray[i]);
                    station.addNeighbour(neighbourID);
                }
                stationsMap.put(stationID, station);
            }
        } catch (Exception e) {
            System.err.println(e.getStackTrace());
        }

        //初始化Graph
        setGraph();
        this.sampleSearch = new GraphSearch(graph);

    }

    /**
     * start subway fare calculation system
     */
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("格式：L3-2,L1-3");
        while (true) {
            System.out.println("input:");
            String input = scanner.next();
            String[] strarray = input.split(",");
            if (strarray.length != 2) {
                System.out.println("输入格式错误，请重新输入!");
                continue;
            }
            Station start = getStationByString(strarray[0]);
            Station destination = getStationByString(strarray[1]);
            if (start == null || destination == null) {
                System.out.println("输入格式错误，请重新输入!");
                continue;
            }
            List<Integer> path = sampleSearch.breadthFirstSearch(start.getStationID(), destination.getStationID());
            int fare = calculateFare(path);

            // input result
            printResult(fare, path);
        }


    }

    private void setGraph() {
        for (Integer id : stationsMap.keySet()) {
            Set<Integer> neighbourSet = new HashSet<Integer>();
            neighbourSet.addAll(stationsMap.get(id).getNeighbourIDs());
            graph.put(id, neighbourSet);
        }

    }

    private Station getStationByString(String str) {
        if (str == null || str == "") {
            System.err.println("Str is null or empty");
            return null;
        }
        for (Station station : stationsMap.values()) {
            if (station.getName().equals(str)) {
                return station;
            }
        }
        return null;
    }

    private Station getStationByID(int stationID) {
        return stationsMap.get(stationID);
    }

    private int calculateFare(List<Integer> path) {
        if (path.size() <= 5) {
            return 2;
        }
        float fare = (float) (2 + 0.5 * (path.size() - 5));
        return new BigDecimal(fare).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
    }

    /**
     * input result
     *
     * @param fare 车费
     * @param path 路线
     */
    public void printResult(int fare, List<Integer> path) {
        System.out.print(fare + ":");
        for (int i = 0; i < path.size(); i++) {
            int stationID = path.get(i);
            System.out.print(getStationByID(stationID).getName());
            if (i + 1 != path.size()) {
                System.out.print(",");
            }
        }
        System.out.println();
    }

    private void debug() {
        System.out.println("debug " + stationsMap.size());
        for (Station s : stationsMap.values()) {
            System.out.print("stationID:" + s.getStationID());
            for (int nb : s.getNeighbourIDs()) {
                System.out.print(" " + nb);
            }
            System.out.println("");
        }

    }

    public void test() {
        List<Integer> rs = sampleSearch.breadthFirstSearch(302, 103);
        System.out.println("route size:" + rs.size());
        for (Integer i : rs) {
            System.out.print(" " + i);
        }
        System.out.println();
    }

}
