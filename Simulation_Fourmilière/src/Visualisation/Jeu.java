/**
 * Projet Informatique Mai 2018 Simulation Fourmiliere
 * @author Marie Bastien
 * @author Guillaume Leroy 
 */

package Visualisation;


import java.util.Iterator;

import Animaux.Combattante;
import Animaux.Eclaireuse;
import Animaux.Ennemi;
import Animaux.Transporteuse;
import Terrain.Grille;


public class Jeu {
	
	/**
	 * Grille sur laquelle se deroule le jeu
	 */
	private Grille grille;

	/**
	 * Constructeur permettant de lancer un jeu entierement parametrable
	 * @param nombreTours nombre de fois que chaque animal pourra agir au cours de la simulation
	 * @param taille valeur du cote de la grille en nombres de cases
	 * @param nombreObstacles nombre de cases inaccessibles aux animaux
	 * @param nombreFourmis nombre de fourmis a la fourmiliere au depart de la simulation
	 * @param stockInitialNourriture quantite initiale de nourriture a la fourmiliere
	 * @param nombreEnnemis nombre de predateurs au depart de la simulation
	 * @param nombreCasesNourriture nombre de cases sur lesquelles se trouvent de la nourriture
	 * en debut de simulation.
	 * @throws InterruptedException gere les exceptions d'interruption
	 */
	public Jeu(int nombreTours, int taille, int nombreObstacles, int nombreFourmis, int stockInitialNourriture, 
			int nombreEnnemis, int nombreCasesNourriture) throws InterruptedException {
		// on construit la grille de base
		grille = new Grille(taille,nombreObstacles);
		// on initialise les animaux
		for(int i = 0; i < nombreFourmis/3 ; i ++) {
			grille.creerTransporteuse(10, 30, 30, 10, 3, 5 ,20);
			grille.creerEclaireuse(10, 30, 30, 10, 3, 5);
			grille.creerCombattante(10, 30, 30, 10, 10, 5, 1);
			}
		// on initialise les ennemis
		for(int i = 0; i < nombreEnnemis; i++) {
			grille.creerEnnemi(20, 40, 30, 40, 3, 5);
		}
		
		grille.getFourmiliere().apparitionNourriture(stockInitialNourriture);
		// on initialise les cases de la nourriture
		for(int i = 0; i < nombreCasesNourriture; i++) {
			grille.ajouterNourritureAleatoirement(40);
		}
		
		InterfaceGraphique affichage=new InterfaceGraphique(grille, 900, 700);
		for(int tour = 1; tour < nombreTours + 1; tour ++) {
			tour(affichage);
			//apparition de 3 nouvelles fourmis, d'un ennemi et de nourriture tous les 10 tours 
			if(tour%10 == 0){
				// les animaux et les quantites de nourriture qui apparaissent sont mises par d�faut
				grille.ajouterNourritureAleatoirement(40);
				grille.creerTransporteuse(10, 30, 30, 10, 3, 5, 10);
				grille.creerEclaireuse(10, 30, 30, 10, 3, 5);
				grille.creerCombattante(10, 30, 30, 10, 10, 5, 1);
				grille.creerEnnemi(20, 40, 30, 10,3, 5);
			}
		}
	}
	
	/**
	 * Lancer du jeu avec les parametres par defaut
	 * @throws InterruptedException gere les exceptions d'interruption
	 */
	public Jeu() throws InterruptedException {
		this(100, 10, 10, 12, 100, 5, 5);
	}
	
	/**
	 * Fonction permettant de realiser l'execution d'un  tour de jeu a l'ensemble des transporteuses 
	 * presentes sur la grille et met a jour l'affichage a chaque tour de transporteuse
	 * @param  affichage interface graphique 
	 * @throws InterruptedException gere les exceptions d'interruption
	 */
	public void tourTransporteuses(InterfaceGraphique affichage) throws InterruptedException {
		Iterator<Transporteuse> iterTransporteuse = this.grille.getListeTransporteuses().iterator();
		//parcours de la liste des transporteuses
		while(iterTransporteuse.hasNext()) {
			Transporteuse transporteuse = (Transporteuse) iterTransporteuse.next();
			//si fourmi vivante on la fait agir et on donne sa position
			if(transporteuse.isVivant()) {
				transporteuse.jouerTour();
			}
			//si la fourmi est morte on supprime toutes les references a celle-ci (dans la liste et sur la case)
			else {
				transporteuse.disparaitre();
				iterTransporteuse.remove();
			}
			//mise a jour de l'affichage 
			Thread.sleep(1000);
			affichage.rafraichir();
		}
	}
	
	/**
	 * Fonction permettant de realiser l'execution d'un tour de jeu a l'ensemble des eclaireuses presentes sur la grille 
	 * et met a jour l'affichage a chaque tour d'eclaireuse
	 * @param  affichage interface graphique
	 * @throws InterruptedException gere les exceptions d'interruption
	 */
	public void tourEclaireuses(InterfaceGraphique affichage) throws InterruptedException {
		Iterator<Eclaireuse> iterEclaireuse = grille.getListeEclaireuses().iterator();
		//parcours de la liste des eclaireuses
		while(iterEclaireuse.hasNext()) {
			Eclaireuse eclaireuse = (Eclaireuse) iterEclaireuse.next();
			//si fourmi vivante on la fait agir et on donne sa position
			if(eclaireuse.isVivant()) {
				eclaireuse.jouerTour();
			}
			//si la fourmi est morte on supprime toutes les references a celle-ci
			else {
				eclaireuse.disparaitre();
				iterEclaireuse.remove();
			}
			//mise a jour de l'affichage (a partir de la grille mise a jour)
			Thread.sleep(1000);
			affichage.rafraichir();		
		}
	}

	/**
	 * Fonction permettant de realiser l'execution d'un  tour de jeu a l'ensemble des combattantes presentes sur la grille
	 * et met a jour l'affichage a chaque tour de combattante
	 * @param  affichage interface graphique
	 * @throws InterruptedException gere les exceptions d'interruption
	 */
	public void tourCombattantes(InterfaceGraphique affichage) throws InterruptedException {
		Iterator<Combattante> iterCombattante = grille.getListeCombattantes().iterator();
		//parcours de la liste des combattantes
		while(iterCombattante.hasNext()) {
			Combattante combattante = (Combattante) iterCombattante.next();
			//si fourmi vivante on la fait agir et on donne sa position
			if(combattante.isVivant()) {
				combattante.jouerTour();
			}
			//si la fourmi est morte on supprime toutes les references a celle-ci
			else {
				combattante.disparaitre();
				iterCombattante.remove();
			}
			//mise a jour de l'affichage (a partir de la grille mise a jour)
			Thread.sleep(1000);
			affichage.rafraichir();		
		}
	}
	
	/**
	 * Fonction permettant de realiser l'execution d'un  tour de jeu a l'ensemble des ennemis presents sur la grille
	 * et met a jour l'affichage a chaque tour d'ennemi
	 * @param  affichage interface graphique
	 * @throws InterruptedException gere les exceptions d'interruption
	 */
	public void tourEnnemis(InterfaceGraphique affichage) throws InterruptedException {
		Iterator<Ennemi> iter = grille.getListeEnnemis().iterator();
		//parcours de la liste des ennemis
		while(iter.hasNext()) {
			Ennemi ennemi = (Ennemi) iter.next();
			//si ennemi vivante on le fait agir et on donne sa position
			if(ennemi.isVivant()) {
				ennemi.jouerTour();
			}
			else {
				//si l'ennemi est mort on supprime toutes les references a celui-ci et on rajoute sur sa case la quantite de nourriture coorespondante
				ennemi.disparaitre();
				ennemi.getPosition().apparitionNourriture(ennemi.getQuantiteNourritureCorrespondante());
				iter.remove();
			}
			//mise a jour de l'affichage (a partir de la grille mise a jour)
			Thread.sleep(1000);
			affichage.rafraichir();		
		}
	}
		

	/**
	 * Fonction qui permet de faire jouer un tour a l'ensemble des animaux et de mettre a jour la fenetre d'affichage
	 * @param affichage affichage de la grille avant le tour
	 * @throws InterruptedException gere les exceptions d'interruption
	 */
	public void tour(InterfaceGraphique affichage) throws InterruptedException {
		//on fait jouer un tour a l'ensemble des animaux de la grille
		tourEclaireuses(affichage);
		tourCombattantes(affichage);
		tourTransporteuses(affichage);
		tourEnnemis(affichage);
		
		System.out.println("quantite de nourriture a la fourmiliere: " + grille.getFourmiliere().getStockNourriture());
		System.out.println();
		
		// mise a jour des pheromones 
		grille.majPheromonesNourriture();
		grille.majPheromonesDanger();
	}
		
	
}
