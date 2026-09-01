import greenfoot.*;

/**
 * HUD del videojuego.
 *
 * Muestra en pantalla la vida y los puntos actuales del Jugador.
 *
 * El Marcador permanece fijo en la vista y no se desplaza junto
 * con la camara del escenario.
 *
 * Para evitar trabajo innecesario, la imagen del HUD solamente
 * se reconstruye cuando cambia la vida o la puntuacion.
 */
public class Marcador extends Actor
{
    private static final int ANCHO = 190;
    private static final int ALTO = 40;

    private Jugador jugador;

    private int ultimaVida = -1;
    private int ultimosPuntos = -1;

    public Marcador(Jugador jugador)
    {
        this.jugador = jugador;

        actualizarImagen();
    }

    public void act()
    {
        if (jugador.getVida() != ultimaVida
            || jugador.getPuntos() != ultimosPuntos)
        {
            actualizarImagen();
        }
    }

    /**
     * Reconstruye la representacion visual del HUD utilizando
     * los valores actuales del Jugador.
     */
    private void actualizarImagen()
    {
        ultimaVida = jugador.getVida();
        ultimosPuntos = jugador.getPuntos();

        GreenfootImage imagen =
            new GreenfootImage(
                ANCHO,
                ALTO
            );

        // Fondo semitransparente.
        imagen.setColor(
            new Color(
                0,
                0,
                0,
                160
            )
        );

        imagen.fillRect(
            0,
            0,
            ANCHO,
            ALTO
        );

        // Texto del HUD.
        imagen.setFont(
            new Font(
                "SansSerif",
                true,
                false,
                16
            )
        );

        imagen.setColor(
            Color.WHITE
        );

        imagen.drawString(
            "Vida: " + ultimaVida,
            10,
            18
        );

        imagen.drawString(
            "Puntos: " + ultimosPuntos,
            10,
            34
        );

        setImage(imagen);
    }
}