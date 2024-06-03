package rc0624;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class RentalApp {
	Tool tool;
	int rentalDays;
	String checkoutDate;
	String dueDate;
	int chargeDays;
	double prediscountCharge;
	int discountPercent;
	double discountAmount;
	double finalCharge;
	
    public RentalApp(
    	Tool tool,
		int rentalDays,
		String checkoutDate,
		String dueDate,
		int chargeDays,
		double prediscountCharge,
		int discountPercent,
		double discountAmount,
		double finalCharge
	){
        this.tool = tool;
		this.rentalDays = rentalDays;
		this.checkoutDate = checkoutDate;
		this.dueDate = dueDate;
		this.chargeDays = chargeDays;
		this.prediscountCharge = prediscountCharge;
		this.discountPercent = discountPercent;
		this.discountAmount = discountAmount;
		this.finalCharge = finalCharge;
    }
    
    public static String calculateDueDate(String checkoutDate, int rentalDays) {
    	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
    	Calendar c = Calendar.getInstance();
    	try{
    	   c.setTime(sdf.parse(checkoutDate));
    	}catch(ParseException e){
    		e.printStackTrace();
    	 }
    	c.add(Calendar.DAY_OF_MONTH, rentalDays);
    	return sdf.format(c.getTime());
   
	}
    
    public static int calculateChargeDays(String checkoutDate, String dueDate, int rentalDays, boolean weekendCharge, boolean holidayCharge) throws ParseException {
		int noChargeDays = 0;
    	if (!weekendCharge) {
    		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");
    		Date date1 = formatter.parse(checkoutDate);
    		Date date2 = formatter.parse(dueDate);
    		noChargeDays += getWeekendDaysBetweenTwoDates(date1, date2);
		}
    	if (!holidayCharge) {
    		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");
    		Date date1 = formatter.parse(checkoutDate);
    		Date date2 = formatter.parse(dueDate);
    		int year = date1.getYear();
    		Date ind = IndependenceDay(year);
    		Date lab = LaborDayObserved(year);
    		if(ind.after(date1) && ind.before(date2)) {
    			noChargeDays += 1;
    		}
    		if(lab.after(date1) && lab.before(date2)) {
    			noChargeDays += 1;
    		}
		}
    	
    	return rentalDays - noChargeDays;
    }
    public static RentalApp checkout(String toolCode, int rentalDays, int discountPercent, String checkoutDate) throws InvalidNumberOfRentalDaysException, InvalidDiscountPercentException, ParseException {
		if (rentalDays < 1) {
			throw new InvalidNumberOfRentalDaysException(String.format("'%s' is not a valid number of rental days", rentalDays));
		}
		if (!(discountPercent >= 0 && discountPercent <= 100)) {
			throw new InvalidDiscountPercentException(String.format("'%s' is not a valid number for discount", discountPercent));
		}
    	Tool tool = null;
		try {
			tool = Tool.findTool(toolCode);
		} catch (IllegalToolCodeException e) {
			e.printStackTrace();
		}
		
		String dueDate = calculateDueDate(checkoutDate, rentalDays);
		int chargeDays = calculateChargeDays(checkoutDate, dueDate, rentalDays, tool.weekendCharge, tool.holidayCharge);
		double prediscountCharge = Math.round((chargeDays * tool.dailyRentalCharge) * 100.0) / 100.0;
		
		double discount = (discountPercent / 100.0) * prediscountCharge;
		double discountAmount = Math.round(discount * 100.0) / 100.0;
		
		double charge = prediscountCharge - discountAmount;
		double finalCharge = Math.round(charge * 100.0) / 100.0;
		
		return new RentalApp(tool, rentalDays, checkoutDate, dueDate, chargeDays, prediscountCharge, discountPercent, discountAmount, finalCharge);
	}
	
    public static int getWeekendDaysBetweenTwoDates(Date startDate, Date endDate) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);        

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);

        int weekendDays = 0;

        //Return 0 if start and end are the same
        if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
            return 0;
        }

        if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
            startCal.setTime(endDate);
            endCal.setTime(startDate);
        }

        do {
           //excluding start date
            startCal.add(Calendar.DAY_OF_MONTH, 1);
            if (startCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || startCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                ++weekendDays;
            }
        } while (startCal.getTimeInMillis() < endCal.getTimeInMillis()); //excluding end date

        return weekendDays;
    }
    
	public static void printRentalApp(RentalApp app) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
		Calendar c = Calendar.getInstance();
		c.setTime(sdf.parse(app.checkoutDate));
		
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		
		System.out.println("Tool code: " + app.tool.toolCode);
		System.out.println("Tool type: " + app.tool.toolType);
		System.out.println("Tool brand: " + app.tool.toolBrand);
		System.out.println("Rental days: " + app.rentalDays);
		System.out.println("Check out date: " + sdf.format(c.getTime()));
		System.out.println("Due date: " + app.dueDate);
		System.out.println("Daily rental charge: " + formatter.format(app.tool.dailyRentalCharge));
		System.out.println("Charge days: " + app.chargeDays);
		System.out.println("Pre-discount charge: " + formatter.format(app.prediscountCharge));
		System.out.println("Discount percent: " + app.discountPercent + "%");
		System.out.println("Discount amount: " + formatter.format(app.discountAmount));
		System.out.println("Final charge: " + formatter.format(app.finalCharge));
	}
    
    public static Date IndependenceDay (int nYear) {
    	return new Date(nYear, 6, 4);
    }

    public static Date LaborDayObserved (int nYear) {
	    // The first Monday in September
	    int nX;
	    int nMonth = 8; // September
	    Date dtD;
	    dtD = new Date(nYear, 9, 1);
	    nX = dtD.getDay();
	    switch(nX)
	        {
	        case 0 : // Sunday
	        return new Date(nYear, nMonth, 2);
	        case 1 : // Monday
	        return new Date(nYear, nMonth, 7);
	        case 2 : // Tuesday
	        return new Date(nYear, nMonth, 6);
	        case 3 : // Wednesday
	        return new Date(nYear, nMonth, 5);
	        case 4 : // Thursday
	        return new Date(nYear, nMonth, 4);
	        case 5 : // Friday
	        return new Date(nYear, nMonth, 3);
	        default : // Saturday
	        return new Date(nYear, nMonth, 2);
	        }
    }

//    public static void main(String[] args) throws ParseException, InvalidNumberOfRentalDaysException, InvalidDiscountPercentException
//    {
//		RentalApp ra1 = RentalApp.checkout("JAKR", 5, 101, "9/3/15");
//		RentalApp.printRentalApp(ra1);
		
//		RentalApp ra2 = RentalApp.checkout("LADW", 3, 10, "7/2/20");
//		RentalApp.printRentalApp(ra2);
    	
//    	RentalApp ra3 = RentalApp.checkout("CHNS", 5, 25, "7/2/15");
//		RentalApp.printRentalApp(ra3);
		
//		RentalApp ra4 = RentalApp.checkout("JAKD", 6, 0, "9/3/20");
//		RentalApp.printRentalApp(ra4);
    	
//    	RentalApp ra5 = RentalApp.checkout("JAKR", 9, 0, "7/2/15");
//		RentalApp.printRentalApp(ra5);
		
//		RentalApp ra6 = RentalApp.checkout("JAKR", 4, 50, "7/2/20");
//		RentalApp.printRentalApp(ra6);
//    }
}
