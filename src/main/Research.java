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

	public static List<ConnectionTableOccurrence> answerQuery() {
		/** Récupération de la table Occurrence */
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
					if(traitementOccurrence(lOccurrence) != null) {
						listOccurrenceMatched.add(lOccurrence);
					}
				}
			}
		}

		/** On tri ensuite cette liste par ordre croissant tf/idf */
		Collections.sort(listOccurrenceMatched);

		/** On affiche ensuite le résultat suivant NB_RESULT */
		int cpt = 0;
		List<ConnectionTableOccurrence> results = new ArrayList<ConnectionTableOccurrence>();
		for (ConnectionTableOccurrence l : listOccurrenceMatched) {
			results.add(l);
			//System.out.println("ConnectionTableOccurrence added : " + l.toString());
			cpt++;
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
		
		/** TODO
		 * Si le mot est dans le titre, modifie toutes les occurences de ce
		 * moment dans ce doc : -> Rajouter du poids aux occurrences des
		 * mots dans le § quand il est dans le titre
		 */
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
		/** Je load la table Occurrence */
		listOccurrence = DatabaseConnection.getOccurrenceRowByDocument();
		/** Je load la table Conteneur */
		listConteneur = DatabaseConnection.s.createQuery("FROM ConnectionTableConteneur").list();
		System.out.println("Occurrence Imported.");
		/** Je load la stopList */
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
			System.out.println("---- Query : " + currentQuery.getIdQuery());
			StringTokenizer tokenizer = new StringTokenizer(currentQuery.getTextQuery());
			/** Je vide research */
			research = new ArrayList<String>();
			/** Parse the request */
			while (tokenizer.hasMoreTokens()) {
				String mot = tokenizer.nextToken().toLowerCase();
				if (!WordUtils.isInStopList(mot, stopList) && !mot.equals("")) {
					research.add(WordUtils.transformWord(mot));
				}
			}
			/** Do the research  : Map<NomDocument,XPATH> */
			System.out.println("Answer Query Started.");
			List<ConnectionTableOccurrence> resultsResearch = answerQuery();
			/** Tmp List<ResultQuery> resultsResearch */
			System.out.println("Treatment of the response.");
			List<ResultQuery> resultsResearchTemp = new ArrayList<ResultQuery>();
			/** Fill the results */
			for(ConnectionTableOccurrence currentOccurrence : resultsResearch) {
				ConnectionTableConteneur currentConteneur = getConteneurFromOccurrence(currentOccurrence);
				/** Build the prefixe */
				String prefIdDocument;
				if(String.valueOf(currentConteneur.getIdDocument()).length() == 1) {
					prefIdDocument = "d00";
				} else {
					if(String.valueOf(currentConteneur.getIdDocument()).length() == 2) {
						prefIdDocument = "d0";
					} else {
						prefIdDocument = "d";
					}
				}
				resultsResearchTemp.add(new ResultQuery("Collection/" + prefIdDocument + currentConteneur.getIdDocument() + ".xml", currentConteneur.getXpathConteneur().replaceAll("[()]", "")));
			}
			/** Check if the result is fine */
			List<ResultQuery> resultsResearchFinal = new ArrayList<ResultQuery>();
			List<Qrel> listQrel = ParseForResearch.parseQrels(currentQuery.getIdQuery().replace("q", ""));
			Iterator<Qrel> iteQrel = listQrel.iterator();
			while(iteQrel.hasNext()) {
				Qrel currentQrel = iteQrel.next();
				for(ResultQuery rq : resultsResearchTemp) {
					System.out.println(rq.getNomDocument() + " / " + currentQrel.getDocQrel() + " --- " + rq.getXpath() + " / " + currentQrel.getXpathQrel());
					if(rq.getNomDocument().equals(currentQrel.getDocQrel()) && rq.getXpath().equals(currentQrel.getXpathQrel())) {
						System.out.println("lol");
						if(currentQrel.getPertinanceQrel().equals("1")) {
							resultsResearchFinal.add(new ResultQuery(currentQrel.getDocQrel(), currentQrel.getXpathQrel()));
						}
					}
				}
				break;
				/** REVOIR LES XPATH */
			}
			/** Donne un % à la requete */
			float percentSuccess = (resultsResearchFinal.size() / NB_RESULT) * 100;
			System.out.println("----> Query n°" + currentQuery.getIdQuery().replace("p", "") 
							+  " : " + percentSuccess + "%");
			break;
		}
		/** Close database connection */
		DatabaseConnection.endConnect();
	}
}
