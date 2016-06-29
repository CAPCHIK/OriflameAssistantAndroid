package ru.cap.oriflameassistant;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import ru.cap.oriflameassistant.Adapters.ProductsAdapter;
import ru.cap.oriflameassistant.Helpers.DataBaseHelper;
import ru.cap.oriflameassistant.Model.Order;
import ru.cap.oriflameassistant.Model.Product;

public class OrderInfo extends AppCompatActivity {

    private static final String TAG = OrderInfo.class.getCanonicalName();

    private Order order;
    private ListView productsList;
    private TextView customerName;
    private TextView orderDate;
    private TextView orderPrice;
    private ProductsAdapter productsAdapter;
    private DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);
        productsList = (ListView) findViewById(R.id.products_list);
        customerName = (TextView) findViewById(R.id.customer_name);
        orderDate = (TextView) findViewById(R.id.order_date);
        orderPrice = (TextView) findViewById(R.id.order_price);

        productsAdapter = new ProductsAdapter(getApplicationContext(), new Product[]{});
        dbHelper = new DataBaseHelper(getApplicationContext());
        productsList.setAdapter(productsAdapter);

        long orderId = getIntent().getLongExtra(Order.ColumnInfo.ID.getName(), 1);
        setTitle("Заказ №" + orderId);
        new LoadOrder().execute(orderId);

        productsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {
                PopupMenu menu = new PopupMenu(getApplicationContext(), view);
                menu.inflate(R.menu.product_popup);
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.remove_product:
                                dbHelper.removeProductFromOrder(order.getId(), id);
                                productsAdapter.remove(position);
                                break;
                            case R.id.details_product:
                                Intent intent = new Intent(OrderInfo.this, ProductDetails.class);
                                intent.putExtra(Product.TABLE_NAME, productsAdapter.getProduct(position - productsList.getHeaderViewsCount()).getOriflameId());
                                startActivity(intent);
                                return true;
                        }
                        return false;
                    }
                });
                menu.show();
            }
        });
    }



    private class LoadOrder extends AsyncTask<Long, Void, Order>{
        @Override
        protected Order doInBackground(Long... params) {
            return dbHelper.getOrder(params[0]);
        }

        @Override
        protected void onPostExecute(Order order) {
            super.onPostExecute(order);
            OrderInfo.this.order = order;
            productsAdapter.addProducts(order.getProductList());
            customerName.setText(order.getCustomer().getName());
            orderPrice.setText(String.valueOf(order.getTotalPrice()));
            Locale local = new Locale("ru","RU");
            DateFormat df = DateFormat.getDateInstance(DateFormat.LONG , local);
            orderDate.setText(df.format(new Date(order.getDate())));
        }
    }
}
