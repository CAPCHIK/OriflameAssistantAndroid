package ru.cap.oriflameassistant;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.cap.oriflameassistant.Adapters.ProductsAdapter;
import ru.cap.oriflameassistant.Helpers.DataBaseHelper;
import ru.cap.oriflameassistant.Model.Product;

public class ProductList extends AppCompatActivity {
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        listView = (ListView) findViewById(R.id.productsListContainer);
        new ProductsLoader().execute();
    }

    private class ProductsLoader extends AsyncTask<Void, Void, Product[]> {
        @Override
        protected Product[] doInBackground(Void... params) {
            Gson gson = new Gson();
            return gson.fromJson(getAllJsonProducts(), Product[].class);
        }
        private String getAllJsonProducts(){
            return requestGET("http://oriflameapi.azurewebsites.net/api/getallproducts");
        }
        private String getJsonProduct(int code) {
            return requestGET("http://oriflameapi.azurewebsites.net/api/getinfo/" + code);
        }

        private String requestGET(String path){
            URL url;
            HttpURLConnection conn;
            BufferedReader rd;
            String line;
            String result = "";
            try {
                url = new URL(path);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = rd.readLine()) != null) {
                    result += line;
                }
                rd.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Product[] products) {
            listView.setAdapter(new ProductsAdapter(ProductList.this, products));
            for (Product product : products) {
                Log.i("Продукты ", product.getName());
            }
        }
    }
}
