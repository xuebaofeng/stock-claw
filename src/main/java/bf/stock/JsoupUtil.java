package bf.stock;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by Baofeng(Shawn) Xue on 12/4/15.
 */
public class JsoupUtil {
    public static final int TIMES = 1;
    private static Logger logger = LoggerFactory.getLogger(JsoupUtil.class);

    static Document getDocument(String stock, WebserviceType type, int times) {
        if (times == -1) return null;
        Document doc = null;
        String url = "";
        try {
            if (type.equals(WebserviceType.ICF))
                url = createIcfUrl(stock);
            else if (type.equals(WebserviceType.THS)) {
                url = createTshUrl(stock);
            } else {
                throw new RuntimeException("Wrong web service type:" + type);
            }
            doc = Jsoup.connect(url).timeout(60000).get();
        } catch (IOException e) {
            logger.error("message:{},url:\n{}", e.getMessage(), url);
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
        return getDocument(stock, type, TIMES);
    }

    private static String createIcfUrl(String stock) {
        return "http://www.icaifu.com/stock/doctora/" + stock + ".shtml";
    }

    public static void main(String[] args) {
        Document doc = getDocument("http://doctor.10jqka.com.cn/600285/", WebserviceType.THS);
        logger.debug(doc.html());
        Elements ele = doc.select("#nav_basic.box2.indexStat div.box2wrap.basic_score div.result p span.gray");
        String html = ele.html();
        logger.debug(html);
    }
}
