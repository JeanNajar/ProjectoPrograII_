package projectoprograii;

public enum Tipobarco {
    DESTRUCTOR 
            ("DT", 2, "Destructor (2)",   "DES"),
    SUBMARINO   
            ("SM", 3, "Submarino (3)",     "SUB"),
    ACORAZADO   
            ("AZ", 4, "Acorazado (4)",     "ACO"),
    PORTAAVIONES 
            ("PA", 5, "Portaaviones (5)",  "POR");

    private final String codigo;
    private final int  tamanio;
    private final String nombreCompleto;
    private final String imagenBase;

    Tipobarco(String codigo, int tamanio, String nombreCompleto, String imagenBase) {
        this.codigo= codigo;
        this.tamanio= tamanio;
        this.nombreCompleto= nombreCompleto;
        this.imagenBase= imagenBase;
    }

    public String getCodigo()   {
        return codigo; }
    public int    getTamanio(){
        return tamanio; }
    public String getNombreCompleto() {
        return nombreCompleto; }
    public String getImagenBase()   { 
        return imagenBase; }

    public static Tipobarco desdeCodigo(String codigo) {
        for (Tipobarco t : values()) {
            if (t.codigo.equals(codigo)) return t;
        }
        return DESTRUCTOR;
    }
}