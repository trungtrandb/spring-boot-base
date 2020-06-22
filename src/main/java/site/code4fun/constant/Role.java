package site.code4fun.constant;

public enum Role {
	ROLE_ADMIN("ROLE_ADMIN"),
	ROLE_USER("ROLE_USER");
	
	private String val;

	Role(String val) {
		this.val = val;
	}

	public String getVal() {
		return this.val;
	}
}
