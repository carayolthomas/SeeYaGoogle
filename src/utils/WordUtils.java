package utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import main.ProcessingFiles;

import org.tartarus.snowball.FrenchSnowballStemmerFactory;
import org.tartarus.snowball.util.StemmerException;

public class WordUtils {

	/**
	 * Lemmatisation d'un mot
	 * 
	 * @param pToTransform
	 * @return
	 */
	public static String transformWord(String pToTransform) {
		try {
			return FrenchSnowballStemmerFactory.getInstance().process(
					pToTransform);
		} catch (StemmerException e) {
			Logger.getGlobal().info("Erreur lors de la lemmatisation");
			e.printStackTrace();
			return null;
		}
	}

	public static boolean isInStopList(String pToCheck, List<String> pStopList) {
		return pStopList.contains(pToCheck);
	}

	public static List<String> loadStopList() {
		List<String> lStopList = new ArrayList<String>();
		try {
			InputStream ips = new FileInputStream(ProcessingFiles.MAIN_DIRECTORY + "/utils/stopliste.txt");
			Reader ipsr = new InputStreamReader(ips,"UTF-8");
			BufferedReader br = new BufferedReader(ipsr);
			String ligne;
			while ((ligne = br.readLine()) != null) {
				lStopList.add(ligne);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lStopList;
	}
}
