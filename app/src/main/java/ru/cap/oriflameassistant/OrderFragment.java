package ru.cap.oriflameassistant;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.cap.oriflameassistant.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment {
    public OrderFragment(){}
    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(getArguments().getInt("id"), container, false);
        TextView textView1 = (TextView) rootView.findViewById(R.id.price);
        textView1.setText(getArguments().getString("first"));
        TextView textView2 = (TextView) rootView.findViewById(R.id.customer);
        textView2.setText(getArguments().getString("second"));
        return rootView;
    }

    public static OrderFragment newInstance(String first, String second, int id) {
        Bundle args = new Bundle();
        args.putString("first", first);
        args.putString("second", second);
        args.putInt("id", id);
        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
