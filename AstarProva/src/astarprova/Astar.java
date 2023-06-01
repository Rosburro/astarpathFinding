
package astarprova;

import java.util.ArrayList;


// c'è un problema con il fatto che ad un certo punto continua a considerare solo alcune locazioni e le altre le lascia stare
// teoricamente il problema citato sopra è risolto
public class Astar <T>{

    private Nodo<T> albero;



    private Nodo<T> radice;
    private Nodo<T> fine;
    private Euristica<T> i;

    private double moltiplicatore;
    public Astar(Euristica<T> i) {
        this.i = i;
    }


    //controllare tutti i padri fino a null se è stato trovato il figlio uguale allora c'è un ciclo

    public void settaFigli(ArrayList<Nodo<T>>figli){
        Nodo<T> appoggio;
        int costo=0;
        if(!figli.isEmpty()){
            costo = calcolaCosto(figli.get(0));
        }
        for(int i=0;i<figli.size();i++){
            appoggio = figli.get(i);
            //contenuto(appoggio, radice);
            //if(!contenuto){
                if(!giaVisto(appoggio) && !controlloRicorsione(appoggio)){
                    appoggio.costo= costo+moltiplicatore*this.i.euristica(this.radice, this.fine, appoggio);
                    albero.aggiungiFiglio(appoggio);
                }
            //}
            //contenuto = false;
        }
    }


    /*
    * questi 2 metodi + un attributo servono a stabilire se l'attributo del nodo passato è stato o meno già visto in qualche nodo passato
    * se si allora il nodo non verrà aggiunto come possibile opzione di evoluzione
    *
    * */
    private boolean giaVisto;
    private boolean giaVisto(Nodo<T> nodo){
        giaVisto=false;
        controlloGiaVisto(radice, nodo.attributo);
        return giaVisto;
    }
    private void controlloGiaVisto(Nodo<T> nodo, T attributo) {
        Nodo<T> pp;
        if(!nodo.figli.isEmpty() && !giaVisto){
            for (int i=0;i<nodo.figli.size();i++){
                if(nodo.attributo.equals(attributo)){
                    giaVisto=true;
                }
                controlloGiaVisto(nodo.figli.get(i), attributo);
            }
        }

    }

    /*
    * calcola il costo necessario (il numero di "padri") ad arrivare al nodo dato.
    * */
    private int calcolaCosto(Nodo<T> nodo){
        int costo=0;
        nodo=nodo.padre;// parte direttamente dal padre perchè se è il primo va direttamente a zero se è il secondo va a 1 e così via
        while(nodo!=null){
            nodo=nodo.padre;
            costo++;
        }
        return costo;
    }


    /*
    * evita che il controllo vada in ciclo su uno un pattern ricorsivo trovato
    *
    * */
    private boolean controlloRicorsione(Nodo<T> nodo){

        T daAnalizzare = nodo.attributo;
        int cont=0;
        //da controlalre
        nodo = nodo.padre;
        while(nodo!=null){
            //System.out.println(nodo.attributo.toString()+" da analizzare "+daAnalizzare.toString());
            if(nodo.attributo.equals(daAnalizzare)){
                //System.out.println("entrato    sdasdasdasdasdasdsa");
                //nodo.costo+=100000000;
                return true;
            }
            nodo = nodo.padre;
            cont++;
        }
        //System.out.println(cont+" giri asdasdsadsadasdasdasdasd");
        return false;
    }



    /*
    * imposta tutti i nodi che hanno lo specifico attributo uguale a già percorsi poichè se non sono stati controllati in precedenza significa
    * significa che non dovranno essere controllati in seguito poichè avevano un costo maggiore.
    * inoltre serve ad evitare che un singolo punto venga controllato da tutti i possibili percorsi.
    *
    * */
    public void impostaComeVisti(T attributo, Nodo<T> nodo){
        Nodo<T> pp;
        if(!nodo.figli.isEmpty()) {
            for (int i = 0; i < nodo.figli.size(); i++) {
                pp = nodo.figli.get(i);

                impostaComeVisti(attributo, pp);
            }
        }else {
            if(nodo.attributo.equals(attributo)){
                nodo.visto=true;
            }
        }
    }

    /*
    * questi 2 metodi + un attributo
    * servono a stabilire il migior nodo da cui evolvere l'albero
    * se necessario prendono l'opzione possibile
    * fanno un controllo se il nodo è già stato percorso o meno e se si allora non lo considerano
    * */
    public Nodo<T> trovaNodoMigliore(Nodo<T> nodo , Nodo<T> migliore){
        this.migliore = migliore;
        trovaNodo(nodo);
        //System.out.println(this.migliore.attributo.toString())
       // System.out.println(this.migliore.attributo.toString()+" "+this.migliore.visto);

        if(this.migliore.visto){
            /*
            * questo if serve nella situazione in cui non si riesca a trovar un nodo non visto e quindi si ricorre al prendere il
            * primo nodo non visto disponibile e a continuare la ricerca da quello
            * */

            this.migliore = trovaNonVisto();

        }

        impostaComeVisti(this.migliore.attributo, radice);
        this.migliore.visto=true;
        return this.migliore;

    }
    private Nodo<T> migliore;
    public void trovaNodo(Nodo<T> nodo){
        Nodo<T> pp;

       if(!nodo.figli.isEmpty()){
           for(int i=0;i<nodo.figli.size();i++){
               pp = nodo.figli.get(i);
               if( !pp.visto && (this.migliore.costo>pp.costo || this.migliore.visto) ){
                   this.migliore = pp;
                   //System.out.println("migliore cambiato");
               }
               //if(!pp.visto) System.out.println("è falso");
               //if(pp.costo<0){System.out.println("minore di 0");try {Thread.sleep(1000);} catch (InterruptedException e) {}}

               //System.out.println(pp.costo);
               trovaNodo(pp);
           }

       }else {
           if(!nodo.visto && (nodo.costo<this.migliore.costo || this.migliore.visto)){
               this.migliore = nodo;
           }
       }
      // System.out.println("andato fuori");

        
    }


    /*
    * questi 2 metodi + un attributo
    * servono a trovare il più vicino nodo ancora non visto.
    *
    * */
    private Nodo<T> nonVisto;
    private Nodo<T> trovaNonVisto(){
        nonVisto=radice;
        primoNonVisto(albero);
        return nonVisto;
    }


    private void primoNonVisto(Nodo<T> nodo){
        Nodo<T> pp;

        if(!nodo.figli.isEmpty() && nonVisto==null){
            for(int i=0;i<nodo.figli.size();i++){
                pp = nodo.figli.get(i);
                if(!pp.visto && (nonVisto==null || pp.costo<nonVisto.costo)){
                    nonVisto=pp;
                }else trovaNodo(pp);
            }

        }
    }


    /*
    * restituisce il valore dell'euristica di uno specifico nodo
    * */
    public double getEuristicaNodo(Nodo<T> nodo){
        return i.euristica(radice, fine, nodo);
    }

    public void setAlbero(Nodo<T> albero) {
        this.albero = albero;
    }

    public void setRadice(Nodo<T> radice) {
        this.radice = radice;
    }

    public void setMoltiplicatore(double moltiplicatore) {
        this.moltiplicatore = moltiplicatore;
    }

    public void setFine(Nodo<T> fine) {
        this.fine = fine;
    }

    public Nodo<T> getAlbero() {
        return albero;
    }

    public Nodo<T> getRadice() {
        return radice;
    }

    public double getMoltiplicatore() {
        return moltiplicatore;
    }
}
