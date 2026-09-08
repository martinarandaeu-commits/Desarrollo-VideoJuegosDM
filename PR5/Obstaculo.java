/**
 * Obstaculo
 * ---------
 * Representa, como un simple rectangulo, una zona solida del mapa
 * (un granero, la casa, el molino, el tractor...) por la que ningun
 * Soldado puede caminar.
 *
 * Driver: Simplicidad.
 *   El mapa completo es una sola imagen de fondo (mapa.png) en vez de
 *   un mosaico de tiles: por eso no existe "colision contra el tile
 *   correcto", sino contra un puñado de rectangulos conocidos de
 *   antemano. Es una aproximacion (no sigue el contorno exacto del
 *   dibujo) pero es mas que suficiente para un prototipo y es facil
 *   de entender y de ajustar.
 */
public class Obstaculo
{
    private final int x0, y0, x1, y1;

    public Obstaculo(int x0, int y0, int x1, int y1)
    {
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
    }

    public boolean contiene(int x, int y)
    {
        return x >= x0 && x <= x1 && y >= y0 && y <= y1;
    }
}
