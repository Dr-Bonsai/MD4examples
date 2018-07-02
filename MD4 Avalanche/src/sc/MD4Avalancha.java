package sc;

import java.util.HashMap;

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


public class MD4Avalancha {

	/*
	 * Este método realiza el experimento
	 */
	public static void main (String[] argv) throws Exception
	{
		int[] Hamming = new int[1000];
		
		for(int i = 0; i<1000; i++) {

			String muestra1 = generarStringAleatoria();
			String muestra2 = muestra1;

			int bit = (int) (Math.random()*1000)+1;
			bit = bit%(muestra1.length()*8);

			byte[] muestra2byte = muestra2.getBytes("US-ASCII");

			byte mod = muestra2byte[bit/8];

			switch (bit%8) {
			case 1 : mod = (byte) (mod^0x40);
			break;
			case 2 : mod = (byte) (mod^0x20);
			break;
			case 3 : mod = (byte) (mod^0x10);
			break;
			case 4 : mod = (byte) (mod^0x08);
			break;
			case 5 : mod = (byte) (mod^0x04);
			break;
			case 6 : mod = (byte) (mod^0x02);
			break;
			case 7 : mod = (byte) (mod^0x01);
			break;
			case 0 : bit++;
			mod = (byte) (mod^0x40);

			}

			muestra2byte[bit/8] = mod;
			muestra2 = replace(muestra2,bit/8,(char)mod);

			byte[] hashMuestra1 = hash(muestra1);
			byte[] hashMuestra2 = hash(muestra2);
			String hashMuestra1Binary = palabraBinario(Hex.toHexString(hashMuestra1));
			String hashMuestra2Binary = palabraBinario(Hex.toHexString(hashMuestra2));
			bit++;
			String signal = "Señal de Bit       : ";
			for(int j = 0; j<bit-1;j++) {
				signal = signal + " ";
			}
			Hamming[i] = getHamming(hashMuestra1Binary,hashMuestra2Binary);

			System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------------------------\n");
			System.out.print("Palabra1           : ");
			System.out.println(muestra1);
			System.out.print("Palabra2           : ");
			System.out.println(muestra2);
			System.out.print("Hash Palabra1      : ");
			Hex.encode (hashMuestra1, System.out);
			System.out.println();
			System.out.print("Hash Palabra2      : ");
			Hex.encode (hashMuestra2, System.out);
			System.out.println();
			System.out.println("nº Bit invertido   : "+ bit);
			System.out.print("Palabra1 binario   : ");
			System.out.println(palabraBinario(muestra1));
			System.out.print("Palabra2 binario   : ");
			System.out.println(palabraBinario(muestra2));
			signal = signal + "^";
			System.out.println(signal);
			System.out.println("Hamming de Hash    : " + Hamming[i]);


		}
		System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------------------------\n");
	
		int totalHamming = 0;
		HashMap<Integer,Integer> HammingOcurrences = new HashMap <Integer,Integer> (); 
		for (int i = 0;i<1000;i++) {
			
			if(HammingOcurrences.containsKey(Hamming[i])){
				HammingOcurrences.put(Hamming[i], HammingOcurrences.get(Hamming[i]) + 1);
			}else {
				HammingOcurrences.put(Hamming[i],1);
			}
			
			totalHamming = totalHamming + Hamming[i];
			
		}
		
		int media = totalHamming / 1000;
		
		System.out.println("Número de ocurrencias de distancias Hamming :");
		for (Integer key : HammingOcurrences.keySet()) {
			System.out.println(key + " : " + HammingOcurrences.get(key));
		}
		System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------------------------\n");
	}

	/*
	 * Este método devuelve la palabra en binario.
	 */
	public static String palabraBinario (String entry){
		String letras = entry;
		String print = "";
		int x=0;
		for (int i=0; i<letras.length(); i++){
			x = letras.charAt(i);
			print = print + String.format("%8s", Integer.toBinaryString(x)).replace(' ', '0');
		}
		return print;
	}

	/*
	 * Este método genera una string de entre 1 o 20 chars aleatoria
	 */
	public static String generarStringAleatoria () {
		char[] elementos={'0','1','2','3','4','5','6','7','8','9','a',
				'b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t',
				'u','v','w','x','y','z'};


		int rndl = (int) ((Math.random()*100)%19) + 1;			//Esto genera un int entre 1 y 20 aleatorio
		char[] conjunto = new char[rndl];

		for(int i=0 ; i < rndl ; i++){									//Este buclea crea caracteres aleatorios
			conjunto[i] = (char)elementos[(int)(Math.random()*36)];
		}
		return new String(conjunto);
	}

	/*
	 * Este método si le pasas una string, te devuelve el hash de dicho método por consola.
	 */
	public static byte[] hash(String entry) throws Exception {
		MD4Digest digest = new MD4Digest();								//Me creo un object Digest
		byte[] byteOutputHash = new byte[digest.getDigestSize()];		//Creo un contenedor para el output del Hash
		byte[] byteMsg = entry.getBytes("US-ASCII");					//Convertimos en bytes codificacando con US-ASCII					//
		digest.update (byteMsg, 0, byteMsg.length);						//Preparamos el hash
		digest.doFinal (byteOutputHash, 0);								//Terminamos el hash, salida en byteOutputHash
		return byteOutputHash;
	}

	/*
	 * Este método reemplaza el char en un punto de una string por otro
	 */
	public static String replace(String str, int index, char replace){     
		if(str==null){
			return str;
		}else if(index<0 || index>=str.length()){
			return str;
		}
		char[] chars = str.toCharArray();
		chars[index] = replace;
		return String.valueOf(chars);       
	}

	/*
	 * Pasando 2 strings obtenemos la distancia de Hamming entre ellas
	 */
	public static int getHamming(String s1, String s2) {

		// check preconditions
		if (s1 == null || s2 == null || s1.length() != s2.length()) {
			throw new IllegalArgumentException();
		}

		// compute hamming distance
		int distance = 0;
		for (int i = 0; i < s1.length(); i++) {
			if (s1.charAt(i) != s2.charAt(i)) {
				distance++;
			}
		}
		return distance;

	}
}