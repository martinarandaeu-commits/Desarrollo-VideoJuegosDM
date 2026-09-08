import greenfoot.*;

/**
 * FabricaImagenes
 * ----------------
 * Responsabilidad unica: cargar cada imagen UNA sola vez desde la carpeta
 * "images" y entregar esa misma instancia a quien la pida.
 *
 * Driver relacionado: Memoria.
 *   Cada GreenfootImage cargada desde disco ocupa memoria. Si cada Soldado
 *   creara su propia copia de "zombie.png" tendriamos decenas de copias
 *   identicas en memoria durante una oleada grande.
 *
 * Decision de diseno:
 *   crear una vez -> reutilizar muchas veces.
 *   Esto es seguro porque Jugador/SoldadoEnemigo/Bala solo llaman a
 *   setRotation(...) sobre el Actor (un atributo del actor, no de la
 *   imagen). Nunca mutan la GreenfootImage compartida (no la escalan,
 *   ni la voltean, ni dibujan sobre ella), por lo que puede compartirse
 *   sin efectos secundarios entre todos los actores del mismo tipo.
 */
public class FabricaImagenes
{
    private static final GreenfootImage JUGADOR = new GreenfootImage("jugador.png");
    private static final GreenfootImage ZOMBIE = new GreenfootImage("zombie.png");
    private static final GreenfootImage BALA = new GreenfootImage("bala.png");

    private static final GreenfootImage SLOT_PISTOLA = new GreenfootImage("slot_pistola.png");
    private static final GreenfootImage SLOT_ESCOPETA = new GreenfootImage("slot_escopeta.png");
    private static final GreenfootImage SLOT_VIDA = new GreenfootImage("slot_vida.png");

    public static GreenfootImage jugador()
    {
        return JUGADOR;
    }

    public static GreenfootImage zombie()
    {
        return ZOMBIE;
    }

    public static GreenfootImage bala()
    {
        return BALA;
    }

    public static GreenfootImage slotPistola()
    {
        return SLOT_PISTOLA;
    }

    public static GreenfootImage slotEscopeta()
    {
        return SLOT_ESCOPETA;
    }

    public static GreenfootImage slotVida()
    {
        return SLOT_VIDA;
    }
}
