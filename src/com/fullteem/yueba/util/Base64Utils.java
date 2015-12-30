package com.fullteem.yueba.util;

public class Base64Utils {
	static private char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="
			.toCharArray();
	static private byte[] codes = new byte[256];
	static {
		for (int i = 0; i < 256; i++)
			codes[i] = -1;
		for (int i = 'A'; i <= 'Z'; i++)
			codes[i] = (byte) (i - 'A');
		for (int i = 'a'; i <= 'z'; i++)
			codes[i] = (byte) (26 + i - 'a');
		for (int i = '0'; i <= '9'; i++)
			codes[i] = (byte) (52 + i - '0');
		codes['+'] = 62;
		codes['/'] = 63;
	}

	public static void main(String[] a) throws Exception {
		runTest();
	}

	public static void runTest() {
		String source = "MzQ2MjQ2NDMzMg==_1423465435726";

		decode(source);
	}

	static public String decode(String data) {
		System.out.println("to decode:" + data);
		String str = new String(Base64.decode(data));
		System.out.println("decoded:" + str);
		return str;
	}

	static public String encode(String data) {
		System.out.println("to encode:" + data);
		String str = Base64.encode(data.getBytes());
		System.out.println("encoded:" + str);

		return str;
	}

}
