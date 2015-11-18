package com.java.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.security.SecureRandom;

import javax.imageio.ImageIO;

public class XOR {
	public long getDuration() {
		return duration;
	}
	public long getOriginalSize() {
		return originalSize;
	}
	public long getXORedSize() {
		return XORedSize;
	}
	private long duration,originalSize,XORedSize;
	public XOR(String sPath) {
		String dPath, password;
		//sPath = "E:/cortana.bmp";
		dPath = "/media/martuza/Application/XOR_decrypted.bmp";
		password = "passwordToSecureMyImage";
		StringBuilder sb = new StringBuilder();
		char st;
		int value;
		try {
			
			SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
			sr.setSeed(sb.toString().getBytes());
			BufferedImage FSImg = ImageIO.read(new File(sPath));
			//
			byte[] ibyte;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(FSImg, "bmp", baos);
			baos.flush();
			ibyte = baos.toByteArray();
			baos.close();
			System.out.println("Original Size = " + ibyte.length);
			originalSize=ibyte.length;
			
			//
			long stime = System.currentTimeMillis();
			for (int i = 0; i < password.length(); i++) {
				st = password.charAt(i);
				value = (int) st;
				sb.append(value);
			}
			for (int w = 0; w < FSImg.getWidth(); w++) {
				for (int h = 0; h < FSImg.getHeight(); h++) {
					Color color = new Color(FSImg.getRGB(w, h));
					Color newColor = new Color(
							color.getRed() ^ sr.nextInt(255), color.getGreen()
									^ sr.nextInt(255), color.getBlue()
									^ sr.nextInt(255));
					FSImg.setRGB(w, h, newColor.getRGB());
				}
			}
			long etime = System.currentTimeMillis();
			duration= etime - stime;
			System.out.println("Duration = " + duration + " mili-sec");
			//
			baos = new ByteArrayOutputStream();
			ImageIO.write(FSImg, "bmp", baos);
			baos.flush();
			ibyte = baos.toByteArray();
			baos.close();
			System.out.println("encrypted Size = " + ibyte.length);
			XORedSize=ibyte.length;
			//
			System.out.println("Process Completed!!..");
			ImageIO.write(FSImg, "bmp", new File(dPath));
			System.out.println("Wrote to " + dPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
