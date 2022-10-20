package model;

public class Table {
String table,date,etat;
int total;

    public Table(String table, String date, String etat, int total) {
        this.table = table;
        this.date = date;
        this.etat = etat;
        this.total = total;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
