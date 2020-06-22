package site.code4fun.constant;

public enum Queue{
	QUEUE_MAIL("mail.queue"),
	QUEUE_NOTIFY("notify.queue");
	
	private String val;

	Queue(String val) {
		this.val = val;
	}

	public String getVal() {
		return this.val;
	}
}