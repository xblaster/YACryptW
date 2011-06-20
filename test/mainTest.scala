import net.lo2k.crypter.Crypter
object mainTest {
	  def main(args: Array[String]) {
	    val crypter = new Crypter
	    println(crypter encrypt "lol")
	    println(crypter decrypt "U/VTUCMFNaY=")
	  }
}