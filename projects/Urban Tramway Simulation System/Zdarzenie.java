public class Zdarzenie {
    private final int dzien;
    private final int godzina;
    private Zdarzenie nastepne;

    private final Tramwaj tramwaj;
    private final Pasazer pasazer;
    private final Przystanek przystanek;
    private final String nazwaPrzystanku;
    // 1 - wsiadanie, -1 - wysiadanie, 0 - brak udzialu pasażera w zdarzeniu, 2 - przyjscie na przystanek.
    private final int ruchPasazera;
    // 1 - przyjedża, -1 - odjeżdża, 0 - nie rusza się.
    private final int ruchTramwaju;
    // 1 - z petli1 na petle 2, -1 - z petli2 na petle1, 0 - brak.
    private final int kierunekTramwaju;

    public Zdarzenie(int dzien, int godzina, Tramwaj tramwaj, Pasazer pasazer, Przystanek przystanek, String nazwaPrzystanku, int ruchPasazera, int ruchTramwaju, int kierunekTramwaju) {
        this.dzien = dzien;
        this.godzina = godzina;
        this.tramwaj = tramwaj;
        this.pasazer = pasazer;
        this.przystanek = przystanek;
        this.nazwaPrzystanku = nazwaPrzystanku;
        this.ruchPasazera = ruchPasazera;
        this.ruchTramwaju = ruchTramwaju;
        this.kierunekTramwaju = kierunekTramwaju;
        nastepne = null;
    }

    public void ustawNastepne(Zdarzenie nastepne) { this.nastepne = nastepne; }

    @Override
    public String toString() {
        String ruch;
        if (ruchTramwaju == -1) {
            ruch = ") odjechał z przystanku ";
        }
        else {
            ruch = ") przyjechał na przystanek ";
        }
        int minuty = godzina % 60;
        String minuty2 = "";
        if (minuty < 10) {
            minuty2 = "0";
        }
        minuty2 += String.valueOf(minuty);
        if (pasazer == null) {
            return dzien + ", " + godzina / 60 + ":" + minuty2 + ": Tramwaj linii " + tramwaj.dajNumerLinii() + " (nr bocz. " + tramwaj.dajNumerBoczny() + ruch + nazwaPrzystanku + ".";
        }
        if (tramwaj == null) {
            return dzien + ", " + godzina / 60 + ":" + minuty2 + ": Pasażer nr " + pasazer.dajNumerPasazera() + " przyszedl na przystanek " + pasazer.dajPoczatkowyPrzystanek().dajNazwe()  + ".";
        }
        if (ruchPasazera == -1) {
            return dzien + ", " + godzina / 60 + ":" + minuty2 + ": Pasażer nr " + pasazer.dajNumerPasazera() + " wysiadł z tramwaju linii " + tramwaj.dajNumerLinii() +
                    " (nr bocz. " + tramwaj.dajNumerBoczny() + ") na przystanku " + przystanek.dajNazwe() + ".";
        }
        return dzien + ", " + godzina / 60 + ":" + minuty2 + ": Pasażer nr " + pasazer.dajNumerPasazera() + " wsiadł do tramwaju linii " + tramwaj.dajNumerLinii() +
                " (nr bocz. " + tramwaj.dajNumerBoczny() + ") z przystanku " + przystanek.dajNazwe() + ".";
    }




    /*   ********************   GETTERY   ********************   */

    public Zdarzenie dajNastepne() { return nastepne; }
    public int dajGodzine() { return godzina; }
    public Tramwaj dajTramwaj() { return tramwaj; }
    public Pasazer dajPasazera() { return pasazer; }
    public Przystanek dajPrzystanek() { return przystanek; }
    public int dajRuchPasazera() { return ruchPasazera; }
    public int dajRuchTramwaju() { return ruchTramwaju; }
    public int dajKierunekTramwaju() { return kierunekTramwaju; }
}
