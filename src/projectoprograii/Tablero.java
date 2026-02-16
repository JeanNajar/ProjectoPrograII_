package projectoprograii;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Tablero {
    private static final int TAMANIO       = 8;
    private static final int TAMANIO_CELDA = 50;

    JButton[][] botones = new JButton[TAMANIO][TAMANIO];
    JPanel  panel;
    JLabel  labelInfo;

    private String[][] tablero = new String[TAMANIO][TAMANIO];

    private ArrayList<Barco> listaBarcosEnUso;
    private boolean modoOculto = false;
    private String  nombreJugador;
    private Random  random;
    private Color   colorBordeActual = Color.WHITE;
    private Color   colorAgua        = new Color(0x5B9BD5);

    private int contadorIds = 0;

    private static final Map<String, ImageIcon[]> cacheImagenes = new HashMap<>();


    private static final Map<String, String> IMAGEN_POR_TIPO = new HashMap<>();
    static {
        IMAGEN_POR_TIPO.put("DT", "DES");
        IMAGEN_POR_TIPO.put("SM", "SUB");
        IMAGEN_POR_TIPO.put("AZ", "ACO");
        IMAGEN_POR_TIPO.put("PA", "POR");
    }

    public static class Barco {
        String tipo;
        String id;
        int vidaRestante;
        int vidaInicial;
        ArrayList<Point> posiciones;
        boolean horizontal;

        public Barco(String tipo, String id, int vidas, boolean horizontal) {
            this.tipo         = tipo;
            this.id           = id;
            this.vidaRestante = vidas;
            this.vidaInicial  = vidas;
            this.posiciones   = new ArrayList<>();
            this.horizontal   = horizontal;
        }

        public boolean hundidos() { return vidaRestante <= 0; }

        public String getNombre() {
            switch (tipo) {
                case "PA": return "Portaaviones";
                case "AZ": return "Acorazado";
                case "SM": return "Submarino";
                case "DT": return "Destructor";
                default:   return "Barco";
            }
        }
    }

    public Tablero(int x, int y, String nombreJugador) {
        this.nombreJugador    = nombreJugador;
        this.listaBarcosEnUso = new ArrayList<>();
        this.random           = new Random();

        panel = new JPanel(null);
        panel.setBounds(x, y, 400, 400 + 40);
        panel.setOpaque(false);

        labelInfo = new JLabel(nombreJugador, SwingConstants.CENTER);
        labelInfo.setBounds(0, 0, TAMANIO_CELDA * TAMANIO, 30);
        labelInfo.setFont(new Font("Arial", Font.BOLD, 16));
        labelInfo.setForeground(Color.WHITE);
        labelInfo.setOpaque(true);
        labelInfo.setBackground(new Color(44, 62, 80, 200));

        panel.add(labelInfo);
        inicializarTablero();
        crearBotonesTablero();
    }

    private void inicializarTablero() {
        for (int f = 0; f < TAMANIO; f++)
            for (int c = 0; c < TAMANIO; c++)
                tablero[f][c] = null;
    }

    private void crearBotonesTablero() {
        for (int f = 0; f < TAMANIO; f++) {
            for (int c = 0; c < TAMANIO; c++) {
                JButton btn = new JButton();
                btn.setBounds(c * TAMANIO_CELDA, f * TAMANIO_CELDA + 40, TAMANIO_CELDA, TAMANIO_CELDA);
                btn.setFocusPainted(false);
                btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
                btn.setFont(new Font("Arial", Font.BOLD, 10));
               
                btn.setMargin(new Insets(0, 0, 0, 0));
                botones[f][c] = btn;
                panel.add(btn);
            }
        }
    }


    private ImageIcon[] cargarSegmentos(String tipo, int tamanio, boolean horizontal) {
        String nombreArchivo = IMAGEN_POR_TIPO.get(tipo);
        if (nombreArchivo == null) return null;

  
        String clave = nombreArchivo + (horizontal ? "_H" : "_V");
        if (cacheImagenes.containsKey(clave)) {
            return cacheImagenes.get(clave);
        }

        try {
            URL url = getClass().getResource("/imagen/" + nombreArchivo + ".png");
            if (url == null) return null;

            ImageIcon iconOriginal = new ImageIcon(url);
            Image imgOriginal = iconOriginal.getImage();

        
   
            int anchoFinal = tamanio * TAMANIO_CELDA;
            int altoFinal  = TAMANIO_CELDA;

            BufferedImage imgHoriz = new BufferedImage(anchoFinal, altoFinal, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = imgHoriz.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(imgOriginal, 0, 0, anchoFinal, altoFinal, null);
            g2.dispose();

            ImageIcon[] segmentos;

            if (horizontal) {
  
                segmentos = new ImageIcon[tamanio];
                for (int i = 0; i < tamanio; i++) {
                    BufferedImage segmento = imgHoriz.getSubimage(i * TAMANIO_CELDA, 0, TAMANIO_CELDA, TAMANIO_CELDA);
                    segmentos[i] = new ImageIcon(segmento);
                }
            } else {
 
                BufferedImage imgVert = new BufferedImage(altoFinal, anchoFinal, BufferedImage.TYPE_INT_ARGB);
                Graphics2D gv = imgVert.createGraphics();
                gv.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                gv.translate(altoFinal, 0);
                gv.rotate(Math.PI / 2);
                gv.drawImage(imgHoriz, 0, 0, null);
                gv.dispose();

       
                segmentos = new ImageIcon[tamanio];
                for (int i = 0; i < tamanio; i++) {
                    BufferedImage segmento = imgVert.getSubimage(0, i * TAMANIO_CELDA, TAMANIO_CELDA, TAMANIO_CELDA);
                    segmentos[i] = new ImageIcon(segmento);
                }
            }

            cacheImagenes.put(clave, segmentos);
            return segmentos;

        } catch (Exception e) {
            return null;
        }
    }

    private void actualizarColorCelda(int f, int c) {
        String celda = tablero[f][c];


        botones[f][c].setIcon(null);
        botones[f][c].setHorizontalAlignment(SwingConstants.CENTER);

        if (celda == null) {
            botones[f][c].setBackground(colorAgua);
            botones[f][c].setText("");
        } else if (celda.equals("T")) {
            botones[f][c].setBackground(new Color(255, 165, 0));
            botones[f][c].setText("");
        } else if (celda.equals("X")) {
            botones[f][c].setBackground(Color.RED);
            botones[f][c].setText("");
        } else if (celda.equals("F")) {
            botones[f][c].setBackground(Color.GRAY);
            botones[f][c].setText("");
        } else {
            String tipo = extraerTipo(celda);
            if (modoOculto) {
                botones[f][c].setBackground(colorAgua);
                botones[f][c].setText("");
            } else {

                Barco barco = buscarBarcoPorId(celda);
                if (barco != null) {
                    ImageIcon[] segs = cargarSegmentos(tipo, barco.vidaInicial, barco.horizontal);
                    if (segs != null) {
                        int segIdx = barco.horizontal
                            ? c - barco.posiciones.get(0).y
                            : f - barco.posiciones.get(0).x;
                        if (segIdx >= 0 && segIdx < segs.length) {
                            botones[f][c].setBackground(colorAgua);
                            botones[f][c].setText("");
                            botones[f][c].setIcon(segs[segIdx]);
                        } else {
                            botones[f][c].setBackground(Color.DARK_GRAY);
                            botones[f][c].setText(tipo);
                        }
                    } else {
                        botones[f][c].setBackground(Color.DARK_GRAY);
                        botones[f][c].setText(tipo);
                    }
                } else {
                    botones[f][c].setBackground(Color.DARK_GRAY);
                    botones[f][c].setText(tipo);
                }
            }
        }

        botones[f][c].setBorder(BorderFactory.createLineBorder(colorBordeActual, 2));
    }


    private Barco buscarBarcoPorId(String id) {
        for (Barco b : listaBarcosEnUso) {
            if (b.id.equals(id)) return b;
        }
        return null;
    }

    private String extraerTipo(String id) {
        if (id == null || id.length() < 2) return id;
        return id.substring(0, 2);
    }

    public void actualizarTableroCompleto() {
        for (int f = 0; f < TAMANIO; f++)
            for (int c = 0; c < TAMANIO; c++)
                actualizarColorCelda(f, c);
    }

    public boolean puedeColocar(int f, int c, int tamanio, boolean horizontal) {
        for (int i = 0; i < tamanio; i++) {
            int nf = horizontal ? f     : f + i;
            int nc = horizontal ? c + i : c;
            if (nf >= TAMANIO || nc >= TAMANIO || nf < 0 || nc < 0) return false;
            if (tablero[nf][nc] != null) return false;
        }
        return true;
    }

    public void colocarBarco(int f, int c, int tamanio, boolean horizontal, String tipo) {
        String idUnico = tipo + contadorIds;
        contadorIds++;

        Barco nuevoBarco = new Barco(tipo, idUnico, tamanio, horizontal);

        for (int i = 0; i < tamanio; i++) {
            int nf = horizontal ? f     : f + i;
            int nc = horizontal ? c + i : c;
            tablero[nf][nc] = idUnico;
            nuevoBarco.posiciones.add(new Point(nf, nc));
        }

        listaBarcosEnUso.add(nuevoBarco);

        for (int i = 0; i < tamanio; i++) {
            int nf = horizontal ? f     : f + i;
            int nc = horizontal ? c + i : c;
            actualizarColorCelda(nf, nc);
        }
    }

    public int atacar(int f, int c) {
        String celda = tablero[f][c];

        if (celda == null || celda.equals("F")) {
            tablero[f][c] = "F";
            actualizarColorCelda(f, c);
            return 0;
        } else if (celda.equals("X") || celda.equals("T")) {
            return 0;
        } else {
            for (Barco barco : listaBarcosEnUso) {
                if (barco.id.equals(celda) && !barco.hundidos()) {
                    barco.vidaRestante--;

                    if (barco.hundidos()) {
                        liberarPosicionesBarcoHundido(barco);
                        return 2;
                    } else {
                        tablero[f][c] = null;
                        actualizarColorCelda(f, c);
                        return 1;
                    }
                }
            }
            return 1;
        }
    }

    private void liberarPosicionesBarcoHundido(Barco barco) {
        for (Point pos : barco.posiciones) {
            tablero[pos.x][pos.y] = null;
            botones[pos.x][pos.y].setEnabled(true);
            botones[pos.x][pos.y].setIcon(null);
            botones[pos.x][pos.y].setBackground(new Color(139, 0, 0));
            botones[pos.x][pos.y].setText("");
            botones[pos.x][pos.y].setForeground(Color.WHITE);
            botones[pos.x][pos.y].setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        }
    }

    public void regenerarTablero() {
        ArrayList<Barco> barcosParaRegenerar = new ArrayList<>();
        for (Barco barco : listaBarcosEnUso)
            if (!barco.hundidos()) barcosParaRegenerar.add(barco);

        for (int f = 0; f < TAMANIO; f++)
            for (int c = 0; c < TAMANIO; c++)
                if (tablero[f][c] != null) tablero[f][c] = null;

        for (Barco barco : barcosParaRegenerar) {
            boolean colocado = false;
            int intentos     = 0;
            int tam          = barco.vidaInicial;

            while (!colocado && intentos < 200) {
                int     nf   = random.nextInt(TAMANIO);
                int     nc   = random.nextInt(TAMANIO);
                boolean horiz = random.nextBoolean();

                if (puedeColocar(nf, nc, tam, horiz)) {
                    barco.posiciones.clear();
                    barco.horizontal = horiz;
                    for (int i = 0; i < tam; i++) {
                        int rf = horiz ? nf     : nf + i;
                        int rc = horiz ? nc + i : nc;
                        tablero[rf][rc] = barco.id;
                        barco.posiciones.add(new Point(rf, rc));
                    }
                    colocado = true;
                }
                intentos++;
            }
        }

        actualizarTableroCompleto();
    }

    public boolean tieneBarcosSinHundir() {
        for (Barco b : listaBarcosEnUso) if (!b.hundidos()) return true;
        return false;
    }

    public void limpiarFallos() {
        for (int f = 0; f < TAMANIO; f++) {
            for (int c = 0; c < TAMANIO; c++) {
                if ("F".equals(tablero[f][c])) {
                    tablero[f][c] = null;
                    actualizarColorCelda(f, c);
                }
            }
        }
    }

    public boolean todosHundidos() {
        for (Barco b : listaBarcosEnUso) if (!b.hundidos()) return false;
        return true;
    }

    public int getBarcosRestantes() {
        int count = 0;
        for (Barco b : listaBarcosEnUso) if (!b.hundidos()) count++;
        return count;
    }

    public void resaltarColocacion(int f, int c, int tamanio, boolean horizontal, boolean valido) {
        Color color = valido ? new Color(46, 204, 110, 100) : new Color(220, 76, 60, 100);
        for (int i = 0; i < tamanio; i++) {
            int nf = horizontal ? f     : f + i;
            int nc = horizontal ? c + i : c;
            if (nf < TAMANIO && nc < TAMANIO && nf >= 0 && nc >= 0)
                botones[nf][nc].setBackground(color);
        }
    }

    public void setColorBordeTablero(Color color) {
        this.colorBordeActual = color;
        for (int f = 0; f < TAMANIO; f++)
            for (int c = 0; c < TAMANIO; c++)
                botones[f][c].setBorder(BorderFactory.createLineBorder(color, 2));
    }

    public void setColorAgua(Color color) {
        this.colorAgua = color;
        actualizarTableroCompleto();
    }

    public void resetColorBordeTablero() { setColorBordeTablero(Color.WHITE); }

    public JPanel  getPanel()             { return panel; }
    public JButton getBoton(int f, int c) { return botones[f][c]; }

    public void setModoOculto(boolean oculto) {
        this.modoOculto = oculto;
        actualizarTableroCompleto();
    }

    public boolean getModoOculto()          { return modoOculto; }
    public void    actualizarInfo(String t) { labelInfo.setText(t); }
    public String  getNombreJugador()       { return nombreJugador; }
    public ArrayList<Barco> getListaBarcos(){ return listaBarcosEnUso; }
}