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

    private static final int MUNICION_MAXIMA = 20;
    private static final int CICLOS_POR_REGENERACION = 70;
    private static final int COSTO_PISTOLA = 1;
    private static final int COSTO_ESCOPETA = 3;
    private static final int DANIO_PISTOLA = 22;
    private static final int DANIO_ESCOPETA = 14;
    private static final int CICLOS_ENTRE_DISPAROS_PISTOLA = 14;
    private static final int CICLOS_ENTRE_DISPAROS_ESCOPETA = 32;

    private Arma arma = Arma.PISTOLA;

    private int municion = MUNICION_MAXIMA;
    private int ciclosDesdeRegeneracion = 0;

    private int ciclosHastaProximoDisparo = 0;

    // Ultima direccion valida (para disparar aunque el jugador este quieto)
    private int direccionDisparoX = 0;
    private int direccionDisparoY = -1;

    public Jugador()
    {
        super(100, 5);
        setImage(FabricaImagenes.jugador());
        mirarHacia(direccionDisparoX, direccionDisparoY);
    }

    public void act()
    {
        controlarMovimiento();
        controlarArma();
        controlarDisparo();
        regenerarMunicion();
        if (ciclosHastaProximoDisparo > 0)
        {
            ciclosHastaProximoDisparo--;
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

        direccionDisparoX = dx;
        direccionDisparoY = dy;
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
        if (!quiereDisparar || ciclosHastaProximoDisparo > 0)
        {
            return;
        }

        int costo = (arma == Arma.PISTOLA) ? COSTO_PISTOLA : COSTO_ESCOPETA;
        if (municion < costo)
        {
            return;
        }

        disparar();
        municion -= costo;
    }

    private void disparar()
    {
        double baseAngulo = Math.atan2(direccionDisparoY, direccionDisparoX);

        if (arma == Arma.PISTOLA)
        {
            crearBala(baseAngulo, DANIO_PISTOLA);
            ciclosHastaProximoDisparo = CICLOS_ENTRE_DISPAROS_PISTOLA;
        }
        else
        {
            // Escopeta: tres proyectiles en abanico
            double abertura = Math.toRadians(14);
            crearBala(baseAngulo - abertura, DANIO_ESCOPETA);
            crearBala(baseAngulo, DANIO_ESCOPETA);
            crearBala(baseAngulo + abertura, DANIO_ESCOPETA);
            ciclosHastaProximoDisparo = CICLOS_ENTRE_DISPAROS_ESCOPETA;
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
        if (municion >= MUNICION_MAXIMA)
        {
            return;
        }
        ciclosDesdeRegeneracion++;
        if (ciclosDesdeRegeneracion >= CICLOS_POR_REGENERACION)
        {
            municion++;
            ciclosDesdeRegeneracion = 0;
        }
    }

    public int getMunicion()
    {
        return municion;
    }

    public int getMunicionMaxima()
    {
        return MUNICION_MAXIMA;
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
