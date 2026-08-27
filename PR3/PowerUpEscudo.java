import greenfoot.*;

public class PowerUpEscudo extends Actor {

    public PowerUpEscudo() {
        crearImagen();
    }

    private void crearImagen() {

        GreenfootImage img =
            new GreenfootImage(34, 34);

        // Brillo exterior
        img.setColor(Color.CYAN);
        img.fillOval(1, 1, 32, 32);

        // Interior
        img.setColor(Color.BLUE);
        img.fillOval(6, 6, 22, 22);

        // Centro brillante
        img.setColor(Color.WHITE);
        img.fillOval(12, 12, 10, 10);

        // Borde
        img.setColor(Color.WHITE);
        img.drawOval(3, 3, 27, 27);

        setImage(img);
    }

    public void act() {

        if (GameManager
                .getInstancia()
                .isJuegoTerminado()) {
            return;
        }

        // Avanza desde la derecha
        setLocation(
            getX() - 2,
            getY()
        );

        Nave nave =
            (Nave) getOneIntersectingObject(
                Nave.class
            );

        if (nave != null) {

            nave.activarEscudo();

            if (getWorld() != null) {
                getWorld().removeObject(this);
            }

            return;
        }

        // Desaparece si no es recogido
        if (getX() <= 1 &&
            getWorld() != null) {

            getWorld().removeObject(this);
        }
    }
}