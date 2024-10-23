package Symulacja;

import Dane.Dane;
import GieldaPapierowWartosciowych.GieldaPapierowWartosciowych;
import Inwestorzy.Inwestor;
import Kolejka.KolejkiZlecenSpolki;
import Losowanie.Losowanie;
import Spolki.Spolka;
import SystemTransakcyjny.SystemTransakcyjny;
import Zlecenie.Zlecenie;

import java.util.ArrayList;
import java.util.Objects;

public class Symulacja {
    Dane dane;
    private final GieldaPapierowWartosciowych gieldaPapierowWartosciowych;
    private final SystemTransakcyjny systemTransakcyjny;
    private final ArrayList<Spolka> spolki;
    private ArrayList<Inwestor> inwestorzy;
    private ArrayList<KolejkiZlecenSpolki> kolejkiZlecenSpolek;

    public Symulacja(Dane dane) {
        this.dane = dane;
        gieldaPapierowWartosciowych = dane.dajGieldePapierowWartosciowych();
        systemTransakcyjny = dane.dajSystemTransakcyjny();
        spolki = dane.dajSpolki();
        inwestorzy = dane.dajInwestorow();
        stworzKolejkiZlecenSpolek();
    }

    private void stworzKolejkiZlecenSpolek() {
        kolejkiZlecenSpolek = new ArrayList<>();
        for (Spolka spolka : spolki) {
            KolejkiZlecenSpolki kolejkiZlecenSpolki = new KolejkiZlecenSpolki(spolka);
            kolejkiZlecenSpolek.add(kolejkiZlecenSpolki);
        }
    }

    public void przeprowadzSymulacje() {
        int liczbaTur = systemTransakcyjny.dajLiczbeTur();
        for (int i = 0; i < liczbaTur; i++) {
            przeprowadzTure();
        }
    }

    public void przeprowadzTure() {
        zbierzDecyzjeInwestycyjne();
        wykonajZlecenia();
        systemTransakcyjny.nastepnaTura();
    }

    public void zbierzDecyzjeInwestycyjne() {
        // Ustawienie inwestorów w losowej kolejnosci.
        Losowanie losowanie = new Losowanie();
        inwestorzy = losowanie.ustawInwestorowLosowo(inwestorzy);

        int numerWturze = 0;
        for (Inwestor inwestor : inwestorzy) {
            if (inwestor.czyChceZlozycZlecenie(numerWturze)) {
                Zlecenie zlecenie = inwestor.zlozZlecenie(numerWturze++);
                for (KolejkiZlecenSpolki kolejkiZlecenSpolki : kolejkiZlecenSpolek) {
                    if (Objects.equals(kolejkiZlecenSpolki.dajNazweSpolki(), zlecenie.dajIdentyfikatorAkcji())) {
                        kolejkiZlecenSpolki.dodajZlecenie(zlecenie);
                    }
                }
            }
        }
    }

    public void wykonajZlecenia() {
        for (KolejkiZlecenSpolki kolejkiZlecenSpolki : kolejkiZlecenSpolek) {
            kolejkiZlecenSpolki.usunPrzedawnioneZlecenia(systemTransakcyjny.dajNumerAktualnejTury());
            kolejkiZlecenSpolki.wykonajZlecenia(systemTransakcyjny.dajNumerAktualnejTury());
        }
    }

    public void wypiszWyniki() {
        for (Inwestor inwestor : inwestorzy) {
            System.out.println(inwestor.dajPortfel().toString());
        }
    }
}
