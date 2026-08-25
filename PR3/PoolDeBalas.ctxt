/**
 * Write a description of class Perseguir here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import greenfoot.*;
import java.util.*;

public class Perseguir implements Estado {

    private int pasos = 0;

    public Estado actuar(Enemigo enemigo) {

        World mundo = enemigo.getWorld();

        if (mundo == null) {
            return this;
        }

        int nuevaY = enemigo.getY();

        List<Nave> naves = mundo.getObjects(Nave.class);

        if (!naves.isEmpty()) {

            Nave nave = naves.get(0);

            // Persigue verticalmente la posicion del jugador
            if (nave.getY() > enemigo.getY() + 5) {
                nuevaY += 3;

            } else if (nave.getY() < enemigo.getY() - 5) {
                nuevaY -= 3;
            }
        }

        enemigo.setLocation(
            enemigo.getX() - 2,
            nuevaY
        );

        pasos++;

        // Después vuelve al zigzag
        if (pasos > 100) {
            return new Zigzag();
        }

        return this;
    }
}
