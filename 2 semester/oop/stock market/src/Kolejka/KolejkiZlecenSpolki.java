package Kolejka;

import Inwestorzy.Inwestor;
import Spolki.Spolka;
import Zlecenie.Zlecenie;
import Zlecenie.TypZlecenia;

public class KolejkiZlecenSpolki {
    private final Spolka spolka;
    private final KolejkaZlecen kolejkaKupna;
    private final KolejkaZlecen kolejkaSprzedazy;

    public KolejkiZlecenSpolki(Spolka spolka) {
        this.spolka = spolka;
        kolejkaKupna = new KolejkaZlecen();
        kolejkaSprzedazy = new KolejkaZlecen();
    }

    public void usunPrzedawnioneZlecenia(int numerAktualnejTury) {
        for (int i = 0; i < kolejkaKupna.rozmiar(); ++i) {
            if (kolejkaKupna.dajTerminWaznosciZlecenia(i) < numerAktualnejTury) {
                kolejkaKupna.usunZlecenie(i);
            }
        }
        for (int i = 0; i < kolejkaSprzedazy.rozmiar(); ++i) {
            if (kolejkaSprzedazy.dajTerminWaznosciZlecenia(i) < numerAktualnejTury) {
                kolejkaSprzedazy.usunZlecenie(i);
            }
        }
    }

    public void wykonajZlecenia(int numerAktualnejTury) {
        kolejkaKupna.posortuj();;
        kolejkaSprzedazy.posortuj();

        if (kolejkaKupna.czyPusta() || kolejkaSprzedazy.czyPusta()) {
            return;
        }
        Zlecenie zlecenieKupna = kolejkaKupna.dajPierwsze();
        Zlecenie zlecenieSprzedazy = kolejkaSprzedazy.dajPierwsze();

        while (zlecenieKupna.dajLimitCeny() >= zlecenieSprzedazy.dajLimitCeny()) {
            if (kolejkaKupna.czyPusta() || kolejkaSprzedazy.czyPusta()) {
                return;
            }

            // Ustalamy cene transakcji.
            int cena;
            if (zlecenieKupna.compareTo(zlecenieSprzedazy) <= 0) {
                cena = zlecenieKupna.dajLimitCeny();
            }
            else {
                cena = zlecenieSprzedazy.dajLimitCeny();
            }

            // Ustalamy liczbe akcji.
            int liczbaAkcji = Math.min(zlecenieKupna.dajLiczbeSztukAkcji(), zlecenieSprzedazy.dajLiczbeSztukAkcji());
            int lacznaCena = liczbaAkcji * cena;

            Inwestor inwestorKupujacy = zlecenieKupna.dajInwestora();
            Inwestor inwestorSprzedajacy = zlecenieSprzedazy.dajInwestora();
            if (inwestorKupujacy.dajIloscPieniedzy() < lacznaCena) {
                kolejkaKupna.usunPierwszeZlecenie();
            }
            else if (inwestorSprzedajacy.dajIloscAkcji(spolka.dajNazwe()) < liczbaAkcji) {
                kolejkaSprzedazy.usunPierwszeZlecenie();
            }
            else {
                if (zlecenieKupna.dajLiczbeSztukAkcji() == zlecenieSprzedazy.dajLiczbeSztukAkcji()) {
                    kolejkaKupna.usunPierwszeZlecenie();
                    kolejkaSprzedazy.usunPierwszeZlecenie();
                }
                else if (zlecenieKupna.dajLiczbeSztukAkcji() > zlecenieSprzedazy.dajLiczbeSztukAkcji()) {
                    kolejkaSprzedazy.usunPierwszeZlecenie();
                }
                else {
                    kolejkaKupna.usunPierwszeZlecenie();
                }
                inwestorKupujacy.kupAkcje(spolka.dajNazwe(), liczbaAkcji, lacznaCena);
                inwestorSprzedajacy.sprzedajAkcje(spolka.dajNazwe(), liczbaAkcji, lacznaCena);

                spolka.ustawCeneOstatniejAkcji(cena);
                spolka.ustawNumerOstatniejTury(numerAktualnejTury);
            }
        }
    }

    public void dodajZlecenie(Zlecenie zlecenie) {
        if (zlecenie.dajTypZlecenia() == TypZlecenia.KUPNO) {
            kolejkaKupna.dodajZlecenie(zlecenie);
        }
        else  {
            kolejkaSprzedazy.dodajZlecenie(zlecenie);
        }
    }

    public String dajNazweSpolki() { return spolka.dajNazwe(); }
}
