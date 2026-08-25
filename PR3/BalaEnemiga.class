import greenfoot.*;

public class Bala extends Actor {

    private boolean activa = false;

    public Bala() {

        GreenfootImage img =
            new GreenfootImage(10, 4);

        img.setColor(Color.YELLOW);
        img.fill();

        setImage(img);
    }

    public void activar(
        World mundo,
        int x,
        int y,
        int angulo
    ) {

        activa = true;

        setRotation(angulo);

        mundo.addObject(
            this,
            x,
            y
        );
    }

    public void act() {

        if (!activa) {
            return;
        }

        move(8);

        Enemigo enemigo =
            (Enemigo)
            getOneIntersectingObject(
                Enemigo.class
            );

        if (enemigo != null) {

            enemigo.recibirImpacto();

            desactivar();

            return;
        }

        if (isAtEdge()) {
            desactivar();
        }
    }

    public void desactivar() {

        activa = false;

        if (getWorld() != null) {
            getWorld().removeObject(this);
        }
    }

    public boolean estaActiva() {
        return activa;
    }
}
