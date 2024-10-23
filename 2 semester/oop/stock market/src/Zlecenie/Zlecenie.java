package Zlecenie;

import Inwestorzy.Inwestor;

public abstract class Zlecenie implements Comparable<Zlecenie>{
    private final TypZlecenia typZlecenia;
    private final String identyfikatorAkcji;
    private int liczbaSztukAkcji;
    private final int limitCeny;
    private final int terminWaznosci;
    private final int numerTury;
    private final int kolejnoscWTurze;
    private final Inwestor inwestor;

    protected Zlecenie(TypZlecenia typZlecenia, String identyfikatorAkcji, int liczbaSztukAkcji, int limitCeny,
                       int terminWaznosci, int numerTury, int kolejnoscWTurze, Inwestor inwestor) {
        this.typZlecenia = typZlecenia;
        this.identyfikatorAkcji = identyfikatorAkcji;
        this.liczbaSztukAkcji = liczbaSztukAkcji;
        this.limitCeny = limitCeny;
        this.terminWaznosci = terminWaznosci;
        this.numerTury = numerTury;
        this.kolejnoscWTurze = kolejnoscWTurze;
        this.inwestor = inwestor;
    }

    public boolean czyPrzedawnione(int numerAktualnejTury) { return numerTury >= numerAktualnejTury; }

    /* *********************************************** GETTERY *********************************************** */
    public TypZlecenia dajTypZlecenia() { return typZlecenia; }
    public String dajIdentyfikatorAkcji() { return identyfikatorAkcji; }
    public int dajLiczbeSztukAkcji() { return liczbaSztukAkcji; }
    public int dajLimitCeny() { return limitCeny; }
    public int dajTerminWaznosci() { return terminWaznosci; }
    public int dajNumerTury() { return numerTury; }
    public int dajKolejnoscWTurze() { return kolejnoscWTurze; }
    public Inwestor dajInwestora() { return inwestor; }

    @Override
    public int compareTo(Zlecenie inne) {
        if (this.typZlecenia == TypZlecenia.KUPNO && inne.dajTypZlecenia() == TypZlecenia.KUPNO) {
            if (this.limitCeny != inne.limitCeny) {
                return Integer.compare(inne.limitCeny, this.limitCeny);
            }
        }
        else if (this.typZlecenia == TypZlecenia.SPRZEDAZ && inne.dajTypZlecenia() == TypZlecenia.SPRZEDAZ) {
            if (this.limitCeny != inne.limitCeny) {
                return Integer.compare(this.limitCeny, inne.limitCeny);
            }
        }

        // Jeśli ceny są takie same, porównujemy numer tury.
        if (this.numerTury != inne.numerTury) {
            return Integer.compare(this.numerTury, inne.numerTury);
        }

        // Jeśli numer tury jest taki sam, porównujemy kolejność w turze
        return Integer.compare(this.kolejnoscWTurze, inne.kolejnoscWTurze);
    }
}
