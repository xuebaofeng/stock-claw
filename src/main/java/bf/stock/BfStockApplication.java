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

    public static final int N_THREADS = 100;
    private static final Logger log = LoggerFactory.getLogger(BfStockApplication.class);
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    ICaifuTask iCaifuTask;

    @Autowired
    TongHuaShunTask tongHuaShunTask;

    @Autowired
    StocksTask stocksTask;

    public static void main(String[] args) {
        SpringApplication.run(BfStockApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {

//        stocksTask.saveBase();
        stocksTask.populate();

        log.info("begin claw");

        ExecutorService executorService = Executors.newFixedThreadPool(N_THREADS);

        List<String> tasks = iCaifuTask.tasks();
        for (String task : tasks) {
            executorService.submit(() -> iCaifuTask.claw(task));
        }

        tasks = tongHuaShunTask.tasks();
        for (String task : tasks) {
            executorService.submit(() -> tongHuaShunTask.claw(task));
        }

        executorService.shutdown();
        log.info("end claw");
    }

}
