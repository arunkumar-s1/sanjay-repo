package com.msf.libsb.utils.keygeneration;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import org.json.me.JSONObject;

import com.msf.libsb.LibSamcoBase;
import com.msf.libsb.utils.helper.SamcoHelper;
//import org.apache.log4j.Logger;
import com.msf.log.Logger;


public class KeyGeneration extends LibSamcoBase{
	//url = "http://192.168.20.21/NestHtml5Mobile/rest/"

	private static Logger log = Logger.getLogger(KeyGeneration.class);


	public KeyGeneration(String url, Integer timeout, String certName, String method)
	{
		super(url, timeout, certName, method);

	}

	public JSONObject getjKey(String userID) throws Exception 
	{

		String sInitialKeyRequest = "GetInitialKey";

		String sInitialKeyResponse = sendRequest(sInitialKeyRequest,"");

		JSONObject responseObject = new JSONObject(sInitialKeyResponse);
		String sPublicKeyOne = responseObject.getString("publicKey");
		String sTomcatNumber = responseObject.getString("tomcatCount");

		String sStats = responseObject.getString("stat");


		byte[] bDecodedPublicKeyOne = Base64.getDecoder().decode(sPublicKeyOne);

		String sDecodedPublicKeyOne = null;
		try
		{
			sDecodedPublicKeyOne = new String(bDecodedPublicKeyOne, "UTF-8");
		}
		catch (UnsupportedEncodingException e) 
		{

		}

		CryptoRSA nCrypto = new CryptoRSA();
		CryptoRSA nRSA = new CryptoRSA();
		NestKeyPair sKeyPair = nCrypto.generateNestKeyPair(2048);

		// Get public key2 and private key2
		String sPublicKeyTwo = sKeyPair.getPemPublicKey();

		PrivateKey pPrivateKeyTwo = sKeyPair.getPrivateKey();

		PublicKey publicKeyOne = nRSA.getPublicKeyFromPemData(sDecodedPublicKeyOne);

		String sEncryptedPublicKeyTwo = nRSA.encrypt(sPublicKeyTwo, publicKeyOne);

		String sReplacedPublicKeyTwo = sEncryptedPublicKeyTwo.replaceAll("\n", "");
		sReplacedPublicKeyTwo = sReplacedPublicKeyTwo.replaceAll("\r", "");
		sReplacedPublicKeyTwo = sReplacedPublicKeyTwo.replaceAll("\t", "");

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
		String sEncodedUserID = Base64.getEncoder().encodeToString(userID.getBytes());

		String sessionid = sEncodedUserID + "." + sTomcatNumber;

		String sPreAuthFields = "jsessionid=" + sessionid + "&jData=" + sReplacedPublicKeyTwo + "&jKey=" + hashedPublicKeyOne +"";
		String sPreAuthURL = "GetPreAuthenticationKey?" + sPreAuthFields;

		String sPreAuthenticationKeyResponse = sendRequest(sPreAuthURL,"");

		JSONObject jPreAuthResponseObject = new JSONObject(sPreAuthenticationKeyResponse);
		String sEncryptedPublicKeyThree = jPreAuthResponseObject.getString("publicKey3");

		String sDecryptedPublicKeyThree = nRSA.decrypt(sEncryptedPublicKeyThree, pPrivateKeyTwo);

		String sValidPasswordKey = SamcoHelper.hashData(sDecryptedPublicKeyThree);
		JSONObject res = new JSONObject();
		res.put("jsession", sessionid);
		res.put("jkey", sValidPasswordKey);

		insertUserToDB();// INSERT DATA TO DB THROUGH A HELPER
		return res;

	}

	public void insertUserToDB()
	{

	}

}
