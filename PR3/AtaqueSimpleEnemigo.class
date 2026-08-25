import greenfoot.*;

public class Asteroide extends Enemigo {

    private Estado estado;

    public Asteroide() {

        super(
            1,   // vida
            10,  // puntos
            10   // carga triple
        );

        crearImagen();

        int tipo =
            Greenfoot.getRandomNumber(3);

        if (tipo == 0) {
            estado = new Avanzar();

        } else if (tipo == 1) {
            estado = new Zigzag();

        } else {
            estado = new Perseguir();
        }
    }

    private void crearImagen() {

        GreenfootImage img =
            new GreenfootImage(34, 34);

        int[] x = {
            4, 11, 23, 31,
            28, 20, 8, 2
        };

        int[] y = {
            10, 3, 5, 13,
            25, 31, 28, 20
        };

        img.setColor(Color.GRAY);
        img.fillPolygon(x, y, 8);

        // Crateres
        img.setColor(Color.DARK_GRAY);

        img.fillOval(
            8,
            9,
            8,
            7
        );

        img.fillOval(
            20,
            18,
            7,
            8
        );

        img.fillOval(
            11,
            22,
            5,
            5
        );

        setImage(img);
    }

    protected void actualizarComportamiento() {

        estado = estado.actuar(this);

        if (getWorld() == null) {
            return;
        }

        if (getX() <= 1) {
            getWorld().removeObject(this);
        }
    }
}