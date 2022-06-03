package ca.uwaterloo.cs446;

import static org.junit.Assert.*;

import org.junit.Test;

public class DiscountTest {

	@Test
	public void testPercent() {
		Order order = new Order("Shane", 444.0f);
		DiscountStrategy strategy = new PercentDiscountStrategy(20.0f);

		assertEquals(order.getPrice()*0.8, strategy.applyDiscount(order), 0.001);
	}

	@Test
	public void testStoreCredit() {
		Order order = new Order("Shane", 444.0f);
		DiscountStrategy strategy = new StoreCreditDiscountStrategy(20.0f);

		assertEquals(order.getPrice()-20.0f, strategy.applyDiscount(order), 0.001);
	}
}
