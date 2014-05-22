import java.util.ArrayList;
import java.util.List;

/**
 * Created by Langley on 5/21/14.
 */
public class Station {

    private int lineID;          //线路号码
    private int lineIndex;       //线路中的车站号
    private int stationID;       //各个车站唯一的标记
    private boolean isExchangeStation = false;  //是否是换乘车站
    private List<Integer> neighbourIDs;  //存储相邻车站的ID

    public Station(int stationID) {
        this.stationID = stationID;
        this.neighbourIDs = new ArrayList<Integer>();
        init();
    }

    private void init() {
        int temp = stationID / 100;
        if (temp == 0) {
            isExchangeStation = true;
            lineID = -1;
        } else {
            lineID = temp;
        }
        lineIndex = stationID % 100;
    }

    public int getLineID() {
        return lineID;
    }

    public void setLineID(int lineID) {
        this.lineID = lineID;
    }

    /**
     * 唯一标记
     *
     * @return
     */
    public int getStationID() {
        return stationID;
    }

    public boolean isExchangeStation() {
        return isExchangeStation;
    }

    /**
     * 加入可到达的车站
     *
     * @param stationID
     */
    public void addNeighbour(int stationID) {
        this.neighbourIDs.add(stationID);
    }

    public List<Integer> getNeighbourIDs() {
        return neighbourIDs;
    }

    public String getName() {
        if (isExchangeStation) {
            return "X" + stationID;
        }
        return "L" + lineID + "-" + lineIndex;
    }

}
