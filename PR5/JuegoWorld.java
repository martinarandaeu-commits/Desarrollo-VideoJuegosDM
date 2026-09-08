import greenfoot.*;

/**
 * JuegoWorld
 * ----------
 * El mundo del juego. Arma el escenario (fondo + franja de HUD),
 * conoce los obstaculos solidos del mapa, lleva la cuenta del dinero
 * ganado y decide cuando termina la partida.
 *
 * Driver: Observabilidad.
 *   El HUD necesita poder preguntarle al mundo cuanto dinero hay y
 *   GestorOleadas necesita saber los limites del area jugable; por eso
 *   esos datos (ANCHO, ALTO_JUEGO, getDinero...) se centralizan aqui
 *   en vez de repartirse entre varias clases.
 */
public class JuegoWorld extends World
{
    public static final int ANCHO = 1400;
    public static final int ALTO_JUEGO = 800;
    public static final int ALTO_TOTAL = 850;

    // Rectangulos solidos calculados a partir de la composicion de mapa.png
    // (granero de techo verde-oliva, granero rojo, cerca del cultivo,
    // base del molino, tractor y la casa de piedra).
    private static final Obstaculo[] OBSTACULOS = {
        new Obstaculo(115, 108, 545, 294),   // granero grande (techo oliva)
        new Obstaculo(930, 114, 1310, 313),  // granero rojo
        new Obstaculo(760, 10, 1060, 260),   // cultivo cercado
        new Obstaculo(648, 340, 668, 390),   // base del molino
        new Obstaculo(199, 293, 280, 377),   // tractor
        new Obstaculo(300, 592, 492, 752)    // casa de piedra
    };

    private Jugador jugador;
    private GestorOleadas gestor;
    private HUD hud;

    private int dinero = 0;
    private int zombisEliminados = 0;

    public JuegoWorld()
    {
        super(ANCHO, ALTO_TOTAL, 1);
        prepararFondo();
        prepararMundo();
    }

    private void prepararFondo()
    {
        GreenfootImage fondo = new GreenfootImage(ANCHO, ALTO_TOTAL);
        fondo.setColor(new Color(10, 8, 6));
        fondo.fill();
        fondo.drawImage(new GreenfootImage("mapa.png"), 0, 0);
        setBackground(fondo);
    }

    private void prepararMundo()
    {
        jugador = new Jugador();
        addObject(jugador, ANCHO / 2 + 20, ALTO_JUEGO / 2 + 100);

        gestor = new GestorOleadas(this, jugador);

        hud = new HUD(this, jugador, gestor);
        addObject(hud, ANCHO / 2, ALTO_JUEGO + (ALTO_TOTAL - ALTO_JUEGO) / 2);
    }

    public void act()
    {
        gestor.actualizar();
    }

    /** true si (x, y) cae dentro de algun obstaculo solido del mapa. */
    public boolean esObstaculo(int x, int y)
    {
        for (Obstaculo o : OBSTACULOS)
        {
            if (o.contiene(x, y))
            {
                return true;
            }
        }
        return false;
    }

    public void registrarZombieEliminado(int x, int y)
    {
        dinero += 5;
        zombisEliminados++;
        dibujarSangre(x, y);
    }

    private void dibujarSangre(int x, int y)
    {
        GreenfootImage fondo = getBackground();
        fondo.setColor(new Color(58, 14, 12, 140));
        int r = 6 + Greenfoot.getRandomNumber(6);
        fondo.fillOval(x - r / 2, y - r / 2, r, r);
    }

    public int getDinero()
    {
        return dinero;
    }

    public int getZombisEliminados()
    {
        return zombisEliminados;
    }

    public void finDelJuego()
    {
        GreenfootImage texto = new GreenfootImage("HAS CAIDO", 40, Color.WHITE, new Color(120, 20, 20));
        getBackground().drawImage(texto,
            ANCHO / 2 - texto.getWidth() / 2,
            ALTO_JUEGO / 2 - texto.getHeight() / 2);

        String stats = "Oleada " + gestor.getOleada()
            + "  -  " + zombisEliminados + " zombis eliminados  -  $" + dinero;
        GreenfootImage subtexto = new GreenfootImage(stats, 22, Color.WHITE, null);
        getBackground().drawImage(subtexto,
            ANCHO / 2 - subtexto.getWidth() / 2,
            ALTO_JUEGO / 2 + 30);

        Greenfoot.stop();
    }
}
