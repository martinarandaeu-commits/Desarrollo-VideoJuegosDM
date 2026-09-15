import greenfoot.*;

/**
 * Representación visual de la vida de una Unidad (una sola clase
 * reutilizada para jugador y enemigo, evitando BarraJugador/BarraEnemigo
 * duplicadas). Dibuja etiqueta, fondo, proporción de HP y el texto
 * "HP actual / HP máximo". Debe actualizarse inmediatamente después de
 * que la unidad reciba daño.
 */
public class BarraHP extends Actor
{
    private static final int ANCHO = 220;
    private static final int ALTO = 50;
    private static final int ALTO_BARRA = 14;

    private Unidad unidad;
    private String etiqueta;

    public BarraHP(Unidad unidad, String etiqueta)
    {
        this.unidad = unidad;
        this.etiqueta = etiqueta;
        actualizar();
    }

    public void actualizar()
    {
        GreenfootImage imagen = new GreenfootImage(ANCHO, ALTO);

        imagen.setFont(new Font("Arial", true, false, 14));
        imagen.setColor(Color.WHITE);
        imagen.drawString(etiqueta, 0, 12);

        int y = 18;
        imagen.setColor(new Color(60, 20, 20));
        imagen.fillRect(0, y, ANCHO, ALTO_BARRA);

        double proporcion = Math.max(0, Math.min(1, unidad.proporcionVida()));
        int anchoVida = (int) Math.round(ANCHO * proporcion);
        Color colorVida;
        if (proporcion > 0.5)
        {
            colorVida = new Color(70, 200, 90);
        }
        else if (proporcion > 0.25)
        {
            colorVida = new Color(230, 190, 60);
        }
        else
        {
            colorVida = new Color(200, 60, 60);
        }
        imagen.setColor(colorVida);
        imagen.fillRect(0, y, anchoVida, ALTO_BARRA);

        imagen.setColor(Color.BLACK);
        imagen.drawRect(0, y, ANCHO - 1, ALTO_BARRA - 1);

        imagen.setFont(new Font("Arial", false, false, 12));
        imagen.setColor(Color.WHITE);
        imagen.drawString(unidad.getHP() + " / " + unidad.getHPMaximo(), 0, y + ALTO_BARRA + 14);

        setImage(imagen);
    }
}
