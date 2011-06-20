package net.lo2k.crypter

import java.io.{FileInputStream, ObjectInputStream}
import javax.crypto.{Cipher, SecretKey}
import javax.crypto.KeyGenerator
import java.io.FileOutputStream
import java.io.ObjectOutputStream

class Crypter() {
	
	val SECRET_KEY_FILENAME =  "cryptKeyStore.dat"
  
	var encCipher: Cipher = null
	var dcipher: Cipher = null
  
	init(getSecretKey)
  
  
	def init(key:SecretKey) {
	  encCipher = Cipher.getInstance("DES")
	  dcipher = Cipher.getInstance("DES")
	  
	  encCipher.init(Cipher.ENCRYPT_MODE, key)
	  dcipher.init(Cipher.DECRYPT_MODE, key)
	}
  
	def decrypt(str: String): String = {
	  var dec = new sun.misc.BASE64Decoder().decodeBuffer(str);
	  new String(dcipher.doFinal(dec),"UTF-8");
	}
  
	def encrypt(str: String): String = {
	  var encodedString = encCipher.doFinal(str.getBytes("UTF-8"))
	  new sun.misc.BASE64Encoder().
	      encodeBuffer(encodedString)
	}
	
	def getSecretKey(): SecretKey = {
	  getStoredKey match {
	    case key: SecretKey => return key
	    case null => generateAndStoreNewKey
	    case _ => throw new Exception("can't append")
	  }
	}
	
	def generateAndStoreNewKey(): SecretKey = {
	  var newKey = KeyGenerator.getInstance("DES").generateKey()
	  storeKey(newKey)	  
	  return newKey
	}
	
	def storeKey(key: SecretKey) {
	  var fout = new FileOutputStream(SECRET_KEY_FILENAME)
	  var oos = new ObjectOutputStream(fout)
	  oos writeObject key
	  oos close
	}
	
	def getStoredKey(): SecretKey = {
	  try {
		  var fin: FileInputStream = new FileInputStream(SECRET_KEY_FILENAME)
		  var ois = new ObjectInputStream(fin)
		  ois.readObject match {
		    case key: SecretKey => return key
		    case _ 				=> throw new Exception("can't unserialize")
		  }
	  } catch {
	    case e: Exception =>  e.printStackTrace
	  }
	  null
	}
}