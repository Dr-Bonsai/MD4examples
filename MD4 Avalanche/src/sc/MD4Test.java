package sc;

/* ----------------------------------------------------------------------------
 * Utilizaremos la librería criptográfica BouncyCastle en esta práctica.
 * https://www.bouncycastle.org/
 * 
 * El archivo del .jar importado es bcprov-ext-jdk15on-159.jar descargado de:
 * https://www.bouncycastle.org/latest_releases.html
 * ----------------------------------------------------------------------------
 */

import org.bouncycastle.crypto.digests.MD4Digest;
import org.bouncycastle.util.encoders.Hex;


public class MD4Test {

	//Este es un Vector de Strings utilizadas en el documento RFC 1320
	public static void main (String[] argv) throws Exception
	{
		String TestVector[] = new String[] {
				"",
				"a",
				"abc",
				"message digest",
				"abcdefghijklmnopqrstuvwxyz",
				"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789",
				"12345678901234567890123456789012345678901234567890123456789012345678901234567890"
		};

		MD4Digest digest = new MD4Digest();								//Me creo un object Digest
		byte[] byteOutputHash = new byte[digest.getDigestSize()];		//Creo un contenedor para el output del Hash
		
		//Con este bucle busco sacar en salida de texto los resultados de los hash de los vectores de prueba
		for (String vprueba : TestVector) {

			byte[] byteMsg = vprueba.getBytes("US-ASCII");				//Convertimos en bytes codificacando con US-ASCII					//
			digest.update (byteMsg, 0, byteMsg.length);					//Preparamos el hash
			digest.doFinal (byteOutputHash, 0);							//Terminamos el hash, salida en byteOutputHash
			Hex.encode(byteOutputHash, System.out);						//Codificamos la salida en System.out en Hexadecimal
			System.out.println();										//Imprimimos una línea con System.out
		}
	}

}