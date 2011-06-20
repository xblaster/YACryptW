package controllers

import play._
import play.mvc._
import net.lo2k.crypter.Crypter
import play.mvc.results.Template

object Application extends Controller {
    
	
  
    def index(code: String = null, encodeBtn: String = null, decodeBtn: String = null) = {
      
      var message: String =""
      
      if (code!=null) {
        val crypt = new Crypter
        
        if (decodeBtn != null) {
          message = crypt.decrypt(code)
        } else {
          message = crypt.encrypt(code)
        }
      }

      Template('message -> message)
    }
}
