package bf.stock;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by Baofeng(Shawn) Xue on 12/4/15.
 */
public class JsoupUtil {
    private static Logger logger = LoggerFactory.getLogger(JsoupUtil.class);

    static Document getDocument(String stock, WebserviceType type, int times) {
        if (times == -1) return null;
        Document doc = null;
        try {
            String icfUrl;
            if (type.equals(WebserviceType.ICF))
                icfUrl = createIcfUrl(stock);
            else if (type.equals(WebserviceType.THS)) {
                icfUrl = createTshUrl(stock);
            } else {
                throw new RuntimeException("Wrong web service type:" + type);
            }
            doc = Jsoup.connect(icfUrl).timeout(10000).get();
        } catch (IOException e) {
            logger.error(e.getMessage());
            if (times > 0) {
                return getDocument(stock, type, --times);
            }
        }
        return doc;
    }

    private static String createTshUrl(String stock) {
        if (stock.length() > 6) stock = stock.substring(2);
        return "http://doctor.10jqka.com.cn/" + stock;
    }


    static Document getDocument(String stock, WebserviceType type) {
        return getDocument(stock, type, 3);
    }

    private static String createIcfUrl(String stock) {
        return "http://www.icaifu.com/stock/doctora/" + stock + ".shtml";
    }
}
