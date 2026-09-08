import greenfoot.*;

/**
 * SoldadoEnemigo
 * --------------
 * Persigue al jugador en linea recta y lo ataca al tocarlo.
 *
 * Driver: Modularidad.
 *   Este actor NO sabe en que oleada esta el juego, ni cuantos zombis
 *   deberian existir en total. Esa responsabilidad es exclusiva de
 *   GestorOleadas. SoldadoEnemigo solo sabe perseguir, atacar y morir;
 *   asi, cambiar la formula de dificultad no obliga a tocar esta clase.
 */
public class SoldadoEnemigo extends Soldado
{
    private final Jugador objetivo;
    private final int danio;
    private int cooldownAtaque = 0;
    private final int ciclosEntreAtaques = 40;

    public SoldadoEnemigo(Jugador objetivo, int vida, int velocidad, int danio)
    {
        super(vida, velocidad);
        this.objetivo = objetivo;
        this.danio = danio;
        setImage(FabricaImagenes.zombie());
    }

    public void act()
    {
        if (objetivo == null || objetivo.getWorld() == null)
        {
            return;
        }
        perseguir();
        if (cooldownAtaque > 0)
        {
            cooldownAtaque--;
        }
        atacar();
    }

    private void perseguir()
    {
        double dx = objetivo.getX() - getX();
        double dy = objetivo.getY() - getY();
        double distancia = Math.sqrt(dx * dx + dy * dy);

        if (distancia < 1)
        {
            return;
        }

        mirarHacia(dx, dy);

        int movX = (int) Math.round(velocidad * dx / distancia);
        int movY = (int) Math.round(velocidad * dy / distancia);
        moverA(getX() + movX, getY() + movY);
    }

    private void atacar()
    {
        if (isTouching(Jugador.class) && cooldownAtaque <= 0)
        {
            objetivo.recibirDanio(danio);
            cooldownAtaque = ciclosEntreAtaques;
        }
    }

    protected void morir()
    {
        World mundo = getWorld();
        if (mundo instanceof JuegoWorld)
        {
            ((JuegoWorld) mundo).registrarZombieEliminado(getX(), getY());
        }
        super.morir();
    }
}
