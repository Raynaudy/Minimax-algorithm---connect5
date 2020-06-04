package planeteH_2;

import Connect5Game.Grille;
import Connect5Game.Joueur;
import Connect5Game.Position;
import java.util.List;
import java.util.ArrayList;
import java.lang.Math;
import Connect5Game.JoueurArtificiel;

public class Etat {
  public Grille grille;
  public Position position;
  public Integer value;
  public Byte joueur;
  public Byte joueurCourant;
  public boolean fini;
  public Intervalle intervalle;
  public JoueurArtificiel ja;

  public Etat(Grille grille, Position position, boolean fini, Intervalle intervalle, JoueurArtificiel ja) {
    this.grille = grille;
    this.position = position;
    this.fini = fini;
    this.intervalle = intervalle;
    this.ja = ja;
    int cptPions = 0;
    for (int l = 0; l < grille.getData().length; l++) {
      for (int c = 0; c < grille.getData()[l].length; c++) {
        if (grille.get(l, c) >= 1 && grille.get(l, c) <=2) {
          cptPions++;
        }
      }
    }
    if (cptPions%2 == 0) {
      this.joueur = 1;
    } else {
      this.joueur = 2;
    }
    this.joueurCourant = this.joueur;
  }

  public Etat(Grille grille, Position position, Byte joueur, Byte joueurCourant, boolean fini, Intervalle intervalle, JoueurArtificiel ja) {
    this.grille = grille;
    this.position = position;
    this.joueur = joueur;
    this.joueurCourant = joueurCourant;
    this.fini = fini;
    this.intervalle = intervalle;
    this.ja = ja;
  }

  public List<Etat> successeurs(int depth) {
    int max = Intervalle.MINUS_INFINITY;
    int min = Intervalle.PLUS_INFINITY;
    if (this.fini) return null;
    List<Etat> successeurs = new ArrayList<>();
    for (int l = 0; l < grille.getData().length; l++) {
      for (int c = 0; c < grille.getData()[l].length; c++) {
        if (grille.getData()[l][c] == 0) {
          Grille clone = grille.clone();
          Position p = new Position(l, c);
          clone.set(p, joueurCourant);
          Etat e = null;
          if (clone.nbLibre() == 0) {
            int t = this.joueurCourant+1;
            t = (t%2) + 1;
            e = new Etat(clone, p, joueur, (byte) t, true, this.intervalle, this.ja);
          } else {
            int t = this.joueurCourant+1;
            t = (t%2) + 1;
            e = new Etat(clone, p, joueur, (byte) t, false, this.intervalle, this.ja);
          }
          if (e != null){
              e.value = e.minimaxValue(depth);
              if (e.value == null) return null;
              //System.out.println("value: " + e.value);
              if(e.value > max) max = e.value;
              if(e.value < min) min = e.value;
              //System.out.println("intervalle: " + this.intervalle.min + ", " + this.intervalle.max + " max: " + max + " min: " + min);

              if(joueurCourant == joueur) { //MAX
                this.intervalle.min = max;
                if(!this.intervalle.verify()) {
                  //System.out.println("intervalle: " + this.intervalle.min + ", " + this.intervalle.max + " err: " + max );
                  return successeurs;
                }

              } else { //MIN
                this.intervalle.max = min;
                if(!this.intervalle.verify()) {
                  //System.out.println("intervalle: " + this.intervalle.min + ", " + this.intervalle.max + " err: " + min );
                  return successeurs;
                }
              }
              successeurs.add(e);
              if (System.currentTimeMillis() - ja.startChrono > ja.delais)
                return null;
          }
        }
      }
    }
    //System.out.println("succ: " + successeurs.size());
    return successeurs;
  }

  public int utility(){

    int score = 0;
    ArrayList<Position> ligneVisitee = new ArrayList<Position>();
    ArrayList<Position> colVisitee = new ArrayList<Position>();
    ArrayList<Position> diag1Visitee = new ArrayList<Position>();
    ArrayList<Position> diag2Visitee = new ArrayList<Position>();

    for (int l = 0; l < grille.getData().length; l++){
      for (int c = 0; c < grille.getData()[l].length; c++){
        Position pos = new Position(l, c);

        if (grille.getData()[l][c] == this.joueur){

          if(!contains(ligneVisitee, pos))
            score += checkScore( 1 + comptePions(0, -1, ligneVisitee, pos, this.joueur) + comptePions(0, 1, ligneVisitee, pos, this.joueur));
          if(!contains(colVisitee, pos))
            score += checkScore(1 + comptePions(1, 0, colVisitee, pos, this.joueur) + comptePions(-1, 0, colVisitee, pos, this.joueur));
          if(!contains(diag1Visitee, pos))
            score += checkScore(1 + comptePions(1, -1, diag1Visitee, pos, this.joueur) + comptePions(-1, 1, diag1Visitee, pos, this.joueur));
          if(!contains(diag2Visitee, pos))
            score += checkScore(1 + comptePions(-1, -1, diag2Visitee, pos, this.joueur) + comptePions(1, 1, diag2Visitee, pos, this.joueur));
        }

        if (grille.getData()[l][c] == (this.joueur%2)+1){

          int temp = (this.joueur%2)+1;
          Byte ennemy = (byte) temp;
          if(!contains(ligneVisitee, pos))
            score -= checkScore(1 + comptePions(0, -1, ligneVisitee, pos, ennemy ) + comptePions(0, 1, ligneVisitee, pos, ennemy));
          if(!contains(colVisitee, pos))
            score -= checkScore(1 + comptePions(1, 0, colVisitee, pos, ennemy) + comptePions(-1, 0, colVisitee, pos, ennemy));
          if(!contains(diag1Visitee, pos))
            score -= checkScore(1 + comptePions(1, -1, diag1Visitee, pos, ennemy) + comptePions(-1, 1, diag1Visitee, pos, ennemy));
          if(!contains(diag2Visitee, pos))
            score -= checkScore(1 + comptePions(-1, -1, diag2Visitee, pos, ennemy) + comptePions(1, 1, diag2Visitee, pos, ennemy));
        }
      }
    }
    return score;
  }

  public static int checkScore(int val) {
    return val > 5 ? 0 : (int) Math.pow(10, val-1);
  }

   public int comptePions(int dirY, int dirX, ArrayList<Position> pionsVisites, Position pos, Byte joueur){

     Position next = new Position(pos.ligne + dirY, pos.colonne + dirX);
     pionsVisites.add(pos);
     if (pos.ligne+dirY > grille.getData().length - 1
      || pos.ligne+dirY < 0 || pos.colonne+dirX > grille.getData()[0].length - 1 || pos.colonne+dirX < 0 || contains(pionsVisites, next)|| grille.getData()[pos.ligne + dirY][pos.colonne + dirX] != joueur )
      return 0;

    return 1 + comptePions(dirY, dirX, pionsVisites, next, joueur);

   }

  public static Integer maxValue(List<Etat> s){
    Integer max = null;
    if(s == null || s.size()==0)
      return max;

    for (Etat e: s){
      if(max == null || e.value > max)
        max = e.value;
    }
    return max;
  }

  public static Integer minValue(List<Etat> s){
    Integer min = null;
    if(s == null || s.size()==0)
      return min;

    for (Etat e: s){
      if(min == null || e.value < min)
        min = e.value;
    }
    return min;
  }

  public Integer minimaxValue(int depth) {
    depth--;
    if(fini || depth == 0)
      return utility();
    //System.out.println("depth: " + depth);

    List<Etat> s = successeurs(depth);

    if(s == null) return null;
    if(joueur == joueurCourant)
      return Etat.maxValue(s);
    else return Etat.minValue(s);
  }

  public static boolean contains(ArrayList<Position> source, Position pos) {
    for(Position p: source) {
      if (p.ligne == pos.ligne && p.colonne == pos.colonne)
        return true;
    }
    return false;
  }
}
