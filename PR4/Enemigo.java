import greenfoot.*;
import java.util.List;

/**
 * Enemigo "Thug" (traje azul marino) de Michael Jackson's Moonwalker.
 *
 * - Persigue al Jugador cuando ambos estan en el MISMO piso.
 * - Si el Jugador esta en otro piso, patrulla horizontalmente.
 * - Puede causar dano por contacto mientras esta activo.
 * - Necesita DOS golpes de patada para ser derrotado.
 * - El primer golpe provoca un estado temporal de TAMBALEANDO.
 * - El segundo golpe lo deja DERROTADO.
 *
 * DECISION ARQUITECTONICA:
 * Al igual que Jugador, Enemigo utiliza coordenadas autoritativas
 * de MUNDO (mundoX / mundoY) y pertenece a un piso fijo.
 *
 * Mundo se encarga de traducir esas coordenadas de mundo
 * a coordenadas visibles de pantalla.
 *
 * La representacion visual y los frames se delegan a
 * AnimadorEnemigo.
 */
public class Enemigo extends Actor
{
    // ---------------------------------------------------------------
    // Estados
    // ---------------------------------------------------------------
    private enum EstadoEnemigo
    {
        CAMINANDO,
        TAMBALEANDO,
        DERROTADO
    }

    private EstadoEnemigo estado =
        EstadoEnemigo.CAMINANDO;

    // ---------------------------------------------------------------
    // Representacion visual delegada
    // ---------------------------------------------------------------
    private AnimadorEnemigo animador;

    // ---------------------------------------------------------------
    // Coordenadas de MUNDO y piso
    // ---------------------------------------------------------------
    private double mundoX = 0;
    private double mundoY = 0;

    private int piso =
        Mundo.TOTAL_PISOS;

    // ---------------------------------------------------------------
    // Movimiento
    // ---------------------------------------------------------------
    private int direccion = 1;

    private static final int VELOCIDAD = 1;

    private static final int LIMITE_IZQUIERDO = 20;
    private static final int LIMITE_DERECHO = 938;

    // ---------------------------------------------------------------
    // Temporizadores de comportamiento
    // ---------------------------------------------------------------
    private int contadorTambaleo = 0;
    private int contadorDerrota = 0;

    // ---------------------------------------------------------------
    // Golpes
    // ---------------------------------------------------------------
    private int golpesRecibidos = 0;

    private static final int GOLPES_PARA_MORIR = 2;

    // Ciclos adicionales que permanece tambaleando despues
    // de recorrer la secuencia de sprites.
    private static final int PAUSA_FINAL_TAMBALEO = 12;

    public Enemigo()
    {
        animador =
            new AnimadorEnemigo(this);
    }

    /**
     * Coloca al enemigo en coordenadas de MUNDO.
     * Mundo invoca este metodo al crear el escenario.
     */
    public void colocar(
        int piso,
        double mundoX,
        double mundoY)
    {
        this.piso = piso;
        this.mundoX = mundoX;
        this.mundoY = mundoY;
    }

    public double getMundoX()
    {
        return mundoX;
    }

    public double getMundoY()
    {
        return mundoY;
    }

    public int getPiso()
    {
        return piso;
    }

    // ---------------------------------------------------------------
    // Ciclo principal
    // ---------------------------------------------------------------
    public void act()
    {
        if (estado == EstadoEnemigo.DERROTADO)
        {
            contadorDerrota++;

            if (contadorDerrota >= 90
                && getWorld() != null)
            {
                getWorld().removeObject(this);
                return;
            }
        }
        else if (
            estado
            == EstadoEnemigo.TAMBALEANDO)
        {
            actualizarTambaleo();
        }
        else
        {
            perseguirJugador();

            animador.animarCaminata(
                direccion
            );
        }

        /*
         * En cualquier estado los pies deben permanecer sobre
         * la linea correspondiente al piso.
         *
         * Los sprites de caminar, tambaleo y derrotado tienen
         * alturas diferentes, por lo que mundoY se recalcula
         * utilizando el alto real de la imagen actual.
         */
        pegarAlSuelo();
    }

    /**
     * Coloca los pies del enemigo exactamente sobre
     * la linea Y correspondiente a su piso.
     */
    private void pegarAlSuelo()
    {
        if (getWorld() != null)
        {
            mundoY =
                ((Mundo) getWorld()).getSuelo(
                    piso,
                    getImage().getHeight()
                );
        }
    }

    // ---------------------------------------------------------------
    // IA de movimiento
    //
    // Si el Jugador esta en el mismo piso, el enemigo lo persigue.
    // En caso contrario patrulla horizontalmente.
    // ---------------------------------------------------------------
    private void perseguirJugador()
    {
        Jugador jugador =
            jugadorEnElMundo();

        if (jugador != null
            && jugador.getPiso() == piso)
        {
            direccion =
                (jugador.getMundoX() < mundoX)
                    ? -1
                    : 1;

            mundoX +=
                direccion * VELOCIDAD;
        }
        else
        {
            // Patrulla entre los limites del escenario.
            mundoX +=
                direccion * VELOCIDAD;

            if (mundoX <= LIMITE_IZQUIERDO
                || mundoX >= LIMITE_DERECHO)
            {
                direccion =
                    -direccion;
            }
        }

        mundoX =
            Math.max(
                LIMITE_IZQUIERDO,
                Math.min(
                    LIMITE_DERECHO,
                    mundoX
                )
            );

        /*
         * mundoY se ajusta posteriormente mediante
         * pegarAlSuelo().
         */
    }

    /**
     * Obtiene el Jugador presente en el mundo.
     *
     * El escenario actual utiliza un unico Jugador.
     */
    private Jugador jugadorEnElMundo()
    {
        if (getWorld() == null)
        {
            return null;
        }

        List<Jugador> jugadores =
            getWorld().getObjects(
                Jugador.class
            );

        return jugadores.isEmpty()
            ? null
            : jugadores.get(0);
    }

    // ---------------------------------------------------------------
    // Combate
    //
    // Hacen falta dos golpes:
    //
    // 1er golpe -> TAMBALEANDO
    // vuelve a CAMINANDO
    // 2do golpe -> DERROTADO
    //
    // Mientras esta TAMBALEANDO no acepta otro golpe.
    // ---------------------------------------------------------------
    public boolean recibirGolpe()
    {
        if (estado
            != EstadoEnemigo.CAMINANDO)
        {
            return false;
        }

        golpesRecibidos++;

        if (golpesRecibidos
            >= GOLPES_PARA_MORIR)
        {
            derrotar();

            return true;
        }

        estado =
            EstadoEnemigo.TAMBALEANDO;

        animador.reiniciarAnimacion();

        contadorTambaleo = 0;

        return false;
    }

    /**
     * true mientras el enemigo esta activo en estado CAMINANDO.
     *
     * SaludJugador utiliza este metodo para determinar si
     * puede producirse dano por contacto.
     */
    public boolean estaPersiguiendo()
    {
        return estado
            == EstadoEnemigo.CAMINANDO;
    }

    public boolean estaDerrotado()
    {
        return estado
            == EstadoEnemigo.DERROTADO;
    }

    // ---------------------------------------------------------------
    // Tambaleo
    // ---------------------------------------------------------------
    private void actualizarTambaleo()
    {
        /*
         * La representacion visual pertenece al animador.
         * Enemigo conserva la duracion y la transicion de estado.
         */
        animador.animarTambaleo();

        contadorTambaleo++;

        if (contadorTambaleo
            >= animador.getDuracionSecuenciaTambaleo()
                + PAUSA_FINAL_TAMBALEO)
        {
            estado =
                EstadoEnemigo.CAMINANDO;
        }
    }

    // ---------------------------------------------------------------
    // Derrota
    // ---------------------------------------------------------------
    private void derrotar()
    {
        estado =
            EstadoEnemigo.DERROTADO;

        contadorDerrota = 0;

        animador.mostrarDerrotado();

        /*
         * El sprite derrotado es mas bajo que el sprite de
         * caminata, por lo que se reajustan los pies
         * inmediatamente.
         */
        pegarAlSuelo();
    }
}
