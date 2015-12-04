package bf.stock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;
import java.util.List;

@SpringBootApplication
public class BfStockApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(BfStockApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(BfStockApplication.class, args);
    }


    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... strings) throws Exception {

        List<String> stocks = Stocks.getAll();
        for (String id : stocks) {
            Date date = new Date();
            Integer count = jdbcTemplate.queryForObject("select count(id) from stock where id=? and claw_date=?",
                    Integer.class, id, date);
            if (count == 0)
                jdbcTemplate.update("INSERT INTO stock(\n" +
                        "            id, icf_level, tsh_percent, claw_date)\n" +
                        "    VALUES (?, ?, ?, ?);\n", id, 0, 0, date);
        }


        ICaifuTask.claw(stocks,jdbcTemplate);
        TongHuaShunTask.claw(stocks,jdbcTemplate);
    }
}
