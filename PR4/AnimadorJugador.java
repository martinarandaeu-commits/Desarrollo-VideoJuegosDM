import greenfoot.*;

/**
 * Responsable exclusivamente de la representacion visual del Jugador.
 *
 * Mantiene los sprites, el frame actual y el contador de animacion.
 * La clase Jugador sigue decidiendo QUE estado tiene el personaje y
 * CUANDO debe cambiar de comportamiento; AnimadorJugador solo decide
 * COMO se representa visualmente ese estado.
 *
 * Este refactor no cambia tiempos, sprites, orientacion ni secuencias
 * respecto de la implementacion original de Jugador.java.
 */
public class AnimadorJugador
{
    private final Jugador jugador;

    // Sprites fijos
    private GreenfootImage imagenQuieto;
    private GreenfootImage imagenSaltando;
    private GreenfootImage imagenCayendo;
    private GreenfootImage imagenPoseFinal;

    // Secuencias de sprites
    private GreenfootImage[] caminar;
    private GreenfootImage[] atacar;
    private GreenfootImage[] baile;
    private GreenfootImage[] lean;

    // Control compartido de animacion, igual que en Jugador original.
    private int frameActual = 0;
    private int contadorAnimacion = 0;

    public AnimadorJugador(Jugador jugador)
    {
        this.jugador = jugador;
        cargarImagenes();
    }

    /**
     * Carga exactamente los mismos sprites que cargaba Jugador.java.
     */
    private void cargarImagenes()
    {
        imagenQuieto = new GreenfootImage("idle.png");
        imagenSaltando = new GreenfootImage("jump.png");
        imagenCayendo = new GreenfootImage("fall.png");

        caminar = new GreenfootImage[6];
        for (int i = 0; i < 6; i++)
        {
            caminar[i] = new GreenfootImage("walk" + (i + 1) + ".png");
        }

        atacar = new GreenfootImage[4];
        for (int i = 0; i < 4; i++)
        {
            atacar[i] = new GreenfootImage("atacar" + (i + 1) + ".png");
        }

        baile = new GreenfootImage[6];
        for (int i = 0; i < 6; i++)
        {
            baile[i] = new GreenfootImage("baile" + (i + 1) + ".png");
        }

        imagenPoseFinal = new GreenfootImage("pose_final.png");

        lean = new GreenfootImage[4];
        for (int i = 0; i < 4; i++)
        {
            lean[i] = new GreenfootImage("lean" + (i + 1) + ".png");
        }
    }

    /**
     * Equivale al setImage(imagenQuieto) directo del codigo original.
     * No aplica espejo horizontal.
     */
    public void mostrarQuietoBase()
    {
        jugador.setImage(imagenQuieto);
    }

    public void mostrarQuieto(boolean mirandoIzquierda)
    {
        mostrarImagenFija(imagenQuieto, mirandoIzquierda);
    }

    public void mostrarSaltando(boolean mirandoIzquierda)
    {
        mostrarImagenFija(imagenSaltando, mirandoIzquierda);
    }

    public void mostrarCayendo(boolean mirandoIzquierda)
    {
        mostrarImagenFija(imagenCayendo, mirandoIzquierda);
    }

    /**
     * Reinicia exactamente las dos variables que Jugador reiniciaba al
     * comenzar un ataque.
     */
    public void reiniciarAnimacion()
    {
        frameActual = 0;
        contadorAnimacion = 0;
    }

    public int getCantidadFramesAtaque()
    {
        return atacar.length;
    }

    public int getCantidadFramesBaile()
    {
        return baile.length;
    }

    public int getCantidadFramesLean()
    {
        return lean.length;
    }

    public void mostrarFrameBaile(int frame, boolean mirandoIzquierda)
    {
        mostrarImagenFija(baile[frame], mirandoIzquierda);
    }

    public void mostrarPoseFinal(boolean mirandoIzquierda)
    {
        mostrarImagenFija(imagenPoseFinal, mirandoIzquierda);
    }

    public void mostrarFrameLean(int frame, boolean mirandoIzquierda)
    {
        mostrarImagenFija(lean[frame], mirandoIzquierda);
    }

    /**
     * Misma implementacion de mostrarImagenFija() que existia en Jugador.
     */
    private void mostrarImagenFija(GreenfootImage original, boolean mirandoIzquierda)
    {
        GreenfootImage imagen = new GreenfootImage(original);

        if (mirandoIzquierda)
        {
            imagen.mirrorHorizontally();
        }

        jugador.setImage(imagen);
    }

    /**
     * Misma logica, frecuencia y progresion de la animacion de ataque.
     */
    public void animarAtaque(boolean mirandoIzquierda)
    {
        contadorAnimacion++;

        if (contadorAnimacion >= 6)
        {
            if (frameActual < atacar.length - 1)
            {
                frameActual++;
            }

            GreenfootImage imagen = new GreenfootImage(atacar[frameActual]);

            if (mirandoIzquierda)
            {
                imagen.mirrorHorizontally();
            }

            jugador.setImage(imagen);
            contadorAnimacion = 0;
        }
    }

    /**
     * Misma logica, frecuencia y progresion de la animacion de caminata.
     */
    public void animarCaminata(boolean izquierda)
    {
        contadorAnimacion++;

        if (contadorAnimacion >= 6)
        {
            frameActual++;

            if (frameActual >= caminar.length)
            {
                frameActual = 0;
            }

            GreenfootImage imagen = new GreenfootImage(caminar[frameActual]);

            if (izquierda)
            {
                imagen.mirrorHorizontally();
            }

            jugador.setImage(imagen);
            contadorAnimacion = 0;
        }
    }
}
