package wargame;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
	public boolean aPortee = false,affCasesAction = false, afficheDetails = false, survol = false;
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
					
				
						ObjectInputStream ois;
						try {
							ois = new ObjectInputStream(new FileInputStream(nomFic));
							Lcarte= (int)ois.read();
							Hcarte= (int)ois.read();
							Tcase= (int)ois.read();
							if(carte.verifDim(Lcarte, Hcarte,Tcase)) {	int ouiOuNon = JOptionPane.showConfirmDialog(null, "Etes vous sur de vouloir charger cette partie?",
									"Attention", JOptionPane.YES_NO_OPTION);
							if (ouiOuNon == JOptionPane.YES_OPTION) {
								
							try {
									carte = (Carte) ois.readObject();
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							ois.close();
							repaint();
							}
							}
								else{
									 JOptionPane.showConfirmDialog(null, "Les dimentions du plateau de jeu et celles de la sauvegarde ne sont pas les memes\n\r" + 
											"(Hauteur="+Hcarte+" Largeur="+Lcarte+" Taille cases="+Tcase,
											"Attention", JOptionPane.DEFAULT_OPTION);
								}
									
							
							
							
							
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} 
					}
					
				}	
			});
			
			charger.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));

	
		this.setVisible(true);

		JButton passer = new JButton(new ImageIcon(this.getClass().getResource("/img/passer.png")));

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
		g.setFont(new Font("TimesRoman ", Font.BOLD, 15));
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

		if (afficheDetails)  // Si le joueur a selectionné un hero
			dessinDetails(g);
		g.drawString(toString(), posx - 20, posy + 80);
		if(carte.getElement(souris) instanceof Soldat && !aPortee && carte.getElement(souris).estVisible) {
			String[] text=new String[4];
			text[0]=((Soldat)carte.getElement(souris)).toString();
			text[1]="Portee: "+((Soldat)carte.getElement(souris)).getPortee();
			text[2]="Degats CaC: "+((Soldat)carte.getElement(souris)).getPuissance();
			text[3]="Degats distant: "+((Soldat)carte.getElement(souris)).getTir();
			infoBulle(g,text,souris.getX(),souris.getY());
			new ImageIcon(this.getClass().getResource("/img/"+((Soldat)carte.getElement(souris)).getType()+"_icone.png"))
			.paintIcon(this, g, 140+souris.getX()*IConfig.NB_PIX_CASE, 65+souris.getY()*IConfig.NB_PIX_CASE );
		}
		 Graphics2D g2d = (Graphics2D) g;
		 int moitie=(IConfig.NB_PIX_CASE*IConfig.LARGEUR_CARTE-300)/2;	
		 int calculAllies=(int)(((float)carte.pvAllies/Carte.pvAlliesMax)*moitie);
		 int calculEnnemis=(int)(((float)carte.pvEnnemis/Carte.pvEnnemisMax)*moitie);
		 System.out.println(moitie+"  "+(calculAllies-calculEnnemis));
		    GradientPaint gp1 = new GradientPaint(350, 40+IConfig.HAUTEUR_CARTE*IConfig.NB_PIX_CASE, Color.green, IConfig.NB_PIX_CASE*IConfig.LARGEUR_CARTE-30, IConfig.HAUTEUR_CARTE*IConfig.NB_PIX_CASE+90, Color.red, true);
		    g.setFont(new Font("TimesRoman ", Font.BOLD, IConfig.LARGEUR_CARTE));
		    g2d.setPaint(gp1);
		g2d.fillRect(300, 40+IConfig.HAUTEUR_CARTE*IConfig.NB_PIX_CASE, IConfig.NB_PIX_CASE*IConfig.LARGEUR_CARTE-300, 50);
		g.setColor(Color.white);
	    g.drawString("Victoire", 310, IConfig.HAUTEUR_CARTE*IConfig.NB_PIX_CASE+50+IConfig.NB_PIX_CASE);
	    g.drawString("Defaite", (IConfig.LARGEUR_CARTE-5)*IConfig.NB_PIX_CASE, IConfig.HAUTEUR_CARTE*IConfig.NB_PIX_CASE+50+IConfig.NB_PIX_CASE);
	    posx=IConfig.NB_PIX_CASE*IConfig.LARGEUR_CARTE-moitie;
	    g.fillRect(posx-calculAllies+calculEnnemis, 40+IConfig.HAUTEUR_CARTE*IConfig.NB_PIX_CASE, 10, 50);
		g.setFont(new Font("TimesRoman ", Font.BOLD, 17));
	}


	
	/**
	 * affichage des details du jeu. Fonction appelee par paint componenent
	 */
	public void dessinDetails(Graphics g) {
		aPortee = false;
		String[] text;
		String arme;
		int posx = IConfig.LARGEUR_CARTE * IConfig.NB_PIX_CASE + 50, posy = 200;// Emplacement des details
		int y, tailleSimul;
		if (elemSelect instanceof Obstacle && elemSelect.estVisible) {
			new ImageIcon(this.getClass().getResource("/img/" + ((Obstacle) elemSelect).toString() + ".png"))
					.paintIcon(this, g, posx-30 , posy - 190);

			return;
		}else if (elemSelect instanceof Soldat && elemSelect.estVisible) {
		int i, pvSimul, pvM;
				
		g.setColor(Color.white);
		g.setFont(new Font("TimesRoman ", Font.BOLD, 17));
		dessinImages(g,posx,posy);
		if (elemSelect instanceof Heros && carte.getElement(souris) instanceof Monstre) {
			Monstre monstre =((Monstre) carte.getElement(souris));
			pvM = (int) (((float) monstre.getPoints() / monstre.getPointsMax()) * 200 - 1);
			new ImageIcon(
					this.getClass().getResource("/img/" + monstre.getType() + ".png"))
							.paintIcon(this, g, posx + 50, posy +140);
			new ImageIcon(this.getClass().getResource("/img/COEUR.png")).paintIcon(this, g, posx - 15, posy + 320);
			g.setColor(Color.gray);
			g.fillRect(posx - 13, posy + 120, 40, 200);
			g.setColor(Color.green);
			g.fillRect(posx - 13, posy + 320 - pvM, 40, pvM);
			g.setColor(Color.white);
			for (i = 0; i < ((Soldat) elemSelect).tabPortee.length; i++) {
				if (((Soldat) elemSelect).tabPortee[i].equals(souris)) {
					g.setColor(Color.red);
					g.drawString("VS", posx + 100, posy + 120);
					new ImageIcon(this.getClass().getResource("/img/vise.png")).paintIcon(this, g, posx + 60,
							posy + 155);
					aPortee = true;
				}
			}
			if (aPortee) {
				if (elemSelect.pos.estVoisine(souris))
					pvSimul = ((Heros) elemSelect).getPuissance();
				else
					pvSimul = ((Heros) elemSelect).getTir();
				
				g.setColor(new Color(.9f, .2f, .2f, .8f));
				tailleSimul = (int) (((float) pvSimul / monstre.getPointsMax()) * 200);
				y = posy + 320 - pvM;
				if (y + tailleSimul > posy + 450)
					tailleSimul = (posy + 450) - y;
				g.fillRect(posx - 13, y, 40, tailleSimul);
				g.setColor(Color.white);
				tailleSimul = (monstre.getPoints() - pvSimul);
				text=new String[4];
				text[0]= "Attaquer "+monstre.getType()+" "+monstre.NOM;
				text[1]=monstre.toString();
				text[2]="Dégats: "+pvSimul;
				text[3]="-->(" + tailleSimul + "/"+ monstre.getPointsMax() + ")";
				infoBulle(g,text,souris.getX(),souris.getY());
				if(souris.estVoisine(elemSelect.pos))
					arme="EPEE";
				else
					arme="ARC";
					new ImageIcon(this.getClass().getResource("/img/"+arme+"_icone.png"))
				.paintIcon(this, g, 130+souris.getX()*IConfig.NB_PIX_CASE, 85+souris.getY()*IConfig.NB_PIX_CASE );
				if (tailleSimul <= 0)
					tailleSimul = 0;

			} 
			

		} else if (elemSelect instanceof Heros && carte.getElement(souris).estVide()
				&& elemSelect.pos.estVoisine(souris) && affCasesAction) {
			text=new String[2];
			text[0]="Se deplacer";
			text[1]="vers "+souris.toString();
			infoBulle(g,text,souris.getX(),souris.getY());
			new ImageIcon(this.getClass().getResource("/img/PAS_icone.png"))
			.paintIcon(this, g, 120+souris.getX()*IConfig.NB_PIX_CASE, 55+souris.getY()*IConfig.NB_PIX_CASE );
			
		}
		}
		g.setColor(Color.white);
		
	}
	
	
	public void dessinImages(Graphics g,int posx, int posy) {
	int pvH = (int) (((float) ((Soldat) elemSelect).getPoints() / ((Soldat) elemSelect).getPointsMax()) * 100
				- 1);// Pourcentage des pv restants


// Dessin de la barre de vie
new ImageIcon(this.getClass().getResource("/img/COEUR.png")).paintIcon(this, g, posx - 15, posy + 8);
g.setColor(Color.gray);
g.fillRect(posx - 13, posy - 191, 40, 200);
g.setColor(Color.green);
g.fillRect(posx - 13, posy - 191 + (200 - pvH * 2), 40, pvH * 2);
		// Affichage de l'image du soldat
				new ImageIcon(this.getClass().getResource("/img/" + ((Soldat) elemSelect).getType() + ".png")).paintIcon(this,
						g, posx + 50, posy - 195);
				// Affichage de la puissance, du tir et de la portée
				new ImageIcon(this.getClass().getResource("/img/EPEE.png")).paintIcon(this, g, posx + 32, posy -35);
				g.drawString("" + ((Soldat) elemSelect).getPuissance(), posx + 50, posy+40 );

				new ImageIcon(this.getClass().getResource("/img/ARC.png")).paintIcon(this, g, posx + 107, posy - 35);
				g.drawString("" + ((Soldat) elemSelect).getTir(), posx + 125, posy +40);

				new ImageIcon(this.getClass().getResource("/img/OEUIL.png")).paintIcon(this, g, posx + 182, posy -35);
				g.drawString("" + ((Soldat) elemSelect).getPortee(), posx + 200, posy +40);

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
	
	
	
	public void infoBulle(Graphics g,String[] text,int x, int y) {
		int i=0;
		int larg=124;
		if(text.length<=2)
			larg=90;
		
		g.setColor(new Color(.2f,.9f,.7f,.8f));
g.fillRect(20+(1+x)*IConfig.NB_PIX_CASE, (y+1)*IConfig.NB_PIX_CASE+IConfig.NB_PIX_CASE/2, larg, 20*text.length);
g.setColor(new Color(.2f,.6f,.7f,.4f));
g.fillRect(22+(x+1)*IConfig.NB_PIX_CASE, (y+1)*IConfig.NB_PIX_CASE+IConfig.NB_PIX_CASE/2+2, larg-4, 20*text.length-4);
g.setColor(Color.black);
g.setFont(new Font("TimesRoman ", Font.BOLD, 10));
while(i!=text.length) {
	g.drawString(text[i],22+(x+1)*IConfig.NB_PIX_CASE, 17*i+(y+2)*IConfig.NB_PIX_CASE);
	i++;
}
g.setFont(new Font("TimesRoman ", Font.BOLD, 15));
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
			souris = pos;
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
