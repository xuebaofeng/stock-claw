package bf.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by Administrator on 2015/12/4.
 */
public class WebServiceTask {
    @Autowired
    JdbcTemplate jdbcTemplate;

    protected void addErrorCount(String id) {
        jdbcTemplate.update("update stock_error set error_count=error_count+1 where id=?", id);
    }

    protected boolean isClosed(String id) {
        Integer count = jdbcTemplate.queryForObject("select count(id) from stock_closed where id=?", Integer.class, id);
        return count == 1;
    }
}
