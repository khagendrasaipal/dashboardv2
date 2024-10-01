package org.saipal.dboard.service;

public class User {
	private String userid;
	private String username;
	private String password;
	private String orgid;
	private String usertype;

//	public User(String string, String string2, String string3, String string4) {
//
//	}

	public User(String id, String user, String pass,String orgids, String string) {
		userid = id;
		username = user;
		password = pass;
		orgid= orgids;
		usertype=string;
	}

	public String getOrgid() {
		return orgid;
	}

	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}

	public String getUserid() {
		return userid;
	}

	public String getUsertype() {
		return usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
