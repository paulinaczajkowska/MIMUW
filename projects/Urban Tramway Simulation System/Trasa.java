public class Trasa {
    private final int dlugosc;
    private final Przystanek[] przystanki;
    private final int[] czasMiedzyPrzystankami;

    public Trasa(int dlugosc, Przystanek[] przystanki, int[] czasMiedzyPrzystankami) {
        this.dlugosc = dlugosc;
        this.przystanki = przystanki;
        this.czasMiedzyPrzystankami = czasMiedzyPrzystankami;
    }

    public int obliczCzasPrzejazdu() {
        int wynik = 0;
        for (int i = 0; i < dlugosc; ++i) {
            wynik += czasMiedzyPrzystankami[i];
        }
        wynik *= 2;
        return wynik;
    }



    /*   ********************   GETTERY   ********************   */

    public int dajDlugosc() { return dlugosc; }
    public Przystanek[] dajPrzystanki() { return przystanki; }
    public Przystanek dajPrzystanek(int i) { return przystanki[i]; }
    public int[] dajCzasMiedzyPrzystankami() { return czasMiedzyPrzystankami; }
}
