import greenfoot.*;

public class AlertaLaser extends Actor {

    private EnemigoLaser enemigo;

    private int contador = 0;

    public AlertaLaser(
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

        actualizarPosicion();

        contador++;

        // Hace parpadear la linea
        if ((contador / 10) % 2 == 0) {

            getImage()
                .setTransparency(70);

        } else {

            getImage()
                .setTransparency(190);
        }
    }

    private void actualizarPosicion() {

        int ancho =
            enemigo.getX();

        if (ancho < 1) {
            ancho = 1;
        }

        GreenfootImage img =
            new GreenfootImage(
                ancho,
                3
            );

        img.setColor(Color.RED);

        img.fillRect(
            0,
            0,
            ancho,
            3
        );

        setImage(img);

        setLocation(
            enemigo.getX() / 2,
            enemigo.getY()
        );
    }
}