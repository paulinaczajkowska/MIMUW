package SystemTransakcyjny;

import GieldaPapierowWartosciowych.GieldaPapierowWartosciowych;
import Spolki.Spolka;

import java.util.ArrayList;

public class SystemTransakcyjny {
    GieldaPapierowWartosciowych gieldaPapierowWartosciowych;
    private final int liczbaTur;
    private int numerAktualnejTury;

    public SystemTransakcyjny(GieldaPapierowWartosciowych gieldaPapierowWartosciowych, int liczbaTur) {
        this.gieldaPapierowWartosciowych = gieldaPapierowWartosciowych;
        this.liczbaTur = liczbaTur;
        numerAktualnejTury = 1;
    }

    public void nastepnaTura() { numerAktualnejTury++; }

    /* *********************************************** GETTERY *********************************************** */
    public int dajLiczbeTur() { return liczbaTur; }
    public int dajNumerAktualnejTury() { return numerAktualnejTury; }
    public ArrayList<Spolka> dajSpolki() { return gieldaPapierowWartosciowych.dajSpolki(); }

    @Override
    public String toString() { return "Liczba tur: " + liczbaTur + ", numerAktualnejTury: " + numerAktualnejTury; }
}
