package bf.stock;

import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.BoneCPDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class StockTaskTests {


    public static void main(String[] args) {

        StocksTask stocksTask = new StocksTask();
        stocksTask.jdbcTemplate = newJdbcTempate();
        StocksTask.logger.info("begin to import tdx data");
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
            StocksTask.logger.debug(name);
            if (id.charAt(0) == '6') id = "sh" + id;
            else id = "sz" + id;

            int count = stocksTask.jdbcTemplate.queryForObject("select count(id) from stock_base where id=?", Integer.class, id);
            if (count == 1) {
                StocksTask.logger.info("id:{} exist, skip adding", id);
                continue;
            }

            StocksTask.logger.info("id:{},name:{},industry:{} added", id, name, industry);
            stocksTask.jdbcTemplate.update("INSERT INTO stock_base(id, name, industry) VALUES (?, ?, ?)", id, name, industry);
        }


    }

    private static JdbcTemplate newJdbcTempate() {
        BoneCPConfig config = new BoneCPConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost/stock");
        config.setPartitionCount(3);
        BoneCPDataSource dataSource = new BoneCPDataSource(config);
        dataSource.setUsername("postgres");
        dataSource.setPassword("123456");
        dataSource.setDriverClass("org.postgresql.Driver");
        return new JdbcTemplate(dataSource);
    }

}
