package mathew_williams_silver_bars;

import java.util.List;

public interface OrderBoard {

    String register(Order order) throws InvalidOrderException;
    List<String> getSummary();
    void cancel(String id);

}
