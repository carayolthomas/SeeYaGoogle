package utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import main.ProcessingFiles;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

/**
 * Class in order to parse QRELS & QUERIES
 * 
 * @author thomas
 * 
 */
public class ParseForResearch {

	public static List<Query> parseQuery() {
		/** Liste des Query */
		List<Query> listQuery = new ArrayList<Query>();

		/** Traitement du fichier */
		SAXBuilder sxb = new SAXBuilder();
		try {
			Document document = sxb.build(new File(
					ProcessingFiles.MAIN_DIRECTORY.getAbsolutePath()
							+ "/queries/queries.xml"));
			/** Element racine */
			Element racine = document.getRootElement();
			List<Element> childFromRacine = racine.getChildren();
			Iterator<Element> iteChildFromRacine = childFromRacine
					.iterator();
			while (iteChildFromRacine.hasNext()) {
				Element currentElement = iteChildFromRacine.next();
				listQuery.add(new Query(currentElement.getAttribute("id")
						.getValue(), currentElement.getChildText("text").replace("\n", ""),
						currentElement.getChildText("narrative").replace("\n", "")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listQuery;
	}
	
	public static List<Qrel> parseQrels(String pNumQrel) {
		List<Qrel> listQrel = new ArrayList<Qrel>();
		try {
			File file = new File("src/qrels/qrel" + pNumQrel + ".txt");
			String fileContent = FileText.readFileAsString(file.getAbsolutePath());
			String[] lineQrel = fileContent.split("\n");
			int cptLineQrel;
			for(cptLineQrel = 0 ; cptLineQrel < lineQrel.length ; cptLineQrel++) {
				/** Parsing de chaque ligne du fichier Qrel ouvert */
				String[] attributeLineQrel = lineQrel[cptLineQrel].split("\t");
				listQrel.add(new Qrel(attributeLineQrel[0],
						attributeLineQrel[1],
						attributeLineQrel[2]));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return listQrel;
	}
}
