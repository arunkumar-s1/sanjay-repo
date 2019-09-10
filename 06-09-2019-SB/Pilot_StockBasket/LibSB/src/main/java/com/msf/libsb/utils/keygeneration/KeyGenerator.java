package com.msf.libsb.utils.keygeneration;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import com.msf.libsb.appconfig.AppConfig;
import com.msf.libsb.utils.exception.SamcoException;
import com.msf.libsb.utils.helper.SamcoHelper;

public class KeyGenerator
{
	
	private String sJKey = "";
	private String sJSession = "";
	
	/**
	 * Middleware will get base64 encoded Public key1 from samco-server using GetInitialKey API. 
	 * Middleware needs to decode it. 
	 * Generate a pair of key ie. Public2/Private2 and send Public key 2 to samco in GetPreAuthenticationKey API by encrypting it using decoded
	 * Public Key1 and along with this middleware needs to send SHA 256 hashed decoded Public key1(in string format) to samco.
	 */
	
	public KeyGenerator(String sUserID) throws Exception
	{
	
		/* 1. Generating public key 1 using InitialKey API */
		String sKeyURL = AppConfig.getConfigValue("get_initial_key_url");
		KeyGenerationRequest keyGenRequest = new KeyGenerationRequest(sKeyURL);
		KeyGenerationResponse keyGenResponse = (KeyGenerationResponse)keyGenRequest.postRequest();
		
		if (keyGenResponse.isSuccessResponse())
		{
			
			String sPublicKeyOne = keyGenResponse.getPublicKey();
			String sTomcatCount = keyGenResponse.getTomcatCount();
			
			/* 2. Decoding public key 1 received from GetInitialKey response */
			byte[] bDecodedPublicKeyOne = Base64.getDecoder().decode(sPublicKeyOne);
			String sDecodedPublicKeyOne = null;
			
			try
			{
				sDecodedPublicKeyOne = new String(bDecodedPublicKeyOne, "UTF-8");
			
			} 
			catch (UnsupportedEncodingException e)
			{
			
			}
			
			/* 3. Generating public key 2, private key 2 */
			CryptoRSA nCrypto = new CryptoRSA();
			CryptoRSA nRSA = new CryptoRSA();
			NestKeyPair sKeyPair = nCrypto.generateNestKeyPair(2048);
			
			String sPublicKeyTwo = sKeyPair.getPemPublicKey();
			PrivateKey pPrivateKeyTwo = sKeyPair.getPrivateKey();
			
			PublicKey publicKeyOne = nRSA.getPublicKeyFromPemData(sDecodedPublicKeyOne);

			/* 4. Encrypting public key 2 using decoded public key 1 */
			String sEncryptedPublicKeyTwo = nRSA.encrypt(sPublicKeyTwo, publicKeyOne);
			
			/* Removing newline, carriage return and tabs from encrypted public key 2 string */
			String sReplacedPublicKeyTwo = sEncryptedPublicKeyTwo.replaceAll("\n", "");
			sReplacedPublicKeyTwo = sReplacedPublicKeyTwo.replaceAll("\r", "");
			sReplacedPublicKeyTwo = sReplacedPublicKeyTwo.replaceAll("\t", "");
			
			/* 5. SHA256 encoding decoded public key 1 string */
			String hashedPublicKeyOne = null;
			try 
			{
				hashedPublicKeyOne = SamcoHelper.hashData(sDecodedPublicKeyOne);
			}
			catch (NoSuchAlgorithmException e) 
			{
			} 
			catch (UnsupportedEncodingException e)
			{
			}
			
			/* Getting pre auth key */
			String sEncodedUserID = Base64.getEncoder().encodeToString(sUserID.getBytes());
			
			String sessionid = sEncodedUserID + "." + sTomcatCount;
			
			String sPreAuthURL = AppConfig.getConfigValue("pre_authentication_key_url");
			
			String sPreAuthFields = "?jsessionid=" + sessionid + "&jData=" + sReplacedPublicKeyTwo + "&jKey=" + hashedPublicKeyOne +"";
			
			String sPreAuthFinalURL = sPreAuthURL + sPreAuthFields;
			PreAuthKeyRequest preAuthKeyReq = new PreAuthKeyRequest(sPreAuthFinalURL);
			PreAuthKeyResponse preAuthKeyRes = (PreAuthKeyResponse)preAuthKeyReq.postRequest();
		
			if (preAuthKeyRes.isSuccessResponse())
			{
				String sEncryptedPublicKeyThree = preAuthKeyRes.getPublicKey3();
				String sDecryptedPublicKeyThree = nRSA.decrypt(sEncryptedPublicKeyThree, pPrivateKeyTwo);
				String sValidPasswordKey = SamcoHelper.hashData(sDecryptedPublicKeyThree);
				
				sJSession = sessionid;
				sJKey = sValidPasswordKey;
			} 
			else 
			{
				throw new SamcoException(preAuthKeyRes.getErrorID());
			}

			
		} 
		else
		{
			throw new SamcoException(keyGenResponse.getErrorID());
		}
		
	}
	
	public String getJKey() 
	{
		return this.sJKey;
	}
	
	public String getJSession() 
	{
		return this.sJSession;
	}

}
