package com.fullteem.yueba.model;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月10日&emsp;上午11:26:50
 * @use 消费记录bean
 */
public class ConsumeRecordsModel {
	private String ConsumeDate; // 消费时间
	private String ConsumeContent; // 消费内容

	public ConsumeRecordsModel(String consumeDate, String consumeContent) {
		ConsumeDate = consumeDate;
		ConsumeContent = consumeContent;
	}

	public ConsumeRecordsModel() {
	}

	public String getConsumeDate() {
		return ConsumeDate;
	}

	public void setConsumeDate(String consumeDate) {
		ConsumeDate = consumeDate;
	}

	public String getConsumeContent() {
		return ConsumeContent;
	}

	public void setConsumeContent(String consumeContent) {
		ConsumeContent = consumeContent;
	}

}
