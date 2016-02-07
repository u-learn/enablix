package com.enablix.commons.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;
import org.mindrot.jbcrypt.BCrypt;

public class EncryptionUtil {

	private static final BasicTextEncryptor textEncryptor = new BasicTextEncryptor();

	static {
		textEncryptor.setPassword("ao3RyS89@");
	}

	/**
	 * Default private constructor.
	 */
	private EncryptionUtil() {
	}

	/**
	 * Encrypts a string using {@link StrongPasswordEncryptor}
	 * 
	 * @param input
	 *            Plain string
	 * @return encrypted string.
	 */
	public static final String oneWayEncryptString(final String input) {
		return BCrypt.hashpw(input, BCrypt.gensalt());
	}

	/**
	 * Checks the given plain password against encrypted password.
	 * 
	 * @see BCrypt#checkpw(String, String)
	 * @param plainPassword
	 * @param encryptedPassword
	 * @return boolean
	 */
	public static boolean oneWayCheckPassword(String plainPassword,
			String encryptedPassword) {
		return BCrypt.checkpw(plainPassword, encryptedPassword);
	}

	public static final String twoWayEncryptString(final String message) {
		return textEncryptor.encrypt(message);
	}

	public static final String twoWayDecryptString(final String encryptedMessage) {
		return textEncryptor.decrypt(encryptedMessage);
	}

	/*
	 * Method to use for AES decryption.
	 * 
	 * @salt , @iv, @passphrase - different type of inputs(or salts used for
	 * decryption).
	 */

	public static final String getAesDecryptedString(
			final String encryptedText, AESParameterProvider aesParams) {
		if (encryptedText != null) {
			return AesUtil.decrypt(aesParams, encryptedText);
		}
		return "";
	}
	
	public static final String getAesEncryptedString(
			final String plainText, AESParameterProvider aesParams) {
		if (plainText != null) {
			return AesUtil.encrypt(aesParams, plainText);
		}
		return "";
	}

	public static void main(String args[]) {
		
		String hashed = twoWayEncryptString("67cc5f666c2111e589fc129d33c27fbe");
		
		System.out.println(hashed);
	}

	public static String sha256(final String str) {
		return DigestUtils.sha256Hex(str);
	}

}
