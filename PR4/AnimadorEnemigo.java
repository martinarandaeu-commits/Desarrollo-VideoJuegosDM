import greenfoot.*;

/**
 * Responsable de la representacion visual del Enemigo.
 *
 * Gestiona:
 * - sprites de caminata;
 * - sprites de tambaleo;
 * - imagen de derrota;
 * - frame actual;
 * - frecuencia de cambio de frames;
 * - espejo horizontal durante la caminata.
 *
 * No controla estados, movimiento, golpes, persecucion ni derrota.
 * Esas responsabilidades permanecen en Enemigo.
 */
public class AnimadorEnemigo
{
    private final Enemigo enemigo;

    private GreenfootImage[] caminar;
    private GreenfootImage[] tambalea;
    private GreenfootImage imagenDerrotado;

    private int frameActual = 0;
    private int contadorAnimacion = 0;

    // Mismo valor utilizado en el codigo original.
    private static final int CICLOS_POR_FRAME = 6;

    public AnimadorEnemigo(Enemigo enemigo)
    {
        this.enemigo = enemigo;

        cargarImagenes();

        // Misma imagen inicial del codigo original.
        enemigo.setImage(caminar[0]);
    }

    /**
     * Carga exactamente los mismos recursos utilizados
     * anteriormente por Enemigo.
     */
    private void cargarImagenes()
    {
        caminar = new GreenfootImage[6];

        for (int i = 0; i < 6; i++)
        {
            caminar[i] =
                new GreenfootImage(
                    "enemigo_caminar"
                    + (i + 1)
                    + ".png"
                );
        }

        tambalea = new GreenfootImage[3];

        for (int i = 0; i < 3; i++)
        {
            tambalea[i] =
                new GreenfootImage(
                    "enemigo_tambalea"
                    + (i + 1)
                    + ".png"
                );
        }

        imagenDerrotado =
            new GreenfootImage(
                "enemigo_derrotado.png"
            );
    }

    /**
     * Reinicia los mismos contadores que antes se reiniciaban
     * directamente dentro de Enemigo.recibirGolpe().
     *
     * Importante:
     * NO cambia inmediatamente la imagen mostrada.
     * Esto conserva exactamente el comportamiento original.
     */
    public void reiniciarAnimacion()
    {
        frameActual = 0;
        contadorAnimacion = 0;
    }

    /**
     * Animacion de caminata.
     *
     * Conserva:
     * - 6 ciclos por frame;
     * - 6 sprites;
     * - repeticion ciclica;
     * - espejo horizontal solo cuando direccion < 0.
     */
    public void animarCaminata(int direccion)
    {
        contadorAnimacion++;

        if (contadorAnimacion >= CICLOS_POR_FRAME)
        {
            frameActual++;

            if (frameActual >= caminar.length)
            {
                frameActual = 0;
            }

            GreenfootImage imagen =
                new GreenfootImage(
                    caminar[frameActual]
                );

            if (direccion < 0)
            {
                imagen.mirrorHorizontally();
            }

            enemigo.setImage(imagen);

            contadorAnimacion = 0;
        }
    }

    /**
     * Avanza la representacion visual del tambaleo.
     *
     * Mantiene exactamente la logica original:
     * el frame avanza hasta el ultimo y luego permanece en el.
     *
     * No decide cuando termina el estado TAMBALEANDO.
     */
    public void animarTambaleo()
    {
        contadorAnimacion++;

        if (contadorAnimacion >= CICLOS_POR_FRAME)
        {
            if (frameActual < tambalea.length - 1)
            {
                frameActual++;
            }

            enemigo.setImage(
                tambalea[frameActual]
            );

            contadorAnimacion = 0;
        }
    }

    /**
     * Muestra exactamente el mismo sprite de enemigo derrotado.
     */
    public void mostrarDerrotado()
    {
        enemigo.setImage(
            imagenDerrotado
        );
    }

    /**
     * Duracion correspondiente a una secuencia completa de
     * frames de tambaleo antes de los 12 ciclos adicionales
     * definidos por Enemigo.
     *
     * 3 frames * 6 ciclos = 18 ciclos.
     */
    public int getDuracionSecuenciaTambaleo()
    {
        return tambalea.length
            * CICLOS_POR_FRAME;
    }
}