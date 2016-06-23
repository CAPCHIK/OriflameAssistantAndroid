package ru.cap.oriflameassistant.Model;

import java.util.List;

/**
 * Created by Maksim on 26.05.2016.
 */
public class Customer {
    public static final String TABLE_NAME = "Customers";
    private long id;


    private String name;
    private Order[] orders;
    private String mobileNumber;

    public Customer(){}

    public Customer(String name, String mobileNumber) {
        this.name = name;
        this.mobileNumber = mobileNumber;
    }


    public static String getTableName() {
        return TABLE_NAME;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Order[] getOrders() {
        return orders;
    }

    public void setOrders(Order[] orders) {
        this.orders = orders;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public static String forCreateTable() {
        return "CREATE TABLE " + TABLE_NAME + " (" +
                ColumnInfo.ID.getStringForSQL() +"," +
                ColumnInfo.NAME.getStringForSQL() + ',' +
                ColumnInfo.MOBILE_NUMBER.getStringForSQL() + ");";
    }
    public static  String forDropTable(){
        return "DROP TABLE IF EXIST " + TABLE_NAME+';';
    }

    public enum ColumnInfo {
        ID("_id", "INTEGER PRIMARY KEY AUTOINCREMENT", 0),
        NAME("Name", "TEXT", 1),
        MOBILE_NUMBER("mobile_number", "TEXT", 2);
        private String cName;
        private String type;
        private int index;

        ColumnInfo(String inName, String intype, int index){
            cName = inName;
            type = intype;
            this.index = index;
        }
        public String getStringForSQL(){
            return cName + " " + type;
        }

        public String getName(){
            return cName;
        }
        public String getType(){
            return type;
        }

        public int getIndex() {
            return index;
        }
    }
}

