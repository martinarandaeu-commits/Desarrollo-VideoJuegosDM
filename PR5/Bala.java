import greenfoot.*;

/**
 * Bala
 * ----
 * Proyectil disparado por el Jugador. Tiene un ciclo de vida explicito:
 *
 *      crear -> moverse -> impactar o expirar -> eliminarse
 *
 * Driver: Memoria / Rendimiento.
 *   Una bala que nunca se elimina seguiria ocupando memoria y sumando
 *   trabajo por act() para siempre. Por eso toda bala tiene una
 *   "vidaUtil" en ciclos y tambien se elimina si sale del mundo.
 *   Ademas, Jugador nunca deja que existan mas de 60 balas a la vez
 *   (ver Jugador.controlarDisparo / GestorOleadas para el limite
 *   equivalente de enemigos).
 */
public class Bala extends Actor
{
    private final double dx;
    private final double dy;
    private final double velocidad = 11.0;
    private int vidaUtil = 55;
    private final int danio;

    public Bala(double dx, double dy, int danio)
    {
        this.dx = dx;
        this.dy = dy;
        this.danio = danio;
        setImage(FabricaImagenes.bala());
        setRotation((int) Math.round(Math.toDegrees(Math.atan2(dy, dx))));
    }

    public void act()
    {
        moverse();
        vidaUtil--;

        if (impacto())
        {
            return;
        }

        if (vidaUtil <= 0 || fueraDelMundo())
        {
            getWorld().removeObject(this);
        }
    }

    private void moverse()
    {
        setLocation((int) (getX() + dx * velocidad), (int) (getY() + dy * velocidad));
    }

    private boolean impacto()
    {
        SoldadoEnemigo enemigo = (SoldadoEnemigo) getOneIntersectingObject(SoldadoEnemigo.class);
        if (enemigo == null)
        {
            return false;
        }
        enemigo.recibirDanio(danio);
        getWorld().removeObject(this);
        return true;
    }

    private boolean fueraDelMundo()
    {
        World mundo = getWorld();
        return getX() <= 1 || getY() <= 1
            || getX() >= mundo.getWidth() - 1
            || getY() >= mundo.getHeight() - 1;
    }
}
