package bf.stock;

import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.BoneCPDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class StockTaskTests {

    private static Logger logger = LoggerFactory.getLogger(StockTaskTests.class);

    public static void main(String[] args) throws IOException {

        StocksTask stocksTask = new StocksTask();
        stocksTask.jdbcTemplate = newJdbcTempate();
        logger.info("begin to import tdx data");
        String fileName = "db-tdx.txt";

        List<String> stocks = Files.lines(Paths.get(fileName), Charset.defaultCharset()).parallel().collect(Collectors.toList());
        stocks.remove(0);
        stocks.remove(stocks.size() - 1);

        stocks.parallelStream()
                .forEach((line) -> {
                    String[] pair = line.split("\t");
                    String id = pair[0];
                    String name = pair[1];
                    String industry = pair[18];
                    if (id.charAt(0) == '6') id = "sh" + id;
                    else id = "sz" + id;

                    if (id.length() != 8) return;

                    int count = stocksTask.jdbcTemplate.queryForObject("select count(id) from stock_base where id=?",
                            Integer.class, id);
                    if (count == 1) {
                        logger.info("id:{} exist, skip adding", id);
                        return;
                    }

                    logger.info("id:{},name:{},industry:{} added", id, name, industry);
                    stocksTask.jdbcTemplate.update("INSERT INTO stock_base(id, name, industry) VALUES (?, ?, ?)", id, name, industry);
                });


    }

    private static JdbcTemplate newJdbcTempate() {
        BoneCPConfig config = new BoneCPConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost/stock");
        config.setPartitionCount(8);
        BoneCPDataSource dataSource = new BoneCPDataSource(config);
        dataSource.setUsername("postgres");
        dataSource.setPassword("123456");
        dataSource.setDriverClass("org.postgresql.Driver");
        return new JdbcTemplate(dataSource);
    }

}
