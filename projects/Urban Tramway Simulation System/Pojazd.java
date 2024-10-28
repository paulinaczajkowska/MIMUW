public abstract class Pojazd {
    protected final int numerBoczny;
    protected final int numerLinii;
    protected Linia linia;

    public Pojazd(int numerBoczny, int numerLinii) {
        this.numerBoczny = numerBoczny;
        this.numerLinii = numerLinii;
    }



    /*   ********************   SETTERY   ********************   */

    public void ustawLinie(Linia linia) { this.linia = linia; }



    /*   ********************   GETTERY   ********************   */

    public int dajNumerBoczny() { return numerBoczny; }
    public int dajNumerLinii() { return numerLinii; }
    public Linia dajLinie() { return linia; }
}
