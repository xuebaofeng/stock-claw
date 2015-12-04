package bf.stock;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/1.
 */
public class StocksTask {

    static Logger logger = LoggerFactory.getLogger(StocksTask.class);

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


    public static List<String> claw(JdbcTemplate jdbcTemplate) {
        List<String> stocks = getAll();
        for (String id : stocks) {
            Integer count = jdbcTemplate.queryForObject("select count(id) from stock where id=? and claw_date=current_date",
                    Integer.class, id);
            if (count == 0)
                jdbcTemplate.update("INSERT INTO stock(\n" +
                        "            id, icf_level, tsh_percent, claw_date)\n" +
                        "    VALUES (?, ?, ?, current_date);\n", id, 0, 0);
        }
        return stocks;
    }
}
