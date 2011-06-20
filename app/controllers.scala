package controllers

import play._
import play.mvc._
import net.lo2k.crypter.Crypter
import play.mvc.results.Template
import net.lo2k.crypter.FileCrypter

object Application extends Controller {
    
	
  
    def index(code: String = null, encodeBtn: String = null, decodeBtn: String = null) = {
      
      var message: String =""
      if (code!=null) {
        val crypt = new Crypter
        if ((decodeBtn != null) && (decodeBtn equals "Decode")) {
          var newCode = code.toString
          newCode = newCode.replace("\n", "").trim()
          message = crypt.decrypt(newCode)
        } else {
          message = crypt.encrypt(code)
        }
      }

      Template('message -> message)
    }
}
