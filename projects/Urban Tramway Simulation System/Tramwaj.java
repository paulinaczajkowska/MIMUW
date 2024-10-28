public class Tramwaj extends Pojazd {
    // Gdy nie mamy jeszcze określonej pojemności tramwaju, to przyjmuje ona wartość -1.
    private static int pojemnosc = -1;
    private Pasazer[] pasazerowie;
    // 1 - tramwaj jedzie z pierwszej pętli na drugą, -1 w przeciwnym przypadku.
    private int kierunek;
    private int aktualnyRozmiar;
    // Przystanki numerowane tak jak w tablicy.
    private int aktualnyNumerPrzystanku;
    private boolean czyCzekaNaPetli;

    public Tramwaj(int numerBoczny, int numerLinii) {
        super(numerBoczny, numerLinii);
        zresetuj();
    }

    @Override
    public String toString() {
        return "NumerLinii: " + numerLinii + ", numerBoczny: " + numerBoczny + ", kierunek: " + kierunek + ", aktualnyRozmiar: " + aktualnyRozmiar + ", aktualnyNumerPrzystanku: " +
                aktualnyNumerPrzystanku + ", czyCzekaNaPetli: " + czyCzekaNaPetli;
    }

    public void zresetuj() {
        aktualnyRozmiar = 0;
        kierunek = 1;
        aktualnyNumerPrzystanku = 0;
        czyCzekaNaPetli = false;
    }

    public void dodajPasazera(Pasazer pasazer) {
        if (pasazerowie == null) {
            pasazerowie = new Pasazer[pojemnosc];
        }
        pasazerowie[aktualnyRozmiar++] = pasazer;
    }

    public void usunPasazera(Pasazer pasazer) {
        for (int i = 0; i < aktualnyRozmiar; ++i) {
            if (pasazerowie[i] == pasazer) {
                pasazerowie[i] = null;
                pasazerowie[i] = pasazerowie[aktualnyRozmiar - 1];
                aktualnyRozmiar--;
                break;
            }
        }
    }

    public static void ustawPojemnosc(int wartosc) {
        if (pojemnosc == -1) {
            pojemnosc = wartosc;
        }
    }

    public boolean czyPelny() { return pojemnosc == aktualnyRozmiar; }

    public void zmienKierunek() { kierunek *= -1; }

    public void nastepnyPrzystanek() {
        if (kierunek == 1) {
            aktualnyNumerPrzystanku++;
        }
        else {
            aktualnyNumerPrzystanku--;
        }
        if (czyNaPetli()) {
            zmienKierunek();
        }
    }

    public int czasNastepnegoPrzejazdu() {
        if (aktualnyNumerPrzystanku == 0) {
            return linia.dajTrase().dajCzasMiedzyPrzystankami()[0];
        }
        if (aktualnyNumerPrzystanku == linia.dajTrase().dajDlugosc() - 1) {
            return linia.dajTrase().dajCzasMiedzyPrzystankami()[linia.dajTrase().dajDlugosc() - 2];
        }
        if (kierunek == 1) {
            return linia.dajTrase().dajCzasMiedzyPrzystankami()[aktualnyNumerPrzystanku];
        }
        return linia.dajTrase().dajCzasMiedzyPrzystankami()[aktualnyNumerPrzystanku - 1];
    }

    public int czasNaPetli() { return linia.dajTrase().dajCzasMiedzyPrzystankami()[linia.dajTrase().dajDlugosc() - 1]; }

    public void ustawPrzystanek(int numerPrzystanku) { aktualnyNumerPrzystanku = numerPrzystanku; }

    public boolean czyNaPetli() { return aktualnyNumerPrzystanku == 0 || aktualnyNumerPrzystanku == linia.dajTrase().dajDlugosc() - 1; }

    public void ustawCzyCzekaNaPetli(boolean czy) { czyCzekaNaPetli = czy; }



    /*   ********************   GETTERY   ********************   */

    public static int dajPojemnosc() { return pojemnosc; }
    public int dajAktualnaLiczbeOsob() { return aktualnyRozmiar; }
    public int dajKierunek() { return kierunek; }
    public int dajAktualnyNumerPrzystanku() { return aktualnyNumerPrzystanku; }
    public boolean dajCzyCzekaNaPetli() { return czyCzekaNaPetli; }
    public int dajAktualnyRozmiar() { return  aktualnyRozmiar; }
    public Pasazer dajPasazera(int i) { return pasazerowie[i]; }
}
