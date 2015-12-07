package bf.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;

/**
 * Created by Administrator on 2015/12/4.
 */
public abstract class WebServiceTask {
    @Autowired
    JdbcTemplate jdbcTemplate;

    Date maxDate;

    protected void addErrorCount(String id, WebserviceType type) {
        if (type == WebserviceType.ICF)
            jdbcTemplate.update("update stock_error set ec_icf=ec_icf+1 where id=?", id);
        else if (type == WebserviceType.THS)
            jdbcTemplate.update("update stock_error set ec_ths=ec_ths+1 where id=?", id);
    }

    protected boolean isClosed(String id) {
        Integer count = jdbcTemplate.queryForObject("select count(id) from stock_closed where id=?", Integer.class, id);
        return count == 1;
    }

    protected boolean isExist(String id, Date date) {
        Integer count = jdbcTemplate.queryForObject("select count(id) from stock where id=? and claw_date=?", Integer.class, id, date);
        return count == 1;
    }

    protected Date getMaxDate() {
        if (maxDate == null) {
            maxDate = jdbcTemplate.queryForObject("select max(claw_date) from stock", Date.class);
        }
        return maxDate;
    }
}
