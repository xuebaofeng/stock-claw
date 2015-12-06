package bf.stock;

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
public class StocksTask {

    static Logger logger = LoggerFactory.getLogger(StocksTask.class);


    @Autowired
    JdbcTemplate jdbcTemplate;

    public void populate() {
        logger.info("populate stock table for current day");
        List<String> stocks = jdbcTemplate.queryForList("select id from stock_base", String.class);
        for (String id : stocks) {
            int count = jdbcTemplate.queryForObject("select count(id) from stock where id=? and claw_date=current_date", Integer.class, id);
            if (count == 1) continue;
            jdbcTemplate.update("INSERT INTO stock(id) VALUES (?)", id);
        }
    }
}
