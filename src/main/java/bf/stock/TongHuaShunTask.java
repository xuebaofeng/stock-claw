package bf.stock;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/12/1.
 */
@Component
public class TongHuaShunTask extends WebServiceTask {


    private static Logger logger = LoggerFactory.getLogger(TongHuaShunTask.class);

    public static void main(String[] args) {
    }

    public void claw(String id) {

        Document doc = JsoupUtil.getDocument(id, WebserviceType.THS);
        if (doc == null) {
            addErrorCount(id, WebserviceType.THS);
            return;
        }
        Elements ele = doc.select("#nav_basic.box2.indexStat div.box2wrap.basic_score div.result p span.gray");
        String html = ele.html();
        if (html.length() == 0) return;
        int percent = 0;
        try {
            html = html.substring(6).split("%")[0];
            percent = Integer.parseInt(html);
        } catch (Exception e) {
            logger.error("error html:{},e:{}", html, e.getMessage());
        }

        logger.info("id:{},ths_percent:{}", id, percent);
        if (percent == 0) {
            addErrorCount(id, WebserviceType.THS);
            return;
        }
        ele = doc.select("span.date");
        Date date = parseDate(ele.html());

        int update = jdbcTemplate.update("update stock set ths_date=?, ths_percent=? where id=? and c_date=current_date", date, percent, id);
        if (update != 1) throw new RuntimeException("tsh update failed");
    }

    private Date parseDate(String html) {
        html = html.split(":")[1].split(" ")[0];

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < html.length(); i++) {
            char c = html.charAt(i);
            if (c >= '0' && c <= '9') sb.append(c);
        }

        SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
        Date date = null;
        try {
            date = f.parse(sb.toString());
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }
        return date;
    }

    public List<String> tasks() {
        List<String> stocks = jdbcTemplate.queryForList("select id from stock where id<>'sz300033' and c_date=current_date and ths_percent=0", String.class);
        logger.info("tonghuashun task size:{}", stocks.size());
        return stocks;
    }
}
