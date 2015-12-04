package bf.stock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class BfStockApplication implements CommandLineRunner {

    public static final int N_THREADS = 20;
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
        int size = stocks.size();
        int step = size / N_THREADS;

        ExecutorService executorService = Executors.newFixedThreadPool(N_THREADS);
        for (int i = 0; i < N_THREADS; i++) {
            final int finalI = i;
            executorService.submit(() -> ICaifuTask.claw(stocks.subList(finalI * step, finalI * step + step), jdbcTemplate));
            executorService.submit(() -> TongHuaShunTask.claw(stocks.subList(finalI * step, finalI * step + step), jdbcTemplate));
        }
        executorService.shutdown();

        log.info("end claw");
    }
}
