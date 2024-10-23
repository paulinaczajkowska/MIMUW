public class Statystyki {
    private int liczbaPrzejazdow;
    private int czasCzekania;
    private int liczbaCzekajacych;

    public Statystyki() {
        liczbaPrzejazdow = 0;
        czasCzekania = 0;
        liczbaCzekajacych = 0;
    }



    /*   ********************   SETTERY   ********************   */

    public void dodajPrzejazd() { liczbaPrzejazdow++; }
    public void dodajCzasCzekania(int czas) { czasCzekania += czas; }
    public void dodajLiczbeCzekajacych() { liczbaCzekajacych++; }



    /*   ********************   GETTERY   ********************   */

    public int dajLiczbePrzejazdow() { return liczbaPrzejazdow; }
    public int dajCzasCzekania() { return czasCzekania; }
    public int dajLiczbeCzekajacyh() { return liczbaCzekajacych; }
}
