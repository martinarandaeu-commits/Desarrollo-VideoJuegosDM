import greenfoot.*;

/**
 * Texto temporal que muestra el resultado de un ataque ("-8 HP",
 * "-12 HP", "-20 HP", "-9 HP", "-14 HP" o "FALLO"). Aparece cerca de
 * quien recibió/evitó el ataque, sube lentamente, permanece unos
 * frames y se elimina sola del mundo: nunca debe quedar acumulando
 * Actors indefinidamente.
 */
public class TextoFlotante extends Actor
{
    private static final int DURACION = 45; // frames que permanece visible

    private int contadorFrames;

    public TextoFlotante(String texto)
    {
        boolean esFallo = "FALLO".equals(texto);

        GreenfootImage imagen = new GreenfootImage(140, 30);
        imagen.setFont(new Font("Arial", true, false, 20));
        imagen.setColor(esFallo ? new Color(210, 210, 210) : new Color(255, 70, 70));
        imagen.drawString(texto, 4, 22);
        setImage(imagen);

        contadorFrames = 0;
    }

    public void act()
    {
        setLocation(getX(), getY() - 1);
        contadorFrames++;
        if (contadorFrames >= DURACION)
        {
            getWorld().removeObject(this);
        }
    }
}
