/**
 * Write a description of class EnemigoLaser here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import greenfoot.*;

public class EnemigoLaser
    extends Enemigo {

    private enum Fase {
        ENTRANDO,
        CARGANDO,
        DISPARANDO,
        ESCAPANDO
    }

    private Fase fase =
        Fase.ENTRANDO;

    private int objetivoX = 510;

    private int direccionVertical;

    // Aproximadamente 3 segundos
    // suponiendo ~60 frames por segundo
    private static final int
        TIEMPO_CARGA = 180;

    // Aproximadamente 4 segundos
    private static final int
        TIEMPO_LASER = 240;

    private int contador = 0;

    private AlertaLaser alerta;
    private RayoLaser laser;

    public EnemigoLaser() {

        super(
            3,   // vida
            50,  // puntos
            40   // carga triple
        );

        direccionVertical =
            Greenfoot.getRandomNumber(2)
            == 0
            ? -1
            : 1;

        crearImagen();
    }

    private void crearImagen() {

        GreenfootImage img =
            new GreenfootImage(
                55,
                38
            );

        // Cuerpo
        img.setColor(Color.DARK_GRAY);

        img.fillOval(
            5,
            5,
            42,
            28
        );

        // Cañon
        img.setColor(Color.RED);

        img.fillRect(
            0,
            15,
            20,
            8
        );

        // Reactor
        img.setColor(Color.ORANGE);

        img.fillOval(
            32,
            11,
            14,
            14
        );

        // Centro
        img.setColor(Color.LIGHT_GRAY);

        img.drawOval(
            7,
            7,
            38,
            24
        );

        setImage(img);
    }

    protected void actualizarComportamiento() {

        if (getWorld() == null) {
            return;
        }

        if (fase == Fase.ENTRANDO) {

            entrar();

        } else if (fase == Fase.CARGANDO) {

            cargar();

        } else if (fase == Fase.DISPARANDO) {

            dispararLaser();

        } else if (fase == Fase.ESCAPANDO) {

            escapar();
        }
    }

    private void entrar() {

        setLocation(
            getX() - 2,
            getY()
        );

        if (getX() <= objetivoX) {

            fase = Fase.CARGANDO;

            contador =
                TIEMPO_CARGA;

            crearAlerta();
        }
    }

    private void cargar() {

        moverVertical(2);

        contador--;

        if (contador <= 0) {

            eliminarAlerta();

            crearLaser();

            contador =
                TIEMPO_LASER;

            fase =
                Fase.DISPARANDO;
        }
    }

    private void dispararLaser() {

        // Se mueve mas lento
        // mientras mantiene el laser
        moverVertical(1);

        contador--;

        if (contador <= 0) {

            eliminarLaser();

            fase =
                Fase.ESCAPANDO;
        }
    }

    private void escapar() {

        setLocation(
            getX() - 4,
            getY()
        );

        if (getX() <= 5) {

            limpiarObjetosLaser();

            if (getWorld() != null) {
                getWorld()
                    .removeObject(this);
            }
        }
    }

    private void moverVertical(
        int velocidad
    ) {

        int nuevaY =
            getY() +
            direccionVertical *
            velocidad;

        if (nuevaY <= 30 ||
            nuevaY >=
            getWorld().getHeight()
            - 30) {

            direccionVertical =
                -direccionVertical;

            nuevaY =
                getY() +
                direccionVertical *
                velocidad;
        }

        setLocation(
            getX(),
            nuevaY
        );
    }

    private void crearAlerta() {

        if (getWorld() == null) {
            return;
        }

        alerta =
            new AlertaLaser(this);

        getWorld().addObject(
            alerta,
            getX() / 2,
            getY()
        );
    }

    private void crearLaser() {

        if (getWorld() == null) {
            return;
        }

        laser =
            new RayoLaser(this);

        getWorld().addObject(
            laser,
            getX() / 2,
            getY()
        );
    }

    private void eliminarAlerta() {

        if (alerta != null &&
            alerta.getWorld() != null) {

            alerta.getWorld()
                .removeObject(alerta);
        }

        alerta = null;
    }

    private void eliminarLaser() {

        if (laser != null &&
            laser.getWorld() != null) {

            laser.getWorld()
                .removeObject(laser);
        }

        laser = null;
    }

    private void limpiarObjetosLaser() {

        eliminarAlerta();
        eliminarLaser();
    }

    protected void alSerDestruido() {

        limpiarObjetosLaser();
    }
}
