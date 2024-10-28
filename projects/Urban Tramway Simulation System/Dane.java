import java.util.Objects;
import java.util.Scanner;
import java.util.Random;

public class Dane {
    Scanner scanner = new Scanner(System.in);
    Random random = new Random();
    Dzien dzien;
    private int liczbaPrzystankow;
    private int liczbaPasazerow;
    private int liczbaLinii;
    Przystanek[] przystanki;
    Pasazer[] pasazerowie;
    Linia[] linieTramwajowe;
    ListaZdarzen kolejka = new ListaZdarzen();
    Statystyki[] statystyki;

    public Dane(){}

    public int dajnumerDnia() { return dzien.dajLiczbeDniSymulacji(); }

    public void tworzenieTablicyPrzystankow() {
        Przystanek.ustawPojemnosc(scanner.nextInt());
        liczbaPrzystankow = scanner.nextInt();
        przystanki = new Przystanek[liczbaPrzystankow];
        for (int i = 0; i < liczbaPrzystankow; ++i) {
            przystanki[i] = new Przystanek(scanner.next());
        }
    }

    public void tworzenieTablicyPasazerow() {
        liczbaPasazerow = scanner.nextInt();
        pasazerowie = new Pasazer[liczbaPasazerow];
        for (int i = 0; i < liczbaPasazerow; ++i) {
            pasazerowie[i] = new Pasazer(i);
        }
    }

    public Trasa tworzenieTrasy() {
        int dlugosc = scanner.nextInt();
        Przystanek[] przystankiTrasy = new Przystanek[dlugosc];
        int[] czasMiedzyPrzystankami = new int[dlugosc];
        String nazwaPrzystanku;
        Przystanek tmpPrzystanek = null;
        for (int j = 0; j < dlugosc; ++j) {
            nazwaPrzystanku = scanner.next();
            for (int k = 0; k < liczbaPrzystankow; ++k) {
                if (Objects.equals(nazwaPrzystanku, przystanki[k].dajNazwe())) {
                    tmpPrzystanek = przystanki[k];
                    break;
                }
            }
            przystankiTrasy[j] = tmpPrzystanek;
            czasMiedzyPrzystankami[j] = scanner.nextInt();
        }
        return new Trasa(dlugosc, przystankiTrasy, czasMiedzyPrzystankami);
    }

    public void tworzenieTablicyLiniiTramwajowych() {
        Tramwaj.ustawPojemnosc(scanner.nextInt());
        liczbaLinii = scanner.nextInt();
        linieTramwajowe = new Linia[liczbaLinii];
        // Zmienna potrzebna, aby każdy tramwaj miał inny numer.
        int aktualnyNumerTramwaju = 0;
        for (int i = 0; i < liczbaLinii; ++i) {
            int numerLinii = i + 1;
            int liczbaTramwajow = scanner.nextInt();
            Trasa trasa = tworzenieTrasy();
            Tramwaj[] tramwaje = new Tramwaj[liczbaTramwajow];
            for (int j = 0; j < liczbaTramwajow; ++j) {
                tramwaje[j] = new Tramwaj(aktualnyNumerTramwaju++, numerLinii);
            }
            Linia linia = new Linia(numerLinii, trasa, liczbaTramwajow, tramwaje);
            linieTramwajowe[i] = linia;
            for (int j = 0; j < liczbaTramwajow; ++j) {
                tramwaje[j].ustawLinie(linia);
            }
        }
    }

    public void tworzenieTablicyStatystyk() {
        statystyki = new Statystyki[dzien.dajLiczbeDniSymulacji()];
        for (int i = 0; i < dzien.dajLiczbeDniSymulacji(); ++i) {
            statystyki[i] = new Statystyki();
        }
    }

    public void wczytajDane() {
        dzien = new Dzien(scanner.nextInt());
        tworzenieTablicyPrzystankow();
        tworzenieTablicyPasazerow();
        tworzenieTablicyLiniiTramwajowych();
        scanner.close();
        tworzenieTablicyStatystyk();
    }

    public void wypiszDane() {
        System.out.println("Dane: ");
        System.out.println(dzien.dajLiczbeDniSymulacji());
        System.out.println(Przystanek.dajPojemnosc());
        System.out.println(liczbaPrzystankow);
        for (int i = 0; i < liczbaPrzystankow; ++i) {
            System.out.print(przystanki[i].dajNazwe() + " ");
        }
        System.out.println("\n" + liczbaPasazerow);
        System.out.println(Tramwaj.dajPojemnosc());
        System.out.println(liczbaLinii);
        for (int i = 0; i < liczbaLinii; ++i) {
            Linia tmpLinia = linieTramwajowe[i];
            System.out.print(tmpLinia.dajLiczbeTramwajow() + " ");
            Trasa tmpTrasa = tmpLinia.dajTrase();
            System.out.print(tmpTrasa.dajDlugosc() + " ");
            Przystanek[] tmpPrzystanki = tmpTrasa.dajPrzystanki();
            int[] tmpCzasMiedzyPrzystankami = tmpTrasa.dajCzasMiedzyPrzystankami();
            for (int j = 0; j < tmpTrasa.dajDlugosc(); ++j) {
                System.out.print(tmpPrzystanki[j].dajNazwe() + " ");
                System.out.print(tmpCzasMiedzyPrzystankami[j] + " ");
            }
        }
        System.out.println();
    }

    public void ustawPasazerow() {
        for (int i = 0; i < liczbaPasazerow; ++i) {
            Pasazer tmpPasazer = pasazerowie[i];
            if (dzien.dajAktualnyDzien() == 1) {
                tmpPasazer.ustawPoczatkowyPrzystanek(przystanki[random.nextInt(liczbaPrzystankow)]);
            }
            tmpPasazer.ustawDocelowyPrzystanek(przystanki[random.nextInt(liczbaPrzystankow)]);
            tmpPasazer.ustawMinuty(random.nextInt(361) + 360);
        }
    }

    public void ustawPrzystanki() {
        for (int i = 0; i < liczbaPrzystankow; ++i) {
            przystanki[i].oproznijPrzystanek();
        }
    }

    public void ustawPrzejazdyTramwaju(Tramwaj tramwaj, int godzina) {
        while (godzina <= 60 * 23 || tramwaj.dajAktualnyNumerPrzystanku() != 0) {
            if (tramwaj.czyNaPetli() && tramwaj.dajCzyCzekaNaPetli()) {
                godzina += tramwaj.czasNaPetli();
                tramwaj.ustawCzyCzekaNaPetli(false);
            }
            else {
                Przystanek przystanek = tramwaj.dajLinie().dajTrase().dajPrzystanek(tramwaj.dajAktualnyNumerPrzystanku());
                Zdarzenie zdarzenie = new Zdarzenie(dzien.dajAktualnyDzien(), godzina, tramwaj, null, przystanek, przystanek.dajNazwe(), 0, -1, tramwaj.dajKierunek());
                kolejka.dodajZdarzenie(zdarzenie);
                godzina += tramwaj.czasNastepnegoPrzejazdu();
                tramwaj.nastepnyPrzystanek();
                Przystanek przystanek2 = tramwaj.dajLinie().dajTrase().dajPrzystanek(tramwaj.dajAktualnyNumerPrzystanku());
                Zdarzenie zdarzenie2 = new Zdarzenie(dzien.dajAktualnyDzien(), godzina, tramwaj, null, przystanek2, przystanek2.dajNazwe(), 0, 1, tramwaj.dajKierunek());
                kolejka.dodajZdarzenie(zdarzenie2);
                if (tramwaj.czyNaPetli()) {
                    tramwaj.ustawCzyCzekaNaPetli(true);
                }
            }
        }
    }

    public void ustawLinie() {
        for (int i = 0; i < liczbaLinii; ++i) {
            Linia tmpLinia = linieTramwajowe[i];
            int godzina = 360; // 6 rano w minutach od północy.

            // Ustawienie tramwajów
            for (int j = 0; j < tmpLinia.dajLiczbeTramwajow(); ++j) {
                Tramwaj tmpTramwaj = tmpLinia.dajTramwaj(j);
                tmpTramwaj.zresetuj();
            }

            for (int j = 0; j < tmpLinia.dajLiczbeTramwajow(); ++j) {
                if (j > tmpLinia.dajLiczbeTramwajow() / 2) {
                    tmpLinia.dajTramwaj(j).zmienKierunek();
                    tmpLinia.dajTramwaj(j).ustawPrzystanek(tmpLinia.dajTrase().dajDlugosc() - 1);
                }
                ustawPrzejazdyTramwaju(tmpLinia.dajTramwaj(j), godzina);
                godzina += tmpLinia.dajCzasPomiedzyOdjazdami();
            }
        }
    }

    public void rozpocznijDzien() {
        dzien.nowyDzien();
        kolejka.wyczysc();
        ustawPasazerow();
        ustawPrzystanki();
        ustawLinie();
    }

    public void symulujPrzyjsciePasazerow() {
        for (int i = 0; i < liczbaPasazerow; ++i) {
            Pasazer pasazer = pasazerowie[i];
            if (!pasazer.dajPoczatkowyPrzystanek().czyPelny()) {
                Zdarzenie zdarzenie = new Zdarzenie(dzien.dajAktualnyDzien(), pasazer.dajGodzine(), null, pasazer, pasazer.dajPoczatkowyPrzystanek(), pasazer.dajPoczatkowyPrzystanek().dajNazwe(), 2, 0, 0);
                kolejka.dodajZdarzenie(zdarzenie);
            }
        }
    }

    public void obsluzPasazerow() {
        Zdarzenie zdarzenie = kolejka.dajPierwsze();
        for (int i = 0; !(zdarzenie.dajNastepne() == null); ++i) {
            if (i != 0) {
                zdarzenie = zdarzenie.dajNastepne();
            }
            // Przyjazd tramwaju (pasażerowie wysiadają).
            if (zdarzenie.dajGodzine() <= 60 * 23 && zdarzenie.dajPasazera() == null && zdarzenie.dajRuchTramwaju() == 1) {
                Tramwaj tramwaj = zdarzenie.dajTramwaj();
                Przystanek przystanek = zdarzenie.dajPrzystanek();
                for (int j = 0; j < tramwaj.dajAktualnyRozmiar() && !przystanek.czyPelny(); ++j) {
                    Pasazer pasazer = tramwaj.dajPasazera(j);
                    if (pasazer.dajDocelowyPrzystanek() == przystanek && pasazer.dajDocelowyKierunek() == zdarzenie.dajKierunekTramwaju()) {
                        tramwaj.usunPasazera(pasazer);
                        przystanek.dodajPasazera(pasazer);
                        --j; // Trzeba jeszcze raz sprawdzić dane miejce ze wzgledu na implementację funkcji usunPasazera(pasazer).
                        Zdarzenie zdarzenie2 = new Zdarzenie(dzien.dajAktualnyDzien(), zdarzenie.dajGodzine(), tramwaj, pasazer, przystanek, przystanek.dajNazwe(), -1, 0, 0);
                        kolejka.dodajZdarzenie(zdarzenie2);
                    }
                }
            }
            // Odjazd tramwaju (pasażerowie wsiadają).
            else if (zdarzenie.dajGodzine() <= 60 * 23 && zdarzenie.dajPasazera() == null && zdarzenie.dajRuchTramwaju() == -1) {
                Tramwaj tramwaj = zdarzenie.dajTramwaj();
                Przystanek przystanek = zdarzenie.dajPrzystanek();
                for (int j = przystanek.dajAktualnyRozmiar() - 1; j >= 0  && !tramwaj.czyPelny(); --j) {
                    Pasazer pasazer = przystanek.dajPasazera(j);
                    pasazer.ustawDocelowyPrzystanek(tramwaj.dajLinie().dajTrase().dajPrzystanek(random.nextInt(tramwaj.dajLinie().dajTrase().dajDlugosc())));
                    // Jezeli przystanek to petla to jest tylko jeden mozliwy kiernuek.
                    if (pasazer.dajDocelowyPrzystanek() == tramwaj.dajLinie().dajTrase().dajPrzystanek(0)) {
                        pasazer.ustawDocelowyKierunek(1);
                    }
                    else if (pasazer.dajDocelowyPrzystanek() == tramwaj.dajLinie().dajTrase().dajPrzystanek(tramwaj.dajLinie().dajTrase().dajDlugosc() - 1)) {
                        pasazer.ustawDocelowyKierunek(-1);
                    }
                    else {
                        pasazer.ustawDocelowyKierunek(random.nextInt(2));
                        if (pasazer.dajDocelowyKierunek() == 0) {
                            pasazer.ustawDocelowyKierunek(-1);
                        }
                    }
                    przystanek.usunPasazera();
                    tramwaj.dodajPasazera(pasazer);
                    Zdarzenie zdarzenie2 = new Zdarzenie(dzien.dajAktualnyDzien(), zdarzenie.dajGodzine(), tramwaj, pasazer, przystanek, przystanek.dajNazwe(), 1, 0, 0);
                    kolejka.dodajZdarzenie(zdarzenie2);
                }
            }
            else if (zdarzenie.dajTramwaj() == null) {
                Pasazer pasazer = zdarzenie.dajPasazera();
                pasazer.dajPoczatkowyPrzystanek().dodajPasazera(pasazer);
            }
        }
    }

    public void wypiszKolejke() {
        System.out.print("\n");
        while (!kolejka.czyPusta()) {
            Zdarzenie zdarzenie = kolejka.zdejmijPierwsze();
            System.out.println(zdarzenie.toString());
        }
    }

    public void zbierzStatystyki() {
        int numerDnia = dzien.dajAktualnyDzien();
        int aktualnaLiczbaPasazerowNaPrzystankach = 0;
        int lacznyCzasCzekaniaNaPrzystnkach = 0;
        int godzina = 0;
        Zdarzenie zdarzenie = kolejka.dajPierwsze();
        for (int i = 0; !(zdarzenie.dajNastepne() == null); ++i) {
            if (i != 0) {
                zdarzenie = zdarzenie.dajNastepne();
            }
            if (zdarzenie.dajRuchPasazera() == 2 || zdarzenie.dajRuchPasazera() == -1) {
                if (godzina == 0) {
                    godzina = zdarzenie.dajGodzine();
                }
                lacznyCzasCzekaniaNaPrzystnkach += (zdarzenie.dajGodzine() - godzina) * aktualnaLiczbaPasazerowNaPrzystankach;
                godzina = zdarzenie.dajGodzine();
                aktualnaLiczbaPasazerowNaPrzystankach++;
                statystyki[numerDnia - 1].dodajLiczbeCzekajacych();
            }
            if (zdarzenie.dajRuchPasazera() == 1) {
                lacznyCzasCzekaniaNaPrzystnkach += (zdarzenie.dajGodzine() - godzina) * aktualnaLiczbaPasazerowNaPrzystankach;
                aktualnaLiczbaPasazerowNaPrzystankach--;
                statystyki[numerDnia - 1].dodajPrzejazd();
                godzina = zdarzenie.dajGodzine();
            }
        }
        lacznyCzasCzekaniaNaPrzystnkach += (60 * 23 - godzina) * aktualnaLiczbaPasazerowNaPrzystankach;
        statystyki[numerDnia - 1].dodajCzasCzekania(lacznyCzasCzekaniaNaPrzystnkach);
    }

    public void wypiszDzienneStatystyki(int numerDnia) {
        System.out.println("Dzień numer " + numerDnia + ":");
        System.out.println("    -łączna liczba przejazdów: " + statystyki[numerDnia - 1].dajLiczbePrzejazdow());
        System.out.println("    -łączny czas czekania na przystankach: " + statystyki[numerDnia - 1].dajCzasCzekania());
    }

    public void wypiszCaleStatystyki() {
        int liczbaPrzyjazdow = 0;
        for (int i = 0; i < dzien.dajLiczbeDniSymulacji(); ++i) {
            liczbaPrzyjazdow += statystyki[i].dajLiczbePrzejazdow();
        }
        int czasCzekania = 0;
        for (int i = 0; i < dzien.dajLiczbeDniSymulacji(); ++i) {
            czasCzekania += statystyki[i].dajCzasCzekania();
        }
        int liczbaCzekajacych = 0;
        for (int i = 0; i < dzien.dajLiczbeDniSymulacji(); ++i) {
            liczbaCzekajacych += statystyki[i].dajLiczbeCzekajacyh();
        }
        int sredniCzasCzekania = 0;
        if (liczbaCzekajacych != 0) {
            sredniCzasCzekania = czasCzekania / liczbaCzekajacych;
        }

        System.out.println();
        System.out.println("Statystyki dla wszystkich dni:");
        System.out.println("    - łączna liczba przejazdów pasażerów: " + liczbaPrzyjazdow);
        System.out.println("    - średni czas czekania na przystanku: " + sredniCzasCzekania);
    }
}
