import greenfoot.*;

/**
 * Jugador
 * -------
 * Controla al sobreviviente: lectura de teclado (WASD + espacio + 1/2),
 * movimiento, rotacion hacia la direccion en la que camina, disparo y
 * regeneracion lenta de municion.
 *
 * Driver: Jugabilidad -> las entradas se procesan en metodos pequenos
 * y separados (controlarMovimiento, controlarDisparo, controlarArma)
 * para que cada uno sea facil de leer y de modificar por separado.
 *
 * Nota sobre la guia de plataformas: alli el estado del jugador (QUIETO,
 * CAMINANDO, SALTANDO) se representaba con un enum porque cada estado
 * tenia una imagen distinta (varios sprites de caminata). Aqui solo
 * existe UN sprite del sobreviviente, asi que el "estado visual" no se
 * resuelve cambiando de imagen sino rotando siempre la misma imagen
 * hacia la direccion de movimiento (ver Soldado.mirarHacia). Por eso no
 * se usa un enum de estados de animacion; en cambio se usa un enum para
 * el arma equipada, que si cambia el comportamiento (dano, velocidad de
 * disparo, costo de municion).
 */
public class Jugador extends Soldado
{
    public enum Arma
    {
        PISTOLA,
        ESCOPETA
    }

    private Arma arma = Arma.PISTOLA;

    private int municion = 20;
    private final int municionMaxima = 20;
    private int contadorRegenMunicion = 0;
    private final int ciclosPorRegenMunicion = 70;

    private int cooldownDisparo = 0;

    // Ultima direccion valida (para disparar aunque el jugador este quieto)
    private int dirX = 0;
    private int dirY = -1;

    public Jugador()
    {
        super(100, 5);
        setImage(FabricaImagenes.jugador());
        mirarHacia(dirX, dirY);
    }

    public void act()
    {
        controlarMovimiento();
        controlarArma();
        controlarDisparo();
        regenerarMunicion();
        if (cooldownDisparo > 0)
        {
            cooldownDisparo--;
        }
    }

    private void controlarMovimiento()
    {
        int dx = 0;
        int dy = 0;

        if (Greenfoot.isKeyDown("w")) dy -= 1;
        if (Greenfoot.isKeyDown("s")) dy += 1;
        if (Greenfoot.isKeyDown("a")) dx -= 1;
        if (Greenfoot.isKeyDown("d")) dx += 1;

        if (dx == 0 && dy == 0)
        {
            return;
        }

        dirX = dx;
        dirY = dy;
        mirarHacia(dx, dy);

        // Normalizar para que la diagonal no sea mas rapida que recto
        double longitud = Math.sqrt(dx * dx + dy * dy);
        int pasoX = (int) Math.round(velocidad * dx / longitud);
        int pasoY = (int) Math.round(velocidad * dy / longitud);

        int mitad = 17;
        World mundo = getWorld();
        int nuevoX = Math.max(mitad, Math.min(mundo.getWidth() - mitad, getX() + pasoX));
        int nuevoY = Math.max(mitad, Math.min(JuegoWorld.ALTO_JUEGO - mitad, getY() + pasoY));
        moverA(nuevoX, nuevoY);
    }

    private void controlarArma()
    {
        if (Greenfoot.isKeyDown("1"))
        {
            arma = Arma.PISTOLA;
        }
        else if (Greenfoot.isKeyDown("2"))
        {
            arma = Arma.ESCOPETA;
        }
    }

    private void controlarDisparo()
    {
        boolean quiereDisparar = Greenfoot.isKeyDown("space") || Greenfoot.mouseClicked(null);
        if (!quiereDisparar || cooldownDisparo > 0)
        {
            return;
        }

        int costo = (arma == Arma.PISTOLA) ? 1 : 3;
        if (municion < costo)
        {
            return;
        }

        disparar();
        municion -= costo;
    }

    private void disparar()
    {
        double baseAngulo = Math.atan2(dirY, dirX);

        if (arma == Arma.PISTOLA)
        {
            crearBala(baseAngulo, 22);
            cooldownDisparo = 14;
        }
        else
        {
            // Escopeta: tres proyectiles en abanico
            double abertura = Math.toRadians(14);
            crearBala(baseAngulo - abertura, 14);
            crearBala(baseAngulo, 14);
            crearBala(baseAngulo + abertura, 14);
            cooldownDisparo = 32;
        }
    }

    private void crearBala(double angulo, int danio)
    {
        World mundo = getWorld();
        double dx = Math.cos(angulo);
        double dy = Math.sin(angulo);
        Bala bala = new Bala(dx, dy, danio);
        mundo.addObject(bala, getX() + (int) (dx * 20), getY() + (int) (dy * 20));
    }

    private void regenerarMunicion()
    {
        if (municion >= municionMaxima)
        {
            return;
        }
        contadorRegenMunicion++;
        if (contadorRegenMunicion >= ciclosPorRegenMunicion)
        {
            municion++;
            contadorRegenMunicion = 0;
        }
    }

    public int getMunicion()
    {
        return municion;
    }

    public int getMunicionMaxima()
    {
        return municionMaxima;
    }

    public Arma getArma()
    {
        return arma;
    }

    protected void morir()
    {
        World mundo = getWorld();
        if (mundo instanceof JuegoWorld)
        {
            ((JuegoWorld) mundo).finDelJuego();
        }
        super.morir();
    }
}
