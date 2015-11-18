package com.java.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Hex;

public class RSA {
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

	private KeyPair keypair;
	private Cipher cipher;
	private long encryptTime, decryptTime, originalSize, encryptedSize,
			decryptedSize;

	public RSA(String path) {
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(1024);
			this.keypair = kpg.generateKeyPair();
			this.cipher = Cipher.getInstance("RSA");

			byte[] ibyte;
			BufferedImage originalImage = ImageIO.read(new File(path));

			// convert BufferedImage to byte array
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(originalImage, "bmp", baos);
			baos.flush();
			ibyte = baos.toByteArray();
			baos.close();

			originalSize = ibyte.length;

			long stime, etime;
			stime = System.currentTimeMillis();
			byte[] encrypted = encrypt(ibyte);
			etime = System.currentTimeMillis();
			encryptTime = etime - stime;
			encryptedSize = encrypted.length;

			System.out.println("encryption done");

			stime = System.currentTimeMillis();
			char[] encryptedTranspherable = Hex.encodeHex(encrypted);
			String encryptedString = new String(encryptedTranspherable);
			byte[] decrypted = decrypt(encryptedString);
			etime = System.currentTimeMillis();
			decryptTime = etime - stime;

			decryptedSize = decrypted.length;

			InputStream in = new ByteArrayInputStream(decrypted);
			BufferedImage bImageFromConvert = ImageIO.read(in);

			ImageIO.write(
					bImageFromConvert,
					"bmp",
					new File(
							"/media/martuza/Application/rsa_decrypted.bmp"));

			System.out.println("decryption done");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// public static void main(String[] args) throws IOException {
	//
	// try {
	// RSA rsa = new RSA();
	//
	// byte[] ibyte;
	// BufferedImage originalImage = ImageIO.read(new File(
	// "E://cortana.bmp"));
	//
	// // convert BufferedImage to byte array
	// ByteArrayOutputStream baos = new ByteArrayOutputStream();
	// ImageIO.write(originalImage, "bmp", baos);
	// baos.flush();
	// ibyte = baos.toByteArray();
	// baos.close();
	//
	// byte[] encrypted = rsa.encrypt(ibyte);
	//
	// System.out.println("encryption done");
	//
	// char[] encryptedTranspherable = Hex.encodeHex(encrypted);
	// String encryptedString = new String(encryptedTranspherable);
	// byte[] decrypted = rsa.decrypt(encryptedString);
	//
	// InputStream in = new ByteArrayInputStream(decrypted);
	// BufferedImage bImageFromConvert = ImageIO.read(in);
	//
	// ImageIO.write(bImageFromConvert, "bmp", new File(
	// "E://rsa_decrypted-cortana.bmp"));
	//
	// System.out.println("decryption done");
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	public byte[] encrypt(byte[] bytes) throws Exception {
		this.cipher.init(Cipher.ENCRYPT_MODE, this.keypair.getPublic());
		// byte[] bytes = plaintext.getBytes("UTF-8");

		byte[] encrypted = blockCipher(bytes, Cipher.ENCRYPT_MODE);

		return encrypted;

		// char[] encryptedTranspherable = Hex.encodeHex(encrypted);
		// System.out.println("encryption done");
		// return new String(encryptedTranspherable);
	}

	public byte[] decrypt(String encrypted) throws Exception {
		this.cipher.init(Cipher.DECRYPT_MODE, this.keypair.getPrivate());
		byte[] bts = Hex.decodeHex(encrypted.toCharArray());

		byte[] decrypted = blockCipher(bts, Cipher.DECRYPT_MODE);

		return decrypted;

		// return new String(decrypted, "UTF-8");
	}

	private byte[] blockCipher(byte[] bytes, int mode)
			throws IllegalBlockSizeException, BadPaddingException {
		// string initialize 2 buffers.
		// scrambled will hold intermediate results
		byte[] scrambled = new byte[0];

		// toReturn will hold the total result
		byte[] toReturn = new byte[0];
		// if we encrypt we use 100 byte long blocks. Decryption requires 128
		// byte long blocks (because of RSA)
		int length = (mode == Cipher.ENCRYPT_MODE) ? 100 : 128;

		// another buffer. this one will hold the bytes that have to be modified
		// in this step
		byte[] buffer = new byte[length];

		for (int i = 0; i < bytes.length; i++) {

			// if we filled our buffer array we have our block ready for de- or
			// encryption
			if ((i > 0) && (i % length == 0)) {
				// execute the operation
				scrambled = cipher.doFinal(buffer);
				// add the result to our total result.
				toReturn = append(toReturn, scrambled);
				// here we calculate the length of the next buffer required
				int newlength = length;

				// if newlength would be longer than remaining bytes in the
				// bytes array we shorten it.
				if (i + length > bytes.length) {
					newlength = bytes.length - i;
				}
				// clean the buffer array
				buffer = new byte[newlength];
			}
			// copy byte into our buffer.
			buffer[i % length] = bytes[i];
		}

		// this step is needed if we had a trailing buffer. should only happen
		// when encrypting.
		// example: we encrypt 110 bytes. 100 bytes per run means we "forgot"
		// the last 10 bytes. they are in the buffer array
		scrambled = cipher.doFinal(buffer);

		// final step before we can return the modified data.
		toReturn = append(toReturn, scrambled);

		return toReturn;
	}

	private byte[] append(byte[] prefix, byte[] suffix) {
		byte[] toReturn = new byte[prefix.length + suffix.length];
		for (int i = 0; i < prefix.length; i++) {
			toReturn[i] = prefix[i];
		}
		for (int i = 0; i < suffix.length; i++) {
			toReturn[i + prefix.length] = suffix[i];
		}
		return toReturn;
	}
}
