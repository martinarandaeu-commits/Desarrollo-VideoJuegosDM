/**
 * Write a description of class NaveEnemiga here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import greenfoot.*;

public abstract class NaveEnemiga
    extends Enemigo {

    private EstrategiaAtaqueEnemigo ataque;

    private int recargaMinima;
    private int recargaVariacion;

    private int recarga;

    private int direccionVertical;

    private int objetivoX;

    public NaveEnemiga(
        int vida,
        int puntos,
        int cargaTriple,
        EstrategiaAtaqueEnemigo ataque,
        int recargaMinima,
        int recargaVariacion,
        Color color
    ) {

        super(
            vida,
            puntos,
            cargaTriple
        );

        this.ataque = ataque;

        this.recargaMinima =
            recargaMinima;

        this.recargaVariacion =
            recargaVariacion;

        objetivoX =
            390 +
            Greenfoot.getRandomNumber(140);

        direccionVertical =
            Greenfoot.getRandomNumber(2)
            == 0
            ? -1
            : 1;

        reiniciarRecarga();

        crearImagen(color);
    }

    private void crearImagen(
        Color color
    ) {

        GreenfootImage img =
            new GreenfootImage(42, 28);

        // Cuerpo
        img.setColor(color);

        int[] x = {
            40,
            12,
            3,
            12
        };

        int[] y = {
            14,
            2,
            14,
            26
        };

        img.fillPolygon(
            x,
            y,
            4
        );

        // Cabina
        img.setColor(Color.DARK_GRAY);

        img.fillOval(
            18,
            9,
            10,
            10
        );

        // Motores
        img.setColor(Color.YELLOW);

        img.fillRect(
            32,
            7,
            8,
            4
        );

        img.fillRect(
            32,
            17,
            8,
            4
        );

        setImage(img);
    }

    protected void actualizarComportamiento() {

        if (getWorld() == null) {
            return;
        }

        // Primero entra desde la derecha
        if (getX() > objetivoX) {

            setLocation(
                getX() - 2,
                getY()
            );

        } else {

            moverVertical();
            actualizarAtaque();
        }
    }

    private void moverVertical() {

        int nuevaY =
            getY() +
            direccionVertical * 2;

        if (nuevaY <= 25 ||
            nuevaY >=
            getWorld().getHeight() - 25) {

            direccionVertical =
                -direccionVertical;

            nuevaY =
                getY() +
                direccionVertical * 2;
        }

        setLocation(
            getX(),
            nuevaY
        );

        // De vez en cuando cambia
        // de direccion espontaneamente
        if (Greenfoot
                .getRandomNumber(120)
                == 0) {

            direccionVertical =
                -direccionVertical;
        }
    }

    private void actualizarAtaque() {

        recarga--;

        if (recarga <= 0) {

            ataque.atacar(this);

            reiniciarRecarga();
        }
    }

    private void reiniciarRecarga() {

        recarga =
            recargaMinima +
            Greenfoot.getRandomNumber(
                recargaVariacion
            );
    }
}
