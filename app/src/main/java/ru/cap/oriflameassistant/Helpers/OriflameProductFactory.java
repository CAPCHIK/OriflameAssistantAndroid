package ru.cap.oriflameassistant.Helpers;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.cap.oriflameassistant.Model.InfoBox;
import ru.cap.oriflameassistant.Model.Product;

/**
 * Created by Maksim on 20.06.2016.
 */
public class OriflameProductFactory {
    private static DataBaseHelper helper;
    private static Gson gson = new Gson();
    public static void init(DataBaseHelper helper){
        OriflameProductFactory.helper = helper;
    }
    public static InfoBox getProduct(int productCode){
        Product product = helper.getProductWithCode(productCode);
        InfoBox box;
        if (product == null){
            box = getFromSite(productCode);
        } else {
            return new InfoBox(true, "OK", product);
        }
        if (box.isSuccess())
            helper.addProduct(box.getData());
        return box;
    }

    private static InfoBox getFromSite(int productCode){
        try{
            String json = get("http://oriflameapi.azurewebsites.net/api/getinfo/" + productCode);
            InfoBox infoBox = gson.fromJson(json, InfoBox.class);
            return infoBox;
        } catch (Exception ex){
            return new InfoBox(false, "Ошибка выполнения запроса", null);
        }
    }

    private static String get(String path){
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
}
