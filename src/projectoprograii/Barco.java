package projectoprograii;

import java.awt.Point;
import java.util.ArrayList;

public class Barco extends Elementotablero implements Atacable {
    private final Tipobarco tipo;
    private int vidaRestante;
    private final int vidaInicial;
    private ArrayList<Point> posiciones;
    private boolean horizontal;

    public Barco(Tipobarco tipo, String id, int vidas, boolean horizontal) {
        super(id);
        this.tipo= tipo;
        this.vidaRestante= vidas;
        this.vidaInicial= vidas;
        this.posiciones = new ArrayList<>();
        this.horizontal = horizontal;
    }

    @Override
    public boolean estaActivo() {
        return vidaRestante > 0;
    }

    @Override
    public String getTipoCodigo() {
        return tipo.getCodigo();
    }

    @Override
    public void recibirDanio() {
        vidaRestante--;
    }

    @Override
    public boolean estaDestruido() {
        return vidaRestante <= 0;
    }

    @Override
    public int getVidaRestante() {
        return vidaRestante;
    }

    public boolean hundidos() {
        return vidaRestante <= 0;
    }

    public void reducirVida() {
        vidaRestante--;
    }

    public Tipobarco        getTipoBarco()   { 
        return tipo; }
    
    public int              getVidaInicial() { 
        return vidaInicial; }
    
    public ArrayList<Point> getPosiciones()  { 
        return posiciones; }
    
    public boolean          isHorizontal()   {
        return horizontal; }
    
    public void             setHorizontal(boolean h) { this.horizontal = h; }

    public void agregarPosicion(Point p) { posiciones.add(p); }
    
    public void limpiarPosiciones()      { posiciones.clear(); }
}