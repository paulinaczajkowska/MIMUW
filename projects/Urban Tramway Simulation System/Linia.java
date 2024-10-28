public class Linia {
    private final int numerLinii;
    private final Trasa trasaLinii;
    private final Tramwaj[] tramwaje;
    private final int liczbaTramwajow;

    public Linia(int numerLinii, Trasa trasaLinii, int liczbaTramwajow, Tramwaj[] tramwaje) {
        this.numerLinii = numerLinii;
        this.trasaLinii = trasaLinii;
        this.liczbaTramwajow = liczbaTramwajow;
        this.tramwaje = tramwaje;
    }

    public int dajCzasPomiedzyOdjazdami() { return trasaLinii.obliczCzasPrzejazdu() / liczbaTramwajow; }



    /*   ********************   GETTERY   ********************   */

    public int dajNumerLinii() { return numerLinii; }
    public Trasa dajTrase() { return trasaLinii; }
    public int dajLiczbeTramwajow() { return liczbaTramwajow; }
    public Tramwaj[] dajTramwaje() { return tramwaje; }
    public Tramwaj dajTramwaj(int i) { return tramwaje[i]; }
}
