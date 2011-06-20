package net.lo2k.crypter

import java.io.{FileInputStream, ObjectInputStream}
import javax.crypto.{Cipher, SecretKey}
import javax.crypto.KeyGenerator
import java.io.FileOutputStream
import java.io.ObjectOutputStream
import com.google.appengine.api.datastore.DatastoreServiceFactory
import com.google.appengine.api.datastore.Entity
import java.io.ByteArrayOutputStream
import com.google.appengine.api.datastore.Query
import java.io.ByteArrayInputStream
import com.google.appengine.repackaged.com.google.common.util.Base64

class Crypter() {
	
	var encCipher: Cipher = null
	var dcipher: Cipher = null
	
	val datastore = DatastoreServiceFactory.getDatastoreService();
  
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
	  Base64.encode(encodedString)
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
	  //var fout = new StringOutputStream(SECRET_KEY_FILENAME)
	  var fout = new ByteArrayOutputStream
	  var oos = new ObjectOutputStream(fout)
	  oos writeObject key
	  oos close
	  
	  var toStore = new Entity("Keys")
	  toStore.setProperty("serialized",Base64.encode(fout.toByteArray))
	  datastore.put(toStore)
  }

  def getStoredKey(): SecretKey = {

    val q = new Query("Keys")

    try {
    val pq = datastore.prepare(q);
    //var fin: FileInputStream = new FileInputStream(SECRET_KEY_FILENAME)
    val e: Entity = pq.asIterator.next

    var propertyString = ""

    e.getProperty("serialized") match {
      case s: String => propertyString = s
      case _ => throw new Exception("lol")
    }

    var fin: ByteArrayInputStream = new ByteArrayInputStream(Base64.decode(propertyString.getBytes("UTF-8")))
    var ois = new ObjectInputStream(fin)
    ois.readObject match {
      case key: SecretKey => return key
      case _ => throw new Exception("can't unserialize")
    }
    } catch {
      case e:Exception => return null
      case _  => return null
    }
    null
  }
}