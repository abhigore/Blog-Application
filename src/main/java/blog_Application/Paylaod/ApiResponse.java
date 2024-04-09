package blog_Application.Paylaod;

import org.springframework.stereotype.Component;

@Component
public class ApiResponse {
	
	private String messeage;
	
	private boolean success;

	public String getMesseage() {
		return messeage;
	}

	public void setMesseage(String messeage) {
		this.messeage = messeage;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public ApiResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ApiResponse(String messeage, boolean success) {
		super();
		this.messeage = messeage;
		this.success = success;
	}
	
	
	

}
