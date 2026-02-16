package projectoprograii;

public abstract class Elementotablero {
    public String id;

    public Elementotablero(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public abstract boolean estaActivo();
    public abstract String getTipoCodigo();
}