package wargame;

import java.awt.Color;

import java.awt.Graphics;
import java.io.Serializable;

import javax.swing.JFrame;
import javax.swing.JPanel;

import wargame.ISoldat.TypesH;
import wargame.ISoldat.TypesM;
import wargame.Obstacle.TypeObstacle;

public class Carte implements ICarte, Serializable {
	/**
	 * Classe principale du projet. Elle contient la methode main. Cette classe permet de gerer la carte du jeu
	 */
	private static final long serialVersionUID = 2824283387823604004L;
	public Element[][] Carte = new Element[IConfig.LARGEUR_CARTE][IConfig.HAUTEUR_CARTE]; //Tableau avec es element de la carte
	public int nbHeros = IConfig.NB_HEROS; 
	public int nbMonstre = IConfig.NB_MONSTRES;
	public int nbObs = IConfig.NB_OBSTACLES;
	public int tour = 1; 
	public int num = 1;
	public char lettre = 'A';// Compteur de lettres pour les noms des heros

	/*
	 * Num est un compteur. Il sert a donner un nom aux monstres lors de
	 * l'initialisation de la carte. il sert ensuite de compteur pour les tours: il
	 * s'incremente a chaque fois qu'un soldat joue un tour.
	 * 
	 */
	

	/**
	 * Constructeur de la classe carte, Elle initialise la carte :
	 * Y ajoute aleatoirement le bon nombre de monstres et de heros (IConfig)
	 * Y ajoute aleatoirement des obstacles
	 * @throws Exception 
	 */
	@SuppressWarnings("unused")
	Carte() throws Exception {
		if (IConfig.LARGEUR_CARTE * IConfig.NB_PIX_CASE > 1000)
			throw new Exception("Le plateau de jeu est trop grand, diminuez la taille des cases ou la largeur");
		else if (IConfig.HAUTEUR_CARTE * IConfig.NB_PIX_CASE > 580)
			throw new Exception("Le plateau de jeu est trop grand, diminuez la taille des cases ou la heuteur");
		else if (IConfig.LARGEUR_CARTE * IConfig.NB_PIX_CASE < 350)
			throw new Exception("Le plateau de jeu est trop petit, augmentez la taille des cases ou la largeur");

		int i, j, n = 0;
		Position pos;
		for (i = 0; i < IConfig.LARGEUR_CARTE; i++)
			for (j = 0; j < IConfig.HAUTEUR_CARTE; j++)
				videCase(new Position(i, j));
				// On initialise tout a vide

		while (n != nbObs) { // On ajoute tout les obstacles dans des positions aleatoires
			pos = trouvePositionVide();
			setCase(pos, new Obstacle(TypeObstacle.getObstacleAlea(), pos));
			n++;
		}
		n = 0;
		while (n != nbMonstre) {
			pos = trouvePositionVide();
			if (pos.getX() > IConfig.LARGEUR_CARTE / 2 && n != nbMonstre) {// On fait comme pour les heros dans la
																			// partie droite
				setCase(pos, new Monstre(this, TypesM.getTypeMAlea(), "" + num, pos));
				num++;
				n++;
			}
		}
		n = 0;
		while (n != nbHeros) {
			pos = trouvePositionVide();
			if (pos.getX() < IConfig.LARGEUR_CARTE / 2 && n != nbHeros) { // Ici, on ajoute nbHeros dans des positions
																			// aleatoires
				// dans la partie gauche du plateau
				Heros H = new Heros(this, TypesH.getTypeHAlea(), "" + lettre, pos);
				setCase(pos, H);
				lettre++;
				n++;
			}
		}

		num = 0;
	}
	

	/** Fonction pour vider une case
	 * Permet de vider la case se trouvant a la position pos d'une carte
	 * @param pos position à vider
	 */
	public void videCase(Position pos) {
		Carte[pos.getX()][pos.getY()] = new Element();
	}

	
	/** Fonction pour remplir une case
	 * Permet de mettre a la case se trouvant a la position pos l'element elem
	 * @param pos position à remplir
	 * @param elem element a mettre dans pos
	 */
	public void setCase(Position pos, Element elem) {
		Carte[pos.getX()][pos.getY()] = elem;
	}
	

	/**
	 * Renvoie l'element des trouvant dans a la position pos
	 * @param pos position de l'element a recuperer 
	 */
	public Element getElement(Position pos) {
		return Carte[pos.getX()][pos.getY()];
	}
	

	/**
	 * Trouve aleatoirement une position vide sur la carte
	 */
	public Position trouvePositionVide() {
		int aleaX = 0;
		int aleaY = 0;
		Position pos = new Position(aleaX, aleaY);
		do {
			pos.setX((int) (Math.random() * ((IConfig.LARGEUR_CARTE))));
			pos.setY((int) (Math.random() * ((IConfig.HAUTEUR_CARTE))));
		} while (!getElement(pos).estVide());

		return pos;
	}
	
	
	/**
	 * Trouve une position vide adjacente a pos
	 * @param pos position de depart pour chercher une case vide adjacente
	 */
	public Position trouvePositionGaucheVide(Position pos) {
		int aleaY = 0, aleaX = 0;
		Position pos1 = new Position(aleaX, aleaY);
		do {
			pos1 = trouvePositionVide(pos);
		} while (pos1.getX() >= pos.getX());
		return pos1;
	}
	
	
	/**
	 * Trouve une position vide adjacente a pos
	 * @param pos position de depart pour chercher une case vide adjacente
	 */
	public Position trouvePositionDroiteVide(Position pos) {
		int aleaY = 0, aleaX = 0;

		Position pos1 = new Position(aleaX, aleaY);
		do {
			pos1 = trouvePositionVide(pos);
		} while (pos1.getX() <= pos.getX());
		return pos1;
	}
	

	/**
	 * Trouve une position vide adjacente a pos
	 * @param pos position de depart pour chercher une case vide adjacente
	 */
	public Position trouvePositionVide(Position pos) {
		Position pos1 = new Position(-1, -1);
		double x;
		double y;
		do {
			x = Math.random() * 3;
			y = Math.random() * 3;
			pos1.setX(pos.getX() + (int) x - 1);
			pos1.setY(pos.getY() + (int) y - 1);
		} while (pos1.equals(pos));
		return pos1;
	}
	
	
	/**
	 * Deplace un soldat soldat a la position pos si cette derniere est valide
	 * @param soldat Soldat que l'ont veut deplacer
	 * @param pos Position à laquelle on veut le deplacer 
	 */
	public boolean deplaceSoldat(Position pos, Soldat soldat) {// cette fonction deplace un soldat a la position pos
		Position anciennePos = new Position(-1, -1);

		if (soldat.estVisible)
			anciennePos = soldat.pos;// On conserve l'ancienne position
		if (pos.estValide()) {
			if (getElement(pos).estVide() && pos.estVoisine(soldat.pos) && tour != soldat.getTour()) {
				// Si la position est vide,valide et voisine de celle du soldat, et que ce
				// dernier n'a pas deja joué a ce tour
				videCase(soldat.pos);
				if (getElement(pos).estVisible)
					soldat.setVisible();
				else
					soldat.setInvisible();
				if (anciennePos.estValide())
					getElement(anciennePos).setVisible();

				setCase(pos, soldat);
				soldat.seDeplace(pos);
				soldat.joueTour(tour);
				return true;
				// On vide l'ancienne case que l'on rends visible, on met le soldat a pos, on
				// incremente num et on joue le tour du soldat
				// Et on retourne true
			}
			return false;
		}
		return false;
	}
	

	/**
	 * Methode qui decremente la nombre de soldat quand l'un d'entre eux meurs et l'enleve de la carte
	 */
	public void mort(Soldat perso) {
		Position pos;
		int i, j;
		videCase(perso.pos);
		if (perso instanceof Heros) {
			nbHeros--;
			for (i = 0; i < ((Heros) perso).tabPortee.length; i++)
				if (((Heros) perso).tabPortee[i].estValide())
					getElement(((Heros) perso).tabPortee[i]).setInvisible();
			for (i = 0; i < IConfig.LARGEUR_CARTE; i++)
				for (j = 0; j < IConfig.HAUTEUR_CARTE; j++) {
					pos = new Position(i, j);
					if (getElement(pos) instanceof Heros)
						((Heros) getElement(pos)).vision();
					// On cherche les monstres
					if (getElement(pos) instanceof Monstre) {
						if (((Monstre) getElement(pos)).cible != null)
							if (((Monstre) getElement(pos)).cible.equals(((Heros) perso)))
								((Monstre) getElement(pos)).setCible(null);
					}
				}
		} else
			nbMonstre--;

		num = ++num - tour;// On met a jour num

	}
	
	
	/**
	 * Methode qui execute l'action d'un hero 
	 * @param pos position du hero
	 * @param pos2 position de l'action du hero
	 */
	public boolean actionHeros(Position pos, Position pos2) {// Fonction qui execute l'action d'un hero
		Element hero = getElement(pos);
		Element e = getElement(pos2);

		if (hero instanceof Heros) {// On aura 4 variables de verification de portee
			int droite = (pos.getX() + ((Heros) hero).getPortee());
			int gauche = (pos.getX() - ((Heros) hero).getPortee());
			int bas = (pos.getY() + ((Heros) hero).getPortee());
			int haut = (pos.getY() - ((Heros) hero).getPortee());
			if (deplaceSoldat(pos2, (Heros) hero))// Si le joueur l'a choisit, on deplace le soldat
				return true;

			if (((Heros) hero).getTour() != tour && e instanceof Monstre && ((haut <= pos2.getY() && bas >= pos2.getY())
					&& (droite >= pos2.getX() && gauche <= pos2.getX()))) {
				// Si le joueur a laché le click sur un monstre a portée, on le combat
				((Heros) hero).combat((Monstre) e);
				// puis on joue le tour du hero
				return true;
			}

		}

		return false;
	}
	
	
	/**
	 * Methode qui fais jouer les monstres
	 */
	public void jouerSoldats() {
		boolean repos = false;
		int i, j, x;
		double distHero, distNewPos;
		Position pos, newpos, posHero = new Position(-1, -1);
		Monstre monstre;

		for (i = 0; i < IConfig.LARGEUR_CARTE; i++) {
			for (j = 0; j < IConfig.HAUTEUR_CARTE; j++) {
				pos = new Position(i, j);

				// On cherche les monstres
				if (getElement(pos) instanceof Monstre) {
					monstre = (Monstre) getElement(pos);

					if (monstre.getTour() != tour) {
						if ((float) monstre.getPoints() / monstre.getPointsMax() * 100 < 25)
							repos = true;
						if ((float) monstre.getPoints() / monstre.getPointsMax() * 100 > 40)
							repos = false;
						// Si le monstre trouvé n'a pas deja joué
						if (repos) {
							if (monstre.estVisible)
								do {
									newpos = trouvePositionDroiteVide(monstre.pos);
									System.out.println(monstre.toString() + "  "
											+ (float) monstre.getPoints() / monstre.getPointsMax() * 100);

								} while (!deplaceSoldat(newpos, monstre));
							else {
								monstre.repos();
							}
						} else {
							for (x = 0; x < monstre.tabPortee.length; x++) {
								if (monstre.tabPortee[x].estValide())
									if (getElement(monstre.tabPortee[x]) instanceof Heros)
										posHero = new Position(monstre.tabPortee[x].getX(),
												monstre.tabPortee[x].getY());
								// Si on trouve un heros a portee on met sa position dans la variable

							}
							if (posHero.estValide()) {
								// Si on a trouvé un heros, posHeros sera valide
								monstre.setCible((Heros) getElement(posHero));
								monstre.combat((Heros) getElement(posHero));
								// On le prends en chasse et on le combat
								posHero = new Position(-1, -1);
								// Sinon on se deplace:
							} else if (monstre.cible instanceof Heros) {// Si le monstre a une cible, on la suit
								distHero = monstre.cible.pos.distance(monstre.pos);
								do {
									do {
										newpos = trouvePositionVide(monstre.pos);
										distNewPos = monstre.cible.pos.distance(newpos);

									} while (distNewPos >= distHero);
								} while (!deplaceSoldat(newpos, monstre));
								// On le deplace a une position aleatoire
							} else {// Sinon on fait des allers retours dans la map
								do {
									if (monstre.pos.getX() <= 2)
										monstre.traversee = true;
									if ((monstre.pos.getX() >= IConfig.LARGEUR_CARTE - 2))
										monstre.traversee = false;
									if (!monstre.traversee)
										newpos = trouvePositionGaucheVide(monstre.pos);
									else
										newpos = trouvePositionDroiteVide(monstre.pos);

								} while (!deplaceSoldat(newpos, monstre));
							}
						}
						monstre.vision();

					}

				}

			}

		}
		++this.tour;
		// Quand la boucle est finie, c'est que tout le monde a joué
	}

	
	/**
	 * Methode qui dessine la carte avec ses element graphiquement
	 */
	public void toutDessiner(Graphics g) {
		int i, j;
		Element element;
		for (i = 0; i <= IConfig.HAUTEUR_CARTE; i++)
			g.drawLine(20, i * IConfig.NB_PIX_CASE + 20, IConfig.LARGEUR_CARTE * IConfig.NB_PIX_CASE + 20,
					i * IConfig.NB_PIX_CASE + 20);
		// On dessine les lignes horizontales

		for (i = 0; i < IConfig.LARGEUR_CARTE; i++) {
			for (j = 0; j < IConfig.HAUTEUR_CARTE; j++) {

				Position pos = new Position(i, j);
				element = getElement(pos);

				element.recupPos(pos);
				element.seDessiner(g);

				// On dessine tout les elements

			}
			g.drawLine(i * IConfig.NB_PIX_CASE + 20, 20, i * IConfig.NB_PIX_CASE + 20,
					IConfig.HAUTEUR_CARTE * IConfig.NB_PIX_CASE + 20);
			// Puis les lignes verticales
		}

		g.drawLine(IConfig.LARGEUR_CARTE * IConfig.NB_PIX_CASE + 20, 20,
				IConfig.LARGEUR_CARTE * IConfig.NB_PIX_CASE + 20, IConfig.HAUTEUR_CARTE * IConfig.NB_PIX_CASE + 20);
		// Ici on dessine la derniere ligne verticale

	}
	
	/**
	 * Methode qui renvoie le nombre de monstres et de heros restants
	 */
	
	public String toString() {
		return "Il reste " + nbHeros + " Heros et " + nbMonstre + " Monstres";

	}
	
	
	public boolean verifDim (int Lcarte, int Hcarte, int Tcase){
		if(Lcarte!=IConfig.LARGEUR_CARTE || Hcarte!=IConfig.HAUTEUR_CARTE || Tcase!=IConfig.NB_PIX_CASE)
			return false;
		return true;
		
	}

	
	@SuppressWarnings("unused")
	/**
	 * Point d'entree du jeu. ici on creer la fenetre et le panneau du jeu.
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) {

	
		// ajoute un listener : ici le listener est cette classe

		JFrame fenetre = new JFrame();
		JPanel panel = new JPanel();
		PanneauJeu p = new PanneauJeu();

		fenetre.setTitle("WARGAME");
		fenetre.setBackground(Color.white);
		fenetre.setSize(IConfig.LARGEUR_CARTE * IConfig.NB_PIX_CASE + 400, 800);
		fenetre.setLocation(IConfig.POSITION_X, IConfig.POSITION_Y);

		fenetre.setJMenuBar(p.menu);
		panel.setSize(IConfig.LARGEUR_CARTE * IConfig.NB_PIX_CASE, IConfig.HAUTEUR_CARTE * IConfig.NB_PIX_CASE);
		panel.setLocation(0, 20);

		fenetre.setContentPane(p);
		fenetre.setVisible(true);
		fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}
