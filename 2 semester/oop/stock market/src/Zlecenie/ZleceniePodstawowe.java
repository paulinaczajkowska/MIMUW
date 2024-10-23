// W obecnym stanie wyróżnienie zlecenia podstawowego nie jest konieczne,
// ale będzie przydatne w przypadku dodania innych typów zleceń.

package Zlecenie;

import Inwestorzy.Inwestor;

public class ZleceniePodstawowe extends Zlecenie{

    public ZleceniePodstawowe(TypZlecenia typZlecenia, String identyfikatorAkcji, int liczbaSztukAkcji, int limitCeny,
                              int terminWaznosci, int numerTury, int kolejnoscWTurze, Inwestor inwestor) {
        super(typZlecenia, identyfikatorAkcji, liczbaSztukAkcji, limitCeny, terminWaznosci, numerTury, kolejnoscWTurze, inwestor);
    }
}
