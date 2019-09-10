package com.msf.libsb.utils.keygeneration;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.bouncycastle.util.io.pem.PemGenerationException;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

public class NestKeyPair
{
  private static final String PRIVATEKEY = "RSA PRIVATE KEY";
  private static final String PUBLICKEY = "PUBLIC KEY";
  private PublicKey publicKey;
  private PrivateKey privateKey;
  
  public final PublicKey getPublicKey()
  {
    return this.publicKey;
  }
  
  public final void setPublicKey(PublicKey publicKey)
  {
    this.publicKey = publicKey;
  }
  
  public final PrivateKey getPrivateKey()
  {
    return this.privateKey;
  }
  
  public final void setPrivateKey(PrivateKey privateKey)
  {
    this.privateKey = privateKey;
  }
  
  public final String getPemPublicKey()
  {
    return getPemString("PUBLIC KEY", this.publicKey);
  }
  
  public final String getPemPrivateKey()
  {
    return getPemString("RSA PRIVATE KEY", this.privateKey);
  }
  
  private String getPemString(String keydescription, Key key)
  {
    String returnValue = null;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try
    {
      PemWriter writer = new PemWriter(new OutputStreamWriter(baos, Charset.defaultCharset()));
      PemObject object = new PemObject(keydescription, key.getEncoded());
      writer.writeObject(object);
      writer.flush();
      baos.flush();
      writer.close();
      baos.close();
      
      returnValue = baos.toString(String.valueOf(Charset.forName("UTF-8")));
    }
    catch (FileNotFoundException e)
    {
      
    }
    catch (PemGenerationException e)
    {
      
    }
    catch (IOException e)
    {
      
    }
    return returnValue;
  }
}


