/**
 * Write a description of class Zigzag here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import greenfoot.*;

public class Zigzag implements Estado {

    private int direccion;
    private int pasos = 0;

    public Zigzag() {
        if (Greenfoot.getRandomNumber(2) == 0) {
            direccion = -1;
        } else {
            direccion = 1;
        }
    }

    public Estado actuar(Enemigo enemigo) {

        World mundo = enemigo.getWorld();

        int nuevaY =
            enemigo.getY() + direccion * 4;

        enemigo.setLocation(
            enemigo.getX() - 3,
            nuevaY
        );

        pasos++;

        // Rebota si llega cerca de los bordes
        if (enemigo.getY() <= 15 ||
            enemigo.getY() >= mundo.getHeight() - 15) {

            direccion = -direccion;
        }

        // Cambia periódicamente la direccion
        if (pasos % 18 == 0) {
            direccion = -direccion;
        }

        // Luego comienza a perseguir al jugador
        if (pasos > 80) {
            return new Perseguir();
        }

        return this;
    }
}
