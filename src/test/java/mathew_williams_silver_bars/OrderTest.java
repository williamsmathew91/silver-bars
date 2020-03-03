package mathew_williams_silver_bars;

import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderTest {

    @Test
    public void isValidReturnsFalseForNullUserId() {
        assertThat(new Order(null, BigDecimal.valueOf(3.5), BigDecimal.valueOf(2.5), OrderType.BUY).isValid()).isFalse();
    }

    @Test
    public void isValidReturnsFalseForEmptyUserId() {
        assertThat(new Order("", BigDecimal.valueOf(3.5), BigDecimal.valueOf(2.5), OrderType.BUY).isValid()).isFalse();
    }

    @Test
    public void isValidReturnsFalseForBlankUserId() {
        assertThat(new Order("  ", BigDecimal.valueOf(3.5), BigDecimal.valueOf(2.5), OrderType.BUY).isValid()).isFalse();
    }

    @Test
    public void isValidReturnsFalseNullOrderQuantity() {
        assertThat(new Order("someUserId", null, BigDecimal.valueOf(2.5), OrderType.SELL).isValid()).isFalse();
    }

    @Test
    public void isValidReturnsFalseNullPricePerKG() {
        assertThat(new Order("someUserId", BigDecimal.valueOf(2.5), null, OrderType.BUY).isValid()).isFalse();
    }

    @Test
    public void isValidReturnsFalseNullOrderType() {
        assertThat(new Order("someUserId", BigDecimal.valueOf(2.5), BigDecimal.valueOf(300), null).isValid()).isFalse();
    }

    @Test
    public void isValidReturnsTrueWhenAllFieldsArePresent() {
        assertThat(new Order("someUserId", BigDecimal.valueOf(2.5), BigDecimal.valueOf(300), OrderType.SELL).isValid()).isTrue();
    }

}