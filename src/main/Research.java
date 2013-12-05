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
import utils.DatabaseConnection;
import utils.ParseForResearch;
import utils.Qrel;
import utils.Query;
import utils.ResultQuery;
import utils.WordUtils;
import main.ProcessingFiles;

public class Research {

	public static List<String> research;
	public static List<String> stopList;
	public static List<ConnectionTableOccurrence> listOccurrence;
	public static List<ConnectionTableOccurrence> listOccurrenceMatched;
	public static List<ConnectionTableConteneur> listConteneur;
	public static int NB_RESULT = 5;

	public static List<ResultQuery> answerQuery() {
		/** Récupération de la table Occurrence */
		listOccurrence = DatabaseConnection.getOccurrenceRowByDocument();
		listOccurrenceMatched = new ArrayList<ConnectionTableOccurrence>();

		/**
		 * Pour tous les mots de la recherche on récupère les lignes de la table
		 * Occurrence précédemment récupérée.
		 */
		for (String mot : research) {
			Iterator<ConnectionTableOccurrence> iteOccurence = listOccurrence
					.iterator();
			while (iteOccurence.hasNext()) {
				ConnectionTableOccurrence lOccurrence = iteOccurence.next();
				if (lOccurrence.getPkOccurrence().getNomTerme().equals(mot)) {
					/** Pour chaque mot on applique un traitement sur le tf/idf */
					listOccurrenceMatched
							.add(traitementOccurrence(lOccurrence));
				}
			}
		}

		/** On tri ensuite cette liste par ordre croissant tf/idf */
		Collections.sort(listOccurrenceMatched,
				new Comparator<ConnectionTableOccurrence>() {
					@Override
					public int compare(ConnectionTableOccurrence tc1,
							ConnectionTableOccurrence tc2) {
						if (tc1.getTfidf() > tc2.getTfidf()) {
							return 1;
						} else if (tc1.getTfidf() == tc2.getTfidf()) {
							return 0;
						} else {
							return -1;
						}
					}
				});

		/** On affiche ensuite le résultat suivant NB_RESULT */
		int cpt = 0;
		List<ResultQuery> results = new ArrayList<ResultQuery>();
		for (ConnectionTableConteneur l : listConteneur) {
			results.add(new ResultQuery("Collection/" + getNomDocument(l), l.getXpathConteneur()));
			System.out.println("---------- DEBUG : " + "Collection/" + getNomDocument(l) + "\t" + l.getXpathConteneur());
			if (cpt > 5) {
				break;
			}
		}
		return results;
	}

	private static String getNomDocument(ConnectionTableConteneur l) {
		@SuppressWarnings("unchecked")
		List<ConnectionTableDocument> lDoc = DatabaseConnection.s.createQuery(
				"FROM ConnectionTableDocument ctd WHERE ctd.idDocument="
						+ l.getIdDocument()).list();
		return lDoc.get(0).getNomDocument();
	}

	@SuppressWarnings("unchecked")
	private static ConnectionTableOccurrence traitementOccurrence(
			ConnectionTableOccurrence lOccurrence) {
		listConteneur = DatabaseConnection.s.createQuery(
				"FROM ConnectionTableConteneur c WHERE c.idConteneur ="
						+ lOccurrence.getPkOccurrence().getIdConteneur())
				.list();
		for (ConnectionTableConteneur ctc : listConteneur) {
			switch (ctc.getTypeConteneur()) {

			/**
			 * Si le mot est dans le titre, modifie toutes les occurences de ce
			 * moment dans ce doc : -> Rajouter du poids aux occurrences des
			 * mots dans le § quand il est dans le titre
			 */
			case ProcessingFiles.TITRE:
				Iterator<ConnectionTableOccurrence> iteOccurence = listOccurrence
						.iterator();
				while (iteOccurence.hasNext()) {
					ConnectionTableOccurrence nOccurrence = iteOccurence.next();
					if (nOccurrence.getPkOccurrence().getIdDocument() == lOccurrence
							.getPkOccurrence().getIdDocument()) {
						nOccurrence.setTfidf(nOccurrence.getTfidf() * 2);
					}
				}
				break;

			default:
				break;
			}
		}
		return null;
	}

	public static void main(String[] args) {
		/*List<Query> list = ParseForResearch.parseQuery();
		Iterator<Query> ite = list.iterator();
		while(ite.hasNext()) {
			System.out.println(ite.next().toString());
		}
		
		System.out.println("-----------------------------------------");
		
		List<Qrel> list1 = ParseForResearch.parseQrels("01");
		Iterator<Qrel> ite1 = list1.iterator();
		while(ite1.hasNext()) {
			System.out.println(ite1.next().toString());
		}*/
		
		/** Init connection database */
		DatabaseConnection.doConnect();
		/** Je load la stopList */
		stopList = WordUtils.loadStopList();
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
			research.clear();
			/** Parse the request */
			while (tokenizer.hasMoreTokens()) {
				String mot = tokenizer.nextToken().toLowerCase();
				if (!WordUtils.isInStopList(mot, stopList) && !mot.equals("")) {
					research.add(WordUtils.transformWord(mot));
				}
			}
			/** Do the research  : Map<NomDocument,XPATH> */
			List<ResultQuery> resultsResearch = answerQuery();
			/** Tmp List<ResultQuery> resultsResearch */
			List<ResultQuery> resultsResearchTemp = new ArrayList<ResultQuery>();
			/** Check if the result is fine */
			List<Qrel> listQrel = ParseForResearch.parseQrels(currentQuery.getIdQuery().replace("p", ""));
			Iterator<Qrel> iteQrel = listQrel.iterator();
			while(iteQrel.hasNext()) {
				Qrel currentQrel = iteQrel.next();
				if(resultsResearch.contains(new ResultQuery(currentQrel.getDocQrel(), currentQrel.getXpathQrel()))) {
					if(currentQrel.getPertinanceQrel().equals("1")) {
						resultsResearchTemp.add(new ResultQuery(currentQrel.getDocQrel(), currentQrel.getXpathQrel()));
					}
				}
			}
			/** Donne un % à la requete */
			float percentSuccess = (resultsResearchTemp.size() / NB_RESULT) * 100;
			System.out.println("----> Query n°" + currentQuery.getIdQuery().replace("p", "") 
							+  " : " + percentSuccess + "%");
		}
		/** Close database connection */
		DatabaseConnection.endConnect();
		
		
		
		
		/*DatabaseConnection.doConnect();
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		System.out.println("Veuillez saisir la rechercher :");
		String search = sc.nextLine();
		stopList = WordUtils.loadStopList();
		research = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(search);
		while (tokenizer.hasMoreTokens()) {
			String mot = tokenizer.nextToken().toLowerCase();
			if (!WordUtils.isInStopList(mot, stopList) && !mot.equals("")) {
				research.add(WordUtils.transformWord(mot));
			}
		}
		System.out.println("-------- DEBUG : " + research.toString());
		answerQuery();
		DatabaseConnection.endConnect();*/
	}
}
