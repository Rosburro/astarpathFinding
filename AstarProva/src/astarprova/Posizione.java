package astarprova;

import java.util.Objects;

public class Posizione {
    int x;
    int y;

    public Posizione(int y, int x) {
        this.x = x;
        this.y = y;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Posizione posizione = (Posizione) o;
        return x == posizione.x && y == posizione.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Posizione: " +
                "y=" + y +
                ",x=" + x+"\n";

    }
}
