package bf.stock;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/1.
 */
@Component
public class StocksTask {

    static Logger logger = LoggerFactory.getLogger(StocksTask.class);


    @Autowired
    JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {

        logger.debug("list size:{}", allForToday().size());
    }

    public static List<String> allForToday() {
        List<String> l = new ArrayList<>();

        String url = "http://ctxalgo.com/api/stocks";
        String json = null;
        try {
            json = Jsoup.connect(url).ignoreContentType(true).execute().body();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        if (json == null) return l;

        json = json.split("\\{")[1].split("\\}")[0];
        String[] split = json.split(",");
        for (String s : split) {
            logger.debug(s);
            String[] pair = s.split(":");
            s = removeQuotes(pair[0]);
            String name = StringEscapeUtils.unescapeJava(removeQuotes(pair[1]));
            logger.debug(name);
            l.add(s);
        }
        return l;
    }

    private static String removeQuotes(String s) {
        return s.split("\"")[1].split("\"")[0];
    }

    public static void all(JdbcTemplate jdbcTemplate) {
        try {

            String url = "http://ctxalgo.com/api/stocks";
            String json = Jsoup.connect(url).ignoreContentType(true).execute().body();


            json = json.split("\\{")[1].split("\\}")[0];
            String[] split = json.split(",");
            for (String id : split) {
                logger.debug(id);
                String[] pair = id.split(":");
                id = removeQuotes(pair[0]);
                String name = StringEscapeUtils.unescapeJava(removeQuotes(pair[1]));
                logger.debug(name);

                Integer count = jdbcTemplate.queryForObject("select count(id) from stock_base where id=?",
                        Integer.class, id);
                if (count == 0)
                    jdbcTemplate.update("INSERT INTO stock_base(\n" +
                            "            id, name)\n" +
                            "    VALUES (?, ?);\n", id, name);
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public List<String> claw() {
        List<String> stocks = allForToday();
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
