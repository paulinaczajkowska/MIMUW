public class Dzien {
    private final int liczbaDniSymulacji;
    private int aktualnyDzien;

    public Dzien(int liczbaDniSymulacji) {
        this.liczbaDniSymulacji = liczbaDniSymulacji;
        aktualnyDzien = 0;
    }

    public void nowyDzien() {
        if (liczbaDniSymulacji != aktualnyDzien) {
            aktualnyDzien++;
        }
    }



    /*   ********************   GETTERY   ********************   */

    public int dajLiczbeDniSymulacji() { return liczbaDniSymulacji; }
    public int dajAktualnyDzien() { return aktualnyDzien; }
}
