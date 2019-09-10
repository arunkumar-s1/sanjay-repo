package com.msf.libsb.utils.keygeneration;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

public class CryptoRSA
{
	private static final Charset UTF8Charset = Charset.forName("UTF-8");
	public static final int KEYSIZE = 2048;
	private static final int byteboundary = 8;
	private static final int byteDifference = 11;

	public final NestKeyPair generateNestKeyPair(int keySize)
	{
		NestKeyPair keyPair = new NestKeyPair();
		try
		{
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(keySize);
			KeyPair key = keyPairGenerator.generateKeyPair();

			PublicKey pubkey = key.getPublic();
			PrivateKey prvkey = key.getPrivate();
			keyPair.setPrivateKey(prvkey);
			keyPair.setPublicKey(pubkey);
		} 
		catch (NoSuchAlgorithmException e)
		{
			// log.catching(e);
		}
		return keyPair;
	}

	public final NestKeyPair generateNestKeyPair(String pemPrivateKey) 
	{
		NestKeyPair pair = new NestKeyPair();
		PemReader reader = new PemReader(new StringReader(pemPrivateKey));
		try 
		{
			PemObject pemObject = reader.readPemObject();
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pemObject.getContent());

			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			pair.setPrivateKey(keyFactory.generatePrivate(keySpec));
			RSAPrivateKey privateKey = (RSAPrivateKey) pair.getPrivateKey();

			RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(privateKey.getModulus(), BigInteger.valueOf(65537L));
			PublicKey publickKey = keyFactory.generatePublic(publicKeySpec);
			pair.setPublicKey(publickKey);
		} 
		catch (IOException e)
		{

		}
		catch (NoSuchAlgorithmException e)
		{

		} 
		catch (InvalidKeySpecException e) 
		{

		}
		return pair;
	}

	public final PublicKey getPublicKeyFromPemData(String pemPublicKey) 
	{
		PublicKey publicKey = null;
		PemReader reader = new PemReader(new StringReader(pemPublicKey));
		try 
		{
			PemObject pemObject = reader.readPemObject();

			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pemObject.getContent());
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");

			publicKey = keyFactory.generatePublic(keySpec);
		}
		catch (IOException e)
		{

		} 
		catch (NoSuchAlgorithmException e)
		{

		} 
		catch (InvalidKeySpecException e)
		{

		}
		return publicKey;
	}

	public final String encryptBlock(Key key, String plainText) 
	{
		String cipherText = null;
		Security.addProvider(new BouncyCastleProvider());
		Cipher cipher = null;
		try 
		{
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(1, key);
			byte[] result = cipher.doFinal(plainText.getBytes(UTF8Charset));
			cipherText = new String(Base64.encodeBase64(result), UTF8Charset);
		} 
		catch (NoSuchAlgorithmException e)
		{

		} 
		catch (NoSuchPaddingException e)
		{

		} 
		catch (IllegalBlockSizeException e) 
		{

		} 
		catch (BadPaddingException e)
		{

		} 
		catch (InvalidKeyException e) 
		{

		}
		return cipherText;
	}

	public final String decryptBlock(Key key, String cipherText) 
	{
		String plainText = null;
		Security.addProvider(new BouncyCastleProvider());
		Cipher cipher = null;
		try
		{
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(2, key);
			byte[] original = cipher.doFinal(Base64.decodeBase64(cipherText.getBytes(UTF8Charset)));
			plainText = new String(original, UTF8Charset);
		} 
		catch (NoSuchAlgorithmException | NoSuchPaddingException e)
		{

		} 
		catch (IllegalBlockSizeException e)
		{

		} 
		catch (BadPaddingException e)
		{

		} 
		catch (InvalidKeyException e) 
		{

		}
		return plainText;
	}

	public final String decrypt(String encryptedText, PrivateKey privateKey) 
	{
		StringBuffer buffer = new StringBuffer();
		String decodedCipherText = new String(Base64.decodeBase64(encryptedText), UTF8Charset);
		String[] splitCipherText = decodedCipherText.split("\\n");
		for (int i = 0; i < splitCipherText.length; i += 1)
		{
			String blockCipherText = splitCipherText[i];
			buffer.append(decryptBlock(privateKey, blockCipherText));
		}
		return buffer.toString();
	}

	public final String encrypt(String plainText, PublicKey publicKey)
	{
		byte[] src = plainText.getBytes(UTF8Charset);
		StringBuffer buffer = new StringBuffer();
		int numberOfBytes = 245;
		try 
		{
			int start = 0;
			int end = 245;
			if (245 > src.length)
			{
				end = src.length;
			}
			do 
			{
				byte[] bytes = copyByteArray(src, start, end);
				buffer.append(encryptBlock(publicKey, IOUtils.toString(bytes, UTF8Charset.toString())));

				buffer.append('\n');

				start = end;
				end += 245;
				if (end > src.length)
				{
					end = src.length;
				}
			} 
			while (end < src.length);
			if (isNotNegative(start, end)) 
			{
				byte[] bytes = copyByteArray(src, start, end);
				buffer.append(encryptBlock(publicKey, IOUtils.toString(bytes, UTF8Charset.toString())));
				buffer.append('\n');
			}
		}
		catch (IOException e) 
		{
			// log.catching(e);
		}
		return Base64.encodeBase64String(buffer.toString().getBytes(UTF8Charset));
	}

	private byte[] copyByteArray(byte[] src, int start, int end)
	{
		byte[] bytes = new byte[end - start];
		int bytesCounter = 0;
		for (int i = start; i < end; i += 1)
		{
			bytes[bytesCounter] = src[i];
			bytesCounter += 1;
		}
		return bytes;
	}

	private boolean isNotNegative(int start, int end)
	{
		return end - start > 0;
	}
}
