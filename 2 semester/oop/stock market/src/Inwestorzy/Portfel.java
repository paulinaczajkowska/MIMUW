package Inwestorzy;

import Spolki.Akcja;

import java.util.ArrayList;
import java.util.Objects;

public class Portfel {
    private int iloscPieniedzy;
    private ArrayList<Akcja> akcje;

    public Portfel(int iloscPieniedzy, ArrayList<Akcja> akcje) {
        this.iloscPieniedzy = iloscPieniedzy;
        this.akcje = akcje;
    }

    /* *********************************************** SETTERY *********************************************** */
    public void dodajPieniadze(int ilosc) { iloscPieniedzy += ilosc; }
    public void zabierzPieniadze(int ilosc) { iloscPieniedzy -= ilosc; }
    public void dodajAkcje(String identyfikatorAkcji, int ilosc) {
        for (Akcja akcja : akcje) {
            if (Objects.equals(identyfikatorAkcji, akcja.dajIdentyfikatorAkcji())) {
                akcja.dodajAkcje(ilosc);
                break;
            }
        }
    }
    public void zabierzAkcje(String identyfikatorAkcji, int ilosc) {
        for (Akcja akcja : akcje) {
            if (Objects.equals(identyfikatorAkcji, akcja.dajIdentyfikatorAkcji())) {
                akcja.zabierzAkcje(ilosc);
                break;
            }
        }
    }

    /* *********************************************** GETTERY *********************************************** */
    public int dajIloscPieniedzy() { return iloscPieniedzy; }
    public Akcja dajAkcje(int i) { return akcje.get(i); }
    public int dajLiczbeRoznychAkcji() { return akcje.size(); }
    public String dajIdentyfikatorAkcji(int i) { return akcje.get(i).dajIdentyfikatorAkcji(); }
    public int dajIloscAkcji(String identyfikatorAkcji) {
        for (Akcja akcja : akcje) {
            if (Objects.equals(akcja.dajIdentyfikatorAkcji(), identyfikatorAkcji)) {
                return akcja.dajIlosc();
            }
        }
        return 0;
    }


    @Override
    public String toString() {
        return "Liczba pieniędzy: " + iloscPieniedzy + ", " + "posiadane akcje:\n" + akcje.toString();
    }
}
