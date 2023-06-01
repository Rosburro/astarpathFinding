package astarprova;

import java.util.ArrayList;

public class Nodo <T> implements Comparable<T>{
    protected ArrayList<Nodo<T>> figli;
    protected Nodo<T> padre;

    protected T attributo;
    protected double costo;
    protected boolean visto;

    public Nodo(Nodo<T> padre) {
        this.figli = new ArrayList<>();
        this.padre = padre;
        visto = false;
    }
    public Nodo(Nodo<T> padre, T att, double costo) {
        this.figli = new ArrayList<>();
        this.padre = padre;
        attributo = att;
        this.costo = costo;
        visto = false;
    }
    public Nodo(Nodo<T> padre, T att) {
        this.figli = new ArrayList<>();
        this.padre = padre;
        attributo = att;
        visto = false;
    }

    public void aggiungiFiglio(Nodo<T> figlio){
        figli.add(figlio);
    }

    @Override
    public int compareTo(T o) {
        return 0;
    }
}
