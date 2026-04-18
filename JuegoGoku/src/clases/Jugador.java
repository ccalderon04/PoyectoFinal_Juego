package clases;

import implementacion.Juego;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

public class Jugador {

    protected int x;
    protected int y;
    protected String indiceImagen;
    protected int velocidad;

    public Jugador(int x, int y, String indiceImagen, int velocidad) {
        this.x = x;
        this.y = y;
        this.indiceImagen = indiceImagen;
        this.velocidad = velocidad;
    }

   
    public int getX() {
    	return x; }
    public void setX(int x) {
    	this.x = x; }

    public int getY() {
    	return y; }
    
    public void setY(int y) {
    	this.y = y; }

    public String getIndiceImagen() {
    	return indiceImagen; }
    
    public void setIndiceImagen(String indiceImagen) {
    	this.indiceImagen = indiceImagen; }

    public int getVelocidad() {
    	return velocidad; }
    
    public void setVelocidad(int velocidad) {
    	this.velocidad = velocidad; }

    
    public void mover() {
        if (Juego.derecha)   this.x += velocidad;
        if (Juego.izquierda) this.x -= velocidad;
        if (Juego.arriba)    this.y -= velocidad;
        if (Juego.abajo)     this.y += velocidad;
    }

    
    public void pintar(GraphicsContext graficos) {
        graficos.drawImage(
            Juego.imagenes.get(this.indiceImagen),
            this.x,
            this.y - Juego.camaraY
        );
    }

    public Rectangle obtenerRectangulo() {
        return new Rectangle(this.x, this.y, 50, 50);
    }
}
