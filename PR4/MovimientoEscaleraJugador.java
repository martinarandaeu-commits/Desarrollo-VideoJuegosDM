import greenfoot.*;

/**
 * Responsable del desplazamiento del Jugador entre pisos mediante
 * las escaleras del escenario.
 *
 * Conserva exactamente la logica que antes estaba dentro de
 * Jugador.java:
 *
 * - lectura de ARRIBA / ABAJO;
 * - consulta a Mundo.usarEscalera();
 * - interpolacion entre origen y destino;
 * - duracion de 14 ciclos;
 * - cambio de piso al finalizar;
 * - cooldown de 12 ciclos para evitar reactivar inmediatamente
 *   la misma escalera.
 *
 * Jugador sigue siendo responsable de su Estado logico:
 * EN_ESCALERA / QUIETO.
 */
public class MovimientoEscaleraJugador
{
    private final Jugador jugador;

    private boolean deslizando = false;
    private int deslizTicks = 0;

    private static final int DESLIZ_CICLOS = 14;

    private double deslizX0;
    private double deslizX1;
    private double deslizY0;
    private double deslizY1;

    private int pisoDestino;

    private int escaleraCooldown = 0;

    public MovimientoEscaleraJugador(Jugador jugador)
    {
        this.jugador = jugador;
    }

    /**
     * Mantiene exactamente el mismo cooldown que antes se actualizaba
     * directamente desde Jugador.act().
     */
    public void actualizarCooldown()
    {
        if (escaleraCooldown > 0)
        {
            escaleraCooldown--;
        }
    }

    public boolean estaDeslizando()
    {
        return deslizando;
    }

    /**
     * Intenta iniciar una transicion entre pisos.
     *
     * Devuelve true solo en el ciclo en que comienza el desplazamiento.
     */
    public boolean intentarIniciar(boolean saltando, boolean atacando)
    {
        if (saltando
            || atacando
            || deslizando
            || escaleraCooldown > 0)
        {
            return false;
        }

        boolean subir = Greenfoot.isKeyDown("up");
        boolean bajar = Greenfoot.isKeyDown("down");

        if (!subir && !bajar)
        {
            return false;
        }

        Mundo mundo = (Mundo) jugador.getWorld();

        int[] destino = mundo.usarEscalera(
            jugador.getPiso(),
            jugador.getMundoX(),
            subir
        );

        if (destino == null)
        {
            return false;
        }

        deslizando = true;
        deslizTicks = 0;

        deslizX0 = jugador.getMundoX();
        deslizX1 = destino[1];

        pisoDestino = destino[0];

        deslizY0 = jugador.getMundoY();

        deslizY1 = mundo.getSuelo(
            pisoDestino,
            jugador.getImage().getHeight()
        );

        return true;
    }

    /**
     * Avanza un ciclo de la interpolacion entre pisos.
     *
     * Devuelve true solamente en el ciclo en que finaliza
     * el desplazamiento.
     */
    public boolean actualizar()
    {
        if (!deslizando)
        {
            return false;
        }

        deslizTicks++;

        double t = Math.min(
            1.0,
            deslizTicks / (double) DESLIZ_CICLOS
        );

        double nuevoX =
            deslizX0
            + (deslizX1 - deslizX0) * t;

        double nuevoY =
            deslizY0
            + (deslizY1 - deslizY0) * t;

        jugador.actualizarPosicionMundo(
            nuevoX,
            nuevoY
        );

        if (t >= 1.0)
        {
            deslizando = false;

            jugador.actualizarPiso(
                pisoDestino
            );

            escaleraCooldown = 12;

            return true;
        }

        return false;
    }
}