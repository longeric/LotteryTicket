import java.io.IOException; 
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;

public class LotteryCipher{
  String strDataToEncrypt = new String();
  String strCipherText = new String();
  String strDecryptedText = new String();
  private byte[] iv;
  private SecretKey secretKey; 
 
  
  public LotteryCipher(){
   
   final int AES_KEYLENGTH = 128; 
   iv = new byte[AES_KEYLENGTH / 8]; 
   SecureRandom prng = new SecureRandom();
   prng.nextBytes(iv); 
    
    
  }
 // Use to genereate a new key 
 public void newKey(){
    try{  
   KeyGenerator keyGen = KeyGenerator.getInstance("AES");
   keyGen.init(128);
   secretKey = keyGen.generateKey();
   

   
    } catch(NoSuchAlgorithmException noSuchAlgo){}
    
  }
  
  
  //**********ENCRYPT*************************
synchronized String encryptInfo(String info){
  
    
  try {
    
  
 
  
   Cipher aesCipherForEncryption = Cipher.getInstance("AES/CBC/PKCS5Padding"); 


   aesCipherForEncryption.init(Cipher.ENCRYPT_MODE, secretKey, 
     new IvParameterSpec(iv));

  
   strDataToEncrypt = info;
   byte[] byteDataToEncrypt = strDataToEncrypt.getBytes();
   byte[] byteCipherText = aesCipherForEncryption
     .doFinal(byteDataToEncrypt);
    
   
   strCipherText = new BASE64Encoder().encode(byteCipherText);
      }
  
  catch (NoSuchAlgorithmException noSuchAlgo) {
   System.out.println(" No Such Algorithm exists " + noSuchAlgo);
  }

  catch (NoSuchPaddingException noSuchPad) {
   System.out.println(" No Such Padding exists " + noSuchPad);
  }

  catch (InvalidKeyException invalidKey) {
   System.out.println(" Invalid Key " + invalidKey);
  }

  catch (BadPaddingException badPadding) {
   System.out.println(" Bad Padding " + badPadding);
  }

  catch (IllegalBlockSizeException illegalBlockSize) {
   System.out.println(" Illegal Block Size " + illegalBlockSize);
  }

  catch (InvalidAlgorithmParameterException invalidParam) {
   System.out.println(" Invalid Parameter " + invalidParam);
  }
    return strCipherText; 
  }
  
//**************DECRYPT**********************
synchronized String decryptInfo(String info){
     
    try{
   
  
   Cipher aesCipherForDecryption = Cipher.getInstance("AES/CBC/PKCS5Padding");     

   aesCipherForDecryption.init(Cipher.DECRYPT_MODE, secretKey,
     new IvParameterSpec(iv));
   byte[] byteCipherText = new BASE64Decoder().decodeBuffer(info);
   byte[] byteDecryptedText = aesCipherForDecryption
     .doFinal(byteCipherText);
   strDecryptedText = new String(byteDecryptedText);
  
  }
    catch(IOException e){}
  
  catch (NoSuchAlgorithmException noSuchAlgo) {
   System.out.println(" No Such Algorithm exists " + noSuchAlgo);
  }

  catch (NoSuchPaddingException noSuchPad) {
   System.out.println(" No Such Padding exists " + noSuchPad);
  }

  catch (InvalidKeyException invalidKey) {
   System.out.println(" Invalid Key " + invalidKey);
  }

  catch (BadPaddingException badPadding) {
    return "NO"; 
  }

  catch (IllegalBlockSizeException illegalBlockSize) {
   System.out.println(" Illegal Block Size " + illegalBlockSize);
  }

  catch (InvalidAlgorithmParameterException invalidParam) {
   System.out.println(" Invalid Parameter " + invalidParam);
  }
  
  return strDecryptedText;
 }
 
  
}