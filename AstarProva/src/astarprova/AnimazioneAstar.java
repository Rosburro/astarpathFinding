package astarprova;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;

import java.util.ArrayList;


public class AnimazioneAstar extends AnimationTimer {

    private Posizione courrent, fine;

    private Astar astar;
    private boolean start;

    private int depth;

    private long prec;

    private Button bottoni[][];

    private Background bgVisto,bgSelected, bgPath;

    private ArrayList<Nodo<Posizione>> possibilita;
    private ArrayList<Posizione> muri;
    private boolean iniziato = false;
    private int depthMax;

    private long tempo1= 1_000_0;
    private long tempo2 = 1_000;

    int numeroNodi = 0;
    Button tramite;

    public AnimazioneAstar(Button[][] bottoni, Background bgVisto, Background bgSelected, Background path, Button b) {
        this.bottoni = bottoni;
        this.bgVisto = bgVisto;
        this.bgSelected = bgSelected;
        this.bgPath = path;
        depthMax=Integer.MAX_VALUE;
        tramite = b;
        possibilita = new ArrayList<>();
    }



    @Override
    public void handle(long now) {

        if(now-prec>=tempo2){
            cancellaPercorsoEffettuato();
        }
        if(now-prec>=tempo1) {//un secondo
            //System.out.println("funziona");

            if (!courrent.equals(fine) && depth < depthMax) {// depth è un numero messo li tanto per far fermare il programma ad un tot di iterazioni
                iniziato=true;
                possibilita = possibilita(courrent, astar.getAlbero());
                astar.settaFigli(possibilita);
                astar.setAlbero( astar.trovaNodoMigliore(astar.getRadice(), astar.getRadice()));

                if (astar.getAlbero()!=astar.getRadice()){// controlla se il nodo la ricerca è arrivata ad un punto di stallo o meno
                    courrent = (Posizione) astar.getAlbero().attributo;
                    depth++;
                    colora(possibilita, courrent);
                    coloraPercorsoEffettuato();
                    prec = now;
                }else{
                    this.stop();
                    System.out.println("finito");
                    coloraPercorsoEffettuato();
                    tramite.fire();
                    iniziato=false;
                }

            }else {
                this.stop();
                System.out.println("finito");
                coloraPercorsoEffettuato();
                tramite.fire();
                iniziato=false;
            }
        }
    }

    private void colora(ArrayList<Nodo<Posizione>> possibilita, Posizione courrent) {
        for(int i=0;i<possibilita.size();i++){
            if(bottoni[possibilita.get(i).attributo.y][possibilita.get(i).attributo.x].getBackground()!=bgSelected){
                bottoni[possibilita.get(i).attributo.y][possibilita.get(i).attributo.x].setBackground(this.bgVisto);

            }
        }
        bottoni[courrent.y][courrent.x].setBackground(this.bgSelected);
    }

    public void setta(Posizione fine, Posizione courrent,  Astar astar, ArrayList<Posizione> muri) {
        this.fine = fine;
        this.courrent = courrent;
        this.astar = astar;
        this.muri = muri;
        System.out.println("fine: "+fine.toString());
        System.out.println("inizio: "+courrent.toString());
        depth=1;
    }

    private ArrayList<Nodo<Posizione>> possibilita(Posizione corrente, Nodo<Posizione> padre){
        int y = bottoni.length;
        int x = bottoni[0].length;
        ArrayList<Nodo<Posizione>> p = new ArrayList<>();

       // System.out.println("posizione padre: "+posPadre.toString());
        Posizione pos;
        pos = new Posizione(corrente.y+1, corrente.x);
        if(corrente.y!=y-1 && !contenuto(pos)){

            p.add( new Nodo<>(padre, pos));//da fare il costo
        }
        pos = new Posizione(corrente.y-1, corrente.x);
        if(corrente.y>0 && !contenuto(pos) ){

            p.add( new Nodo<>(padre,pos ));
        }
        pos = new Posizione(corrente.y, corrente.x+1);
        if(corrente.x!=x-1 && !contenuto(pos)){

            p.add( new Nodo<>(padre,pos  ));
        }
        pos = new Posizione(corrente.y, corrente.x-1);
        if(corrente.x>0 && !contenuto(pos) ){

            p.add( new Nodo<>(padre,pos ));
        }
        return p;
    }


    private boolean contenuto(Posizione p){
        for(Posizione pos : muri){
            if(pos.equals(p)){
                return true;
            }
        }
        return false;
    }

    private void coloraPercorsoEffettuato(){
        Nodo<Posizione> app = astar.getAlbero();
        numeroNodi=-1;
        while(app!=null){
            bottoni[app.attributo.y][app.attributo.x].setBackground(bgPath);
            numeroNodi++;
            app= app.padre;
        }
    }
    private void cancellaPercorsoEffettuato(){
        for(int i=0;i<bottoni.length;i++){
            for(int j=0;j<bottoni[0].length;j++){
                if(bottoni[i][j].getBackground()==bgPath){// se non è l'inizio ne la fine ne un muro
                    bottoni[i][j].setBackground(bgSelected);
                }
            }
        }

    }

    public boolean isIniziato() {
        return iniziato;
    }

    public void setTempo1(long tempo1) {
        this.tempo1 = tempo1;
    }

    public void setTempo2(long tempo2) {
        this.tempo2 = tempo2;
    }

    public void setDepthMax(int depthMax) {
        this.depthMax = depthMax;
    }
}
