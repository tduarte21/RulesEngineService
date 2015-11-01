package com.es.rulesengine.model;

public class TV {

	public static final int OFF = 0;
	public static final int ON = 1;

	public static final int ACTION_IDLE = 0;
	public static final int ACTION_VOL_DOWN = 1;
	public static final int ACTION_VOL_UP = 2;
	public static final int ACTION_PREV_CHANNEL = 3;
	public static final int ACTION_NEXT_CHANNEL = 4;

	private int status;
	private int nextStatus;

	private int action;

	private String message;

	public int getAction() {
		return this.action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public int getNextStatus() {
		return this.nextStatus;
	}

	public void setNextStatus(int nextStatus) {
		this.nextStatus = nextStatus;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
