/**
 * Write a description of class DisparoSimple here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import greenfoot.*;

public class DisparoSimple implements EstrategiaDisparo {

    public void disparar(Actor nave, PoolDeBalas pool) {
        Bala b = pool.obtener();

        if (b != null) {
            b.activar(
                nave.getWorld(),
                nave.getX() + 20,
                nave.getY(),
                0
            );
        }
    }
}
