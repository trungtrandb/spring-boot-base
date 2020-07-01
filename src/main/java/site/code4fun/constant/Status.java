package site.code4fun.constant;

public enum Status {
	ACTIVE("ACTIVE"),
	INACTIVE("INACTIVE"),
	PENDING("PENDING"),
	COMPLETE("COMPLETE"),
	ERROR("ERROR"),
	PUBLIC("PUBLIC"),
	PRIVATE("PRIVATE"),
	LOCK("LOCK");
	
	
	private String val;

	Status(String val) {
		this.val = val;
	}

	public String getVal() {
		return this.val;
	}
}
