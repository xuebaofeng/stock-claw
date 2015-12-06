package bf.stock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Administrator on 2015/12/1.
 */
@Component
public class StocksTask {

    static Logger logger = LoggerFactory.getLogger(StocksTask.class);


    @Autowired
    JdbcTemplate jdbcTemplate;


    public void saveBase() {
        logger.info("begin to import tdx data");
        Scanner scanner = null;
        try {
            scanner = new Scanner(Paths.get("db-tdx.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (scanner == null) return;
        scanner.nextLine();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] pair = line.split("\t");
            if (pair.length < 2) continue;
            String id = pair[0];


            String name = pair[1];
            String industry = pair[18];
            logger.debug(name);
            if (id.charAt(0) == '6') id = "sh" + id;
            else id = "sz" + id;

            int count = jdbcTemplate.queryForObject("select count(id) from stock_base where id=?", Integer.class, id);
            if (count == 1) {
                logger.info("id:{} exist, skip adding", id);
                continue;
            }

            logger.info("id:{},name:{},industry:{} added", id, name, industry);
            jdbcTemplate.update("INSERT INTO stock_base(id, name, industry) VALUES (?, ?, ?)", id, name, industry);
        }


    }


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
