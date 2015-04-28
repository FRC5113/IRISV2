package tools;

/**
 * Created by Jake on 4/26/2015.
 * Keeps track of the last several seconds of a value for use on charts and such
 */
public class HistoryArray {

    private double[] hist;
    private int lastPos = 0;

    public HistoryArray(int size) {
        hist = new double[size];
    }

    public void addHistory(double val) {
        hist[lastPos] = val;
        lastPos++;
        lastPos = lastPos % hist.length;
    }

    public double getValueAtTicksAgo(int ticksAgo) {
        ticksAgo = 1 + ticksAgo % hist.length;
        return hist[(lastPos - ticksAgo + hist.length) % hist.length];
    }

}
