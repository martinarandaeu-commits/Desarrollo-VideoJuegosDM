/**
 * Write a description of class FabricaEnemigos here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import greenfoot.*;

public class FabricaEnemigos {

    public static Enemigo crear(
        int nivel
    ) {

        int numero =
            Greenfoot
                .getRandomNumber(100);

        // NIVEL 1
        if (nivel == 1) {

            if (numero < 70) {
                return new Asteroide();

            } else if (numero < 90) {
                return new CazaSimple();

            } else {
                return new CazaTriple();
            }
        }

        // NIVEL 2
        if (nivel == 2) {

            if (numero < 55) {
                return new Asteroide();

            } else if (numero < 80) {
                return new CazaSimple();

            } else if (numero < 95) {
                return new CazaTriple();

            } else {
                return new EnemigoLaser();
            }
        }

        // NIVEL 3
        if (nivel == 3) {

            if (numero < 40) {
                return new Asteroide();

            } else if (numero < 65) {
                return new CazaSimple();

            } else if (numero < 85) {
                return new CazaTriple();

            } else {
                return new EnemigoLaser();
            }
        }

        // NIVEL 4+
        if (numero < 30) {
            return new Asteroide();

        } else if (numero < 55) {
            return new CazaSimple();

        } else if (numero < 80) {
            return new CazaTriple();

        } else {
            return new EnemigoLaser();
        }
    }
}
