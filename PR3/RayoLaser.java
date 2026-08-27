/**
 * Write a description of class RayoLaser here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import greenfoot.*;

public class RayoLaser extends Actor {

    private EnemigoLaser enemigo;

    public RayoLaser(
        EnemigoLaser enemigo
    ) {

        this.enemigo = enemigo;
    }

    public void act() {

        if (enemigo == null ||
            enemigo.getWorld() == null) {

            if (getWorld() != null) {
                getWorld().removeObject(this);
            }

            return;
        }

        actualizarHaz();

        Nave nave =
            (Nave)
            getOneIntersectingObject(
                Nave.class
            );

        if (nave != null) {
            nave.morir();
        }
    }

    private void actualizarHaz() {

        int ancho =
            enemigo.getX();

        if (ancho < 1) {
            ancho = 1;
        }

        GreenfootImage img =
            new GreenfootImage(
                ancho,
                22
            );

        // Haz exterior
        img.setColor(Color.RED);

        img.fillRect(
            0,
            0,
            ancho,
            22
        );

        // Centro brillante
        img.setColor(Color.YELLOW);

        img.fillRect(
            0,
            7,
            ancho,
            8
        );

        setImage(img);

        setLocation(
            enemigo.getX() / 2,
            enemigo.getY()
        );
    }
}