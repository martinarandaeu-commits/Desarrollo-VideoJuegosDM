/**
 * Representa una conexion por escalera entre dos pisos del escenario.
 *
 * Cada escalera conoce:
 * - el piso superior;
 * - el piso inferior;
 * - la coordenada X donde termina en el piso superior;
 * - la coordenada X donde termina en el piso inferior.
 *
 * Es un objeto de datos inmutable: una vez creada la escalera,
 * sus coordenadas y pisos no cambian.
 */
public final class Escalera
{
    private final int pisoAlto;
    private final int pisoBajo;

    private final int xAlto;
    private final int xBajo;

    public Escalera(
        int pisoAlto,
        int pisoBajo,
        int xAlto,
        int xBajo)
    {
        this.pisoAlto = pisoAlto;
        this.pisoBajo = pisoBajo;
        this.xAlto = xAlto;
        this.xBajo = xBajo;
    }

    public int getPisoAlto()
    {
        return pisoAlto;
    }

    public int getPisoBajo()
    {
        return pisoBajo;
    }

    public int getXAlto()
    {
        return xAlto;
    }

    public int getXBajo()
    {
        return xBajo;
    }
}
