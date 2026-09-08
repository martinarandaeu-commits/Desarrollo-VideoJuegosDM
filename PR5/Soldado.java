import greenfoot.*;

/**
 * Soldado (clase base abstracta)
 * -------------------------------
 * Representa lo que Jugador y SoldadoEnemigo tienen en comun: vida,
 * velocidad, la forma de recibir danio/morir, y la forma de girar el
 * sprite para que "mire" hacia donde se mueve.
 *
 * Driver relacionado: Mantenibilidad.
 *   Si "vida", "recibirDanio" o la rotacion se repitieran en Jugador
 *   y en SoldadoEnemigo por separado, cualquier cambio futuro (por
 *   ejemplo agregar armadura, o cambiar la formula de rotacion)
 *   tendria que aplicarse dos veces y podria quedar inconsistente.
 *   Por eso ese comportamiento comun vive aqui una sola vez.
 */
public abstract class Soldado extends Actor
{
    protected int vida;
    protected int vidaMaxima;
    protected int velocidad;

    public Soldado(int vida, int velocidad)
    {
        this.vida = vida;
        this.vidaMaxima = vida;
        this.velocidad = velocidad;
    }

    public void recibirDanio(int cantidad)
    {
        vida -= cantidad;
        if (vida <= 0)
        {
            vida = 0;
            morir();
        }
    }

    public int getVida()
    {
        return vida;
    }

    public int getVidaMaxima()
    {
        return vidaMaxima;
    }

    /**
     * Elimina al actor del mundo. Jugador y SoldadoEnemigo la sobrescriben
     * para avisarle al mundo lo que corresponda (fin del juego, dinero, etc.)
     * pero ambos terminan llamando a este comportamiento base.
     */
    protected void morir()
    {
        World mundo = getWorld();
        if (mundo != null)
        {
            mundo.removeObject(this);
        }
    }

    /**
     * Gira el sprite para que la cabeza (que en las imagenes originales
     * apunta "hacia arriba") quede mirando en la direccion (dx, dy).
     *
     * Greenfoot mide la rotacion en grados, en sentido horario, donde
     * 0 grados es "mirando a la derecha". Como nuestras imagenes en
     * reposo apuntan hacia arriba (-90 grados en esa convencion), se
     * suma 90 para compensar ese desfase.
     */
    protected void mirarHacia(double dx, double dy)
    {
        if (dx == 0 && dy == 0)
        {
            return;
        }
        double angulo = Math.toDegrees(Math.atan2(dy, dx)) + 90.0;
        setRotation((int) Math.round(angulo));
    }

    /**
     * Intenta mover al actor a (nuevoX, nuevoY). Si esa posicion cae
     * dentro de un obstaculo del mapa (granero, casa, molino, etc.) el
     * movimiento se ignora, de lo contrario se aplica.
     *
     * Centralizar esto aqui evita que Jugador y SoldadoEnemigo dupliquen
     * la logica de "preguntarle al mundo si ahi se puede caminar".
     */
    protected void moverA(int nuevoX, int nuevoY)
    {
        World mundo = getWorld();
        if (mundo instanceof JuegoWorld && ((JuegoWorld) mundo).esObstaculo(nuevoX, nuevoY))
        {
            return;
        }
        setLocation(nuevoX, nuevoY);
    }
}
