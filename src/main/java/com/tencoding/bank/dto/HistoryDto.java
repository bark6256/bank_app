package com.tencoding.bank.dto;

import java.sql.Timestamp;
import java.text.DecimalFormat;

import com.tencoding.bank.util.TimestampUtil;

import lombok.Data;

@Data
public class HistoryDto {
	private Integer id;
	private Long amount;
	private Long balance;
	private String sender;
	private String receiver;
	private Timestamp createdAt;
	
	public String formatCreatedAt() {
		return TimestampUtil.timestampToString(createdAt);
	}
	
	// 금액 처리
	public String formatBalance() {
		DecimalFormat df = new DecimalFormat("#,###");
		return df.format(balance);
	}
	public String formatAmount() {
		DecimalFormat df = new DecimalFormat("#,###");
		return df.format(amount);
	}
}
