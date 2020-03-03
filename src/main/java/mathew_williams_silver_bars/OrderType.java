package mathew_williams_silver_bars;

import java.util.Comparator;

public enum OrderType {
    BUY(Comparator.comparing(Order::getPricePerKg).reversed()), SELL(Comparator.comparing(Order::getPricePerKg));

    private Comparator<Order> orderComparator;

    OrderType(Comparator<Order> orderComparator) {
        this.orderComparator = orderComparator;
    }

    public Comparator<Order> getOrderComparator() {
        return orderComparator;
    }
}
