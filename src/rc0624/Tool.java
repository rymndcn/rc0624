package rc0624;

public class Tool {
	String toolCode;
	String toolType;
	String toolBrand;
	double dailyRentalCharge;
	boolean weekendCharge;
	boolean holidayCharge;
	
	public Tool(String toolCode, String toolType, String toolBrand, double dailyRentalCharge, boolean weekendCharge, boolean holidayCharge) {
		this.toolCode = toolCode;
		this.toolType = toolType;
		this.toolBrand = toolBrand;
		this.dailyRentalCharge = dailyRentalCharge;
		this.weekendCharge = weekendCharge;
		this.holidayCharge = holidayCharge;
	}


	public static Tool findTool(String toolCode) throws IllegalToolCodeException {
		switch (toolCode) {
		case "CHNS":
			return new Tool("CHNS", "Chainsaw", "Stihl", 1.49, false, true);
		case "LADW":
			return new Tool("LADW", "Ladder", "Werner", 1.99, true, false);
		case "JAKD":
			return new Tool("JAKD", "Jackhammer", "DeWalt", 2.99, false, false);
		case "JAKR":
			return new Tool("JAKR", "Jackhammer", "Ridgid", 2.99, false, false);
		default:  
			throw new IllegalToolCodeException(String.format("Code '%s' does not exist", toolCode));
//			return new Tool("INVALID", "INVALID", "INVALID", 0, false, false);
		}
	}
}
