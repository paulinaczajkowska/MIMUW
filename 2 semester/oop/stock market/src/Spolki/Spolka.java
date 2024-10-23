package Spolki;

public class Spolka {
    private final String nazwa;
    private int cenaOstatniejTransakcji;
    private int numerOstatniejTury;

    public Spolka(String nazwa, int cenaOstatniejTury) {
        this.nazwa = nazwa;
        this.cenaOstatniejTransakcji = cenaOstatniejTury;
        this.numerOstatniejTury = 0;
    }

    /* *********************************************** SETTERY *********************************************** */
    public void ustawCeneOstatniejAkcji(int cena) { this.cenaOstatniejTransakcji = cena; }
    public void ustawNumerOstatniejTury(int numer) {this.numerOstatniejTury = numer; }

    /* *********************************************** GETTERY *********************************************** */
    public String dajNazwe() { return nazwa; }
    public int dajCeneOstatniejAkcji() { return cenaOstatniejTransakcji; }
    public int dajNumerOstatniejAkcji() { return numerOstatniejTury; }

    @Override
    public String toString() {
        return "Spolka " + nazwa + ", cena ostatniej transakcji: " + cenaOstatniejTransakcji + ", numerOstatniejTury: "
                + numerOstatniejTury;
    }
}
