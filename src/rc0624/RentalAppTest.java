package rc0624;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RentalAppTest {

	@Test
	@DisplayName("Test 1 - fails with exception")
	void exceptionTesting() {
		InvalidDiscountPercentException exception = assertThrows(InvalidDiscountPercentException.class, () ->
		RentalApp.checkout("JAKR", 5, 101, "9/3/15"));

	    assertEquals("'101' is not a valid number for discount", exception.getMessage());
	}

	@Test
	@DisplayName("Test 2 - No charge for holiday")
	void noHolidayChargeForLadder() throws Exception, InvalidDiscountPercentException, ParseException {
		RentalApp ra2 = RentalApp.checkout("LADW", 3, 10, "7/2/20");
	    assertEquals(3, ra2.rentalDays);
	    assertEquals(2, ra2.chargeDays);
	    assertEquals(3.58, ra2.finalCharge);
	}
	
	@Test
	@DisplayName("Test 3 - Charge for holiday but not weekend")
	void holidayChargeNoWeekendForLadder() throws Exception, InvalidDiscountPercentException, ParseException {
		RentalApp ra3 = RentalApp.checkout("CHNS", 5, 25, "7/2/15");
	    assertEquals(5, ra3.rentalDays);
	    assertEquals(3, ra3.chargeDays);
	    assertEquals(3.35, ra3.finalCharge);
	}
	
	@Test
	@DisplayName("Test 4 - No Charge for holiday or weekend")
	void noHolidayChargeNoWeekendForJh() throws Exception, InvalidDiscountPercentException, ParseException {
		RentalApp ra4 = RentalApp.checkout("JAKD", 6, 0, "9/3/20");
	    assertEquals(6, ra4.rentalDays);
	    assertEquals(3, ra4.chargeDays);
	    assertEquals(8.97, ra4.finalCharge);
	}
	
	@Test
	@DisplayName("Test 5 - No charge for holiday or weekend")
	void noHolidayChargeNoWeekend() throws Exception, InvalidDiscountPercentException, ParseException {
		RentalApp ra5 = RentalApp.checkout("JAKR", 9, 0, "7/2/15");
	    assertEquals(9, ra5.rentalDays);
	    assertEquals(5, ra5.chargeDays);
	    assertEquals(14.95, ra5.finalCharge);
	}
	
	@Test
	@DisplayName("Test 6")
	void test6() throws Exception, InvalidDiscountPercentException, ParseException {
		RentalApp ra6 = RentalApp.checkout("JAKR", 4, 50, "7/2/20");
	    assertEquals(4, ra6.rentalDays);
	    assertEquals(1, ra6.chargeDays);
	    assertEquals(1.49, ra6.finalCharge);
	}
}
