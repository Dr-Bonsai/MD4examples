package sc;

import org.bouncycastle.util.encoders.Hex;


public class test {
	public static void main (String[] argv) throws Exception
	{
		byte[] mod = new byte[]{(byte)0xff};
		Hex.encode (mod,System.out);
		System.out.println();
	}
}
