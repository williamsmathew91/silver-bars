package mathew_williams_silver_bars;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.collect.Maps.newHashMap;

public class LiveOrderBoard implements OrderBoard {

    private Map<String, Order> orders = newHashMap();

    public String register(Order order) throws InvalidOrderException {
        if (order == null || !order.isValid())
            throw new InvalidOrderException();

        String id = UUID.randomUUID().toString();
        orders.put(id, order);

        return id;
    }

    public List<String> getSummary() {
        Map<OrderType, Map<BigDecimal, List<Order>>> ordersByOrderType = groupOrdersByTypeAndPrice();

        return ordersByOrderType.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(reduceAndSortOrdersWithMatchingPriceAndType())
                .flatMap(List::stream)
                .map(Order::summary)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void cancel(String id) {
        orders.remove(id);
    }

    private Function<Map.Entry<OrderType, Map<BigDecimal, List<Order>>>, List<Order>> reduceAndSortOrdersWithMatchingPriceAndType() {
        return entry -> reduceOrdersWithMatchingPrice(entry.getValue())
                .stream()
                .sorted(entry.getKey().getOrderComparator())
                .collect(Collectors.toList());
    }

    private List<Order> reduceOrdersWithMatchingPrice(Map<BigDecimal, List<Order>> ordersWithMatchingPrice) {
        return ordersWithMatchingPrice.values().stream().map(reduceOrderList()).collect(Collectors.toList());
    }

    private Function<List<Order>, Order> reduceOrderList() {
        return list -> list.stream().reduce(Order.empty(), orderReducer());
    }

    private Map<OrderType, Map<BigDecimal, List<Order>>> groupOrdersByTypeAndPrice() {
        return orders
                .values()
                .stream()
                .collect(Collectors.groupingBy(Order::getType,
                        Collectors.groupingBy(Order::getPricePerKg, Collectors.toList())));
    }

    private BinaryOperator<Order> orderReducer() {
        return (order1, order2) -> new Order(order2.getUserId(), order1.getQuantity().add(order2.getQuantity()), order2.getPricePerKg(), order2.getType());
    }
}
