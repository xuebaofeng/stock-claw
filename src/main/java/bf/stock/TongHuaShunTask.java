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

    public void claw(List<String> stocks) {
        for (String id : stocks) {

            Integer count = jdbcTemplate.queryForObject("select count(id) from stock where id=? and claw_date=current_date and tsh_percent>0",
                    Integer.class, id);
            if (count == 1) continue;
            if (isClosed(id)) continue;

            Document doc = JsoupUtil.getDocument(id, WebserviceType.ICF);
            if (doc == null) {
                addErrorCount(id);
                continue;
            }
            Elements ele = doc.select("#nav_basic.box2.indexStat div.box2wrap.basic_score div.result p span.gray");
            String html = ele.html();
            if (html.length() == 0) continue;
            int percent = 0;
            try {
                html = html.substring(6).split("%")[0];
                percent = Integer.parseInt(html);
            } catch (Exception e) {
                logger.error("error html:{},e:{}", html, e.getMessage());
            }

            logger.info("id:{},tsh_percent:{}", id, percent);
            if (percent == 0) {
                addErrorCount(id);
                continue;
            }
            jdbcTemplate.update("update stock set tsh_percent=? where id=? and claw_date=current_date", percent, id);
        }
    }

}
