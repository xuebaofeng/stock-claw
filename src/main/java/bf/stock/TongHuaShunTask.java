package bf.stock;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Administrator on 2015/12/1.
 */
@Component
public class TongHuaShunTask extends WebServiceTask {


    private static Logger logger = LoggerFactory.getLogger(TongHuaShunTask.class);


    @Autowired
    JdbcTemplate jdbcTemplate;

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
        jdbcTemplate.update("update stock set ths_percent=? where id=? and claw_date=current_date", percent, id);
    }

    public List<String> tasks() {
        return jdbcTemplate.queryForList("select id from stock " +
                "where claw_date=current_date and ths_percent=0 and id<>'sz300033'", String.class);
    }
}
