package com.tuti.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class WchatAccount {

	private static final String JSON_FILE = "/Users/miaopeng/Desktop/微信号.json";

	public static void main(String args[]) {
		parseJson();
	}

	private static void parseJson() {
		List<User> users = new ArrayList<User>();
		List<User> origin_users = new ArrayList<User>();//原始id
		String content = "";
		try {
			content = readJsonFile(JSON_FILE);
		} catch (IOException e) {
			return;
		}
		User user = null;
		JSONArray jArray = (JSONArray) JSON.parse(content);
		int size = jArray.size();
		for (int i = 0; i < size; i++) {
			user = new User();
			JSONObject jUser = (JSONObject)jArray.get(i);
			user.setNick_name(jUser.getString("nick_name"));
			user.setWxid(jUser.getString("wxid"));
			
			if(user.getWxid().startsWith("wxid_")){
				origin_users.add(user);
			}else{
				users.add(user);
			}
		}
		System.out.println("origin size:"+origin_users.size());
		System.out.println("true size:"+users.size());
	}

	private static String readJsonFile(String filePath) throws IOException {
		StringBuffer sb = new StringBuffer();
		BufferedReader br = new BufferedReader(new FileReader(filePath));

		String line = null;
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		return sb.toString();
	}

	private static class User {
		private String nick_name;
		private String wxid;

		public User() {
			super();
			// TODO Auto-generated constructor stub
		}

		public String getNick_name() {
			return nick_name;
		}

		public void setNick_name(String nick_name) {
			this.nick_name = nick_name;
		}

		public String getWxid() {
			return wxid;
		}

		public void setWxid(String wxid) {
			this.wxid = wxid;
		}

	}
}
