package bf.stock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/12/1.
 */
@Component
public class StockBaseTask extends WebServiceTask {


    private static Logger logger = LoggerFactory.getLogger(StockBaseTask.class);

    public static void main(String[] args) {
    }


    public void populate() {
        List<String> stocks = jdbcTemplate.queryForList("select id from stock_base", String.class);

        logger.info("total stocks:{}", stocks.size());

        stocks.parallelStream().forEach(id -> {

            boolean exist = isExist(id, new Date());
            if (exist) return;

            jdbcTemplate.update("insert into stock(id) values(?)", id);

        });
    }
}
