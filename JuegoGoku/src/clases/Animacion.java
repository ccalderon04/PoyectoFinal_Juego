package clases;

import javafx.scene.shape.Rectangle;
public class Animacion {

    private String nombreAnimacion;
    private Rectangle coordenadasImagenes[];  
    private int cantidadFrames;
    private int frameActual = 0;
    private double duracion; 

    public Animacion(String nombreAnimacion, Rectangle[] coordenadasImagenes, double duracion) {
        this.nombreAnimacion      = nombreAnimacion;
        this.coordenadasImagenes  = coordenadasImagenes;
        this.duracion             = duracion;
    }

    public String getNombreAnimacion() {
    	return nombreAnimacion; }
    
    public void setNombreAnimacion(String n) {
    	this.nombreAnimacion = n; }

    public Rectangle[] getCoordenadasImagenes() {
    	return coordenadasImagenes; }
    
    public void setCoordenadasImagenes(Rectangle[] r) {
    	this.coordenadasImagenes = r; }

    public int getCantidadFrames() {
    	return cantidadFrames; }
    
    public void setCantidadFrames(int n) {
    	this.cantidadFrames = n; }

    public int getFrameActual() {
    	return frameActual; }
    
    public void setFrameActual(int n) {
    	this.frameActual = n; }

    public double getDuracion() {
    	return duracion; }
    
    public void setDuracion(double d) {
    	this.duracion = d; }

   
    public Rectangle calcularFrame(double t) {
        this.cantidadFrames = coordenadasImagenes.length;
        this.frameActual = (int) ((t % (cantidadFrames * duracion)) / duracion);
        return coordenadasImagenes[frameActual];
    }
}


