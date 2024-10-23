package GieldaPapierowWartosciowych;

import Spolki.Spolka;

import java.util.ArrayList;
import java.util.Objects;

public class GieldaPapierowWartosciowych {
    private ArrayList<Spolka> spolki;

    public GieldaPapierowWartosciowych(ArrayList<Spolka> spolki) { this.spolki = spolki; }

    /* *********************************************** GETTERY *********************************************** */
    public ArrayList<Spolka> dajSpolki() { return spolki; }
    public Spolka dajSpolke(int i) { return spolki.get(i); }
    public int dajIloscSpolek() { return spolki.size(); }
    public int dajCeneOstatniejAkcji(String nazwaSpolki) {
        for (Spolka spolka : spolki) {
            if (Objects.equals(spolka.dajNazwe(), nazwaSpolki)) {
                return spolka.dajCeneOstatniejAkcji();
            }
        }
        return 0;
    }
    public int dajNumerOstatniejAkcji(String nazwaSpolki) {
        for (Spolka spolka : spolki) {
            if (Objects.equals(spolka.dajNazwe(), nazwaSpolki)) {
                return spolka.dajNumerOstatniejAkcji();
            }
        }
        return 0;
    }
}
