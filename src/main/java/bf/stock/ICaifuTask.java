package bf.stock;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Administrator on 2015/12/1.
 * 财富数据不准确
 * @deprecated
 */
@Component
public class ICaifuTask extends WebServiceTask {


    private static Logger logger = LoggerFactory.getLogger(ICaifuTask.class);

    public static void main(String[] args) {
        ICaifuTask main = new ICaifuTask();
        main.claw("sz000002");
    }

    public void claw(String id) {

        Document doc = JsoupUtil.getDocument(id, WebserviceType.ICF);
        if (doc == null) {
            addErrorCount(id, WebserviceType.ICF);
            return;
        }

        int icf_level = 0;

        Elements ele = doc.select("html body div.grid_main div.grid_conten_022 div.grid_conten_2 div.i-nav div.i-nav-02 div.ti-one4 div.ti-biaodan table tbody tr td.g5 span.red");
        String html = ele.html();
        html = html.trim();

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
        }

        ele = doc.select(".picL_top");
        if (ele.html().contains("--")) {
            icf_level = 1;
        }

        logger.info("id:{},icf_level:{}", id, icf_level);
        if (icf_level == 0) {
            addErrorCount(id, WebserviceType.ICF);
            return;
        }
        int update = jdbcTemplate.update("update stock set icf_level=? where id=? and c_date=current_date", icf_level, id);
        if (update != 1) throw new RuntimeException("icf update failed");
    }

    public List<String> tasks() {

        List<String> stocks = jdbcTemplate.queryForList("select id from stock where c_date=current_date and icf_level=0 and ths_percent > 0", String.class);
        logger.info("icaifu task size:{}", stocks.size());
        return stocks;
    }

}
