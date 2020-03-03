package mathew_williams_silver_bars;

import java.math.BigDecimal;
import java.util.Objects;

public class Order {
    public static final String KG_FOR_£_TEMPLATE = "%s: %s kg for £%s";

    private final String userId;
    private final BigDecimal quantity;
    private final BigDecimal pricePerKg;
    private final OrderType type;

    public Order(String userId, BigDecimal quantity, BigDecimal pricePerKg, OrderType orderType) {
        this.userId = userId;
        this.quantity = quantity;
        this.pricePerKg = pricePerKg;
        this.type = orderType;
    }

    public String getUserId() {
        return userId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public BigDecimal getPricePerKg() {
        return pricePerKg;
    }

    public OrderType getType() {
        return type;
    }

    public boolean isValid() {
        return userId != null && !userId.trim().isEmpty() && quantity != null && pricePerKg != null && type != null;
    }

    public String summary() {
        return String.format(KG_FOR_£_TEMPLATE, type, quantity, pricePerKg);
    }

    public static Order empty() {
        return new Order(null, BigDecimal.ZERO, BigDecimal.ZERO, null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(userId, order.userId) &&
                Objects.equals(quantity, order.quantity) &&
                Objects.equals(pricePerKg, order.pricePerKg) &&
                type == order.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, quantity, pricePerKg, type);
    }

    @Override
    public String toString() {
        return "Order{" +
                "userId='" + userId + '\'' +
                ", quantity=" + quantity +
                ", pricePerKg=" + pricePerKg +
                ", orderType=" + type +
                '}';
    }
}
