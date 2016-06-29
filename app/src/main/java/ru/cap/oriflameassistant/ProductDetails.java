package ru.cap.oriflameassistant;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ru.cap.oriflameassistant.Helpers.OriflameProductFactory;
import ru.cap.oriflameassistant.Model.InfoBox;
import ru.cap.oriflameassistant.Model.Product;

public class ProductDetails extends AppCompatActivity {

    private ImageView image;
    private TextView  title;
    private TextView description;
    private TextView price;
    private TextView codeOriflame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        image = (ImageView) findViewById(R.id.product_image);
        title = (TextView) findViewById(R.id.product_title);
        description = (TextView) findViewById(R.id.product_description);
        price = (TextView) findViewById(R.id.product_price);
        codeOriflame = (TextView) findViewById(R.id.product_code_oriflame);
        new LoadProduct().execute(getIntent().getIntExtra(Product.TABLE_NAME, 0));
    }







    private class LoadProduct extends AsyncTask<Integer, Void, InfoBox>{

        @Override
        protected InfoBox doInBackground(Integer... params) {
            return OriflameProductFactory.getProductBox(params[0]);
        }
        @Override
        protected void onPostExecute(InfoBox box) {
            if (box.isSuccess()){
                Product product = box.getData();
                Picasso.with(getApplicationContext())
                        .load(product.getImgPath()).
                        into(image);
                title.setText(product.getName());
                description.setText(product.getDescription());
                price.setText(String.valueOf(product.getPrice()));
                codeOriflame.setText(String.valueOf(product.getOriflameId()));
            }
        }
    }
}
