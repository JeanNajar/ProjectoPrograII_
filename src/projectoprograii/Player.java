package projectoprograii;

public class Player {

    private String username;
    private String password;
    private int puntos;

    private String[] logsPartidas = new String[10];
    private int totalLogs = 0;

    public Player(String username, String password) {
        this.username = username;
        this.password = password;
        this.puntos = 0;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getPuntos() { return puntos; }

    public void agregarPuntos(int cantidad) {
        this.puntos += cantidad;
    }


    public void agregarLog(String log) {
        for (int i = logsPartidas.length - 1; i > 0; i--) {
            logsPartidas[i] = logsPartidas[i - 1];
        }
        logsPartidas[0] = log;
        if (totalLogs < logsPartidas.length) {
            totalLogs++;
        }
    }

    public String[] getLogs() {
        return logsPartidas;
    }

    public int getTotalLogs() {
        return totalLogs;
    }

    @Override
    public String toString() {
        return username + " | Puntos: " + puntos;
    }
}
