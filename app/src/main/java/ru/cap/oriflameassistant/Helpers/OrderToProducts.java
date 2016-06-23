package ru.cap.oriflameassistant.Helpers;

import ru.cap.oriflameassistant.Model.Customer;

/**
 * Created by Maksim on 19.06.2016.
 */
public class OrderToProducts {

    public static final String TABLE_NAME= "order_to_products";


    private long _id;
    private long OrderId;
    private long ProductId;



    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public long getOrderId() {
        return OrderId;
    }

    public void setOrderId(long orderId) {
        OrderId = orderId;
    }

    public long getProductId() {
        return ProductId;
    }

    public void setProductId(long productId) {
        ProductId = productId;
    }
    public static String forCreateTable(){
        return "CREATE TABLE " + TABLE_NAME + "(" +
                ColumnInfo.ID.getStringForSQL() + ", "+
                ColumnInfo.ORDER_ID.getStringForSQL() + ", "+
                ColumnInfo.PRODUCT_ID.getStringForSQL() + ");";
    }

    public static  String forDropTable(){
        return "DROP TABLE IF EXIST " + TABLE_NAME+';';
    }

    public enum ColumnInfo {
        ID("_id", "INTEGER PRIMARY KEY AUTOINCREMENT", 0),
        ORDER_ID("order_id", "INTEGER", 1),
        PRODUCT_ID("product_id", "INTEGER", 2);
        private String cName;
        private String type;
        private int index;

        ColumnInfo(String Name, String type, int index){
            this.cName = Name;
            this.type = type;
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
