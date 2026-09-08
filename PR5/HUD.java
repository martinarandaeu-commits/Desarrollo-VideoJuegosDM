import greenfoot.*;

/**
 * HUD
 * ---
 * Dibuja, en una franja propia debajo del mapa, la vida, la municion,
 * el dinero, la oleada actual y el arma equipada.
 *
 * Driver: Rendimiento.
 *   Redibujar texto e imagenes en cada act() es trabajo innecesario
 *   si los numeros no cambiaron. Por eso, igual que la animacion de
 *   caminata de la guia de plataformas usa un contador para no cambiar
 *   de sprite en cada ciclo, aqui se usa un contador para refrescar la
 *   imagen del HUD cada varios ciclos en vez de en cada uno.
 */
public class HUD extends Actor
{
    private final JuegoWorld mundo;
    private final Jugador jugador;
    private final GestorOleadas gestor;

    private int contador = 0;
    private final int ciclosPorRefresco = 6;

    private static final int ANCHO = JuegoWorld.ANCHO;
    private static final int ALTO = JuegoWorld.ALTO_TOTAL - JuegoWorld.ALTO_JUEGO;

    public HUD(JuegoWorld mundo, Jugador jugador, GestorOleadas gestor)
    {
        this.mundo = mundo;
        this.jugador = jugador;
        this.gestor = gestor;
        redibujar();
    }

    public void act()
    {
        contador++;
        if (contador >= ciclosPorRefresco)
        {
            redibujar();
            contador = 0;
        }
    }

    private void redibujar()
    {
        GreenfootImage imagen = new GreenfootImage(ANCHO, ALTO);
        imagen.setColor(new Color(14, 12, 10));
        imagen.fillRect(0, 0, ANCHO, ALTO);
        imagen.setColor(new Color(58, 51, 42));
        imagen.drawLine(0, 0, ANCHO, 0);
        imagen.drawLine(0, 1, ANCHO, 1);

        int y = ALTO / 2 + 6;
        imagen.setFont(new Font("Courier New", true, false, 16));

        // --- Vida ---
        imagen.setColor(new Color(168, 154, 130));
        imagen.drawString("VIDA", 20, y - 12);
        dibujarBarra(imagen, 20, y - 6, 140, 14, jugador.getVida(), jugador.getVidaMaxima());

        // --- Municion ---
        imagen.setColor(new Color(168, 154, 130));
        imagen.drawString("MUNICION", 190, y - 12);
        imagen.setColor(new Color(232, 201, 107));
        imagen.drawString(jugador.getMunicion() + " / " + jugador.getMunicionMaxima(), 190, y + 6);

        // --- Dinero ---
        imagen.setColor(new Color(168, 154, 130));
        imagen.drawString("$", 340, y - 12);
        imagen.setColor(new Color(123, 201, 111));
        imagen.drawString("" + mundo.getDinero(), 340, y + 6);

        // --- Oleada ---
        imagen.setColor(new Color(168, 154, 130));
        imagen.drawString("OLEADA", 430, y - 12);
        imagen.setColor(new Color(217, 138, 61));
        imagen.drawString("" + gestor.getOleada(), 430, y + 6);

        // --- Armas ---
        dibujarSlotArma(imagen, ANCHO - 130, ALTO / 2 - 18, FabricaImagenes.slotPistola(),
            jugador.getArma() == Jugador.Arma.PISTOLA);
        dibujarSlotArma(imagen, ANCHO - 70, ALTO / 2 - 18, FabricaImagenes.slotEscopeta(),
            jugador.getArma() == Jugador.Arma.ESCOPETA);

        setImage(imagen);
    }

    private void dibujarBarra(GreenfootImage imagen, int x, int y, int ancho, int alto, int valor, int maximo)
    {
        imagen.setColor(new Color(58, 20, 20));
        imagen.fillRect(x, y, ancho, alto);
        int lleno = (int) (ancho * Math.max(0, Math.min(1.0, valor / (double) maximo)));
        imagen.setColor(new Color(192, 57, 43));
        imagen.fillRect(x, y, lleno, alto);
        imagen.setColor(Color.BLACK);
        imagen.drawRect(x, y, ancho, alto);
    }

    private void dibujarSlotArma(GreenfootImage imagen, int x, int y, GreenfootImage icono, boolean activo)
    {
        if (activo)
        {
            imagen.setColor(new Color(217, 138, 61));
            imagen.fillRect(x - 3, y - 3, 38, 28);
        }
        imagen.drawImage(icono, x, y);
    }
}
