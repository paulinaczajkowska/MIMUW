package Kolejka;

import Zlecenie.Zlecenie;

import java.util.ArrayList;
import java.util.Collections;

public class KolejkaZlecen {
    ArrayList<Zlecenie> kolejka;

    public KolejkaZlecen() { kolejka = new ArrayList<>(); }

    public void posortuj() { Collections.sort(kolejka); }
    public boolean czyPusta() { return kolejka.isEmpty(); }

    public void usunZlecenie(int i) { kolejka.remove(i); }
    public void dodajZlecenie(Zlecenie zlecenie) { kolejka.add(zlecenie); }
    public int rozmiar() { return kolejka.size(); }
    public Zlecenie dajZlecenie(int i) { return kolejka.get(i); }
    public int dajTerminWaznosciZlecenia(int i) { return kolejka.get(i).dajTerminWaznosci(); }
    public Zlecenie dajPierwsze() { return kolejka.getFirst(); }
    public void usunPierwszeZlecenie() { kolejka.removeFirst(); }
}
