package com.hnctdz.aiLock.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.swetake.util.Qrcode;

public class TestQRCode {

	private static int pixoff;

	public static void main(String[] args) {
		createQRCode("http://www.baidu.com/", "d:/michael_QRCode.png", "d:/MENU_ICON_569_1414657199819.png");
	}

	/**
	 * 生成二维码(QRCode)图片
	 * 
	 * @param content
	 *            二维码图片的内容
	 * @param imgPath
	 *            生成二维码图片完整的路径
	 * @param ccbpath
	 *            二维码图片中间的logo路径
	 */
	public static int createQRCode(String content, String imgPath, String ccbPath) {
		try {
			Qrcode qrcodeHandler = new Qrcode();
			qrcodeHandler.setQrcodeErrorCorrect('M');
			qrcodeHandler.setQrcodeEncodeMode('B');
			qrcodeHandler.setQrcodeVersion(7);
			// System.out.println(content);
			byte[] contentBytes = content.getBytes("gb2312");
			BufferedImage bufImg = new BufferedImage(140, 140, BufferedImage.TYPE_INT_RGB);
			Graphics2D gs = bufImg.createGraphics();
			gs.setBackground(Color.WHITE);
			gs.clearRect(0, 0, bufImg.getWidth(), bufImg.getHeight());
			// 设定图像颜色 > BLACK 
			gs.setColor(Color.BLACK);
			// 设置偏移量 不设置可能导致解析出错 int pixoff = 2; // 输出内容 > 二维码
			if (contentBytes.length > 0 && contentBytes.length < 120) {
				boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);
				for (int i = 0; i < codeOut.length; i++) {
					for (int j = 0; j < codeOut.length; j++) {
						if (codeOut[j][i]) {
							gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
						}
					}
				}
			} else {
				System.err.println("QRCode content bytes length = " + contentBytes.length + " not in [ 0,120 ]. ");
				return -1;
			}

			BufferedImage img = ImageIO.read(new File(ccbPath));
			// 实例化一个Image对象。
			gs.drawImage(img, bufImg.getWidth() - img.getWidth() >> 1, bufImg.getHeight() - img.getHeight() >> 1, null);
			gs.dispose();
			bufImg.flush();
			// 生成二维码QRCode图片
			File imgFile = new File(imgPath);
			ImageIO.write(bufImg, "png", imgFile);
		} catch (Exception e) {
			e.printStackTrace();
			return -100;
		}
		return 0;
	}
}
