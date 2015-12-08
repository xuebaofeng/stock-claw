package bf.stock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Administrator on 2015/12/4.
 */
public abstract class WebServiceTask {


    private static Logger logger = LoggerFactory.getLogger(TongHuaShunTask.class);


    @Autowired
    JdbcTemplate jdbcTemplate;

    Date currentChinaDate;

    public static void main(String[] args) {

        String zoneId = "Asia/Chongqing";

        int offset = TimeZone.getTimeZone(zoneId).getOffset(System.currentTimeMillis());
        Date date = new Date(offset + System.currentTimeMillis());
        logger.debug(date.toString());
    }

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
        Integer count = jdbcTemplate.queryForObject("select count(id) from stock where id=? and c_date=?", Integer.class, id, date);
        return count == 1;
    }

    protected Date getCurrentChinaDate() {
        if (currentChinaDate != null) return currentChinaDate;
        String zoneId = "Asia/Chongqing";

        int offset = TimeZone.getTimeZone(zoneId).getOffset(System.currentTimeMillis());
        currentChinaDate = new Date(offset + System.currentTimeMillis());
        return currentChinaDate;
    }


}
