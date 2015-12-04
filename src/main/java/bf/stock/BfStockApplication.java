package bf.stock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@SpringBootApplication
public class BfStockApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(BfStockApplication.class);
    @Autowired
    JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(BfStockApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {

        log.info("begin claw");

        List<String> stocks = StocksTask.claw(jdbcTemplate);

        ICaifuTask.claw(stocks, jdbcTemplate);

        TongHuaShunTask.claw(stocks, jdbcTemplate);

        log.info("end claw");
    }
}
