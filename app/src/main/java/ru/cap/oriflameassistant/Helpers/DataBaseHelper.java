package ru.cap.oriflameassistant.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.List;

import ru.cap.oriflameassistant.Model.Customer;
import ru.cap.oriflameassistant.Model.Order;
import ru.cap.oriflameassistant.Model.Product;

/**
 * Created by Maksim on 26.05.2016.
 * Куча плохого кода, неэффективно, но как есть
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "general.db";
    private static final int SCHEMA = 1;


    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //TODO Транзикации, мб стоит убыстрить? Можно просто плюсовать строки, проверить.
        db.execSQL(Customer.forCreateTable());
        db.execSQL(Order.forCreateTable());
        db.execSQL(Product.forCreateTable());
        db.execSQL(OrderToProducts.forCreateTable());
        addEmptyModels(db);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Customer.forDropTable() +
                Order.forDropTable() +
                Product.forDropTable() +
                OrderToProducts.forDropTable());
        onCreate(db);
    }




    private void addEmptyModels(SQLiteDatabase db) {
        ContentValues vals = new ContentValues();
        vals.put(Product.ColumnInfo.PRICE.getName(), 0);
        vals.put(Product.ColumnInfo.NAME.getName(), "Неизвестный продукт");
        vals.put(Product.ColumnInfo.DESCRIPTION.getName(), "Описание продукта, длнное-длинное");
        vals.put(Product.ColumnInfo.ORIFLAME_ID.getName(), 0);
        db.insert(Product.TABLE_NAME, null, vals);
    }
    public void addCustomer(Customer customer) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO " + Customer.TABLE_NAME + " (" +
                Customer.ColumnInfo.NAME.getName() + ", " +
                Customer.ColumnInfo.MOBILE_NUMBER.getName() + ")" +
                " VALUES ('" + customer.getName() + "', '" + customer.getMobileNumber() + "')");
        db.close();
    }
    public void addOrder(Order order) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Order.ColumnInfo.CUSTOMER.getName(), order.getCustomerId());
        values.put(Order.ColumnInfo.DATE.getName(), order.getDate());
        order.setId(db.insert(Order.TABLE_NAME, null, values));
        values = new ContentValues();
        for (Product product: order.getProductList()){
            values.put(OrderToProducts.ColumnInfo.ORDER_ID.getName(), order.getId());
            values.put(OrderToProducts.ColumnInfo.PRODUCT_ID.getName(), product.getId());
            db.insert(OrderToProducts.TABLE_NAME, null, values);
        }
        db.close();
    }
    public void addProduct(Product product) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Product.ColumnInfo.NAME.getName(), product.getName());
        values.put(Product.ColumnInfo.DESCRIPTION.getName(), product.getDescription());
        values.put(Product.ColumnInfo.PRICE.getName(), product.getPrice());
        values.put(Product.ColumnInfo.ORIFLAME_ID.getName(), product.getOriflameId());
        values.put(Product.ColumnInfo.IMAGE_PATH.getName(), product.getImgPath());
        product.setId(db.insert(Product.TABLE_NAME, null, values));
        db.close();
    }


    public Customer[] getCustomersFromBase(boolean withOrders) {
        Customer[] customers;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Customer.TABLE_NAME, null);
        customers = new Customer[cursor.getCount()];
        int i = 0;
        while (cursor.moveToNext()) {
            Customer customer = new Customer();
            customer.setName(cursor.getString(Customer.ColumnInfo.NAME.getIndex()));
            customer.setId(cursor.getInt(Customer.ColumnInfo.ID.getIndex()));
            customer.setMobileNumber(cursor.getString(Customer.ColumnInfo.MOBILE_NUMBER.getIndex()));
            customers[i++] = customer;
        }
        cursor.close();
        return customers;
    }
    public String[] getCustomersNames()
    {
        String[] names;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Customer.TABLE_NAME,
                new String[]{Customer.ColumnInfo.NAME.getName()},
                null, null, null, null, null);
        names = new String[cursor.getCount()];
        int i = 0;
        while (cursor.moveToNext()) {
            names[i++] = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return names;
    }
    private Customer getCustomerWithId(long customerId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+Customer.TABLE_NAME + " WHERE " +
                Customer.ColumnInfo.ID.getName() + " = " + customerId, null);
        Customer customer = new Customer();
        if (cursor.moveToNext()){
            customer.setName(cursor.getString(Customer.ColumnInfo.NAME.getIndex()));
            customer.setMobileNumber(cursor.getString(Customer.ColumnInfo.MOBILE_NUMBER.getIndex()));
        }
        cursor.close();
        db.close();
        return customer;
    }
    public Customer getCustomerWithName(String customerName) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Customer.TABLE_NAME + " WHERE " + Customer.ColumnInfo.NAME.getName() + " = '" + customerName + '\'', null);
        Customer customer = null;
        if (cursor.moveToNext()){
            customer = new Customer();
            customer.setId(cursor.getLong(Customer.ColumnInfo.ID.getIndex()));
            customer.setMobileNumber(cursor.getString(Customer.ColumnInfo.MOBILE_NUMBER.getIndex()));
            customer.setName(customerName);
        }
        cursor.close();
        db.close();
        return  customer;
    }



    public void updateOrders(Customer customer) {
        customer.setOrders(getOrders(customer));
    }

    public Order[] getOrders(Customer customer) {
        Order[] orders;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Order.TABLE_NAME + " WHERE " + Order.ColumnInfo.CUSTOMER + " = " + customer.getId(), null);
        orders = new Order[cursor.getCount()];
        while (cursor.moveToNext()) {
            Order order = new Order();
            order.setId(cursor.getInt(Order.ColumnInfo.ID.getIndex()));
            order.setCustomerId(cursor.getInt(Order.ColumnInfo.CUSTOMER.getIndex()));
            order.setDate(cursor.getLong(Order.ColumnInfo.DATE.getIndex()));
            order.setCustomer(customer);
        }
        cursor.close();
        db.close();
        return orders;
    }

    public Order[] getOrders() {
        Order[] orders;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Order.TABLE_NAME, null);
        orders = new Order[cursor.getCount()];
        int i = cursor.getCount();
        while (cursor.moveToNext()) {
            Order order = new Order();
            order.setId(cursor.getLong(Order.ColumnInfo.ID.getIndex()));
            order.setCustomerId(cursor.getInt(Order.ColumnInfo.CUSTOMER.getIndex()));
            order.setProductList(getProductsForOrder(order.getId()));
            order.setCustomer(getCustomerWithId(order.getCustomerId()));
            order.setDate(cursor.getLong(Order.ColumnInfo.DATE.getIndex()));
            orders[--i] = order;
        }
        cursor.close();
        db.close();
        return orders;
    }

    private List<Product> getProductsForOrder(long id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + OrderToProducts.TABLE_NAME + " WHERE "
                + OrderToProducts.ColumnInfo.ORDER_ID.getName() + " = " + id, null);
        List<Product> products= new LinkedList<>();
        while (cursor.moveToNext()){
            products.add(getProductWithId(cursor.getLong(OrderToProducts.ColumnInfo.PRODUCT_ID.getIndex())));
        }
        cursor.close();
        db.close();
        return products;
    }

    private Product getProductWithId(long aLong) {
        SQLiteDatabase db = getReadableDatabase();
        Product product = null;
        Cursor cursor = db.rawQuery("SELECT * FROM " +Product.TABLE_NAME + " WHERE "+
                Product.ColumnInfo.ID.getName()+" = " + aLong, null);
        if (cursor.moveToNext()){
            product = Product.createFromCursor(cursor);
        }
        cursor.close();
        db.close();
        return product;
    }



    public Product[] getProducts() {
        Product[] products;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Product.TABLE_NAME, null);
        products = new Product[cursor.getCount()];
        int i = 0;
        while (cursor.moveToNext()) {
            products[i++] = Product.createFromCursor(cursor);
        }
        cursor.close();
        db.close();
        return products;
    }
    public Product getProductWithCode(int productCode) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Product.TABLE_NAME +" WHERE "+ Product.ColumnInfo.ORIFLAME_ID.getName() +" = "+productCode, null);
        Product product = null;
        if (cursor.moveToNext()){
            product = Product.createFromCursor(cursor);
        }
        cursor.close();
        db.close();
        return product;
    }



    public Order getOrder(long param){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Order.TABLE_NAME + " WHERE "+Order.ColumnInfo.ID.getName() + "=" + param, null);
        Order order = null;
        if (cursor.moveToNext()){
            order = new Order();
            order.fillFromCursor(cursor);
            Log.i("DataBaseHelper", "CustomerId = " + order.getCustomerId() + " Customer: " + new Gson().toJson(order.getCustomer()));
            order.setCustomer(getCustomerWithId(order.getCustomerId()));
            Log.i("DataBasseHelper", "Customer = " + new Gson().toJson(order.getCustomer()));
            order.setProductList(getProductsForOrder(order.getId()));
        }
        cursor.close();
        db.close();
        return order;
    }

    public void printOrders() {
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + Order.TABLE_NAME, null);
        while (cursor.moveToNext()){
            Log.i("Orders", String.format("id : %d date : %d customerId : %d",
                    cursor.getLong(Order.ColumnInfo.ID.getIndex()),
                    cursor.getLong(Order.ColumnInfo.DATE.getIndex()),
                    cursor.getLong(Order.ColumnInfo.CUSTOMER.getIndex())));
        }
        cursor.close();
    }
    public void printOrderToProduct(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+OrderToProducts.TABLE_NAME, null);
        while (cursor.moveToNext()){
            Log.i("ПАРЫ", String.format("id : %s orderId = %s prodId = %s",
                    cursor.getString(OrderToProducts.ColumnInfo.ID.getIndex()),
                    cursor.getString(OrderToProducts.ColumnInfo.ORDER_ID.getIndex()),
                    cursor.getString(OrderToProducts.ColumnInfo.PRODUCT_ID.getIndex())));
        }
        cursor.close();
        db.close();
    }

    public void removeProductFromOrder(long orderId, long productId) {
        getWritableDatabase().delete(OrderToProducts.TABLE_NAME,
                        OrderToProducts.ColumnInfo.ORDER_ID.getName() + "=" + orderId + " AND " +
                        OrderToProducts.ColumnInfo.PRODUCT_ID.getName() + "=" + productId, null );
    }
}
