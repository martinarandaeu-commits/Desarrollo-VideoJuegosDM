import greenfoot.*;
import java.util.List;

/**
 * Mundo del videojuego — escena unica "Club 30" de Michael Jackson's
 * Moonwalker (Sega, 1990) con CAMARA SCROLL.
 *
 * DECISION ARQUITECTONICA:
 * Se utiliza un unico mapa grande (images/fondo_stage1.png, 958x550)
 * compuesto por 4 pisos conectados mediante 3 escaleras diagonales.
 *
 * La ventana del juego sigue siendo de 800x500, mientras una camara
 * se desplaza sobre el mapa siguiendo al personaje.
 *
 * <pre>
 *   PISO 1 (arriba) ---- escalera A ---- PISO 2
 *   PISO 2          ---- escalera B ---- PISO 3
 *   PISO 3          ---- escalera C ---- PISO 4 (planta baja, inicio)
 * </pre>
 *
 * El escenario NO es ciclico: el personaje no puede atravesar los
 * bordes izquierdo ni derecho del mapa
 * (ver Mundo.X_MIN_MAPA / Mundo.X_MAX_MAPA).
 *
 * -----------------------------------------------------------------------
 * FUNCIONAMIENTO DE LA CAMARA
 * -----------------------------------------------------------------------
 *
 * Jugador y Enemigo conservan sus coordenadas autoritativas de mundo
 * (mundoX / mundoY). Su logica utiliza esas coordenadas y no depende
 * directamente de su posicion visible en la pantalla.
 *
 * Cada actualizacion del Mundo:
 *
 *  1. Ajusta camaraX / camaraY segun la posicion del jugador.
 *  2. Dibuja sobre la vista la region correspondiente del mapa.
 *  3. Traduce las coordenadas de mundo de Jugador y Enemigo a
 *     coordenadas de pantalla.
 *
 * Marcador funciona como HUD y permanece fijo en pantalla.
 *
 * El mapa mide 958x550 mientras la vista mide 800x500, por lo que
 * el desplazamiento disponible es de 158 px en X y 50 px en Y.
 * Esto produce un scroll moderado, coherente con el tamano actual
 * del escenario.
 */
public class Mundo extends World
{
    // -----------------------------------------------------------------
    // Vista y mapa completo
    // -----------------------------------------------------------------
    public static final int VISTA_W = 800;
    public static final int VISTA_H = 500;

    public static final int MAPA_W = 958;
    public static final int MAPA_H = 550;

    // Muros del mapa.
    public static final int X_MIN_MAPA = 18;
    public static final int X_MAX_MAPA = 940;

    // Zona muerta de la camara.
    private static final int MARGEN = 260;
    private static final int MARGEN_V = 180;

    // -----------------------------------------------------------------
    // Lineas de piso
    //
    // Indice 0 sin usar.
    // 1..4 representan los cuatro pisos del escenario.
    // -----------------------------------------------------------------
    private static final int[] FEET_Y =
        {
            0,
            100,
            246,
            390,
            534
        };

    public static final int TOTAL_PISOS = 4;

    // -----------------------------------------------------------------
    // Escaleras
    //
    // Cada objeto representa:
    // pisoAlto, pisoBajo, xAlto, xBajo.
    // -----------------------------------------------------------------
    private static final Escalera[] ESCALERAS =
        {
            // Escalera A: piso 1 <-> piso 2.
            new Escalera(
                1,
                2,
                938,
                805
            ),

            // Escalera B: piso 2 <-> piso 3.
            new Escalera(
                2,
                3,
                32,
                158
            ),

            // Escalera C: piso 3 <-> piso 4.
            new Escalera(
                3,
                4,
                905,
                790
            )
        };

    // Tolerancia horizontal para considerar al personaje
    // correctamente ubicado sobre una escalera.
    private static final int TOL_ESCALERA = 44;

    // -----------------------------------------------------------------
    // Estado del mundo
    // -----------------------------------------------------------------
    private GreenfootImage mapa;

    private int camaraX = 0;
    private int camaraY = 0;

    private Jugador jugador;
    private Marcador marcador;

    private boolean mensajeFinMostrado = false;

    // Musica de fondo.
    private GreenfootSound musica;

    public Mundo()
    {
        /*
         * 800x500 corresponde a la vista.
         *
         * false crea un mundo no acotado, permitiendo que los Actor
         * puedan quedar temporalmente fuera de la vista durante
         * el funcionamiento de la camara.
         */
        super(
            VISTA_W,
            VISTA_H,
            1,
            false
        );

        /*
         * Orden visual:
         *
         * Marcador permanece al frente como HUD.
         * Jugador se dibuja delante de los enemigos.
         */
        setPaintOrder(
            Marcador.class,
            Jugador.class,
            Enemigo.class
        );

        /*
         * El fondo del World mide solamente lo que mide la vista.
         * La region visible del mapa completo se dibuja manualmente.
         */
        setBackground(
            new GreenfootImage(
                VISTA_W,
                VISTA_H
            )
        );

        mapa =
        new GreenfootImage(
            "fondo_stage1.png"
        );

        musica =
        new GreenfootSound(
            "Smooth Criminal.wav"
        );

        // -------------------------------------------------------------
        // Jugador
        // -------------------------------------------------------------
        jugador = new Jugador();

        addObject(
            jugador,
            0,
            0
        );

        jugador.colocar(
            60,
            TOTAL_PISOS,
            getSuelo(
                TOTAL_PISOS,
                jugador.getImage().getHeight()
            )
        );

        // -------------------------------------------------------------
        // HUD
        // -------------------------------------------------------------
        marcador =
        new Marcador(jugador);

        addObject(
            marcador,
            95,
            26
        );

        // -------------------------------------------------------------
        // Enemigos
        // -------------------------------------------------------------
        spawnEnemigos();

        // -------------------------------------------------------------
        // Camara inicial
        // -------------------------------------------------------------
        camaraX = clamp(
            (int) jugador.getMundoX()
            - VISTA_W / 2,
            0,
            MAPA_W - VISTA_W
        );

        camaraY = clamp(
            (int) jugador.getMundoY()
            - VISTA_H / 2,
            0,
            MAPA_H - VISTA_H
        );

        dibujarFondo();
        sincronizarPantalla();
        prepare();
    }

    // -----------------------------------------------------------------
    // Ciclo principal del mundo
    // -----------------------------------------------------------------
    public void act()
    {
        // -------------------------------------------------------------
        // Game Complete
        // -------------------------------------------------------------
        if (!mensajeFinMostrado
        && jugador.isJuegoCompletado())
        {
            mensajeFinMostrado = true;

            musica.stop();

            /*
             * (char)161 representa el signo de exclamacion
             * invertido de apertura.
             */
            showText(
                (char) 161 + "GAME COMPLETE!",
                VISTA_W / 2,
                VISTA_H / 2
            );

            Greenfoot.stop();

            return;
        }

        // -------------------------------------------------------------
        // Game Over
        // -------------------------------------------------------------
        if (!mensajeFinMostrado
        && jugador.isJuegoTerminado())
        {
            mensajeFinMostrado = true;

            musica.stop();

            showText(
                "GAME OVER",
                VISTA_W / 2,
                VISTA_H / 2
            );

            Greenfoot.stop();

            return;
        }

        // -------------------------------------------------------------
        // Representacion del mundo
        // -------------------------------------------------------------
        actualizarCamara();
        dibujarFondo();
        sincronizarPantalla();
    }

    // -----------------------------------------------------------------
    // Musica
    // -----------------------------------------------------------------

    /**
     * Greenfoot ejecuta este callback al iniciar o reanudar
     * la simulacion.
     */
    public void started()
    {
        if (!mensajeFinMostrado)
        {
            musica.playLoop();
        }
    }

    /**
     * Greenfoot ejecuta este callback al pausar o detener
     * la simulacion.
     */
    public void stopped()
    {
        musica.pause();
    }

    // -----------------------------------------------------------------
    // Camara con zona muerta
    // -----------------------------------------------------------------
    private void actualizarCamara()
    {
        int px =
            (int) Math.round(
                jugador.getMundoX()
            )
            - camaraX;

        int py =
            (int) Math.round(
                jugador.getMundoY()
            )
            - camaraY;

        if (px < MARGEN)
        {
            camaraX -=
            MARGEN - px;
        }
        else if (
        px > VISTA_W - MARGEN)
        {
            camaraX +=
            px
            - (VISTA_W - MARGEN);
        }

        if (py < MARGEN_V)
        {
            camaraY -=
            MARGEN_V - py;
        }
        else if (
        py > VISTA_H - MARGEN_V)
        {
            camaraY +=
            py
            - (VISTA_H - MARGEN_V);
        }

        camaraX = clamp(
            camaraX,
            0,
            MAPA_W - VISTA_W
        );

        camaraY = clamp(
            camaraY,
            0,
            MAPA_H - VISTA_H
        );
    }

    /**
     * Dibuja sobre la vista la parte del mapa correspondiente
     * a la posicion actual de la camara.
     */
    private void dibujarFondo()
    {
        getBackground().drawImage(
            mapa,
            -camaraX,
            -camaraY
        );
    }

    /**
     * Convierte las coordenadas de mundo de Jugador y Enemigo
     * a coordenadas visibles de pantalla.
     *
     * Marcador no se modifica porque funciona como HUD.
     */
    private void sincronizarPantalla()
    {
        List<Jugador> jugadores =
            getObjects(Jugador.class);

        for (Jugador j : jugadores)
        {
            j.setLocation(
                (int) Math.round(
                    j.getMundoX()
                )
                - camaraX,

                (int) Math.round(
                    j.getMundoY()
                )
                - camaraY
            );
        }

        List<Enemigo> enemigos =
            getObjects(Enemigo.class);

        for (Enemigo e : enemigos)
        {
            e.setLocation(
                (int) Math.round(
                    e.getMundoX()
                )
                - camaraX,

                (int) Math.round(
                    e.getMundoY()
                )
                - camaraY
            );
        }
    }

    // -----------------------------------------------------------------
    // Pisos
    // -----------------------------------------------------------------

    /**
     * Devuelve la linea Y correspondiente a los pies
     * del piso indicado.
     */
    public int getSuelo(int piso)
    {
        return FEET_Y[
            clamp(
                piso,
                1,
                TOTAL_PISOS
            )
        ];
    }

    /**
     * Devuelve la coordenada Y del centro de un Actor necesaria
     * para que sus pies coincidan con la linea del piso.
     *
     * Greenfoot posiciona los Actor utilizando el centro de su imagen,
     * por lo que se considera la altura real del sprite.
     */
    public int getSuelo(
    int piso,
    int alturaImagen)
    {
        return getSuelo(piso)
        - alturaImagen / 2;
    }

    // -----------------------------------------------------------------
    // Escaleras
    // -----------------------------------------------------------------

    /**
     * Busca una escalera compatible con la posicion y direccion
     * indicadas.
     *
     * Si existe, devuelve:
     *
     * { pisoDestino, xDestino }
     *
     * Si no existe una escalera valida, devuelve null.
     */
    public int[] usarEscalera(
    int piso,
    double x,
    boolean subir)
    {
        for (Escalera escalera : ESCALERAS)
        {
            if (subir
            && piso
            == escalera.getPisoBajo()
            && Math.abs(
                x
                - escalera.getXBajo()
            ) <= TOL_ESCALERA)
            {
                return new int[]
                {
                    escalera.getPisoAlto(),
                    escalera.getXAlto()
                };
            }

            if (!subir
            && piso
            == escalera.getPisoAlto()
            && Math.abs(
                x
                - escalera.getXAlto()
            ) <= TOL_ESCALERA)
            {
                return new int[]
                {
                    escalera.getPisoBajo(),
                    escalera.getXBajo()
                };
            }
        }

        return null;
    }

    // -----------------------------------------------------------------
    // Informacion de camara
    // -----------------------------------------------------------------
    public int getCamaraX()
    {
        return camaraX;
    }

    public int getCamaraY()
    {
        return camaraY;
    }

    // -----------------------------------------------------------------
    // Enemigos
    //
    // El piso 1 permanece como zona final sin enemigos.
    // -----------------------------------------------------------------
    private void spawnEnemigos()
    {
        spawnEnemigo(
            4,
            480
        );

        spawnEnemigo(
            4,
            760
        );

        spawnEnemigo(
            3,
            300
        );

        spawnEnemigo(
            3,
            640
        );

        spawnEnemigo(
            2,
            500
        );
    }

    private void spawnEnemigo(
    int piso,
    double mundoX)
    {
        Enemigo enemigo =
            new Enemigo();

        addObject(
            enemigo,
            0,
            0
        );

        enemigo.colocar(
            piso,
            mundoX,
            getSuelo(
                piso,
                enemigo.getImage().getHeight()
            )
        );
    }

    // -----------------------------------------------------------------
    // Utilidad
    // -----------------------------------------------------------------
    private static int clamp(
    int valor,
    int minimo,
    int maximo)
    {
        return Math.max(
            minimo,
            Math.min(
                maximo,
                valor
            )
        );
    }
    /**
     * Prepare the world for the start of the program.
     * That is: create the initial objects and add them to the world.
     */
    private void prepare()
    {
    }
}