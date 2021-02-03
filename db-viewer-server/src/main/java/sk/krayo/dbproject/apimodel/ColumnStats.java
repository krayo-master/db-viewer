package sk.krayo.dbproject.apimodel;

import java.io.Serializable;

public class ColumnStats implements Serializable {

    private static final long serialVersionUID = 1L;

    private String min;
    private String max;
    private String median;
    private String avg;


    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getMedian() {
        return median;
    }

    public void setMedian(String median) {
        this.median = median;
    }

    public String getAvg() {
        return avg;
    }

    public void setAvg(String avg) {
        this.avg = avg;
    }
}
