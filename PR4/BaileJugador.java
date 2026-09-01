import greenfoot.*;

/**
 * Responsable de la logica de los bailes del Jugador.
 *
 * Extrae de Jugador.java, sin cambiar su comportamiento:
 *  - tecla D: giro completo con baile1..6 y pose final;
 *  - tecla F: inclinacion antigravedad lean1..4;
 *  - baile automatico despues de un periodo de inactividad;
 *  - deteccion por flanco de D/F;
 *  - interrupcion inmediata al pulsar una tecla de accion;
 *  - temporizadores y duracion de cada coreografia.
 *
 * Jugador conserva Estado.BAILANDO y su booleano "bailando". Esta clase
 * solamente informa cuando un baile comienza o cuando debe terminar.
 */
public class BaileJugador
{
    private final Jugador jugador;
    private final AnimadorJugador animador;

    private int baileTicks = 0;
    private int baileVariante = 0;   // 0 = giro (D); 1 = inclinacion (F)
    private boolean teclaBaileAntes = false;
    private int quietoTicks = 0;

    // Giro (variante 0): mismos valores del Jugador original.
    private static final int BAILE_TICKS_FRAME = 5;
    private static final int BAILE_VUELTAS = 3;
    private static final int BAILE_DUR_GIRO =
        6 * BAILE_TICKS_FRAME * BAILE_VUELTAS;
    private static final int BAILE_DUR_POSE = 24;

    // Inclinacion antigravedad "Smooth Criminal" (variante 1).
    private static final int LEAN_TICKS_FRAME = 6;
    private static final int LEAN_DUR_ENTRADA = 4 * LEAN_TICKS_FRAME;
    private static final int LEAN_DUR_HOLD = 40;
    private static final int LEAN_DUR_SALIDA = 3 * LEAN_TICKS_FRAME;

    private static final int AUTO_BAILE_TICKS = 320;

    public BaileJugador(Jugador jugador, AnimadorJugador animador)
    {
        this.jugador = jugador;
        this.animador = animador;
    }

    /**
     * Ejecuta exactamente la logica que antes estaba en
     * Jugador.controlarBaile().
     *
     * Devuelve true solamente en el ciclo en que debe comenzar un baile.
     */
    public boolean intentarIniciar(
        boolean estadoQuieto,
        boolean saltando,
        boolean atacando)
    {
        boolean d = Greenfoot.isKeyDown("d");
        boolean f = Greenfoot.isKeyDown("f");
        boolean teclaAhora = d || f;
        boolean pidioBaile = teclaAhora && !teclaBaileAntes;

        // Es importante actualizar esto ANTES de comprobar si el jugador
        // esta saltando o atacando. Asi una pulsacion D/F durante esas
        // acciones se consume exactamente igual que en el codigo original.
        teclaBaileAntes = teclaAhora;

        // Mismo contador de inactividad del codigo original.
        if (estadoQuieto && !hayEntradaDeAccion() && !teclaAhora)
        {
            quietoTicks++;
        }
        else
        {
            quietoTicks = 0;
        }

        if (saltando || atacando)
        {
            return false;
        }

        if (pidioBaile)
        {
            iniciar(d ? 0 : 1);  // D = giro; F = inclinacion
            return true;
        }

        if (quietoTicks >= AUTO_BAILE_TICKS)
        {
            iniciar(0);          // baile automatico: siempre giro
            return true;
        }

        return false;
    }

    private void iniciar(int variante)
    {
        baileVariante = variante;
        baileTicks = 0;
        quietoTicks = 0;
    }

    /**
     * Avanza un ciclo de la coreografia.
     *
     * Devuelve true cuando el baile debe terminar, ya sea porque el
     * jugador lo interrumpio con una tecla de accion o porque la
     * coreografia termino naturalmente.
     */
    public boolean actualizar(boolean mirandoIzquierda)
    {
        // Igual que antes: una accion interrumpe inmediatamente el baile.
        // En este caso NO se reajusta mundoY ni se fuerza idle en este
        // mismo ciclo.
        if (hayEntradaDeAccion())
        {
            quietoTicks = 0;
            return true;
        }

        baileTicks++;

        boolean finalizado;

        if (baileVariante == 1)
        {
            finalizado = actualizarInclinacion(mirandoIzquierda);
        }
        else
        {
            finalizado = actualizarGiro(mirandoIzquierda);
        }

        // En el codigo original este ajuste se hacia incluso en el ciclo
        // en que la coreografia terminaba naturalmente.
        jugador.ajustarAlSueloActual();

        return finalizado;
    }

    /**
     * Variante D: baile1..6 durante tres vueltas y luego pose final.
     */
    private boolean actualizarGiro(boolean mirandoIzquierda)
    {
        if (baileTicks < BAILE_DUR_GIRO)
        {
            int frame =
                (baileTicks / BAILE_TICKS_FRAME)
                % animador.getCantidadFramesBaile();

            animador.mostrarFrameBaile(
                frame,
                mirandoIzquierda
            );

            return false;
        }

        if (baileTicks < BAILE_DUR_GIRO + BAILE_DUR_POSE)
        {
            animador.mostrarPoseFinal(mirandoIzquierda);
            return false;
        }

        finalizarNaturalmente();
        return true;
    }

    /**
     * Variante F: lean1 -> lean4, mantiene lean4 y vuelve lean4 -> lean1.
     */
    private boolean actualizarInclinacion(boolean mirandoIzquierda)
    {
        int tHold = LEAN_DUR_ENTRADA + LEAN_DUR_HOLD;
        int tFin = tHold + LEAN_DUR_SALIDA;

        if (baileTicks < LEAN_DUR_ENTRADA)
        {
            int frame = Math.min(
                animador.getCantidadFramesLean() - 1,
                baileTicks / LEAN_TICKS_FRAME
            );

            animador.mostrarFrameLean(
                frame,
                mirandoIzquierda
            );

            return false;
        }

        if (baileTicks < tHold)
        {
            animador.mostrarFrameLean(
                animador.getCantidadFramesLean() - 1,
                mirandoIzquierda
            );

            return false;
        }

        if (baileTicks < tFin)
        {
            int frame = Math.max(
                0,
                (animador.getCantidadFramesLean() - 1)
                    - (baileTicks - tHold) / LEAN_TICKS_FRAME
            );

            animador.mostrarFrameLean(
                frame,
                mirandoIzquierda
            );

            return false;
        }

        finalizarNaturalmente();
        return true;
    }

    /**
     * Corresponde al antiguo finBaile(): reinicia el contador de
     * inactividad y muestra idle sin aplicar espejo horizontal.
     */
    private void finalizarNaturalmente()
    {
        quietoTicks = 0;
        animador.mostrarQuietoBase();
    }

    /**
     * Misma definicion de "entrada de accion" del Jugador original.
     * D y F no forman parte de este conjunto.
     */
    private boolean hayEntradaDeAccion()
    {
        return Greenfoot.isKeyDown("left")
            || Greenfoot.isKeyDown("right")
            || Greenfoot.isKeyDown("space")
            || Greenfoot.isKeyDown("k")
            || Greenfoot.isKeyDown("up")
            || Greenfoot.isKeyDown("down");
    }
}