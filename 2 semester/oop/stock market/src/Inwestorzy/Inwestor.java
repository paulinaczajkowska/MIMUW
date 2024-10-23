package Inwestorzy;

import Spolki.Akcja;
import SystemTransakcyjny.SystemTransakcyjny;
import Zlecenie.Zlecenie;

public abstract class Inwestor {
    protected final int id;
    protected final Portfel portfel;
    protected final SystemTransakcyjny systemTransakcyjny;
    protected Zlecenie aktualneZlecenie;

    protected Inwestor(int id, Portfel portfel, SystemTransakcyjny systemTransakcyjny) {
        this.id = id;
        this.portfel = portfel;
        this.systemTransakcyjny = systemTransakcyjny;
    }

    public void kupAkcje(String identyfikatorAkcji, int iloscAkcji, int cena) {
        portfel.zabierzPieniadze(cena);
        portfel.dodajAkcje(identyfikatorAkcji, iloscAkcji);
    }

    public void sprzedajAkcje(String identyfikatorAkcji, int iloscAkcji, int cena) {
        portfel.dodajPieniadze(cena);
        portfel.zabierzAkcje(identyfikatorAkcji, iloscAkcji);
    }

    public abstract boolean czyChceZlozycZlecenie(int kolejnoscWTurze);

    public abstract Zlecenie zlozZlecenie(int kolejnoscWTurze);

    /* *********************************************** GETTERY *********************************************** */
    public int dajId() { return id; }
    public Portfel dajPortfel() { return portfel; }
    public int dajIloscPieniedzy() { return portfel.dajIloscPieniedzy(); }
    public Akcja dajAkcje(int i) { return portfel.dajAkcje(i); }
    public int dajLiczbeRoznychAkcji() { return portfel.dajLiczbeRoznychAkcji(); }
    public int dajIloscAkcji(String identyfikatorAkcji) { return portfel.dajIloscAkcji(identyfikatorAkcji); }
    public String dajIdentyfikatorAkcji(int i) { return portfel.dajIdentyfikatorAkcji(i); }
}
