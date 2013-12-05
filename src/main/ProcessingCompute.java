package main;

import java.util.Iterator;
import java.util.List;

import utils.DatabaseConnection;
import databasetable.ConnectionTableOccurrence;

public class ProcessingCompute {

	public static List<ConnectionTableOccurrence> listOccurrence;
	
	public static void computeFreq() {
		/** 
		 * On parcours les lignes de la table Occurrence et on complete 
		 * la colonne TF
		 */
		Iterator<ConnectionTableOccurrence> iteOccurence = listOccurrence.iterator();
		while(iteOccurence.hasNext()) {
			ConnectionTableOccurrence lOccurrence = iteOccurence.next();
			int nbWords = countNbWordInConteneur(lOccurrence.getPkOccurrence().getIdConteneur(), listOccurrence);
			lOccurrence.setTf((float)((float) lOccurrence.getNbOccurrence() / (float) nbWords));
			DatabaseConnection.s.update(lOccurrence);
		}
	}
	
	private static int countNbWordInConteneur(int idConteneur, List<ConnectionTableOccurrence> listOccurrence) {
		int nbWords = 0;
		Iterator<ConnectionTableOccurrence> iteOccurence = listOccurrence.iterator();
		while(iteOccurence.hasNext()) {
			ConnectionTableOccurrence lOccurrence = iteOccurence.next();
			if(lOccurrence.getPkOccurrence().getIdConteneur() == idConteneur) {
				nbWords++;
			}
		}
		if(nbWords == 0) {
			System.err.println("nbWords == 0");
			System.exit(0);
		}
		return nbWords;
	}

	public static void computeInvFreq() {
		/**
		 * Nombre total de P dans le document / nombre de P où le terme apparait
		 * Pour un terme, il faut savoir dans combien de paragraphe il apparait
		 */
		Iterator<ConnectionTableOccurrence> iteOccurence = listOccurrence.iterator();
		while(iteOccurence.hasNext()) {
			ConnectionTableOccurrence lOccurrence = iteOccurence.next();
			@SuppressWarnings("unchecked")
			//TODO : UTILISER HIBERNATE POUR LE COUNT
			List<java.math.BigInteger> lListCount = DatabaseConnection.s.createSQLQuery("SELECT COUNT(*) FROM Occurrence WHERE "
					+ "nomTerme='" + lOccurrence.getPkOccurrence().getNomTerme() +
					"' AND idConteneur!=" + lOccurrence.getPkOccurrence().getIdConteneur() + 
					" AND idDocument=" + lOccurrence.getPkOccurrence().getIdDocument() + ";").list();
			lOccurrence.setIdf(Math.log(
					(double)(countNbConteneurInDocument(lOccurrence.getPkOccurrence().getIdDocument(), listOccurrence))
					/(double)(lListCount.get(0).floatValue() + 1.)));
			DatabaseConnection.s.update(lOccurrence);
		}
	}
	
	private static int countNbConteneurInDocument(int idDocument, List<ConnectionTableOccurrence> listOccurrence) {
		int numConteneur = 0;
		Iterator<ConnectionTableOccurrence> iteOccurence = listOccurrence.iterator();
		while(iteOccurence.hasNext()) {
			ConnectionTableOccurrence lOccurrence = iteOccurence.next();
			if(lOccurrence.getPkOccurrence().getIdDocument() == idDocument && lOccurrence.getPkOccurrence().getIdConteneur() > numConteneur) {
				numConteneur = lOccurrence.getPkOccurrence().getIdConteneur();
			}
		}
		return numConteneur;
	}
	
	public static void computeTFIDF() {
		Iterator<ConnectionTableOccurrence> iteOccurence = listOccurrence.iterator();
		while(iteOccurence.hasNext()) {
			ConnectionTableOccurrence lOccurrence = iteOccurence.next();
			lOccurrence.setTfidf(lOccurrence.getIdf() * lOccurrence.getTf());
			DatabaseConnection.s.update(lOccurrence);
		}
	}
	
	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args) {
		DatabaseConnection.doConnect();
		listOccurrence = DatabaseConnection.getOccurrenceRowByDocument();
		computeFreq();
		computeInvFreq();
		computeTFIDF();
		DatabaseConnection.t.commit();
		DatabaseConnection.endConnect();
	}
}
