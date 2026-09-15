import greenfoot.*;

/**
 * Caballero rojo controlado por el juego. Solo aporta sus sprites
 * (heredados de Unidad). No elige su propio ataque en act(), no
 * controla los turnos y no conoce los botones del jugador: la IA
 * (qué ataque enemigo usar) la coordina GestorCombate.
 */
public class InfanteriaEnemiga extends Unidad
{
    public InfanteriaEnemiga()
    {
        super(100);
        cargarAnimaciones("enemy", 4, 6, 3, 5, 3);
    }
}
