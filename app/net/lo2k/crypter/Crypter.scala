package net.lo2k.crypter

import java.io.{FileInputStream, ObjectInputStream}
import javax.crypto.{Cipher, SecretKey}
import javax.crypto.KeyGenerator
import java.io.FileOutputStream
import java.io.ObjectOutputStream
import java.io.ByteArrayOutputStream
import java.io.ByteArrayInputStream

import net.iharder.Base64

class Crypter() {
	
	var encCipher: Cipher = null
	var dcipher: Cipher = null
	
	var key : SecretKey = null
	
	init(getSecretKey)
  
  
	def init(key:SecretKey) {
	  encCipher = Cipher.getInstance("DES")
	  dcipher = Cipher.getInstance("DES")
	  
	  encCipher.init(Cipher.ENCRYPT_MODE, key)
	  dcipher.init(Cipher.DECRYPT_MODE, key)
	}
  
	def decrypt(str: String): String = {
	  //println(";"+str+";")
	  var dec = Base64.decode(str.getBytes("UTF-8"));
	  //println(";"+dec+";")
	  new String(dcipher.doFinal(dec),"UTF-8");
	}
  
	def encrypt(str: String): String = {
	  var encodedString = encCipher.doFinal(str.getBytes("UTF-8"))
	  //println(";"+encodedString+";")
	  Base64.encodeBytes(encodedString)
	}
	
	def getSecretKey(): SecretKey = {
	  getStoredKey match {
	    case key: SecretKey => return key
	    case null => generateNewKey
	    case _ => throw new Exception("can't append")
	  }
	}
	
	def generateNewKey(): SecretKey = {
	  var newKey = KeyGenerator.getInstance("DES").generateKey()
	  //storeKey(newKey)	  
	  return newKey
	}
	
  def getStoredKey(): SecretKey = key
}