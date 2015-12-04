package bf.stock;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Stocks {

    static Logger logger = LoggerFactory.getLogger(Stocks.class);

    public static void main(String[] args) {

        logger.debug("list size:{}", getAll().size());
    }

    public static List<String> getAll() {
        List<String> l = new ArrayList<>();

        String url = "http://ctxalgo.com/api/stocks";
        String json = null;
        try {
            json = Jsoup.connect(url).ignoreContentType(true).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (json == null) return l;

        json = json.split("\\{")[1].split("\\}")[0];
        String[] split = json.split(",");
        for (String s : split) {
            s = s.split(":")[0].split("\"")[1].split("\"")[0];
            l.add(s);
        }
        return l;
    }


}
