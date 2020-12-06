package sk.krayo.interviewproject.apimodel;

import java.io.Serializable;

public class ForeignKey implements Serializable {

    private static final long serialVersionUID = 1L;


    private String foreignKeyColumnName;
    private String connectedTableColumnName;

    public String getForeignKeyColumnName() {
        return foreignKeyColumnName;
    }

    public void setForeignKeyColumnName(String foreignKeyColumnName) {
        this.foreignKeyColumnName = foreignKeyColumnName;
    }

    public String getConnectedTableColumnName() {
        return connectedTableColumnName;
    }

    public void setConnectedTableColumnName(String connectedTableColumnName) {
        this.connectedTableColumnName = connectedTableColumnName;
    }
}
