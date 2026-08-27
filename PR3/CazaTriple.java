/**
 * Write a description of class CazaTriple here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import greenfoot.*;

public class CazaTriple
    extends NaveEnemiga {

    public CazaTriple() {

        super(
            3,      // vida
            30,     // puntos
            30,     // carga
            new AtaqueTripleEnemigo(),
            100,    // dispara menos seguido
            60,
            Color.MAGENTA
        );
    }
}
