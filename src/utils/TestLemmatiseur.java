package utils;

import org.tartarus.snowball.*;
import org.tartarus.snowball.util.StemmerException;

public class TestLemmatiseur {
	public static void main(String[] args) {
		
		try {
			System.out.println(FrenchSnowballStemmerFactory.getInstance().process("écologiquement"));
		} catch (StemmerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
}
