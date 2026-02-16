package projectoprograii;

public class Player extends Jugador {
    private String password;
    private String[] logsPartidas = new String[10];
    private int totalLogs = 0;

    public Player(String username, String password) {
        super(username);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void agregarLog(String log) {
        for (int i = logsPartidas.length - 1; i > 0; i--) {
            logsPartidas[i] = logsPartidas[i - 1];
        }
        logsPartidas[0] = log;
        if (totalLogs < logsPartidas.length) totalLogs++;
    }

    public String[] getLogs() {
        return logsPartidas;
    }

    public int getTotalLogs() {
        return totalLogs;
    }
}