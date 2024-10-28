public class Pasazer {
    private final int numerPasazera;
    private Przystanek poczatkowyPrzystanek;
    private Przystanek docelowyPrzystanek;
    private int docelowyKierunek;
    private int godzina;

    public Pasazer(int numerPasazera) { this.numerPasazera = numerPasazera; }



    /*   ********************   SETTERY   ********************   */

    public void ustawPoczatkowyPrzystanek(Przystanek poczatkowyPrzystanek) { this.poczatkowyPrzystanek = poczatkowyPrzystanek; }
    public void ustawDocelowyPrzystanek(Przystanek docelowyPrzystanek) { this.docelowyPrzystanek = docelowyPrzystanek; }
    public void ustawMinuty(int minuty) { this.godzina = minuty; }
    public void ustawDocelowyKierunek(int docelowyKierunek) { this.docelowyKierunek = docelowyKierunek; }



    /*   ********************   GETTERY   ********************   */

    public int dajNumerPasazera() { return numerPasazera; }
    public Przystanek dajPoczatkowyPrzystanek() { return poczatkowyPrzystanek; }
    public Przystanek dajDocelowyPrzystanek() { return docelowyPrzystanek; }
    public int dajGodzine() { return godzina; }
    public int dajDocelowyKierunek() { return docelowyKierunek; }
}