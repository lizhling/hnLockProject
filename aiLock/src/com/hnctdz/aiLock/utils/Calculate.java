package com.hnctdz.aiLock.utils;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.util.Calendar;

public class Calculate {
	public static byte[] getCallNameCommound(String id, byte index, String btMac, String phonMac, String key) {
		byte[] data = new byte[27];
		// 命令字
		data[0] = (byte) 0x80;
		data[1] = 0x46;

		{// 手机 mac
			String[] macNum = phonMac.split(":");
			for (int i = 0; i < macNum.length; ++i)
				data[2 + i] = (byte) Integer.parseInt(macNum[i], 16);
		}
		String yymmdd = null;
		{// 手机时间
			Calendar cale = Calendar.getInstance();
			int year = cale.get(Calendar.YEAR) % 100;
			int month = cale.get(Calendar.MONTH) + 1;
			int date = cale.get(Calendar.DAY_OF_MONTH);

			data[9] = (byte) (year / 10 * 16 + year % 10);
			data[10] = (byte) (month / 10 * 16 + month % 10);
			data[11] = (byte) (date / 10 * 16 + date % 10);

			yymmdd = String.format("%02d%02d%02d", (year & 0xff), month, date);

		}

		try {// 加密结果
			String str = btMac.replace(":", "");
			str += phonMac.replace(":", "");
			str += yymmdd;
			str += key;
			str = str.toLowerCase();
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(str.getBytes("UTF-8"));
			byte[] encryption = md5.digest();
			System.arraycopy(encryption, 0, data, 11, encryption.length);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return turnMeanEncode(encode(id, index, data));
	}

	/**
	 * (0x8041)蓝牙开/关锁
	 * 
	 * @param btMac
	 * @param phonMac
	 * @param key
	 * @return
	 */
	public static byte[] getLockCommound(String id, byte index, String btMac, String phonMac, String key, boolean is2Open) {

		byte[] data = new byte[28];

		// 命令字
		data[0] = (byte) 0x80;
		data[1] = 0x41;

		// 开锁
		if (is2Open)
			data[2] = 0x0;
		else
			data[2] = 0x1;

		{// 手机 mac
			String[] macNum = phonMac.split(":");
			for (int i = 0; i < macNum.length; ++i)
				data[3 + i] = (byte) Integer.parseInt(macNum[i], 16);
		}
		String yymmdd = null;
		{// 手机时间
			Calendar cale = Calendar.getInstance();
			int year = cale.get(Calendar.YEAR) % 100;
			int month = cale.get(Calendar.MONTH) + 1;
			int date = cale.get(Calendar.DAY_OF_MONTH);

			data[9] = (byte) (year / 10 * 16 + year % 10);
			data[10] = (byte) (month / 10 * 16 + month % 10);
			data[11] = (byte) (date / 10 * 16 + date % 10);

			yymmdd = String.format("%02d%02d%02d", (year & 0xff), month, date);

		}

		try {// 加密结果
			String str = btMac.replace(":", "");
			str += phonMac.replace(":", "");
			str += yymmdd;
			str += key;
			str = str.toLowerCase();
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(str.getBytes("UTF-8"));
			byte[] encryption = md5.digest();
			System.arraycopy(encryption, 0, data, 12, encryption.length);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return turnMeanEncode(encode(id, index, data));
	}

	private static byte[] encode(String id, byte index, byte[] data) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// 起始
		baos.write(0xaa);
		// 校验
		int checkcode = (data.length >> 8) & 0xff;
		checkcode ^= data.length & 0xff;

		// id
		if (id == null || id.length() < 20)
			return null;
		try {
			for (int i = 0; i < id.length(); i += 2) {
				baos.write(Integer.parseInt(id.substring(i, i + 2), 16) & 0xff);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}

		baos.write(index);// 流水号
		checkcode ^= index;

		// 消息体长
		baos.write((int) ((data.length >> 8) & 0xff));
		baos.write((int) (data.length & 0xff));

		// 消息体
		baos.write(data, 0, data.length);
		for (int i = 0; i < data.length; ++i)
			checkcode ^= data[i];

		baos.write(checkcode);

		// 结束
		baos.write(0xaa);

		return baos.toByteArray();
	}

	/**
	 * 正转义
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] turnMeanEncode(byte[] data) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(data, 0, 1);

		for (int i = 1; i < data.length - 1; ++i) {
			if (data[i] == 0xaa) {
				baos.write(0x55);
				baos.write(0x01);
			} else if (data[i] == 0x55) {
				baos.write(0x55);
				baos.write(0x02);
			} else
				baos.write(data[i]);
		}
		baos.write(data, data.length - 1, 1);

		return baos.toByteArray();
	}

	/**
	 * 反转义
	 * 
	 * @param data
	 * @return
	 */
	public byte[] turnMeandecode(byte[] data) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(data, 0, 1);

		for (int i = 1; i < data.length - 1; ++i) {
			if (data[i] == 0x55) {
				if (data[++i] == 0x01) {
					baos.write(0xaa);
				} else {
					baos.write(0x55);
				}
			} else
				baos.write(data[i]);
		}
		baos.write(data, data.length - 1, 1);

		return baos.toByteArray();
	}

}
