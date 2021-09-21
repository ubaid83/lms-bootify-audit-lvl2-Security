package com.spts.lms.web.utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.apache.log4j.Logger;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.fusionauth.der.DerDecodingException;
import io.fusionauth.der.DerInputStream;
import io.fusionauth.der.DerValue;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

public class RSAConfig {
	private static final Logger logger = Logger.getLogger(RSAConfig.class);
	public static String decrypt(byte[] data, PrivateKey privateKey) throws NoSuchPaddingException,
			NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return new String(cipher.doFinal(data));
	}

	public static String decrypt(String data, String base64PrivateKey) throws IllegalBlockSizeException,
			InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
		try {
			return decrypt(Base64.getDecoder().decode(data.getBytes()),
					pemFileLoadPrivateKeyPkcs1OrPkcs8Encoded(base64PrivateKey));
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
		return base64PrivateKey;
	}

	public static PrivateKey pemFileLoadPrivateKeyPkcs1OrPkcs8Encoded(String privateKeyPem)
			throws GeneralSecurityException {
		// PKCS#8 format
		final String PEM_PRIVATE_START = "-----BEGIN PRIVATE KEY-----";
		final String PEM_PRIVATE_END = "-----END PRIVATE KEY-----";

		// PKCS#1 format
		final String PEM_RSA_PRIVATE_START = "-----BEGIN RSA PRIVATE KEY-----";
		final String PEM_RSA_PRIVATE_END = "-----END RSA PRIVATE KEY-----";

		if (privateKeyPem.indexOf(PEM_PRIVATE_START) != -1) { // PKCS#8 format
			privateKeyPem = privateKeyPem.replace(PEM_PRIVATE_START, "").replace(PEM_PRIVATE_END, "");
			privateKeyPem = privateKeyPem.replaceAll("\\s", "");

			byte[] pkcs8EncodedKey = Base64.getDecoder().decode(privateKeyPem);

			KeyFactory factory = KeyFactory.getInstance("RSA");
			return factory.generatePrivate(new PKCS8EncodedKeySpec(pkcs8EncodedKey));

		} else if (privateKeyPem.indexOf(PEM_RSA_PRIVATE_START) != -1) { // PKCS#1 format

			privateKeyPem = privateKeyPem.replace(PEM_RSA_PRIVATE_START, "").replace(PEM_RSA_PRIVATE_END, "");
			privateKeyPem = privateKeyPem.replaceAll("\\s", "");

			DerInputStream derReader = new DerInputStream(Base64.getDecoder().decode(privateKeyPem));

			DerValue[] seq = null;
			try {
				seq = derReader.getSequence();
			} catch (DerDecodingException e) {
				e.printStackTrace();
			}
			if (seq.length < 9) {
				throw new GeneralSecurityException("Could not parse a PKCS1 private key.");
			}
			BigInteger modulus = seq[1].getBigInteger();
			BigInteger publicExp = seq[2].getBigInteger();
			BigInteger privateExp = seq[3].getBigInteger();
			BigInteger prime1 = seq[4].getBigInteger();
			BigInteger prime2 = seq[5].getBigInteger();
			BigInteger exp1 = seq[6].getBigInteger();
			BigInteger exp2 = seq[7].getBigInteger();
			BigInteger crtCoef = seq[8].getBigInteger();
			RSAPrivateCrtKeySpec keySpec = new RSAPrivateCrtKeySpec(modulus, publicExp, privateExp, prime1, prime2,
					exp1, exp2, crtCoef);
			KeyFactory factory = KeyFactory.getInstance("RSA");
			return factory.generatePrivate(keySpec);
		}
		throw new GeneralSecurityException("Not supported format of a private key");
	}

	public static Key getPublicKeyFromString(String public_key) throws GeneralSecurityException, IOException {
		public_key =  public_key.replace("-----BEGIN PUBLIC KEY-----", "")
			    .replace("-----END PUBLIC KEY-----", "");;
		byte[] data = Base64.getDecoder().decode(public_key);
		X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
		KeyFactory fact = KeyFactory.getInstance("RSA");
		return fact.generatePublic(spec);
	}

	public static String encrypt(byte[] cipherText, String public_key) throws Exception {
		String result_data = null;
		try {
			PublicKey publicKey = (RSAPublicKey) getPublicKeyFromString(public_key);
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] result = cipher.doFinal(cipherText);
			result_data = Base64.getEncoder().encodeToString(result);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return result_data;
	}
	
	public static String jwtTokenValidator(String token, String secret) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			JWTVerifier verifier = JWT.require(algorithm).acceptLeeway(60).build(); // valid for 60 seconds only
			DecodedJWT jwt = verifier.verify(token);
			logger.info("jwt getClaim token data :::::>>>>> " + jwt.getClaim("data")); // this claim should be same as it is passed in INFRA project
			return jwt.getClaim("data").toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String createJwtToken(String token, String secret) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			long expMillis = System.currentTimeMillis() + 60000; // valid for 60 seconds only
		    Date exp = new Date(expMillis);
			String encrypted_with_jwt = JWT.create().withClaim("data", token).withExpiresAt(exp).sign(algorithm); // this claim should be same as it is expected in INFRA project
			return encrypted_with_jwt;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}