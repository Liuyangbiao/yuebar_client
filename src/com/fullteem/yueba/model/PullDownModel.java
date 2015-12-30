package com.fullteem.yueba.model;

import java.io.Serializable;
import java.util.List;

/**
 * 星座行业等下拉列表
 * 
 * @author ssy
 * 
 */
public class PullDownModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Constellation> constellation;
	private List<MusicStyleName> musicStyle;
	private List<IndustryName> industry;
	private List<Hobby> hobby;

	public List<Hobby> getHobby() {
		return hobby;
	}

	public void setHobby(List<Hobby> hobby) {
		this.hobby = hobby;
	}

	public List<Constellation> getConstellation() {
		return constellation;
	}

	public void setConstellation(List<Constellation> constellation) {
		this.constellation = constellation;
	}

	public List<MusicStyleName> getMusicStyle() {
		return musicStyle;
	}

	public void setMusicStyle(List<MusicStyleName> musicStyle) {
		this.musicStyle = musicStyle;
	}

	public List<IndustryName> getIndustry() {
		return industry;
	}

	public void setIndustry(List<IndustryName> industry) {
		this.industry = industry;
	}

	public class Constellation {
		private int constellationId;
		private String constellationName;

		public int getConstellationId() {
			return constellationId;
		}

		public void setConstellationId(int constellationId) {
			this.constellationId = constellationId;
		}

		public String getConstellationName() {
			return constellationName;
		}

		public void setConstellationName(String constellationName) {
			this.constellationName = constellationName;
		}

	}

	public class MusicStyleName {
		private int musicStyleId;
		private String musicStyleName;

		public int getMusicStyleId() {
			return musicStyleId;
		}

		public void setMusicStyleId(int musicStyleId) {
			this.musicStyleId = musicStyleId;
		}

		public String getMusicStyleName() {
			return musicStyleName;
		}

		public void setMusicStyleName(String musicStyleName) {
			this.musicStyleName = musicStyleName;
		}
	}

	public class IndustryName {
		private int industryId;
		private String industryName;

		public int getIndustryId() {
			return industryId;
		}

		public void setIndustryId(int industryId) {
			this.industryId = industryId;
		}

		public String getIndustryName() {
			return industryName;
		}

		public void setIndustryName(String industryName) {
			this.industryName = industryName;
		}

	}

	public class Hobby {
		private int hobbyId;
		private String hobbyName;

		public int getHobbyId() {
			return hobbyId;
		}

		public void setHobbyId(int hobbyId) {
			this.hobbyId = hobbyId;
		}

		public String getHobbyName() {
			return hobbyName;
		}

		public void setHobbyName(String hobbyName) {
			this.hobbyName = hobbyName;
		}
	}

}
