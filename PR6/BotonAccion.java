import greenfoot.*;

/**
 * Botón interactivo que representa una AccionCombate del jugador.
 * Muestra nombre, daño y porcentaje de acierto para que el jugador
 * decida antes de hacer clic. Nunca calcula daño ni decide si el
 * ataque acierta: solo avisa a GestorCombate.seleccionarAccion(accion)
 * cuando está habilitado y se hace clic sobre él.
 */
public class BotonAccion extends Actor
{
    private static final int ANCHO = 220;
    private static final int ALTO = 70;

    private AccionCombate accion;
    private GestorCombate gestor;
    private boolean habilitado;

    public BotonAccion(AccionCombate accion, GestorCombate gestor)
    {
        this.accion = accion;
        this.gestor = gestor;
        this.habilitado = true;
        dibujar();
    }

    public void setHabilitado(boolean habilitado)
    {
        this.habilitado = habilitado;
        dibujar();
    }

    public boolean isHabilitado()
    {
        return habilitado;
    }

    private void dibujar()
    {
        GreenfootImage imagen = new GreenfootImage(ANCHO, ALTO);

        Color fondo = habilitado ? new Color(35, 35, 55, 235) : new Color(55, 55, 55, 140);
        Color borde = habilitado ? new Color(255, 205, 80) : new Color(110, 110, 110);
        Color texto = habilitado ? Color.WHITE : new Color(170, 170, 170);

        imagen.setColor(fondo);
        imagen.fillRect(0, 0, ANCHO, ALTO);
        imagen.setColor(borde);
        imagen.drawRect(0, 0, ANCHO - 1, ALTO - 1);
        imagen.drawRect(1, 1, ANCHO - 3, ALTO - 3);

        imagen.setFont(new Font("Arial", true, false, 15));
        imagen.setColor(texto);
        imagen.drawString(accion.getNombre().toUpperCase(), 12, 28);

        imagen.setFont(new Font("Arial", false, false, 13));
        imagen.drawString(accion.getDanio() + " daño | " + accion.getProbabilidadAcierto() + "%", 12, 50);

        setImage(imagen);
    }

    public void act()
    {
        if (habilitado && Greenfoot.mouseClicked(this))
        {
            gestor.seleccionarAccion(accion);
        }
    }
}
