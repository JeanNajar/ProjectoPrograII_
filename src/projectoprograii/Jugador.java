package projectoprograii;

public class Jugador {
    protected String username;
    protected int puntos;

    public Jugador(String username) {
        this.username = username;
        this.puntos = 0;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPuntos() {
        return puntos;
    }

    public void agregarPuntos(int cantidad) {
        this.puntos += cantidad;
    }

    @Override
    public String toString() {
        return username + " | Puntos: " + puntos;
    }
}
