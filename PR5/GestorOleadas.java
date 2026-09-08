import greenfoot.*;

/**
 * GestorOleadas
 * -------------
 * Decide cuantos zombis existen, con que atributos, y cuando aparece
 * la siguiente oleada. Es la misma logica de la guia de arquitectura
 * del rogue-like (drivers de Memoria/Rendimiento/Escalabilidad de
 * dificultad), adaptada a este mundo de 1400x800:
 *
 *   enemigos totales de la oleada  !=  enemigos simultaneos
 *
 * Una oleada puede tener hasta 80 zombis en total, pero nunca hay mas
 * de 35 activos en pantalla al mismo tiempo (Ne <= 35). Esto evita que
 * el juego se llene de actores sin control y se vuelva lento o
 * consuma memoria de forma descontrolada.
 */
public class GestorOleadas
{
    private final JuegoWorld mundo;
    private final Jugador jugador;

    private int oleada = 1;
    private int generados = 0;
    private int objetivoOleada = 0;
    private int contadorSpawn = 0;
    private int esperaEntreOleadas = 0;

    public GestorOleadas(JuegoWorld mundo, Jugador jugador)
    {
        this.mundo = mundo;
        this.jugador = jugador;
        prepararOleada();
    }

    public void actualizar()
    {
        if (jugador.getWorld() == null)
        {
            return;
        }

        int activos = mundo.getObjects(SoldadoEnemigo.class).size();

        if (generados < objetivoOleada)
        {
            contadorSpawn--;
            if (contadorSpawn <= 0 && activos < maxSimultaneos())
            {
                crearEnemigo();
                generados++;
                contadorSpawn = intervaloSpawn();
            }
        }
        else if (activos == 0)
        {
            if (esperaEntreOleadas == 0)
            {
                esperaEntreOleadas = 90;
            }
            esperaEntreOleadas--;
            if (esperaEntreOleadas <= 0)
            {
                oleada++;
                prepararOleada();
            }
        }
    }

    private void prepararOleada()
    {
        generados = 0;
        objetivoOleada = Math.min(5 + oleada * 3, 80);
        contadorSpawn = 20;
        esperaEntreOleadas = 0;
    }

    private int maxSimultaneos()
    {
        return Math.min(6 + oleada * 2, 35);
    }

    private int intervaloSpawn()
    {
        return Math.max(12, 50 - oleada * 3);
    }

    private int vidaEnemigo()
    {
        return 20 + oleada * 4;
    }

    private int velocidadEnemigo()
    {
        return Math.min(2 + oleada / 3, 6);
    }

    private int danioEnemigo()
    {
        return Math.min(4 + oleada, 15);
    }

    private void crearEnemigo()
    {
        SoldadoEnemigo enemigo = new SoldadoEnemigo(
            jugador, vidaEnemigo(), velocidadEnemigo(), danioEnemigo());

        int lado = Greenfoot.getRandomNumber(4);
        int x;
        int y;

        int altoJuego = JuegoWorld.ALTO_JUEGO;

        if (lado == 0)
        {
            x = 12;
            y = Greenfoot.getRandomNumber(altoJuego);
        }
        else if (lado == 1)
        {
            x = mundo.getWidth() - 12;
            y = Greenfoot.getRandomNumber(altoJuego);
        }
        else if (lado == 2)
        {
            // Hueco libre de obstaculos entre el granero grande y el
            // cultivo cercado (los otros tramos del borde superior
            // quedan ocupados por los graneros o el cultivo).
            x = 550 + Greenfoot.getRandomNumber(200);
            y = 12;
        }
        else
        {
            x = Greenfoot.getRandomNumber(mundo.getWidth());
            y = altoJuego - 12;
        }

        mundo.addObject(enemigo, x, y);
    }

    public int getOleada()
    {
        return oleada;
    }
}
