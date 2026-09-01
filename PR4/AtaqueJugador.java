import greenfoot.*;
import java.util.List;

/**
 * Responsable de la logica del ataque del Jugador.
 *
 * Conserva exactamente el comportamiento que antes estaba dentro de
 * Jugador.java: lectura de la tecla K, duracion del ataque, vocalizacion,
 * deteccion de enemigos en el mismo piso, alcance frontal y recompensa
 * de puntos al derrotar un enemigo.
 *
 * No decide el Estado visual del jugador. Jugador sigue siendo quien
 * cambia a ATACANDO/QUIETO; esta clase solo informa cuando el ataque
 * comienza o termina.
 */
public class AtaqueJugador
{
    private final Jugador jugador;
    private final AnimadorJugador animador;

    private GreenfootSound[] sonidosGolpe;

    private boolean atacando = false;
    private int duracionAtaque = 0;

    // Valores identicos a los que existian en Jugador.java.
    private static final int ALCANCE_ATAQUE = 36;
    private static final int PUNTOS_POR_ENEMIGO = 100;
    private static final int CICLOS_POR_FRAME = 6;

    public AtaqueJugador(Jugador jugador, AnimadorJugador animador)
    {
        this.jugador = jugador;
        this.animador = animador;
        cargarSonidos();
    }

    private void cargarSonidos()
    {
        sonidosGolpe = new GreenfootSound[] {
            new GreenfootSound("UHH.wav"),
            new GreenfootSound("auu.mp3"),
            new GreenfootSound("AU_MJ.wav")
        };
    }

    /**
     * Intenta iniciar el ataque exactamente bajo las mismas condiciones
     * que el codigo original.
     *
     * Devuelve true solo en el ciclo en que el ataque comienza.
     */
    public boolean intentarIniciar(boolean saltando)
    {
        if (Greenfoot.isKeyDown("k") && !atacando && !saltando)
        {
            atacando = true;
            duracionAtaque = 0;

            animador.reiniciarAnimacion();

            sonidosGolpe[
                Greenfoot.getRandomNumber(sonidosGolpe.length)
            ].play();

            return true;
        }

        return false;
    }

    /**
     * Avanza un ciclo del ataque.
     *
     * Igual que antes, durante cada ciclo activo se vuelve a comprobar
     * el alcance de la patada.
     *
     * Devuelve true solamente en el ciclo en que termina el ataque.
     */
    public boolean actualizar(boolean mirandoIzquierda)
    {
        if (!atacando)
        {
            return false;
        }

        duracionAtaque++;

        verificarGolpe(mirandoIzquierda);

        if (duracionAtaque
            >= animador.getCantidadFramesAtaque() * CICLOS_POR_FRAME)
        {
            atacando = false;
            return true;
        }

        return false;
    }

    public boolean estaAtacando()
    {
        return atacando;
    }

    /**
     * Busca enemigos en el mismo piso, delante del jugador y dentro
     * del mismo alcance utilizado antes del refactor.
     */
    private void verificarGolpe(boolean mirandoIzquierda)
    {
        List<Enemigo> enemigos =
            jugador.getWorld().getObjects(Enemigo.class);

        for (Enemigo enemigo : enemigos)
        {
            if (enemigo.getPiso() != jugador.getPiso())
            {
                continue;
            }

            double dx =
                enemigo.getMundoX()
                - jugador.getMundoX();

            boolean enMiDireccion =
                mirandoIzquierda
                ? (dx < 0)
                : (dx > 0);

            if (enMiDireccion
                && Math.abs(dx) <= ALCANCE_ATAQUE)
            {
                boolean derrotado =
                    enemigo.recibirGolpe();

                if (derrotado)
                {
                    jugador.sumarPuntos(
                        PUNTOS_POR_ENEMIGO
                    );
                }
            }
        }
    }
}
