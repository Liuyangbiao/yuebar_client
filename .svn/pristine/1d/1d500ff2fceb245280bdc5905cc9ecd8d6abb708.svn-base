package com.fullteem.yueba.model;

import java.io.Serializable;

import com.simple.util.db.annotation.SimpleColumn;
import com.simple.util.db.annotation.SimpleId;
import com.simple.util.db.annotation.SimpleTable;

@SimpleTable(name = "t_city")
public class City implements Serializable {
	private static final long serialVersionUID = 8903041678093405987L;
	private boolean choosed;
	@SimpleId
	@SimpleColumn(name = "CID")
	private Integer CID;
	@SimpleColumn(name = "CPinyin")
	private String CPinyin;
	@SimpleColumn(name = "CName")
	private String CName;
	@SimpleColumn(name = "PID")
	private Integer PID;
	@SimpleColumn(name = "PName")
	private String PName;

	public Integer getCID() {
		return CID;
	}

	public void setCID(Integer cID) {
		CID = cID;
	}

	public String getCPinyin() {
		return CPinyin;
	}

	public void setCPinyin(String cPinyin) {
		CPinyin = cPinyin;
	}

	public String getCName() {
		return CName;
	}

	public void setCName(String cName) {
		CName = cName;
	}

	public boolean isChoosed() {
		return choosed;
	}

	public void setChoosed(boolean choosed) {
		this.choosed = choosed;
	}

	public Integer getPID() {
		return PID;
	}

	public void setPID(Integer pID) {
		PID = pID;
	}

	public String getPName() {
		return PName;
	}

	public void setPName(String pName) {
		PName = pName;
	}

	@Override
	public String toString() {
		return "City [choosed=" + choosed + ", CID=" + CID + ", CPinyin="
				+ CPinyin + ", CName=" + CName + ", PID=" + PID + ", PName="
				+ PName + "]";
	}
}
