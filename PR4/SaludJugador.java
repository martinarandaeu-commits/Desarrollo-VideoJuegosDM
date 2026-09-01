import greenfoot.*;
import java.util.List;

/**
 * Responsable del sistema de salud del Jugador.
 *
 * Conserva exactamente la logica que antes estaba dentro de Jugador:
 *
 * - 3 vidas iniciales.
 * - dano por contacto con enemigos.
 * - contacto solamente con enemigos del mismo piso.
 * - contacto solamente con enemigos que estan persiguiendo.
 * - distancia maxima de contacto de 24 px de mundo.
 * - invulnerabilidad temporal despues de recibir dano.
 * - parpadeo visual durante la invulnerabilidad.
 * - Game Over al llegar a 0 vidas.
 *
 * Esta clase no controla movimiento, estados, ataque ni animaciones.
 */
public class SaludJugador
{
    private final Jugador jugador;

    private int vida = 3;

    private boolean invulnerable = false;
    private int contadorInvulnerable = 0;

    private boolean muerto = false;

    private static final int DURACION_INVULNERABILIDAD = 60;
    private static final int ANCHO_CONTACTO = 24;

    public SaludJugador(Jugador jugador)
    {
        this.jugador = jugador;
    }

    /**
     * Comprueba contacto con enemigos utilizando exactamente
     * las mismas condiciones del codigo original.
     *
     * El booleano enEscalera mantiene explicitamente la regla de que
     * el jugador no recibe dano por contacto durante una transicion
     * entre pisos.
     */
    public void verificarContactoEnemigo(boolean enEscalera)
    {
        if (enEscalera)
        {
            return;
        }

        List<Enemigo> enemigos =
            jugador.getWorld().getObjects(Enemigo.class);

        for (Enemigo enemigo : enemigos)
        {
            if (enemigo.getPiso() == jugador.getPiso()
                && enemigo.estaPersiguiendo()
                && Math.abs(
                    enemigo.getMundoX()
                    - jugador.getMundoX()
                ) <= ANCHO_CONTACTO)
            {
                recibirDano();
                return;
            }
        }
    }

    /**
     * Resta una vida y activa invulnerabilidad.
     *
     * Si el jugador ya es invulnerable o esta muerto,
     * no ocurre nada.
     */
    public void recibirDano()
    {
        if (invulnerable || muerto)
        {
            return;
        }

        vida--;

        invulnerable = true;
        contadorInvulnerable = 0;

        if (vida <= 0)
        {
            vida = 0;
            muerto = true;
        }
    }

    /**
     * Mantiene exactamente el mismo sistema de parpadeo
     * y los mismos 60 ciclos de invulnerabilidad.
     */
    public void actualizarInvulnerabilidad()
    {
        if (!invulnerable)
        {
            return;
        }

        contadorInvulnerable++;

        jugador.getImage().setTransparency(
            (contadorInvulnerable / 5) % 2 == 0
                ? 255
                : 90
        );

        if (contadorInvulnerable
            >= DURACION_INVULNERABILIDAD)
        {
            invulnerable = false;

            jugador.getImage().setTransparency(255);
        }
    }

    public int getVida()
    {
        return vida;
    }

    public boolean estaMuerto()
    {
        return muerto;
    }

    public boolean estaInvulnerable()
    {
        return invulnerable;
    }
}