package bf.stock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BfStockApplication.class)
public class StockTaskTests {

    @Autowired
    StocksTask stocksTask;


    @Test
    public void testStockTask() {
        stocksTask.all();
    }

}
