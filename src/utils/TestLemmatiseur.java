package utils;

import java.util.ArrayList;

import org.tartarus.snowball.*;
import org.tartarus.snowball.util.StemmerException;

public class TestLemmatiseur {
	public static void main(String[] args) {
		
		try {
			System.out.println(FrenchSnowballStemmerFactory.getInstance().process("l'entraîneur"));
		} catch (StemmerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<String> test = (ArrayList<String>) WordUtils.loadStopList();
		System.out.println(WordUtils.isInStopList("telles", test));
		System.out.println(WordUtils.isInStopList("sfddsgegs", test));
		
	}
}
