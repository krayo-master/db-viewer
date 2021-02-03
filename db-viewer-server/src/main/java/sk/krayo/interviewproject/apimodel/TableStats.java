package sk.krayo.interviewproject.apimodel;

import java.io.Serializable;

public class TableStats implements Serializable {

    private static final long serialVersionUID = 1L;

    private int numberOfRecords;
    private int numberOfAttributes;

    public int getNumberOfRecords() {
        return numberOfRecords;
    }

    public void setNumberOfRecords(int numberOfRecords) {
        this.numberOfRecords = numberOfRecords;
    }

    public int getNumberOfAttributes() {
        return numberOfAttributes;
    }

    public void setNumberOfAttributes(int numberOfAttributes) {
        this.numberOfAttributes = numberOfAttributes;
    }
}
