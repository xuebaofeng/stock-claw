package bf.stock;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2015/12/1.
 */
@Component
public class ICaifuTask extends WebServiceTask {


    private static Logger logger = LoggerFactory.getLogger(ICaifuTask.class);


    public void claw(String id) {

        Integer count = jdbcTemplate.queryForObject("select count(id) from stock where id=? and claw_date=current_date and icf_level>0",
                Integer.class, id);
        if (count == 1) return;
        if (isClosed(id)) return;

        Document doc = JsoupUtil.getDocument(id, WebserviceType.ICF);
        if (doc == null) {
            addErrorCount(id, WebserviceType.ICF);
            return;
        }
        Elements ele = doc.select("html body div.grid_main div.grid_conten_022 div.grid_conten_2 div.i-nav div.i-nav-02 div.ti-one4 div.ti-biaodan table tbody tr td.g5 span.red");
        String html = ele.html();
        html = html.trim();

        int icf_level;

        switch (html) {
            case "严重低估":
                icf_level = 50;
                break;
            case "相对低估":
                icf_level = 40;
                break;
            case "合理估值":
                icf_level = 30;
                break;
            case "相对高估":
                icf_level = 20;
                break;
            case "严重高估":
                icf_level = 10;
                break;
            default:
                icf_level = 0;
                break;
        }

        logger.info("id:{},icf_level:{}", id, icf_level);
        if (icf_level == 0) {
            addErrorCount(id, WebserviceType.ICF);
            return;
        }
        jdbcTemplate.update("update stock set icf_level=? where id=? and claw_date=current_date", icf_level, id);
    }

}
