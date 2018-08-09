package main;

public class User {
	private String userName;
	private String nickName;
	private String password;
	public User(String userName,String nickName,String password) {
		this.nickName = nickName;
		this.userName = userName;
		this.password = password;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public String getNickName() {
		return nickName;
	}
	
	public String getPassword() {
		return password;
	}
}
