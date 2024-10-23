package Dane;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import GieldaPapierowWartosciowych.GieldaPapierowWartosciowych;
import Inwestorzy.Inwestor;
import Inwestorzy.InwestorRandom;
import Inwestorzy.Portfel;
import Spolki.Akcja;
import Spolki.Spolka;
import SystemTransakcyjny.SystemTransakcyjny;

public class Dane {
    private final GieldaPapierowWartosciowych gieldaPapierowWartosciowych;
    private final SystemTransakcyjny systemTransakcyjny;
    private final ArrayList<Spolka> spolki;
    private final ArrayList<Inwestor> inwestorzy;

    public Dane(GieldaPapierowWartosciowych gieldaPapierowWartosciowych, SystemTransakcyjny systemTransakcyjny,
                ArrayList<Spolka> spolki, ArrayList<Inwestor> inwestorzy) {
        this.gieldaPapierowWartosciowych = gieldaPapierowWartosciowych;
        this.systemTransakcyjny = systemTransakcyjny;
        this.spolki = spolki;
        this.inwestorzy = inwestorzy;
    }

    public static Dane wczytajDane(String[] args) {
        // Poprawna ilość parametrów dla tej symulacji to 2: nazwa pliku z danymi oraz liczba tur.
        assert (args.length == 2);

        String nazwaPliku = args[0];
        int liczbaTur = Integer.parseInt(args[1]);
        assert (liczbaTur >= 0);
        String[] linia = new String[3];

        try (Scanner skaner = new Scanner(new File(nazwaPliku))) {
            int numerLinii = 0;

            while (skaner.hasNext()) {
                String dana = skaner.nextLine();
                if (!(dana.charAt(0) == '#')) {
                    linia[numerLinii++] = dana;
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println("Nie znaleziono pliku: " + e.getMessage());
        }

        // Przygotowanie zmiennych do odczytania danych.
        GieldaPapierowWartosciowych gieldaPapierowWartosciowych;
        SystemTransakcyjny systemTransakcyjny;
        ArrayList<Spolka> spolki = new ArrayList<>();
        ArrayList<Inwestor> inwestorzy = new ArrayList<>();
        int iloscRandom = 0;
        ArrayList<Akcja> akcje = new ArrayList<>();
        int pieniadze;

        // Odczytanie pierwszej linii.
        String[] daneInwestorzy = linia[0].split(" ");
        for (int i = 0; i < daneInwestorzy.length; i++) {
            assert (daneInwestorzy[i].length() == 1 && daneInwestorzy[i].charAt(0) == 'R');
            iloscRandom++;
        }

        // Odczytanie drugiej linii.
        String[] daneSpolki = linia[1].split(" ");
        for (int i = 0; i < daneSpolki.length; i++) {
            String[] daneSpolki2 = daneSpolki[i].split(":");
            assert (daneSpolki2.length == 2 && !daneSpolki2[0].isEmpty() && daneSpolki2[0].length() <= 5);
            String nazwa = daneSpolki2[0];
            int cena = Integer.parseInt(daneSpolki2[1]);
            Spolka spolka = new Spolka(nazwa, cena);
            spolki.add(spolka);
        }
        gieldaPapierowWartosciowych = new GieldaPapierowWartosciowych(spolki);
        systemTransakcyjny = new SystemTransakcyjny(gieldaPapierowWartosciowych, liczbaTur);


        // Odczytanie trzeciej linii.
        String[] danePortfele = linia[2].split(" ");
        pieniadze = Integer.parseInt(danePortfele[0]);
        for (int i = 1; i < danePortfele.length; i++) {
            String[] danePorfele2 = danePortfele[i].split(":");
            assert (danePorfele2.length == 2 && !danePorfele2[0].isEmpty() && danePorfele2[0].length() <= 5);
            String nazwa = danePorfele2[0];
            int ilosc = Integer.parseInt(danePorfele2[1]);

            // Szukanie spółki.
            int iloscZnalezien = 0;
            for (Spolka spolka : spolki) {
                if (nazwa.equals(spolka.dajNazwe())) {
                    Akcja akcja = new Akcja(spolka.dajNazwe(), ilosc);
                    akcje.add(akcja);
                    iloscZnalezien++;
                }
            }
            assert (iloscZnalezien == 1);
        }

        // Tworzenie inwestorów.
        for (int i = 0; i < iloscRandom; ++i) {
            ArrayList<Akcja> kopiaAkcji = new ArrayList<>();
            for (Akcja akcja : akcje) {
                kopiaAkcji.add(akcja.dajKopieAkcji());
            }
            Portfel portfel = new Portfel(pieniadze, kopiaAkcji);
            InwestorRandom inwestor = new InwestorRandom(i, portfel, systemTransakcyjny);
            inwestorzy.add(inwestor);
        }

        // Stworzenie danych
        return new Dane(gieldaPapierowWartosciowych, systemTransakcyjny, spolki, inwestorzy);
    }

    // TODO Poprawić wypisywanie danych (na sam koniec).
    public void wypiszDane() {
        // Wypisz system transakcyjny.
        System.out.println(systemTransakcyjny.toString());

        // Wypisanie inwestorów.
        System.out.println("\nInwestorzy:");
        for (Inwestor inwestor : inwestorzy) {
            System.out.println("\n" + inwestor.toString());
        }

        // Wypisanie spółek.
        System.out.println("\nSpółki:");
        for (Spolka spolka : spolki) {
            System.out.println(spolka.toString());
        }
    }

    /* *********************************************** GETTERY *********************************************** */
    public SystemTransakcyjny dajSystemTransakcyjny() { return systemTransakcyjny; }
    public GieldaPapierowWartosciowych dajGieldePapierowWartosciowych() { return gieldaPapierowWartosciowych; }
    public ArrayList<Spolka> dajSpolki() { return spolki; }
    public Spolka dajSpolke(int i) { return spolki.get(i); }
    public ArrayList<Inwestor> dajInwestorow() { return inwestorzy; }
    public Inwestor dajInwestora(int i) { return inwestorzy.get(i); }
}
