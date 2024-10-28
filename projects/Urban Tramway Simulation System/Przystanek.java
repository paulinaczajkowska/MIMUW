public class Przystanek extends Postoj {
    private final String nazwa;
    private static int pojemnosc = -1;
    private Pasazer[] pasazerowie;
    private int aktualnyRozmiar;

    public Przystanek(String nazwa) {
        this.nazwa = nazwa;
        aktualnyRozmiar = 0;
    }

    public static void ustawPojemnosc(int wartosc) {
        if (pojemnosc == -1) {
            pojemnosc = wartosc;
        }
    }

    public boolean czyPelny() { return pojemnosc == aktualnyRozmiar; }

    public boolean czyPusty() { return aktualnyRozmiar == 0; }

    public void dodajPasazera(Pasazer pasazer) {
        if (pasazerowie == null) {
            pasazerowie = new Pasazer[pojemnosc];
        }
        pasazerowie[aktualnyRozmiar++] = pasazer;
    }

    public void usunPasazera() {
        if (aktualnyRozmiar != 0) {
            aktualnyRozmiar--;
        }
    }

    public void oproznijPrzystanek() { aktualnyRozmiar = 0; }



    /*   ********************   GETTERY   ********************   */

    public String dajNazwe() { return nazwa; }
    public static int dajPojemnosc() { return pojemnosc; }
    public Pasazer dajPasazera(int i) { return pasazerowie[i]; }
    public int dajAktualnyRozmiar() { return aktualnyRozmiar; }
}
