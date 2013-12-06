package main;

import java.util.Iterator;
import java.util.List;

import utils.DatabaseConnection;
import utils.MemoryConnection;
import databasetable.ConnectionTableOccurrence;

public class ProcessingCompute {
	
	public static void computeFreq() {
		/** 
		 * On parcours les lignes de la table Occurrence et on complete 
		 * la colonne TF
		 */
		int lIndexOccurrence = 0;
		Iterator<ConnectionTableOccurrence> iteOccurence = MemoryConnection.tableOccurrence.iterator();
		while(iteOccurence.hasNext()) {
			ConnectionTableOccurrence lOccurrence = iteOccurence.next();
			int nbWords = countNbWordInConteneur(lOccurrence.getPkOccurrence().getIdConteneur(), MemoryConnection.tableOccurrence);
			MemoryConnection.tableOccurrence.get(lIndexOccurrence).setTf((float)((float) lOccurrence.getNbOccurrence() / (float) nbWords));
			lIndexOccurrence++;
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
		int lIndexOccurrence = 0;
		Iterator<ConnectionTableOccurrence> iteOccurence = MemoryConnection.tableOccurrence.iterator();
		while(iteOccurence.hasNext()) {
			ConnectionTableOccurrence lOccurrence = iteOccurence.next();
			double countResult = 0;
			Iterator<ConnectionTableOccurrence> iteOccurenceInner = MemoryConnection.tableOccurrence.iterator();
			while(iteOccurenceInner.hasNext()) {
				ConnectionTableOccurrence lOccurrenceInner = iteOccurenceInner.next();
				if(lOccurrenceInner.getPkOccurrence().getNomTerme().equals(lOccurrence.getPkOccurrence().getNomTerme()) &&
						lOccurrenceInner.getPkOccurrence().getIdConteneur() == lOccurrence.getPkOccurrence().getIdConteneur() &&
						lOccurrenceInner.getPkOccurrence().getIdDocument() == lOccurrence.getPkOccurrence().getIdDocument()) {
					countResult++;
				}
			}
			MemoryConnection.tableOccurrence.get(lIndexOccurrence).setIdf(Math.log(
					(double)(countNbConteneurInDocument(lOccurrence.getPkOccurrence().getIdDocument()))
					/(double)(countResult + 1.)));
			lIndexOccurrence++;
		}
	}
	
	private static int countNbConteneurInDocument(int idDocument) {
		int numConteneur = 0;
		Iterator<ConnectionTableOccurrence> iteOccurence = MemoryConnection.tableOccurrence.iterator();
		while(iteOccurence.hasNext()) {
			ConnectionTableOccurrence lOccurrence = iteOccurence.next();
			if(lOccurrence.getPkOccurrence().getIdDocument() == idDocument && lOccurrence.getPkOccurrence().getIdConteneur() > numConteneur) {
				numConteneur = lOccurrence.getPkOccurrence().getIdConteneur();
			}
		}
		return numConteneur;
	}
	
	public static void computeTFIDF() {
		Iterator<ConnectionTableOccurrence> iteOccurence = MemoryConnection.tableOccurrence.iterator();
		int lIndexOccurrence = 0;
		while(iteOccurence.hasNext()) {
			ConnectionTableOccurrence lOccurrence = iteOccurence.next();
			MemoryConnection.tableOccurrence.get(lIndexOccurrence).setTfidf(lOccurrence.getIdf() * lOccurrence.getTf());
			lIndexOccurrence++;
		}
	}
	
	public static void doCompute() {
		computeFreq();
		computeInvFreq();
		computeTFIDF();
	}
}
