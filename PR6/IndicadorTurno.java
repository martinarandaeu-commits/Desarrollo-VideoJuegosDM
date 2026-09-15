import greenfoot.*;

/**
 * Indicador visual central del combate. Responde en todo momento a
 * "¿de quién es el turno?" mostrando mensajes como TU TURNO,
 * ATACANDO..., TURNO ENEMIGO, ENEMIGO ATACA..., VICTORIA o DERROTA.
 */
public class IndicadorTurno extends Actor
{
    private static final int ANCHO = 340;
    private static final int ALTO = 46;

    public IndicadorTurno()
    {
        setMensaje("TU TURNO");
    }

    public void setMensaje(String mensaje)
    {
        GreenfootImage imagen = new GreenfootImage(ANCHO, ALTO);
        imagen.setColor(new Color(20, 20, 30, 215));
        imagen.fillRect(0, 0, ANCHO, ALTO);
        imagen.setColor(new Color(255, 205, 80));
        imagen.drawRect(0, 0, ANCHO - 1, ALTO - 1);

        imagen.setFont(new Font("Arial", true, false, 20));
        imagen.setColor(Color.WHITE);
        imagen.drawString(mensaje, 18, 30);

        setImage(imagen);
    }
}
