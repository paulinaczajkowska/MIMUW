public class ListaZdarzen {
    private Zdarzenie pierwsze;

    public ListaZdarzen() { pierwsze = null; }

    public boolean czyPusta() { return pierwsze == null; }

    public void dodajZdarzenie(Zdarzenie nowe) {
        if (pierwsze == null) {
            pierwsze = nowe;
        }
        else if (pierwsze.dajGodzine() > nowe.dajGodzine()) {
            nowe.ustawNastepne(pierwsze);
            pierwsze = nowe;
        }
        else if (pierwsze.dajNastepne() == null) {
            pierwsze.ustawNastepne(nowe);
        }
        else {
            Zdarzenie poprzednie = pierwsze;
            Zdarzenie stare = pierwsze.dajNastepne();
            boolean czy = true;
            while (stare.dajGodzine() <= nowe.dajGodzine()) {
                if (stare.dajNastepne() == null)  {
                    stare.ustawNastepne(nowe);
                    czy = false;
                    break;
                }
                poprzednie = stare;
                stare = stare.dajNastepne();
            }
            if (czy) {
                poprzednie.ustawNastepne(nowe);
                nowe.ustawNastepne(stare);
            }
        }
    }

    public Zdarzenie zdejmijPierwsze() {
        assert(!czyPusta());
        Zdarzenie tmp = pierwsze;
        pierwsze = pierwsze.dajNastepne();
        return tmp;
    }

    public Zdarzenie dajPierwsze() { return pierwsze; }

    public void wyczysc() { pierwsze = null; }
}
