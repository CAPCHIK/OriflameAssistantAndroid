package ru.cap.oriflameassistant;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ru.cap.oriflameassistant.Model.Product;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductItem extends Fragment {

    private ImageView pruductImage;
    private TextView productName;
    private TextView productPrice;

    public ProductItem() {
    }



    public static ProductItem getInstance(Product product){
        ProductItem productItemFragment = new ProductItem();
        Bundle args = new Bundle();
        args.putString("name", product.getName());
        args.putDouble("price", product.getPrice());
        args.putString("imgPath", product.getImgPath());
        productItemFragment.setArguments(args);
        return productItemFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_product_item, container, false);
        productName = ((TextView) root.findViewById(R.id.productTitle));
        productPrice = ((TextView) root.findViewById(R.id.productPrice));
        pruductImage = ((ImageView) root.findViewById(R.id.productImage));
        Bundle args = getArguments();
        productName.setText(args.getString("name"));
        productPrice.setText(String.valueOf(args.getDouble("price")));
        Picasso.with(inflater.getContext())
                .load(args.getString("imgPath"))
                .into(pruductImage);
        return root;
    }

    public void fillView(View view) {
        ((TextView) view.findViewById(R.id.productTitle)).setText(getArguments().getString("name"));
        ((TextView) view.findViewById(R.id.productPrice)).setText(String.valueOf(getArguments().getDouble("price")));
        Picasso.with(getContext())
                .load(getArguments().getString("imgPath"))
                .into(((ImageView) view.findViewById(R.id.productImage)));
    }
}
