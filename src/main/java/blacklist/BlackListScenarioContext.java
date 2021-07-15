package blacklist;

public class BlackListScenarioContext {
	String userinfotype;
	String userinfo;
	String code;
	int httpCode;
	Integer blackListItemId;
	Integer analystId;
	private boolean permission=true;

	public Integer getBlackListItemId() {
		return blackListItemId;
	}

	public void setBlackListItemId(Integer blackListItemId) {
		this.blackListItemId = blackListItemId;
	}

	public Integer getAnalystId() {
		return analystId;
	}

	public void setAnalystId(Integer analystId) {
		this.analystId = analystId;
	}

	public int getHttpCode() {
		return httpCode;
	}

	public void setHttpCode(int httpCode) {
		this.httpCode = httpCode;
	}

	public void setScenarioContext(String userinfotype, String userinfo) {
		this.userinfotype = userinfotype;
		this.userinfo = userinfo;
	}

	public String getUserinfotype() {
		return userinfotype;
	}

	public void setUserinfotype(String userinfotype) {
		this.userinfotype = userinfotype;
	}

	public String getUserinfo() {
		return userinfo;
	}

	public void setUserinfo(String userinfo) {
		this.userinfo = userinfo;
	}

	public String getCode() {
		if (code == null || code.equals("")) {
			code = "bl_approve";
		}
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public boolean getPermission() {
		return this.permission;
		
	}
	public void setPermission(boolean permission) {
		this.permission = permission;
	}

}
