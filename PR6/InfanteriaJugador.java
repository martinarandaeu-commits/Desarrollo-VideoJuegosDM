import greenfoot.*;

/**
 * Caballero azul controlado por el jugador. Solo aporta sus sprites
 * (heredados de Unidad). No decide el turno, no calcula probabilidades,
 * no cambia el estado del combate y no maneja los botones: todo eso es
 * responsabilidad de GestorCombate.
 */
public class InfanteriaJugador extends Unidad
{
    public InfanteriaJugador()
    {
        super(100);
        cargarAnimaciones("player", 4, 6, 3, 5, 3);
    }
}
