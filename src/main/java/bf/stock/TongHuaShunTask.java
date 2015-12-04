package bf.stock;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2015/12/1.
 */
public class TongHuaShunTask {

    private static final int TIMES = 2;

    private static Logger logger = LoggerFactory.getLogger(TongHuaShunTask.class);


    public static void main(String[] args) throws Exception {

    }

    public static void claw(List<String> stocks, JdbcTemplate jdbcTemplate) {
        for (String id : stocks) {

            Integer count = jdbcTemplate.queryForObject("select count(id) from stock where id=? and claw_date=current_date and tsh_percent>0",
                    Integer.class, id);
            if (count == 1) continue;


            Document doc = getDocument(id, TIMES);
            if (doc == null) {
                logger.error("error id:{}", id);
                continue;
            }
            Elements ele = doc.select("#nav_basic.box2.indexStat div.box2wrap.basic_score div.result p span.gray");
            String html = ele.html();
            logger.debug(html);
            int percent = 0;
            try {
                html = html.substring(6).split("%")[0];
                percent = Integer.parseInt(html);
            } catch (Exception e) {
                logger.error("error html:{}", html);
            }

            logger.info("id:{},tsh_percent:{}", id, percent);
            jdbcTemplate.update("update stock set tsh_percent=? where id=? and claw_date=current_date", percent, id);
        }
    }

    private static Document getDocument(String stock, int times) {
        if (times == -1) return null;
        Document doc = null;
        try {
            if (stock.length() > 6) stock = stock.substring(2);
            String url = "http://doctor.10jqka.com.cn/" + stock;
            logger.debug(url);
            doc = Jsoup.connect(url).timeout(Integer.MAX_VALUE).get();
        } catch (IOException e) {
            if (times > 0) {
                return getDocument(stock, --times);
            }
        }
        return doc;
    }
}
