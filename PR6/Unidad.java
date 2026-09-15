import greenfoot.*;

/**
 * Clase base para los combatientes (jugador y enemigo). Concentra todo
 * lo que ambos comparten: HP y el sistema de animaciones (idle, attack,
 * hurt, death, block). Las subclases solo cargan sus propios sprites
 * mediante cargarAnimaciones(); nunca deciden turnos ni probabilidades.
 */
public abstract class Unidad extends Actor
{
    public enum TipoAnimacion
    {
        IDLE,
        ATTACK,
        HURT,
        DEATH,
        BLOCK
    }

    private static final int TAMANO_VISUAL = 200;
    private static final int VELOCIDAD_ANIMACION = 6; // ticks de act() por frame

    // --- HP ---
    protected int hp;
    protected int hpMaximo;

    // --- Sets de sprites por animación ---
    private GreenfootImage[] framesIdle;
    private GreenfootImage[] framesAttack;
    private GreenfootImage[] framesHurt;
    private GreenfootImage[] framesDeath;
    private GreenfootImage[] framesBlock; // reservado, no se usa todavía

    // --- Estado de la animación en curso ---
    private TipoAnimacion animacionActual;
    private GreenfootImage[] framesActuales;
    private int indiceFrame;
    private int contadorTicks;
    private boolean enLoop;
    private boolean terminada;

    protected Unidad(int hpMaximo)
    {
        this.hpMaximo = hpMaximo;
        this.hp = hpMaximo;
    }

    /**
     * Carga los cinco sets de sprites de esta unidad a partir de un
     * prefijo ("player" o "enemy") y la cantidad de frames de cada
     * animación, siguiendo la convención "prefijo_animacion_NN.png".
     * Todos los frames quedan escalados al mismo tamaño (TAMANO_VISUAL).
     */
    protected void cargarAnimaciones(String prefijo, int nIdle, int nAttack, int nHurt, int nDeath, int nBlock)
    {
        framesIdle = cargarFrames(prefijo + "_idle_", nIdle);
        framesAttack = cargarFrames(prefijo + "_attack_", nAttack);
        framesHurt = cargarFrames(prefijo + "_hurt_", nHurt);
        framesDeath = cargarFrames(prefijo + "_death_", nDeath);
        framesBlock = cargarFrames(prefijo + "_block_", nBlock);
    }

    private GreenfootImage[] cargarFrames(String prefijo, int cantidad)
    {
        GreenfootImage[] frames = new GreenfootImage[cantidad];
        for (int i = 0; i < cantidad; i++)
        {
            String numero = (i + 1 < 10) ? "0" + (i + 1) : Integer.toString(i + 1);
            GreenfootImage imagen = new GreenfootImage(prefijo + numero + ".png");
            imagen.scale(TAMANO_VISUAL, TAMANO_VISUAL);
            frames[i] = imagen;
        }
        return frames;
    }

    // --- HP ---

    public void recibirDanio(int cantidad)
    {
        hp -= cantidad;
        if (hp < 0)
        {
            hp = 0;
        }
        if (hp > hpMaximo)
        {
            hp = hpMaximo;
        }
    }

    public boolean estaViva()
    {
        return hp > 0;
    }

    public int getHP()
    {
        return hp;
    }

    public int getHPMaximo()
    {
        return hpMaximo;
    }

    public double proporcionVida()
    {
        return (double) hp / hpMaximo;
    }

    // --- Animación (estrategia A: la unidad actualiza sus propios frames en act()) ---

    public void reproducirIdle()
    {
        iniciarAnimacion(TipoAnimacion.IDLE, framesIdle, true);
    }

    public void reproducirAtaque()
    {
        iniciarAnimacion(TipoAnimacion.ATTACK, framesAttack, false);
    }

    public void reproducirHerido()
    {
        iniciarAnimacion(TipoAnimacion.HURT, framesHurt, false);
    }

    public void reproducirMuerte()
    {
        iniciarAnimacion(TipoAnimacion.DEATH, framesDeath, false);
    }

    private void iniciarAnimacion(TipoAnimacion tipo, GreenfootImage[] frames, boolean loop)
    {
        animacionActual = tipo;
        framesActuales = frames;
        enLoop = loop;
        indiceFrame = 0;
        contadorTicks = 0;
        terminada = false;
        if (framesActuales != null && framesActuales.length > 0)
        {
            setImage(framesActuales[0]);
        }
    }

    protected void actualizarAnimacion()
    {
        if (framesActuales == null || framesActuales.length == 0 || terminada)
        {
            return;
        }

        contadorTicks++;
        if (contadorTicks < VELOCIDAD_ANIMACION)
        {
            return;
        }
        contadorTicks = 0;

        if (indiceFrame < framesActuales.length - 1)
        {
            indiceFrame++;
            setImage(framesActuales[indiceFrame]);
        }
        else if (enLoop)
        {
            indiceFrame = 0;
            setImage(framesActuales[indiceFrame]);
        }
        else
        {
            // ATTACK/HURT/DEATH: se queda detenida en el último frame.
            terminada = true;
        }
    }

    public boolean animacionTerminada()
    {
        return terminada;
    }

    public TipoAnimacion getAnimacionActual()
    {
        return animacionActual;
    }

    public void act()
    {
        actualizarAnimacion();
    }
}
