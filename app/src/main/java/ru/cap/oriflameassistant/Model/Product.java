package ru.cap.oriflameassistant.Model;

import android.database.Cursor;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maksim on 26.05.2016.
 */
public class Product {

    public static final String TABLE_NAME = "products";
    transient private long id;
    @SerializedName("id")
    private int oriflameId;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    private String price;
    @SerializedName("image")
    private String imgPath;



    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getOriflameId() {
        return oriflameId;
    }

    public void setOriflameId(int oriflameId) {
        this.oriflameId = oriflameId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return Float.parseFloat(price.replace(',','.'));
    }

    public void setPrice(double price) {
        this.price = String.valueOf(price);
    }

    public static String forCreateTable() {
        return "CREATE TABLE " + TABLE_NAME + " (" +
                ColumnInfo.ID.getStringForSql() + ','+
                ColumnInfo.ORIFLAME_ID.getStringForSql()+','+
                ColumnInfo.NAME.getStringForSql()+','+
                ColumnInfo.DESCRIPTION.getStringForSql()+','+
                ColumnInfo.PRICE.getStringForSql()+','+
                ColumnInfo.IMAGE_PATH.getStringForSql()+");";
    }

    public static Product createFromCursor(Cursor cursor) {
        Product product = new Product();
        product.setId(cursor.getInt(ColumnInfo.ID.getIndex()));
        product.setOriflameId(cursor.getInt(ColumnInfo.ORIFLAME_ID.getIndex()));
        product.setName(cursor.getString(ColumnInfo.NAME.getIndex()));
        product.setDescription(cursor.getString(ColumnInfo.DESCRIPTION.getIndex()));
        product.setPrice(cursor.getDouble(ColumnInfo.PRICE.getIndex()));
        product.setImgPath(cursor.getString(ColumnInfo.IMAGE_PATH.getIndex()));
        return product;
    }

    public static  String forDropTable(){
        return "DROP TABLE IF EXIST " + TABLE_NAME+';';
    }

    public enum ColumnInfo{
        ID("_id", "INTEGER PRIMARY KEY AUTOINCREMENT", 0),
        ORIFLAME_ID("oriflame_id", "TEXT", 1),
        NAME("name", "TEXT", 2),
        DESCRIPTION("description", "TEXT", 3),
        PRICE("price", "REAL", 4),
        IMAGE_PATH("imq_path", "TEXT", 5);
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
