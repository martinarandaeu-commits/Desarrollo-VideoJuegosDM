import greenfoot.*;

/**
 * Mundo principal del combate.
 *
 * Responsabilidad única: crear el escenario (900x600), cargar y escalar
 * el fondo, crear el GestorCombate y delegarle toda la lógica del
 * juego en cada ciclo de act(). NO contiene probabilidades, daño,
 * control de turnos, IA enemiga ni resolución de ataques: todo eso
 * vive en GestorCombate.
 */
public class JuegoTurnos extends World
{
    private static final int VOLUMEN_MUSICA = 35; // 0-100, volumen moderado

    private GestorCombate gestor;
    private GreenfootSound musica;

    public JuegoTurnos()
    {
        super(900, 600, 1);

        GreenfootImage fondo = new GreenfootImage("scenario.jpeg");
        fondo.scale(getWidth(), getHeight());
        setBackground(fondo);

        gestor = new GestorCombate(this);
        gestor.inicializar();

        musica = new GreenfootSound("musica_fondo.mp3");
        musica.setVolume(VOLUMEN_MUSICA);
    }

    public void act()
    {
        gestor.actualizar();
    }

    /**
     * Greenfoot llama a este método automáticamente cada vez que se
     * presiona "Run". Aquí arrancamos (o reanudamos) la música de
     * fondo, salvo que el combate ya haya terminado.
     */
    public void started()
    {
        if (!gestor.combateTerminado() && !musica.isPlaying())
        {
            musica.playLoop();
        }
    }

    /**
     * Greenfoot llama a este método automáticamente al pausar la
     * simulación (botón "Pause"). Pausamos la música junto con el
     * juego para que no siga sonando de fondo.
     */
    public void stopped()
    {
        musica.pause();
    }

    /**
     * Llamado por GestorCombate apenas el combate llega a FIN
     * (VICTORIA o DERROTA) para cortar la música de fondo.
     */
    public void detenerMusica()
    {
        musica.stop();
    }
}
