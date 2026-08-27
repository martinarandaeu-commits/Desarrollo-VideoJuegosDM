/**
 * Write a description of class AtaqueTripleEnemigo here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import greenfoot.*;

public class AtaqueTripleEnemigo
    implements EstrategiaAtaqueEnemigo {

    public void atacar(
        NaveEnemiga nave
    ) {

        if (nave.getWorld() == null) {
            return;
        }

        int[] angulos = {
            165,
            180,
            195
        };

        for (int angulo : angulos) {

            BalaEnemiga bala =
                new BalaEnemiga(
                    angulo,
                    6
                );

            nave.getWorld().addObject(
                bala,
                nave.getX() - 20,
                nave.getY()
            );
        }
    }
}
