public class Main {
    public static void main(String[] args) {
        Dane dane = new Dane();
        dane.wczytajDane();
        dane.wypiszDane();
        for (int i = 0; i < dane.dajnumerDnia(); ++i) {
            dane.rozpocznijDzien();
            dane.symulujPrzyjsciePasazerow();
            dane.obsluzPasazerow();
            dane.zbierzStatystyki();
            dane.wypiszKolejke();
        }

        dane.wypiszCaleStatystyki();
        for (int i = 1; i <= dane.dajnumerDnia(); ++i) {
            dane.wypiszDzienneStatystyki(i);
        }
    }
}