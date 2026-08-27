import greenfoot.*;

public class BalaEnemiga extends Actor {

    private int velocidad;

    public BalaEnemiga(
        int angulo,
        int velocidad
    ) {

        this.velocidad = velocidad;

        setRotation(angulo);

        GreenfootImage img =
            new GreenfootImage(12, 5);

        img.setColor(Color.RED);
        img.fillOval(0, 0, 12, 5);

        setImage(img);
    }

    public void act() {

        if (GameManager
                .getInstancia()
                .isJuegoTerminado()) {
            return;
        }

        move(velocidad);

        Nave nave =
            (Nave)
            getOneIntersectingObject(
                Nave.class
            );

        if (nave != null) {

            nave.morir();

            if (getWorld() != null) {
                getWorld().removeObject(this);
            }

            return;
        }

        if (isAtEdge()) {

            if (getWorld() != null) {
                getWorld().removeObject(this);
            }
        }
    }
}
