package clases;

import implementacion.Juego;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

public class Tile {

    private int x;         
    private int y;          
    private int anchoImagen;
    private int altoImagen;
    private int xImagen;  
    private int yImagen;
    private String indiceImagen;
    private int velocidad;

    public Tile(int x, int y, int anchoImagen, int altoImagen,
                int xImagen, int yImagen, String indiceImagen, int velocidad) {
        this.x = x;
        this.y = y;
        this.altoImagen  = altoImagen;
        this.anchoImagen = anchoImagen;
        this.xImagen     = xImagen;
        this.yImagen     = yImagen;
        this.indiceImagen = indiceImagen;
        this.velocidad   = velocidad;
    }

    public Tile(int tipoTile, int x, int y, String indiceImagen, int velocidad) {
        this.x = x;
        this.y = y;
        this.indiceImagen = indiceImagen;
        this.velocidad    = velocidad;
        switch (tipoTile) {
            case 1:
                this.anchoImagen = 70;
                this.altoImagen = 70;
                this.xImagen = 0;
                this.yImagen = 0;
            break;
            case 2:
                this.anchoImagen = 70;
                this.altoImagen = 70;
                this.xImagen = 0;
                this.yImagen = 70;
            break;
            case 3:
                this.anchoImagen = 70;
                this.altoImagen = 70;
                this.xImagen = 0;
                this.yImagen = 140;
            break;
            case 4:
                this.anchoImagen = 70;
                this.altoImagen = 70;
                this.xImagen = 490;
                this.yImagen = 558;
            break;
            case 5:
                this.anchoImagen = 70;
                this.altoImagen = 70;
                this.xImagen = 560;
                this.yImagen = 558;
            break;
            case 6:
                this.anchoImagen = 70;
                this.altoImagen = 70;
                this.xImagen = 560;
                this.yImagen = 698;
            break;
            case 666:
                this.anchoImagen = 70;
                this.altoImagen = 70;
                this.xImagen = 70;
                this.yImagen = 558;
            break;
        }
    }

    
    public int getX() {
    	return x; }
    
    public void setX(int x) {
    	this.x = x; }
    
    public int getY() {
    	return y; }
    
    public void setY(int y) {
    	this.y = y; }
    
    public int getAltoImagen()  {
    	return altoImagen; }
    
    public int getAnchoImagen() {
    	return anchoImagen; }
    
    public int getxImagen()     {
    	return xImagen; }
    
    public int getyImagen()     {
    	return yImagen; }
    
    public String getIndiceImagen() {
    	return indiceImagen; }
    
    public int getVelocidad()   {
    	return velocidad; }

    public void pintar(GraphicsContext graficos) {
        int screenY = this.y - Juego.camaraY;
        if (screenY > -70 && screenY < 500) {
            graficos.drawImage(
                Juego.imagenes.get(this.indiceImagen),
                this.xImagen, this.yImagen,
                this.anchoImagen, this.altoImagen,
                this.x, screenY,
                this.anchoImagen, this.altoImagen
            );
        }
    }

    
    public Rectangle obtenerRectangulo() {
        return new Rectangle(this.x, this.y, this.anchoImagen, this.altoImagen);
    }
}






