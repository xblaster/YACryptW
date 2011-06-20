package net.lo2k.crypter
import javax.crypto.SecretKey
import java.io.ObjectOutputStream
import java.io.ByteArrayOutputStream
import com.google.appengine.api.datastore.Entity
import java.io.ByteArrayInputStream
import java.io.ObjectInputStream
import com.google.appengine.api.datastore.Query
import java.io.FileInputStream
import java.io.FileOutputStream

class FileCrypter extends Crypter {
  
  	/*val SECRET_KEY_FILENAME =  "cryptKeyStore2.dat"*/
  
	override def storeKey(key: SecretKey) {
	  var fout = new FileOutputStream("cryptKeyStore.dat")
	  var oos = new ObjectOutputStream(fout)
	  oos writeObject key
	  oos close
	}
	
	override def getStoredKey(): SecretKey = {
	  
	  //val q = new Query("Keys")
	  
	  //val pq = datastore.prepare(q);
	  try {
		  var fin: FileInputStream = new FileInputStream("cryptKeyStore.dat")
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