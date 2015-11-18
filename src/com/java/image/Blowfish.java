package com.java.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;

public class Blowfish {
	public long getEncryptTime() {
		return encryptTime;
	}
	public long getDecryptTime() {
		return decryptTime;
	}
	public long getOriginalSize() {
		return originalSize;
	}
	public long getEncryptedSize() {
		return encryptedSize;
	}
	public long getDecryptedSize() {
		return decryptedSize;
	}

	private long encryptTime,decryptTime,originalSize,encryptedSize,decryptedSize;
	byte[] skey = new byte[1000];
	String skeyString;
	static byte[] raw;
	String inputMessage, encryptedData, decryptedMessage;

	public Blowfish(String path) {
		try {
			generateSymmetricKey();

			// inputMessage=JOptionPane.showInputDialog(null,"Enter message to encrypt");
			// byte[] ibyte = inputMessage.getBytes();
			byte[] ibyte;
			BufferedImage originalImage = ImageIO
					.read(new File(path));

			// convert BufferedImage to byte array
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(originalImage, "bmp", baos);
			baos.flush();
			ibyte = baos.toByteArray();
			baos.close();
			originalSize=ibyte.length;
			System.out.println("Original Size = " + ibyte.length);
			System.out.println("Original Byte = " + ibyte);
			//
			long stime,etime;
			stime=System.currentTimeMillis();
			byte[] ebyte = encrypt(raw, ibyte);
			etime=System.currentTimeMillis();
			encryptTime=etime-stime;
			//String encryptedData = new String(ebyte);
			System.out.println("encrypted byte = " + ebyte);
			System.out.println("encrypted Size = " + ebyte.length);
			encryptedSize=ebyte.length;
			//System.out.println("Encrypted message " + encryptedData);
			// JOptionPane.showMessageDialog(null,"Encrypted Data "+"\n"+encryptedData);
			stime=System.currentTimeMillis();
			byte[] dbyte = decrypt(raw, ebyte);
			etime=System.currentTimeMillis();
			decryptTime=etime-stime;
			System.out.println("Encrypt time = " + encryptTime);
			System.out.println("Decrypt time = " + decryptTime);
			//String decryptedMessage = new String(dbyte);
			System.out.println("decrypted byte = " + dbyte);
			System.out.println("decrypted Size = " + dbyte.length);
			decryptedSize=dbyte.length;
			//System.out.println("Decrypted message " + decryptedMessage);

			// convert byte array back to BufferedImage
			InputStream in = new ByteArrayInputStream(dbyte);
			BufferedImage bImageFromConvert = ImageIO.read(in);

			ImageIO.write(bImageFromConvert, "bmp", new File(
					"/media/martuza/Application/Blowfish_decrypted.bmp"));

			// JOptionPane.showMessageDialog(null,"Decrypted Data "+"\n"+decryptedMessage);
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	void generateSymmetricKey() {
		try {
			Random r = new Random();
			int num = r.nextInt(10000);
			String knum = String.valueOf(num);
			byte[] knumb = knum.getBytes();
			skey = getRawKey(knumb);
			skeyString = new String(skey);
			System.out.println("Blowfish Symmetric key = " + skeyString);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private static byte[] getRawKey(byte[] seed) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("Blowfish");
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		sr.setSeed(seed);
		kgen.init(128, sr); // 128, 256 and 448 bits may not be available
		SecretKey skey = kgen.generateKey();
		raw = skey.getEncoded();
		return raw;
	}

	private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "Blowfish");
		Cipher cipher = Cipher.getInstance("Blowfish");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(clear);
		return encrypted;
	}

	private static byte[] decrypt(byte[] raw, byte[] encrypted)
			throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "Blowfish");
		Cipher cipher = Cipher.getInstance("Blowfish");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] decrypted = cipher.doFinal(encrypted);
		return decrypted;
	}
}
