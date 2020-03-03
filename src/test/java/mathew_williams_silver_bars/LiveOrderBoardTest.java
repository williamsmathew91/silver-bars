package mathew_williams_silver_bars;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class LiveOrderBoardTest {

    private LiveOrderBoard liveOrderBoard;

    @Before
    public void setUp() throws Exception {
        liveOrderBoard = new LiveOrderBoard();
    }

    @Test(expected = InvalidOrderException.class)
    public void nullOrderShouldThrowInvalidOrderException() throws InvalidOrderException {
        liveOrderBoard.register(null);
    }

    @Test(expected = InvalidOrderException.class)
    public void invalidOrderShouldThrowInvalidOrderException() throws InvalidOrderException {
        Order orderMock = mock(Order.class);
        when(orderMock.isValid()).thenReturn(false);

        liveOrderBoard.register(orderMock);
    }

    @Test
    public void registerShouldReturnNewIDEachTime() throws InvalidOrderException {
        String id1 = liveOrderBoard.register(new Order("someUserId", BigDecimal.valueOf(2.5), BigDecimal.valueOf(200), OrderType.BUY));
        String id2 = liveOrderBoard.register(new Order("someUserId", BigDecimal.valueOf(2.5), BigDecimal.valueOf(200), OrderType.BUY));

        assertThat(id1).isNotEqualTo(id2);
    }

    @Test
    public void getSummaryShouldReturnEmptyListIfNoOrdersRegistered() {
        assertThat(liveOrderBoard.getSummary()).isEmpty();
    }

    @Test
    public void registeredBUYOrderShouldAppearInOrderSummary() throws InvalidOrderException {
        liveOrderBoard.register(new Order("someUserId", BigDecimal.valueOf(2.5), BigDecimal.valueOf(200), OrderType.BUY));

        List<String> orders = liveOrderBoard.getSummary();
        assertThat(orders).containsExactly("BUY: 2.5 kg for £200");
    }

    @Test
    public void registeredBuyOrdersShouldBeListedHighestPriceToLow() throws InvalidOrderException {
        liveOrderBoard.register(new Order("someUserId", BigDecimal.valueOf(2.5), BigDecimal.valueOf(200), OrderType.BUY));
        liveOrderBoard.register(new Order("someOtherUserId", BigDecimal.valueOf(5.5), BigDecimal.valueOf(400), OrderType.BUY));

        List<String> orders = liveOrderBoard.getSummary();
        assertThat(orders).containsExactly("BUY: 5.5 kg for £400", "BUY: 2.5 kg for £200");
    }

    @Test
    public void registeredSELLOrderShouldAppearInOrderSummary() throws InvalidOrderException {
        liveOrderBoard.register(new Order("someOtherUserId", BigDecimal.valueOf(3.5), BigDecimal.valueOf(300), OrderType.SELL));

        List<String> orders = liveOrderBoard.getSummary();
        assertThat(orders).containsExactly("SELL: 3.5 kg for £300");
    }

    @Test
    public void registeredSellOrdersShouldBeListedLowestPriceToHigh() throws InvalidOrderException {
        liveOrderBoard.register(new Order("someUserId", BigDecimal.valueOf(6.5), BigDecimal.valueOf(1000), OrderType.SELL));
        liveOrderBoard.register(new Order("someOtherUserId", BigDecimal.valueOf(3.5), BigDecimal.valueOf(300), OrderType.SELL));

        List<String> orders = liveOrderBoard.getSummary();
        assertThat(orders).containsExactly("SELL: 3.5 kg for £300", "SELL: 6.5 kg for £1000");
    }

    @Test
    public void registeredSellOrdersForTheSamePriceShouldBeMergedTogether() throws InvalidOrderException {
        liveOrderBoard.register(new Order("someUserId", BigDecimal.valueOf(6.5), BigDecimal.valueOf(1000), OrderType.SELL));
        liveOrderBoard.register(new Order("someOtherUserId", BigDecimal.valueOf(3.5), BigDecimal.valueOf(300), OrderType.SELL));
        liveOrderBoard.register(new Order("someDifferentUserId", BigDecimal.valueOf(1.5), BigDecimal.valueOf(300), OrderType.SELL));

        List<String> orders = liveOrderBoard.getSummary();
        assertThat(orders).containsExactly("SELL: 5.0 kg for £300", "SELL: 6.5 kg for £1000");
    }

    @Test
    public void registeredSellAndBuyOrdersShouldBeGroupedTogetherInSummary() throws InvalidOrderException {
        liveOrderBoard.register(new Order("user1", BigDecimal.valueOf(5.0), BigDecimal.valueOf(1000), OrderType.SELL));
        liveOrderBoard.register(new Order("user3", BigDecimal.valueOf(4.2), BigDecimal.valueOf(400), OrderType.BUY));
        liveOrderBoard.register(new Order("user2", BigDecimal.valueOf(3.5), BigDecimal.valueOf(300), OrderType.SELL));
        liveOrderBoard.register(new Order("user4", BigDecimal.valueOf(7.1), BigDecimal.valueOf(900), OrderType.BUY));

        List<String> summary = liveOrderBoard.getSummary();
        assertThat(summary).containsExactly("BUY: 7.1 kg for £900", "BUY: 4.2 kg for £400", "SELL: 3.5 kg for £300", "SELL: 5.0 kg for £1000");
    }

    @Test
    public void cancelForAnIdThatDoesNotExistShouldBeIdempotent() {
        liveOrderBoard.cancel("id1");
    }

    @Test
    public void cancelledOrdersShouldNotAppearInTheSummary() throws InvalidOrderException {
        String id1 = liveOrderBoard.register(new Order("user1", BigDecimal.valueOf(5.0), BigDecimal.valueOf(1000), OrderType.SELL));
        liveOrderBoard.register(new Order("user3", BigDecimal.valueOf(6.2), BigDecimal.valueOf(500), OrderType.BUY));

        liveOrderBoard.cancel(id1);

        assertThat(liveOrderBoard.getSummary()).containsExactly("BUY: 6.2 kg for £500");
    }
}