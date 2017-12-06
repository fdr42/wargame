package wargame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import wargame.PanneauJeu;

public class PanneauJeu extends JPanel implements MouseListener, MouseMotionListener {
	/**
	 * Classe du panneau de jeu. Cette classe gere l'affichage des differents
	 * element du jeu.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 */
	public Carte carte;
	public Position souris = new Position(0, 0);// ,posii=new Position(0,0);;
	public boolean affCasesAction = false, afficheDetails = false, survol = false;
	private Position[] portee;
	Element elemSelect;
	public JMenuBar menu;
	private JMenu jeu;
	/** Nouvelle partie. */
	private JMenuItem nouveau;
	/** Quitter. */
	private JMenuItem quitter;
	private JMenu Savecharg;
	/** Sauvegarder */
	private JMenuItem sauvegarder;
	/** Charger */
	private JMenuItem charger;
	protected String nomFic;

	/**
	 * Constructeur du panneau de jeu
	 */
	PanneauJeu() {
try {
	carte=new Carte();
} catch (Exception e2) {
	// TODO Auto-generated catch block
	e2.printStackTrace();
}
		/* LE SYSTEME D'OPTIONS (SAUVEGARDE, NOUVELLE PARTIE ...) */
		menu = new JMenuBar();
		jeu = new JMenu("Options");
		nouveau = new JMenuItem("Nouvelle partie");
		quitter = new JMenuItem("Quitter");
		Savecharg = new JMenu("Sauver/Charger");
		sauvegarder = new JMenuItem("Sauvegarder");
		charger = new JMenuItem("Charger");

		this.setVisible(true);
		jeu.add(nouveau);
		jeu.addSeparator();
		jeu.add(quitter);
		Savecharg.add(sauvegarder);
		Savecharg.addSeparator();
		Savecharg.add(charger);
		menu.add(Savecharg);
		menu.add(jeu);

		/* Quitter */
		quitter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int ouiOuNon = JOptionPane.showConfirmDialog(null,
						"Etes vous sur de voulour quitter?\n\r La progression non sauvegardée sera perdue", "Attention",
						JOptionPane.YES_NO_OPTION);
				if (ouiOuNon == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});
		quitter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));

		/* Nouvelle partie. */
		nouveau.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int ouiOuNon = JOptionPane.showConfirmDialog(null,
						"La progression n'est pas sauvegardée automatiquement,\n\r Etes vous sur de vouloir relancer une partie?",
						"Attention", JOptionPane.YES_NO_OPTION);
				if (ouiOuNon == JOptionPane.YES_OPTION) {
					try {
						carte = new Carte();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					repaint();
				}
			}
		});
		nouveau.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));

		/* Sauvegarde */
		sauvegarder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nomSave = JOptionPane.showInputDialog(null, "Donnez un nom a votre sauvegarde");
				int ouiOuNon = JOptionPane.showConfirmDialog(null, "Etes vous sur de vouloir sauvegarder cette partie?",
						"Attention", JOptionPane.YES_NO_OPTION);
				if (ouiOuNon == JOptionPane.YES_OPTION) {
					File fichier = new File(nomSave + ".save");
					try {
						ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichier));
						oos.write(IConfig.LARGEUR_CARTE);
						oos.write(IConfig.HAUTEUR_CARTE);
						oos.write(IConfig.NB_PIX_CASE);
						oos.writeObject(carte);
						oos.close();
					} catch (Exception sauvegarde) {
						sauvegarde.printStackTrace();
					}
				}
			}
		});
		sauvegarder.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));

		/* Chargement de la partie sauvegardee */ 
		
		

		
			charger.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					final JFileChooser choixFichier = new JFileChooser(".");
					int Tcase,Lcarte,Hcarte, fichierChoisis = choixFichier.showOpenDialog(null);
					if (fichierChoisis == JFileChooser.APPROVE_OPTION) {
		                File fichierSelect = choixFichier.getSelectedFile();
		                nomFic = fichierSelect.getName();
					
					int ouiOuNon = JOptionPane.showConfirmDialog(null, "Etes vous sur de vouloir charger cette partie?",
							"Attention", JOptionPane.YES_NO_OPTION);
					if (ouiOuNon == JOptionPane.YES_OPTION) {
						ObjectInputStream ois;
						try {
							ois = new ObjectInputStream(new FileInputStream(nomFic));
							Lcarte= (int)ois.read();
							Hcarte= (int)ois.read();
							Tcase= (int)ois.read();
							if(carte.verifDim(Lcarte, Hcarte,Tcase)) {
							try {
									carte = (Carte) ois.readObject();
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							}
								else{
									 JOptionPane.showConfirmDialog(null, "Les dimentions du plateau de jeu et celles de la sauvegarde ne sont pas les memes\n\r" + 
											"(Hauteur="+Hcarte+" Largeur="+Lcarte+" Taille cases="+Tcase,
											"Attention", JOptionPane.DEFAULT_OPTION);
								}
									
							
							
							ois.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} 
					}
					}
				}	
			});
			
			charger.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));

	
		this.setVisible(true);

		JButton passer = new JButton(new ImageIcon(this.getClass().getResource("/img/passer.png")));
		passer.setToolTipText("Passer");

		init();
		passer.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// quand on a cliqué sur le bouton de passage de tour
				int tour = carte.tour;
				Heros hero;
				int i, j;
				Position pos = new Position(-1, -1);
				for (i = 0; i < IConfig.LARGEUR_CARTE; i++)
					for (j = 0; j < IConfig.HAUTEUR_CARTE; j++) {
						pos = new Position(i, j);
						if (carte.getElement(pos) instanceof Heros) {
							hero = (Heros) carte.getElement(pos);
							if (tour == carte.tour && hero.getTour() != carte.tour) {
								if (hero.getPoints() < hero.getPointsMax())
									hero.repos();
								else
									hero.joueTour(carte.tour);

							}
						}
					}
				repaint();

			}
		});

		this.setLayout(null);
		passer.setBackground(new Color(0f, .9f, .9f, .0f));
		menu.add(passer);

	}

	public void init() {
		addMouseListener((MouseListener) this);
		addMouseMotionListener((MouseMotionListener) this);
	}

	
	/**
	 * Affichage de tous les element du jeu
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(carte.nbHeros==0) {
			int ouiOuNon = JOptionPane.showConfirmDialog(null, "Vous avez perdu...\n\rVoulez vous recommencer?",
					"Attention", JOptionPane.YES_NO_OPTION);
			if (ouiOuNon == JOptionPane.YES_OPTION)
				try {
					carte=new Carte();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			else
				System.exit(0);
			
		}else if(carte.nbMonstre==0) {
			int ouiOuNon = JOptionPane.showConfirmDialog(null, "Bravo,vous avez gagné! \n\r Voulez vous recommencer?",
					"Attention", JOptionPane.YES_NO_OPTION);
			if (ouiOuNon == JOptionPane.YES_OPTION)
				try {
					carte=new Carte();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			else
				System.exit(0);
			
		}
		int posx = IConfig.LARGEUR_CARTE * IConfig.NB_PIX_CASE + 50;
		int posy = 200;

		ImageIcon image = new ImageIcon(this.getClass().getResource("/img/ImageFond.jpg"));
		Image img = image.getImage();
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);

		// On recupere la position ou seront affichés la pos et les stats
		carte.toutDessiner(g);

		g.setColor(Color.white);
		g.setFont(new Font("TimesRoman ", Font.BOLD, 20));
		g.drawString(carte.toString(), 10, IConfig.HAUTEUR_CARTE * IConfig.NB_PIX_CASE + 50);
		g.drawString("Vous jouez le tour n°" + carte.tour, 40, IConfig.HAUTEUR_CARTE * IConfig.NB_PIX_CASE + 80);
		// On ecrit le nombre de heros et monstres en haut

		if (elemSelect instanceof Soldat) {
			portee = ((Soldat) elemSelect).tabPortee;

			if (affCasesAction) // Si le joueur a selectionné un hero
				dessinTab(g, true);
			else if (survol)
				dessinTab(g, false);

		}

		if (afficheDetails) { // Si le joueur a selectionné un hero
			dessinDetails(g);
			g.drawString(elemSelect.pos.toString(), posx - 20, posy + 100);
			g.drawString(elemSelect.toString(), posx + 50, posy + 180);
		} else
			g.drawString(toString(), posx - 20, posy + 100);
	}

	public void dessinAttaque(Graphics g) {

	}

	
	/**
	 * affichage des details du jeu. Fonction appelee par paint componenent
	 */
	public void dessinDetails(Graphics g) {

		int posx = IConfig.LARGEUR_CARTE * IConfig.NB_PIX_CASE + 50, posy = 200;// Emplacement des details
		int y, tailleSimul;
		if (elemSelect instanceof Obstacle && elemSelect.estVisible) {
			new ImageIcon(this.getClass().getResource("/img/" + ((Obstacle) elemSelect).toString() + ".png"))
					.paintIcon(this, g, posx + 50, posy - 150);

			return;
		}

		boolean aPortee = false;
		int i, pvSimul, pvM,
				pvH = (int) (((float) ((Soldat) elemSelect).getPoints() / ((Soldat) elemSelect).getPointsMax()) * 100
						- 1);// Pourcentage des pv restants
		// Affichage de l'image du monstre
		new ImageIcon(this.getClass().getResource("/img/" + ((Soldat) elemSelect).getType() + ".png")).paintIcon(this,
				g, posx + 50, posy - 200);
		// Dessin de la barre de vie
		new ImageIcon(this.getClass().getResource("/img/COEUR.png")).paintIcon(this, g, posx - 15, posy + 8);
		g.setColor(Color.gray);
		g.fillRect(posx - 13, posy - 191, 60, 200);
		g.setColor(Color.green);
		g.fillRect(posx - 13, posy - 191 + (200 - pvH * 2), 60, pvH * 2);
		g.setColor(Color.white);
		g.setFont(new Font("TimesRoman ", Font.BOLD, 20));

		// Affichage de la puissance, du tir et de la portée
		new ImageIcon(this.getClass().getResource("/img/EPEE.png")).paintIcon(this, g, posx + 52, posy + 60);
		g.drawString("" + ((Soldat) elemSelect).getPuissance(), posx + 70, posy + 145);

		new ImageIcon(this.getClass().getResource("/img/ARC.png")).paintIcon(this, g, posx + 147, posy + 60);
		g.drawString("" + ((Soldat) elemSelect).getTir(), posx + 165, posy + 145);

		new ImageIcon(this.getClass().getResource("/img/OEUIL.png")).paintIcon(this, g, posx + 242, posy + 60);
		g.drawString("" + ((Soldat) elemSelect).getPortee(), posx + 260, posy + 145);

		if (elemSelect instanceof Heros && carte.getElement(souris) instanceof Monstre) {
			pvM = (int) (((float) ((Monstre) carte.getElement(souris)).getPoints()
					/ ((Monstre) carte.getElement(souris)).getPointsMax()) * 200 - 1);
			new ImageIcon(
					this.getClass().getResource("/img/" + ((Monstre) carte.getElement(souris)).getType() + ".png"))
							.paintIcon(this, g, posx + 50, posy + 250);
			new ImageIcon(this.getClass().getResource("/img/COEUR.png")).paintIcon(this, g, posx - 15, posy + 450);
			g.setColor(Color.gray);
			g.fillRect(posx - 13, posy + 250, 60, 200);
			g.setColor(Color.green);
			g.fillRect(posx - 13, posy + 450 - pvM, 60, pvM);
			g.setColor(Color.white);
			for (i = 0; i < ((Soldat) elemSelect).tabPortee.length; i++) {
				if (((Soldat) elemSelect).tabPortee[i].equals(souris)) {
					g.setColor(Color.red);
					g.drawString("VS", posx + 150, posy + 210);
					new ImageIcon(this.getClass().getResource("/img/vise.png")).paintIcon(this, g, posx + 80,
							posy + 275);
					aPortee = true;
				}
			}
			if (aPortee) {
				if (elemSelect.pos.estVoisine(souris))
					pvSimul = ((Heros) elemSelect).getPuissance();
				else
					pvSimul = ((Heros) elemSelect).getTir();
				g.setColor(new Color(.9f, .2f, .2f, .8f));
				tailleSimul = (int) (((float) pvSimul / ((Monstre) carte.getElement(souris)).getPointsMax()) * 200);
				y = posy + 450 - pvM;
				if (y + tailleSimul > posy + 450)
					tailleSimul = (posy + 450) - y;
				g.fillRect(posx - 13, y, 60, tailleSimul);
				g.setColor(Color.white);
				tailleSimul = (((Monstre) carte.getElement(souris)).getPoints() - pvSimul);
				if (tailleSimul <= 0)
					tailleSimul = 0;
				g.drawString(carte.getElement(souris).toString() + "-->(" + tailleSimul + "/"
						+ ((Monstre) carte.getElement(souris)).getPointsMax() + ")", posx - 25, posy + 245);

			} else
				g.drawString(carte.getElement(souris).toString(), posx + 50, posy + 250);

		} else if (elemSelect instanceof Heros && carte.getElement(souris).estVide()
				&& elemSelect.pos.estVoisine(souris)) {
			g.setColor(Color.green);
			g.drawString("Se deplacer vers", posx + 80, posy + 210);
			g.setColor(Color.white);
			g.drawString(souris.toString() + carte.getElement(souris).toString(), posx + 20, posy + 245);

		}
		g.setColor(Color.white);
	}
	
	
	/**
	 * Affichage de la portee avec des cases 
	 */
	public void dessinTab(Graphics g, boolean click) {
		int i;
		float transparence;
		Color couleur;
		if (click)
			transparence = 1f;
		else
			transparence = .6f;

		// On dessine les cases du tableau portee

		for (i = 0; i < portee.length; i++)
			if (portee[i].estValide() && carte.getElement(portee[i]).estVide()
					&& carte.getElement(portee[i]).estVisible) {
				if (portee[i].estVoisine(elemSelect.pos))
					couleur = new Color(.9f, .9f, .1f, transparence);
				else
					couleur = new Color(.2f, .9f, .1f, transparence);
				g.setColor(couleur);
				g.fillRect(20 + portee[i].getX() * IConfig.NB_PIX_CASE + 1,
						20 + portee[i].getY() * IConfig.NB_PIX_CASE + 1, IConfig.NB_PIX_CASE - 1,
						IConfig.NB_PIX_CASE - 1);

			}

		g.setColor(Color.black);
	}
	
	
	/**
	 * Retourne une chaine donant des informations sur la case ou le curseur pointe
	 */
	public String toString() {
		return souris.toString() + " " + carte.getElement(souris).toString();
	}
	
	

	public void mousePressed(MouseEvent e) {
		Position pos = new Position((e.getX() - 20) / IConfig.NB_PIX_CASE, (e.getY() - 20) / IConfig.NB_PIX_CASE);

		if (pos.estValide()) {
			if (carte.getElement(pos) instanceof Heros) {
				// Si le joueur garde la souris cliquée sur un hero, on affiche les tableaux
				affCasesAction = true;
			}
			elemSelect = carte.getElement(pos);
		}
		repaint();

	}

	public void mouseDragged(MouseEvent e) {
		Position pos = new Position((e.getX() - 20) / IConfig.NB_PIX_CASE, (e.getY() - 20) / IConfig.NB_PIX_CASE);
		if (pos.estValide() && !pos.equals(souris)) {
			survol = false;
			afficheDetails = false;
			if (carte.getElement(pos) instanceof Heros && !(elemSelect instanceof Heros))
				elemSelect = carte.getElement(pos);

			if (elemSelect instanceof Heros)
				afficheDetails = true;
			if(carte.getElement(pos).estVisible)
			souris = pos;
			repaint();

		}

	}

	public void mouseMoved(MouseEvent e) {
		Position pos = new Position((e.getX() - 20) / IConfig.NB_PIX_CASE, (e.getY() - 20) / IConfig.NB_PIX_CASE);
		if (pos.estValide() && !pos.equals(souris)) {
			afficheDetails = false;
			survol = false;

			if ((carte.getElement(pos) instanceof Soldat || carte.getElement(pos) instanceof Obstacle)
					&& carte.getElement(pos).estVisible) {
				afficheDetails = true;
				elemSelect = carte.getElement(pos);
			}
			if (carte.getElement(pos) instanceof Soldat)
				survol = true;
			souris = pos;
			repaint();

		}
		// On stoque la position de la souris
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {

		Position pos = new Position((e.getX() - 20) / IConfig.NB_PIX_CASE, (e.getY() - 20) / IConfig.NB_PIX_CASE);
		// On execute l'action du hero a la position ou la souris a été relachée

		if (pos.estValide()) {
			if (affCasesAction) {
				carte.actionHeros(elemSelect.pos, pos);
				if(carte.getElement(pos).estVisible)
					souris = pos;
			}
		

			if (carte.getElement(pos) instanceof Soldat || carte.getElement(pos) instanceof Obstacle) {
				elemSelect = carte.getElement(pos);
				afficheDetails = true;
			}
			
		}
		affCasesAction = false;
		repaint();
	}

	public void mouseEntered(MouseEvent e) {

	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
