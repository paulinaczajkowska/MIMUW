import static Dane.Dane.wczytajDane;
import Dane.Dane;
import Symulacja.Symulacja;

public class Main {
    public static void main(String[] args) {
        Dane dane = wczytajDane(args);
        Symulacja symulacja = new Symulacja(dane);
        symulacja.przeprowadzSymulacje();
        symulacja.wypiszWyniki();
    }
}