package ru.cap.oriflameassistant.Model;

import android.database.Cursor;

import java.util.Collection;
import java.util.List;

/**
 * Created by Maksim on 26.05.2016.
 */
public class Order {
    public static final String TABLE_NAME = "orders";
    private long id;
    private List<Product> productList;
    private long Date;

    private long customerId;
    private Customer customer;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public long getDate() {
        return Date;
    }

    public void setDate(long date) {
        Date = date;
    }

    public long getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        customerId = customer.getId();
    }


    public static String forCreateTable() {
        return "CREATE TABLE " + TABLE_NAME + " (" +
                ColumnInfo.ID.getStringForSql()+ " , " +
                ColumnInfo.CUSTOMER.getStringForSql() + " , " +
                ColumnInfo.DATE.getStringForSql() + ");";
    }
    public static  String forDropTable(){
        return "DROP TABLE IF EXIST " + TABLE_NAME+';';
    }

    public double getTotalPrice() {
        double totalPrice = 0;
        for (Product product : productList)
            totalPrice += product.getPrice();
        return totalPrice;
    }

    public void fillFromCursor(Cursor cursor) {
        setId(cursor.getLong(Order.ColumnInfo.ID.getIndex()));
        setCustomerId(cursor.getInt(Order.ColumnInfo.CUSTOMER.getIndex()));
        setDate(cursor.getLong(Order.ColumnInfo.DATE.getIndex()));
    }

    public enum ColumnInfo{
        //TODO Внешний ключ!!
        ID("_id", "INTEGER PRIMARY KEY AUTOINCREMENT", 0),
        CUSTOMER("Customer", "INTEGER", 1),
        DATE("Date", "INTEGER", 2);


        private String customerName, type;
        private int index;

        ColumnInfo(String cusName, String type, int index) {
            this.customerName = cusName;
            this.type = type;
            this.index = index;
        }

        public String getName() {
            return customerName;
        }

        public String getType() {
            return type;
        }

        public String getStringForSql(){
            return customerName + ' ' + type;
        }

        public int getIndex() {
            return index;
        }
    }

}
