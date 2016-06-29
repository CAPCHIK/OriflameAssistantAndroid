package ru.cap.oriflameassistant;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import ru.cap.oriflameassistant.Adapters.ProductsAdapter;
import ru.cap.oriflameassistant.Helpers.DataBaseHelper;
import ru.cap.oriflameassistant.Helpers.OriflameProductFactory;
import ru.cap.oriflameassistant.Model.Customer;
import ru.cap.oriflameassistant.Model.InfoBox;
import ru.cap.oriflameassistant.Model.Order;
import ru.cap.oriflameassistant.Model.Product;

public class NewOrderActivity extends AppCompatActivity {

    private DataBaseHelper DB;
    private Button addProductButton;
    private Button submitOrderButton;
    private ImageButton newCustomerButton;
    private ProgressBar productInLoading;
    private EditText forCode;
    private ProductsAdapter productsAdapter;
    private AutoCompleteTextView customerChooser;
    ListView productsListView;

    @Override
    protected void onResume() {
        super.onResume();
        customerChooser.setAdapter(new ArrayAdapter<>(getApplicationContext(),
                R.layout.fragment_customer_item, R.id.CustomerName, DB.getCustomersNames()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        setTitle("Добавление нового заказа");
        productsListView = (ListView) findViewById(R.id.products_list);
        productsListView.addHeaderView(getLayoutInflater().inflate(R.layout.header_for_new_order, productsListView, false));
        productsListView.addFooterView(getLayoutInflater().inflate(R.layout.footer_for_new_order, productsListView, false));

        productInLoading = ((ProgressBar) findViewById(R.id.product_in_loading));
        forCode = ((EditText) findViewById(R.id.product_code_edit));
        addProductButton = ((Button) findViewById(R.id.add_product_button));
        newCustomerButton = (ImageButton) findViewById(R.id.add_customer_button);
        customerChooser = (AutoCompleteTextView) findViewById(R.id.customer_choice);
        submitOrderButton = (Button) findViewById(R.id.submit_order);

        DB = new DataBaseHelper(getApplicationContext());

        productsAdapter = new ProductsAdapter(this, new Product[]{});
        productsListView.setAdapter(productsAdapter);

        addProductButton.setOnClickListener(new AddProductListener());

        newCustomerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewOrderActivity.this, NewCustomer.class);
                startActivityForResult(intent, 0);
            }
        });

        submitOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String customerName = customerChooser.getText().toString();
                if (customerName.length() == 0) {
                    showToast("Введите имя заказчика");
                    return;
                }
                Customer checkedCustomer = DB.getCustomerWithName(customerName);
                if (checkedCustomer == null){
                    showToast("Не существует заказчика '" + customerName + '\'');
                    return;
                }
                Order order = new Order();
                order.setCustomer(checkedCustomer);
                order.setDate(System.currentTimeMillis());
                order.setProductList(productsAdapter.getProducts());
                DB.addOrder(order);
                finish();
            }
        });

        productsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
                popupMenu.inflate(R.menu.product_popup);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.remove_product:
                                productsAdapter.remove(position - productsListView.getHeaderViewsCount());
                                return true;
                            case R.id.details_product:
                                Intent intent = new Intent(NewOrderActivity.this, ProductDetails.class);
                                intent.putExtra(Product.TABLE_NAME, productsAdapter.getProduct(position - productsListView.getHeaderViewsCount()).getOriflameId());
                                startActivity(intent);
                                return true;
                            default:
                                showToast("Неизвестный пункт меню");
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });
    }
    private void showToast(String text){
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null)
            customerChooser.setText(data.getStringExtra(Customer.ColumnInfo.NAME.getName()));
    }

    private class AddProductListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if (productInLoading.getVisibility() == View.GONE) {
                String codeAsString = forCode.getText().toString();
                int code;
                try {
                    code = Integer.parseInt(codeAsString);
                } catch (NumberFormatException ex){
                    Toast.makeText(getApplicationContext(), "Введите артикул", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                new GetProduct().execute(code);
            }
        }
        private class GetProduct extends AsyncTask<Integer, Void, InfoBox>{
            @Override
            protected void onPreExecute() {
                productInLoading.setVisibility(ProgressBar.VISIBLE);
            }

            @Override
            protected InfoBox doInBackground(Integer... params) {
                return OriflameProductFactory.getProductBox(params[0]);
            }
            @Override
            protected void onPostExecute(InfoBox fromSite) {
                productInLoading.setVisibility(ProgressBar.GONE);
                if (!fromSite.isSuccess()){
                    Toast.makeText(getApplicationContext(), fromSite.getMessage(), Toast.LENGTH_LONG)
                            .show();
                } else {
                    Log.i("Продукт получен!!))", new Gson()
                            .toJson(fromSite));
                    productsAdapter.addProduct(fromSite.getData());
                }
            }

        }
    }
}