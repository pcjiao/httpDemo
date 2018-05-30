package com.dhb.http;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 编码
 * 
 * @author rsh
 *
 */
public class EncodeUtil {

	private static final String DEFAULT_URL_ENCODING = "UTF-8";

	public static String encode(byte[] plain) {
		return byteToHexStr(plain);
	}

	/**
	 * 将16进制字符串转换为二进制
	 * 
	 * @param hexStr
	 * @return
	 */
	public static byte[] hexStrToByte(String hexStr) {
		if (hexStr == null) {
			return null;
		}

		byte[] result = new byte[hexStr.length() / 2];

		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}

		return result;
	}

	/**
	 * 将二进制转换成16进制字符串
	 * 
	 * @param buf
	 * @return
	 */
	public static String byteToHexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);

			if (hex.length() == 1) {
				hex = '0' + hex;
			}

			sb.append(hex);
		}

		return sb.toString();
	}

	/**
	 * base64编码
	 * 
	 * @param value
	 * @return
	 */
	public static String base64Encode(byte[] value) {
		return new String(Base64.encodeBase64(value));
	}

	/**
	 * Base64编码, URL安全(将Base64中的URL非法字符如+,/=转为其他字符, 见RFC3548).
	 */
	public static String base64UrlSafeEncode(byte[] input) {
		return Base64.encodeBase64URLSafeString(input);
	}

	/**
	 * base64解码
	 * 
	 * @param key
	 * @return
	 */
	public static byte[] base64Decode(String key) {
		return Base64.decodeBase64(key);
	}

	/**
	 * URL 编码, Encode默认为UTF-8.
	 */
	public static String urlEncode(String input) {
		try {
			return URLEncoder.encode(input, DEFAULT_URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("Unsupported Encoding Exception", e);
		}
	}

	/**
	 * URL 解码, Encode默认为UTF-8.
	 */
	public static String urlDecode(String input) {
		try {
			return URLDecoder.decode(input, DEFAULT_URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("Unsupported Encoding Exception", e);
		}
	}

}
