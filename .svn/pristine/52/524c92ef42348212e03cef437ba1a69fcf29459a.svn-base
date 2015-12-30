package com.fullteem.yueba.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import android.text.TextUtils;

import com.fullteem.yueba.engine.pay.PayUtil;

public class EncryptUtil {
	public static void main(String[] a) throws Exception {
		runTest();
	}

	public static void runTest() {
		String input;
		String key = "m2u0m1u4";

		EncryptUtil des = new EncryptUtil();

		input = "123 456";
		try {
			printInfo(des, input, key);

			input = PayUtil.partner;
			printInfo(des, input, key);

			input = PayUtil.seller;
			printInfo(des, input, key);

			input = PayUtil.privateKey;
			printInfo(des, input, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void printInfo(EncryptUtil des, String input, String key)
			throws Exception {
		System.out.println("input:" + input);

		String result = EncryptUtil.encryption(input, key);
		System.out.println("len:" + result.length() + "  content:" + result);

		String decResult = EncryptUtil.decryption(result, key);

		if (TextUtils.equals(input, decResult)) {
			System.out.println("input:" + input + " parse correctly");
		} else {
			System.out.println("Wrong parsing:" + input);
			System.out.println(EncryptUtil.decryption(result, key));
		}

	}

	public static String encrypt(String content) {
		try {
			return encryption(content, PayUtil.MUMU_KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String decrypt(String content) {
		try {
			return decryption(content, PayUtil.MUMU_KEY);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * DES加密
	 * 
	 * @param plainData
	 * @param secretKey
	 * @return
	 * @throws Exception
	 */
	public static String encryption(String plainData, String secretKey)
			throws Exception {

		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(PayUtil.DES_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, generateKey(secretKey));

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {

		}

		try {
			// 为了防止解密时报javax.crypto.IllegalBlockSizeException: Input length must
			// be multiple of 8 when decrypting with padded cipher异常，
			// 不能把加密后的字节数组直接转换成字符串
			byte[] buf = cipher.doFinal(plainData.getBytes());

			return Base64Utils.encode(buf);

		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			throw new Exception("IllegalBlockSizeException", e);
		} catch (BadPaddingException e) {
			e.printStackTrace();
			throw new Exception("BadPaddingException", e);
		}

	}

	/**
	 * DES解密
	 * 
	 * @param secretData
	 * @param secretKey
	 * @return
	 * @throws Exception
	 */
	public static String decryption(String secretData, String secretKey)
			throws Exception {

		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(PayUtil.DES_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, generateKey(secretKey));

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new Exception("NoSuchAlgorithmException", e);
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			throw new Exception("NoSuchPaddingException", e);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			throw new Exception("InvalidKeyException", e);

		}

		try {

			byte[] buf = cipher.doFinal(Base64Utils.decode(secretData
					.toCharArray()));

			return new String(buf);

		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			throw new Exception("IllegalBlockSizeException", e);
		} catch (BadPaddingException e) {
			e.printStackTrace();
			throw new Exception("BadPaddingException", e);
		}
	}

	/**
	 * 获得秘密密钥
	 * 
	 * @param secretKey
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private static SecretKey generateKey(String secretKey)
			throws NoSuchAlgorithmException {
		SecureRandom secureRandom = new SecureRandom(secretKey.getBytes());

		// 为我们选择的DES算法生成一个KeyGenerator对象
		KeyGenerator kg = null;
		try {
			kg = KeyGenerator.getInstance(PayUtil.DES_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
		}
		kg.init(secureRandom);
		// kg.init(56, secureRandom);

		// 生成密钥
		return kg.generateKey();
	}

	static class Base64Utils {

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

		/**
		 * 将原始数据编码为base64编码
		 */
		static public String encode(byte[] data) {
			char[] out = new char[((data.length + 2) / 3) * 4];
			for (int i = 0, index = 0; i < data.length; i += 3, index += 4) {
				boolean quad = false;
				boolean trip = false;
				int val = (0xFF & data[i]);
				val <<= 8;
				if ((i + 1) < data.length) {
					val |= (0xFF & data[i + 1]);
					trip = true;
				}
				val <<= 8;
				if ((i + 2) < data.length) {
					val |= (0xFF & data[i + 2]);
					quad = true;
				}
				out[index + 3] = alphabet[(quad ? (val & 0x3F) : 64)];
				val >>= 6;
				out[index + 2] = alphabet[(trip ? (val & 0x3F) : 64)];
				val >>= 6;
				out[index + 1] = alphabet[val & 0x3F];
				val >>= 6;
				out[index + 0] = alphabet[val & 0x3F];
			}

			return new String(out);
		}

		/**
		 * 将base64编码的数据解码成原始数据
		 */
		static public byte[] decode(char[] data) {
			int len = ((data.length + 3) / 4) * 3;
			if (data.length > 0 && data[data.length - 1] == '=')
				--len;
			if (data.length > 1 && data[data.length - 2] == '=')
				--len;
			byte[] out = new byte[len];
			int shift = 0;
			int accum = 0;
			int index = 0;
			for (int ix = 0; ix < data.length; ix++) {
				int value = codes[data[ix] & 0xFF];
				if (value >= 0) {
					accum <<= 6;
					shift += 6;
					accum |= value;
					if (shift >= 8) {
						shift -= 8;
						out[index++] = (byte) ((accum >> shift) & 0xff);
					}
				}
			}
			if (index != out.length)
				throw new Error("miscalculated data length!");
			return out;
		}
	}

}
