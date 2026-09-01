import greenfoot.*;

/**
 * Actor visual utilizado para ambientar el escenario.
 *
 * No posee comportamiento ni participa en la logica del juego.
 * Su unica responsabilidad es mostrar un sprite decorativo.
 */
public class Decoracion extends Actor
{
    public Decoracion(String imagen)
    {
        setImage(imagen);
    }
}
