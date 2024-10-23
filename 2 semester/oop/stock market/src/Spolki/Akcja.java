package Spolki;

public class Akcja {
    private final String identyfikatorAkcji;
    private int ilosc;

    public Akcja(String identyfikatorAkcji, int ilosc) {
        this.identyfikatorAkcji = identyfikatorAkcji;
        this.ilosc = ilosc;
    }

    /* *********************************************** SETTERY *********************************************** */
    public void dodajAkcje(int ilosc) { this.ilosc += ilosc; }
    public void zabierzAkcje(int ilosc) { this.ilosc -= ilosc; }

    /* *********************************************** GETTERY *********************************************** */
    public String dajIdentyfikatorAkcji() { return identyfikatorAkcji; }
    public int dajIlosc() { return ilosc; }
    public Akcja dajKopieAkcji() { return new Akcja(identyfikatorAkcji, ilosc); }

    @Override
    public String toString() { return "Akcje spółki " + identyfikatorAkcji + ", ilosc " + ilosc; }
}
