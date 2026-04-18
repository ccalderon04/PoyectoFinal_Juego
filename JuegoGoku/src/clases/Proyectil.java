package clases;

import implementacion.Juego;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Proyectil {

    private int x;
    private int y;        
    private int velocidad;
    private boolean activo;

    public Proyectil(int x, int y, int velocidad) {
        this.x = x;
        this.y = y;
        this.velocidad = velocidad;
        this.activo = true;
    }

    public int getX() {
    	return x; }
    
    public int getY() {
    	return y; }
    
    public boolean isActivo() {
    	return activo; }
    
    public void desactivar() {
    	this.activo = false; }

    public void mover() {
        this.y -= velocidad;
    }

    public void pintar(GraphicsContext graficos) {
        if (!activo) return;
        int screenY = this.y - Juego.camaraY;
        graficos.setFill(Color.YELLOW);
        graficos.fillOval(this.x, screenY, 10, 15);
    }

    public Rectangle obtenerRectangulo() {
        return new Rectangle(this.x, this.y, 10, 15);
    }
}








