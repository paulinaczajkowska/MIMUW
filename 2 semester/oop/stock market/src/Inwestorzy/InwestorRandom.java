package Inwestorzy;

import Losowanie.Losowanie;
import Spolki.Spolka;
import SystemTransakcyjny.SystemTransakcyjny;
import Zlecenie.Zlecenie;
import Zlecenie.TypZlecenia;
import Zlecenie.ZleceniePodstawowe;

import java.util.ArrayList;

public class InwestorRandom extends Inwestor {

    public InwestorRandom(int id, Portfel portfel, SystemTransakcyjny systemTransakcyjny) {
        super(id, portfel, systemTransakcyjny);
    }

    @Override
    public boolean czyChceZlozycZlecenie(int kolejnoscWturze) {
        Losowanie losowanie = new Losowanie();
        int numerAktualnejTury = systemTransakcyjny.dajNumerAktualnejTury();
        int liczbaTur = systemTransakcyjny.dajLiczbeTur();;

        // Dane potrzebne do zlecenia.
        TypZlecenia typZlecenia;
        String identyfikatorAkcji;
        int liczbaSztukAkcji;
        int limitCeny;
        int terminWaznosci = losowanie.wylosujLiczbeZPrzedzialu(numerAktualnejTury, liczbaTur);
        int numerTury = systemTransakcyjny.dajNumerAktualnejTury();
        Inwestor inwestor = this;

        ArrayList<Spolka> spolki = systemTransakcyjny.dajSpolki();
        int numerSpolki = losowanie.wylosujLiczbe(spolki.size());
        identyfikatorAkcji = spolki.get(numerSpolki).dajNazwe();

        int cenaOstatniejTransakcji = spolki.get(numerSpolki).dajCeneOstatniejAkcji();
        int dolnaGranica = Math.min(1, cenaOstatniejTransakcji - 10);
        limitCeny = losowanie.wylosujLiczbeZPrzedzialu(dolnaGranica, cenaOstatniejTransakcji + 10);

        int liczbaTypZlecenia = losowanie.wylosujLiczbe(2);
        if (liczbaTypZlecenia == 0) {
            typZlecenia = TypZlecenia.KUPNO;

            if (limitCeny > portfel.dajIloscPieniedzy()) {
                return false;
            }
            else {
                if (limitCeny <= 0) {
                    return false;
                }
                int maxAkcji = portfel.dajIloscPieniedzy() / limitCeny;
                if (maxAkcji < 1) {
                    return false;
                }
                liczbaSztukAkcji = losowanie.wylosujLiczbe(maxAkcji) + 1;
                if (liczbaSztukAkcji <= 0) {
                    return false;
                }
                aktualneZlecenie = new ZleceniePodstawowe(typZlecenia, identyfikatorAkcji, liczbaSztukAkcji, limitCeny,
                        terminWaznosci, numerTury, kolejnoscWturze, inwestor);
                return true;
            }
        }
        else {
            typZlecenia = TypZlecenia.SPRZEDAZ;
            int maxAkcji = portfel.dajIloscAkcji(identyfikatorAkcji);
            if (maxAkcji <= 0) {
                return false;
            }
            liczbaSztukAkcji = losowanie.wylosujLiczbeZPrzedzialu(1, maxAkcji);
            if (limitCeny <= 0 || liczbaSztukAkcji <= 0) {
                return false;
            }
            aktualneZlecenie = new ZleceniePodstawowe(typZlecenia, identyfikatorAkcji, liczbaSztukAkcji, limitCeny,
                    terminWaznosci, numerTury, kolejnoscWturze, inwestor);
            return true;
        }
    }

    @Override
    public Zlecenie zlozZlecenie(int kolejnoscWTurze) {
        return aktualneZlecenie;
    }

    @Override
    public String toString() { return "Inwestor (random) nr " + id + "\n" + portfel.toString(); }
}
