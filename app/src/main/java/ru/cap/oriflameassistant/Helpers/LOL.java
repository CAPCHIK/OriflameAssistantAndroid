package ru.cap.oriflameassistant.Helpers;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import ru.cap.oriflameassistant.Model.InfoBox;
import ru.cap.oriflameassistant.Model.Product;

/**
 * Created by Maksim on 27.05.2016.
 */
public class LOL {
    private static String fromSite = "{\"Success\":true,\"Message\":\"OK\",\"Data\":{\"id\":30460,\"name\":\"Объемная тушь для ресниц The ONE Volume Blast\",\"price\":\"199,00\",\"description\":\"Кремовая формула с тремя видами воска* и уникальная щеточка выделяют каждую ресничку, равномерно распределяя тушь по всей длине, и заметно увеличивают объем. Увеличивает объем ресниц в 24 раза** • Идеально разделяет ресницы*** • Не осыпается, не размазывается и не оставляет комочков*** • Легко смывается и подходит для тех, кто носит контактные линзы*** • Офтальмологически протестирована. * Карнаубский воск, воск подсолнечника и воск рисовых отрубей. **По результатам клинических исследований в сравнении с ресницами без макияжа. **По результатам потребительских и клинических тестирований.\",\"image\":\"http://ru.oriflame.com//media-cis.oriflame.com/-/media/Images/Catalog/Products/30460.ashx?u=0101010000\"}}";
    public static void main(String[] args) throws Exception {
        String json = fromSite;
        Gson gson = new Gson();
        System.out.println(json);
        InfoBox productFS = gson.fromJson(json, InfoBox.class);
        System.out.println(productFS.getData().getPrice());
        System.out.println(gson.toJson(productFS));
    }


    private class FromServer{
        boolean Success;
        String Message;
        Product Source;
    }

    private static String GET(String path)throws Exception{
        URL url = new URL(path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String res = "", line = "";
        while ((line = reader.readLine()) != null)
            res += line;
        return res;
    }
}

