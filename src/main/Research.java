package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import databasetable.ConnectionTableDocument;
import databasetable.ConnectionTableOccurrence;
import databasetable.ConnectionTableConteneur;
import databasetable.ConnectionTableTerme;
import sparqlclient.SparqlUtil;
import utils.DatabaseConnection;
import utils.ParseForResearch;
import utils.Qrel;
import utils.Query;
import utils.ResultQuery;
import utils.WordUtils;
import main.ProcessingFiles;

public class Research {

	public static List<String> research;
	public static List<String> finalResearch;
	public static List<String> stopList;
	public static List<ConnectionTableOccurrence> listOccurrence;
	public static List<ConnectionTableOccurrence> listOccurrenceMatched;
	public static List<ConnectionTableOccurrence> listOccurrenceMatchedModified;
	public static List<ConnectionTableConteneur> listConteneur;
	public static List<ConnectionTableConteneur> listConteneurTemp;
	public static List<ConnectionTableTerme> listTerme;
	public static List<ConnectionTableTerme> listTermeTemp;
	public static int NB_RESULT;
	/**
	 * 22% en 25 --> 22%
	 * 30% en 10 --> 31%
	 * 31% en 5 --> 33%
	 */
	

	public static List<ConnectionTableOccurrence> answerQuery() {
		/** Récupération de la table Occurrence */
		
		listOccurrenceMatched = new ArrayList<ConnectionTableOccurrence>();
		listOccurrenceMatchedModified = new ArrayList<ConnectionTableOccurrence>();
		
		/**
		 * Pour tous les mots de la recherche on récupère les lignes de la table
		 * Occurrence précédemment récupérée.
		 */
		for (String mot : finalResearch) {
			Iterator<ConnectionTableOccurrence> iteOccurence = listOccurrence
					.iterator();
			while (iteOccurence.hasNext()) {
				ConnectionTableOccurrence lOccurrence = iteOccurence.next();
				if (lOccurrence.getPkOccurrence().getNomTerme().equals(mot)) {
					/** Pour chaque mot on applique un traitement sur le tf/idf */
					if(traitementOccurrence(lOccurrence) != null) {
						listOccurrenceMatched.add(lOccurrence);
					}
				}
			}
		}
		
		/** Update TF/IDF dans listOccurrenceMatched en fonction du titre du document */
		for(ConnectionTableOccurrence cto : listOccurrenceMatched) {
			/** Select des conteneurs ayant pour idDocument cto.getPkOccurrence().getIdDocument() et pour typeConteneur PRESENTATION */
			listConteneurTemp = new ArrayList<ConnectionTableConteneur>();
			for(ConnectionTableConteneur ctc : listConteneur) {
				if(ctc.getIdDocument() == cto.getPkOccurrence().getIdDocument() && ctc.getTypeConteneur().equals(ProcessingFiles.PRESENTATION)) {
					listConteneurTemp.add(ctc);
				}
			}
			/** Select des Termes du titre */
			listTermeTemp = new ArrayList<ConnectionTableTerme>();
			for(ConnectionTableTerme ctt : listTerme) {
				if(ctt.getIdConteneur() == listConteneurTemp.get(0).getIdConteneur()) {
					listTermeTemp.add(ctt);
				}
			}
			/** Comparaison des différents mots */
			for (String mot : finalResearch) {
				for(ConnectionTableTerme ctt : listTermeTemp) {
					if(mot.equals(ctt.getNomTerme())) {
						cto.setTfidf(cto.getTfidf()*10);
					}
					if(!listOccurrenceMatchedModified.contains(cto)) {
						listOccurrenceMatchedModified.add(cto);
					}
				}
			}
		}

		/** On tri ensuite cette liste par ordre croissant tf/idf */
		Collections.sort(listOccurrenceMatchedModified);

		/** On affiche ensuite le résultat suivant NB_RESULT */
		int cpt = 0;
		List<ConnectionTableOccurrence> results = new ArrayList<ConnectionTableOccurrence>();
		for (ConnectionTableOccurrence l : listOccurrenceMatchedModified) {
			results.add(l);
			cpt++;
			if (cpt >= NB_RESULT) {
				break;
			}
		}
		return results;
	}

	/**
	 * Permet de garder uniquement les paragraphes
	 * @param lOccurrence
	 * @return
	 */
	private static ConnectionTableOccurrence traitementOccurrence(
			ConnectionTableOccurrence lOccurrence) {
		ConnectionTableConteneur associatedConteneur = getConteneurFromOccurrence(lOccurrence);
		if(associatedConteneur.getTypeConteneur().equals(ProcessingFiles.P)) {
			return lOccurrence;
		} else {
			return null;
		}
	}

	private static ConnectionTableConteneur getConteneurFromOccurrence(
			ConnectionTableOccurrence lOccurrence) {
		Iterator<ConnectionTableConteneur> iteListConteneur = listConteneur.iterator();
		while(iteListConteneur.hasNext()) {
			ConnectionTableConteneur currentConteneur = iteListConteneur.next();
			if(currentConteneur.getIdConteneur() == lOccurrence.getPkOccurrence().getIdConteneur()) {
				return currentConteneur;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		/** Init connection database */
		DatabaseConnection.doConnect();
		System.out.println("Database Connected.");
		for(int i = 0 ; i < 3 ; i++) {
			/** Je fais les differents résultats possiles */
			switch (i) {
			case 0:
				NB_RESULT = 5;
				break;
			case 1:
				NB_RESULT = 10;
				break;
			case 2:
				NB_RESULT = 25;
				break;
			default:
				break;
			}
			System.out.println("*********************************************************");
			System.out.println("******************* CALCUL A " + NB_RESULT + " *************************");
			System.out.println("*********************************************************");
			/** Je load la table Occurrence */
			listOccurrence = new ArrayList<ConnectionTableOccurrence>();
			listOccurrence.clear();
			listOccurrence = DatabaseConnection.getOccurrenceRowByDocument();
			System.out.println("Occurrences Imported.");
			/** Je load la table Conteneur */
			listConteneur = new ArrayList<ConnectionTableConteneur>();
			listConteneur.clear();
			listConteneur = DatabaseConnection.s.createQuery("FROM ConnectionTableConteneur").list();
			System.out.println("Conteneurs Imported.");
			/** Je load la table Terme */
			listTerme = new ArrayList<ConnectionTableTerme>();
			listTerme.clear();
			listTerme = DatabaseConnection.s.createQuery("FROM ConnectionTableTerme").list();
			System.out.println("Termes Imported.");
			/** Je load la stopList */
			stopList = new ArrayList<String>();
			stopList.clear();
			stopList = WordUtils.loadStopList();
			System.out.println("Stop List Loaded.");
			/** 
			 * Prendre les requetes une par une puis
			 * Regarder si la liste de xpath retournée 
			 * est = a la liste de Qrels
			 */
			List<Query> listQuery = ParseForResearch.parseQuery();
			Iterator<Query> iteQuery = listQuery.iterator();
			while(iteQuery.hasNext()) {
				/** Query courante */
				Query currentQuery = iteQuery.next();
				StringTokenizer tokenizer = new StringTokenizer(currentQuery.getTextQuery());
				/** Je vide research */
				research = new ArrayList<String>();
				/** Parse the request */
				while (tokenizer.hasMoreTokens()) {
					String mot = tokenizer.nextToken().toLowerCase();
					if (!WordUtils.isInStopList(mot, stopList) && !mot.equals("")) {
						/** On creer une nouvelle liste pour la balancer à SPARQL */
						research.add(mot);
						//research.add(WordUtils.transformWord(mot));
					}
				}
				finalResearch = new ArrayList<String>();
				for(String mot : research) {
					if (!WordUtils.isInStopList(mot, stopList) && !mot.equals("")) {
						finalResearch.add(WordUtils.transformWord(mot));
						finalResearch.add(WordUtils.transformWord(mot));
					}
				}
				research = SparqlUtil.requestToSPARQL(research);
				/** LES MOTS GENERES PAR SPARQL PEUVENT CONTENIR PLUSIEURS MOTS */
				for (String word : research) {
					String[] tempResearch = word.split("[ ]");
					for(int k = 0 ; k < tempResearch.length ; k++) {
						if(!WordUtils.isInStopList(tempResearch[k], stopList) && !tempResearch[k].equals("") && !finalResearch.contains(WordUtils.transformWord(tempResearch[k]))) {
							finalResearch.add(WordUtils.transformWord(tempResearch[k]));
						}
					}
				}
				/*System.out.println("-------");
				for (String word : finalResearch) {
					System.out.println(word);
				}
				System.out.println("-------");
				System.exit(0);*/
				/** Do the research  : Map<NomDocument,XPATH> */
				List<ConnectionTableOccurrence> resultsResearch = answerQuery();
				/** Tmp List<ResultQuery> resultsResearch */
				List<ResultQuery> resultsResearchTemp = new ArrayList<ResultQuery>();
				/** Fill the results */
				for(ConnectionTableOccurrence currentOccurrence : resultsResearch) {
					ConnectionTableConteneur currentConteneur = getConteneurFromOccurrence(currentOccurrence);
					/** Get nomDocument */
					ArrayList<ConnectionTableDocument> matchedDocuments = (ArrayList<ConnectionTableDocument>) DatabaseConnection.s.createQuery("FROM ConnectionTableDocument d WHERE d.idDocument = " +  currentConteneur.getIdDocument()).list();
					resultsResearchTemp.add(new ResultQuery("Collection/" + matchedDocuments.get(0).getNomDocument(), currentConteneur.getXpathConteneur().replaceAll("[()]", "")));
				}
				/** Check if the result is fine */
				List<ResultQuery> resultsResearchFinal = new ArrayList<ResultQuery>();
				List<Qrel> listQrel = ParseForResearch.parseQrels(currentQuery.getIdQuery().replace("q", ""));
				Iterator<Qrel> iteQrel = listQrel.iterator();
				while(iteQrel.hasNext()) {
					Qrel currentQrel = iteQrel.next();
					for(ResultQuery rq : resultsResearchTemp) {
						if(rq.getNomDocument().equals(currentQrel.getDocQrel()) && rq.getXpath().equals(currentQrel.getXpathQrel())) {
							if(currentQrel.getPertinanceQrel().contains("1")) {
								resultsResearchFinal.add(new ResultQuery(currentQrel.getDocQrel(), currentQrel.getXpathQrel()));
							}
						}
					}
				}
				/** Donne un % à la requete */
				float percentSuccess = ((float) resultsResearchFinal.size() / (float) NB_RESULT) * 100;
				System.out.println("----> Query n°" + currentQuery.getIdQuery().replace("p", "") 
								+  " : " + percentSuccess + "%");
			}
		}
		/** Close database connection */
		DatabaseConnection.endConnect();
	}
}
