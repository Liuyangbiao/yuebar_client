package com.fullteem.yueba.service;

import java.io.Serializable;

import com.fullteem.yueba.model.CommonModel;

public class ServerHostModel extends CommonModel implements Serializable {
	private static final long serialVersionUID = 1L;

	private static ServerHostModel instance;

	private ServerHostModel() {
	};

	public static synchronized ServerHostModel getInstance() {
		if (instance == null) {
			instance = new ServerHostModel();
		}
		return instance;
	}

	private ServerHostDataModel result;

	public ServerHostDataModel getResult() {
		return result;
	}

	public void setResult(ServerHostDataModel result) {
		this.result = result;
	}

	public class ServerHostDataModel implements Serializable{
		private int eJpush; // Jpush开关，0 false 1 true
		private int eRb; // Robot开关
		private int eTingyun; // Tingyun 开关
		private int eUmeng; // Umeng 开关
		private String host;
		private String imgUrl;
		private int intExt1;
		private int intExt2;
		private int intExt3;
		private String strExt1;
		private String strExt2;
		private String strExt3;

		public int getEJpush() {
			return eJpush;
		}

		public void setEJpush(int eJpush) {
			this.eJpush = eJpush;
		}

		public int getERb() {
			return eRb;
		}

		public void setERb(int eRb) {
			this.eRb = eRb;
		}

		public int getETingyun() {
			return eTingyun;
		}

		public void setETingyun(int eTingyun) {
			this.eTingyun = eTingyun;
		}

		public int getEUmeng() {
			return eUmeng;
		}

		public void setEUmeng(int eUmeng) {
			this.eUmeng = eUmeng;
		}

		public String getImgUrl() {
			return imgUrl;
		}

		public void setImgUrl(String imgUrl) {
			this.imgUrl = imgUrl;
		}

		public int getIntExt1() {
			return intExt1;
		}

		public void setIntExt1(int intExt1) {
			this.intExt1 = intExt1;
		}

		public int getIntExt2() {
			return intExt2;
		}

		public void setIntExt2(int intExt2) {
			this.intExt2 = intExt2;
		}

		public int getIntExt3() {
			return intExt3;
		}

		public void setIntExt3(int intExt3) {
			this.intExt3 = intExt3;
		}

		public String getStrExt1() {
			return strExt1;
		}

		public void setStrExt1(String strExt1) {
			this.strExt1 = strExt1;
		}

		public String getStrExt2() {
			return strExt2;
		}

		public void setStrExt2(String strExt2) {
			this.strExt2 = strExt2;
		}

		public String getStrExt3() {
			return strExt3;
		}

		public void setStrExt3(String strExt3) {
			this.strExt3 = strExt3;
		}

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

	}

}
