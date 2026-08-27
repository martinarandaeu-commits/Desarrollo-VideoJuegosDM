/**
 * Write a description of class Avanzar here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import greenfoot.*;

public class Avanzar implements Estado {

    private int pasos = 0;

    public Estado actuar(Enemigo enemigo) {

        enemigo.setLocation(
            enemigo.getX() - 3,
            enemigo.getY()
        );

        pasos++;

        if (pasos > 35) {
            return new Zigzag();
        }

        return this;
    }
}