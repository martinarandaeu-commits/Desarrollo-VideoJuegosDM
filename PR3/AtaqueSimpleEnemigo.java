/**
 * Write a description of class AtaqueSimpleEnemigo here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import greenfoot.*;

public class AtaqueSimpleEnemigo
    implements EstrategiaAtaqueEnemigo {

    public void atacar(
        NaveEnemiga nave
    ) {

        if (nave.getWorld() == null) {
            return;
        }

        BalaEnemiga bala =
            new BalaEnemiga(
                180,
                6
            );

        nave.getWorld().addObject(
            bala,
            nave.getX() - 20,
            nave.getY()
        );
    }
}
