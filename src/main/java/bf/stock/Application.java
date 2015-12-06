package bf.stock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @Autowired
    ICaifuTask iCaifuTask;

    @Autowired
    TongHuaShunTask tongHuaShunTask;

    @Autowired
    StocksTask stocksTask;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {

        log.info("begin claw");

        stocksTask.populate();
        iCaifuTask.tasks().parallelStream().forEach(iCaifuTask::claw);
        tongHuaShunTask.tasks().parallelStream().forEach(tongHuaShunTask::claw);

        log.info("end claw");
    }

}