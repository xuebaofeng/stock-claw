package bf.stock;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class StockTaskTests {


    public static void main(String[] args) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost/stock");
        dataSource.setUsername("postgres");
        dataSource.setPassword("123456");
        dataSource.setInitialSize(8);
        StocksTask stocksTask = new StocksTask();
        stocksTask.jdbcTemplate = new JdbcTemplate(dataSource);
        stocksTask.saveBase();
    }

}
