package Losowanie;

import Inwestorzy.Inwestor;

import java.util.ArrayList;
import java.util.Random;

public class Losowanie {
    Random random;

    public Losowanie() { random = new Random(); }

    public ArrayList<Inwestor> ustawInwestorowLosowo(ArrayList<Inwestor> inwestorzy) {
        ArrayList<Inwestor> inwestorzyLosowaKolejnosc = new ArrayList<>();
        while (!inwestorzy.isEmpty()) {
            int losowaLiczba = random.nextInt(inwestorzy.size());
            inwestorzyLosowaKolejnosc.add(inwestorzy.get(losowaLiczba));
            inwestorzy.remove(losowaLiczba);
        }
        return inwestorzyLosowaKolejnosc;
    }

    // Losuje liczbe z przedziału [0, gornaGranica).
    public int wylosujLiczbe(int gornaGranica) { return random.nextInt(gornaGranica); }

    // Losuje liczbe z przedziału [dolnaGranica, gornaGranica].
    public int wylosujLiczbeZPrzedzialu(int dolnaGranica, int gornaGranica) {
        return random.nextInt(gornaGranica - dolnaGranica + 1) + dolnaGranica;
    }


}
