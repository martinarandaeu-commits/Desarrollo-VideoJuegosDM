import greenfoot.*;

/**
 * Clase de datos y lógica pequeña que representa un ataque (nombre,
 * daño y probabilidad de acierto). La usan tanto el jugador
 * (Ataque rápido, Ataque normal, Golpe fuerte) como el enemigo
 * (Corte, Estocada). GestorCombate decide cuándo comprobar el
 * acierto; esta clase solo sabe cómo comprobarlo.
 */
public class AccionCombate
{
    private String nombre;
    private int danio;
    private int probabilidadAcierto;

    public AccionCombate(String nombre, int danio, int probabilidadAcierto)
    {
        this.nombre = nombre;
        this.danio = danio;
        this.probabilidadAcierto = probabilidadAcierto;
    }

    public String getNombre()
    {
        return nombre;
    }

    public int getDanio()
    {
        return danio;
    }

    public int getProbabilidadAcierto()
    {
        return probabilidadAcierto;
    }

    /**
     * Debe llamarse una única vez por resolución de ataque.
     */
    public boolean acierta()
    {
        return Greenfoot.getRandomNumber(100) < probabilidadAcierto;
    }
}
