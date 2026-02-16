package projectoprograii;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BattleShipGUI {


    private static final String[] TIPOS_BASE    = {"DT", "SM", "AZ", "PA"};
    private static final int[]    TAMANIOS_BASE = {2, 3, 4, 5};
    private static final String[] NOMBRES_BASE  = {
        "Destructor (2)", "Submarino (3)", "Acorazado (4)", "Portaaviones (5)"
    };


    private String[] tiposBarcos;
    private int[]    tamaniosBarcos;
    private String[] nombresBarcos;

    JFrame frame;
    JLabel fondo;
    JLayeredPane capas;

    Tablero tableroP1;
    Tablero tableroP2;

    JPanel panelControl;
    JPanel panelInfo;
    JLabel labelEstado;
    JLabel labelBarcoActual;
    JLabel labelBarcosP1;
    JLabel labelBarcosP2;
    JProgressBar progressBarco;

    JButton btnRotar;
    JButton btnRendirse;
    JButton btnReiniciar;
    JButton btnMenu;

 
    private Player player1;
    private Player player2;

    private boolean turnoPlayer1    = true;
    private boolean faseColocacion  = true;
    private boolean colocandoPlayer1 = true;

    private int     barcoActualIndex = 0;
    private boolean horizontal       = true;
    private int[]   barcosColocadosP1;
    private int[]   barcosColocadosP2;

    private String dificultad = "NORMAL";
    private String modoJuego  = "TUTORIAL";

    private Menuinicio menuReferencia;

    public BattleShipGUI(Player p1, Player p2, String dificultad, String modo, Menuinicio menu) {
        this.player1       = p1;
        this.player2       = p2;
        this.dificultad    = dificultad;
        this.modoJuego     = modo;
        this.menuReferencia = menu;

        construirListaBarcos();

        barcosColocadosP1 = new int[tiposBarcos.length];
        barcosColocadosP2 = new int[tiposBarcos.length];

        inicializarFrame();
        crearTableros();
        crearPanelControl();
        crearPanelInfo();
        configurarEventos();
        actualizarEstado();
    }


    private void construirListaBarcos() {
        int cantidad = getCantidadBarcosPorDificultad();

        if (dificultad.equals("EASY")) {
   
            tiposBarcos    = new String[]{"DT", "SM", "AZ", "PA", "DT"};
            tamaniosBarcos = new int[]   {2,    3,    4,    5,    2};
            nombresBarcos  = new String[]{"Destructor (2)", "Submarino (3)",
                                          "Acorazado (4)",  "Portaaviones (5)",
                                          "Destructor 2 (2)"};
        } else {
    
            tiposBarcos    = new String[cantidad];
            tamaniosBarcos = new int[cantidad];
            nombresBarcos  = new String[cantidad];
            for (int i = 0; i < cantidad; i++) {
                tiposBarcos[i]    = TIPOS_BASE[i];
                tamaniosBarcos[i] = TAMANIOS_BASE[i];
                nombresBarcos[i]  = NOMBRES_BASE[i];
            }
        }
    }

    private void inicializarFrame() {
        frame = new JFrame("Battleship - " + player1.getUsername() + " vs " + player2.getUsername());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1500, 850);
        frame.setResizable(false);
        frame.setLayout(null);

        capas = new JLayeredPane();
        capas.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        frame.setContentPane(capas);

        fondo = new JLabel();
        ImageIcon iconfondo = new ImageIcon(getClass().getResource("/imagen/fondo_2.jpg"));
        fondo.setIcon(iconfondo);
        fondo.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        capas.add(fondo, Integer.valueOf(0));
    }

    private void crearTableros() {
        tableroP1 = new Tablero(50,  150, player1.getUsername());
        tableroP2 = new Tablero(850, 150, player2.getUsername());

        capas.add(tableroP1.getPanel(), Integer.valueOf(1));
        capas.add(tableroP2.getPanel(), Integer.valueOf(1));
    }

    private void crearPanelControl() {
        panelControl = new JPanel();
        panelControl.setBounds(500, 150, 300, 250);
        panelControl.setLayout(new GridLayout(6, 1, 10, 10));
        panelControl.setOpaque(false);

        btnRotar = new JButton(" Rotar Barco");
        btnRotar.setFont(new Font("Arial", Font.BOLD, 14));
        btnRotar.setBackground(new Color(52, 152, 219));
        btnRotar.setForeground(Color.BLACK);
        btnRotar.setFocusPainted(false);
        btnRotar.addActionListener(e -> rotarBarco());

        btnRendirse = new JButton(" Rendirse");
        btnRendirse.setFont(new Font("Arial", Font.BOLD, 14));
        btnRendirse.setBackground(new Color(231, 76, 60));
        btnRendirse.setForeground(Color.BLACK);
        btnRendirse.setFocusPainted(false);
        btnRendirse.setEnabled(false);
        btnRendirse.addActionListener(e -> rendirse());

        btnReiniciar = new JButton("Nuevo Juego");
        btnReiniciar.setFont(new Font("Arial", Font.BOLD, 14));
        btnReiniciar.setBackground(new Color(46, 204, 113));
        btnReiniciar.setForeground(Color.BLACK);
        btnReiniciar.setFocusPainted(false);
        btnReiniciar.setEnabled(false);
        btnReiniciar.addActionListener(e -> reiniciarJuego());

        btnMenu = new JButton(" Volver al Menu");
        btnMenu.setFont(new Font("Arial", Font.BOLD, 14));
        btnMenu.setBackground(new Color(155, 89, 182));
        btnMenu.setForeground(Color.BLACK);
        btnMenu.setFocusPainted(false);
        btnMenu.setEnabled(false);
        btnMenu.addActionListener(e -> volverAlMenu());

        labelBarcoActual = new JLabel("Barco: Destructor (2)", SwingConstants.CENTER);
        labelBarcoActual.setFont(new Font("Arial", Font.BOLD, 16));
        labelBarcoActual.setForeground(Color.WHITE);
        labelBarcoActual.setOpaque(true);
        labelBarcoActual.setBackground(new Color(44, 62, 80, 200));

        progressBarco = new JProgressBar(0, tiposBarcos.length);
        progressBarco.setValue(0);
        progressBarco.setStringPainted(true);
        progressBarco.setString("0/" + tiposBarcos.length);

        panelControl.add(labelBarcoActual);
        panelControl.add(progressBarco);
        panelControl.add(btnRotar);
        panelControl.add(btnRendirse);
        panelControl.add(btnReiniciar);
        panelControl.add(btnMenu);

        capas.add(panelControl, Integer.valueOf(1));
    }

    private void crearPanelInfo() {
        panelInfo = new JPanel();
        panelInfo.setBounds(400, 50, 700, 80);
        panelInfo.setLayout(new GridLayout(3, 1, 5, 5));
        panelInfo.setOpaque(false);

        labelEstado = new JLabel("Fase: Colocacion - " + player1.getUsername(), SwingConstants.CENTER);
        labelEstado.setFont(new Font("Arial", Font.BOLD, 20));
        labelEstado.setForeground(Color.WHITE);
        labelEstado.setOpaque(true);
        labelEstado.setBackground(new Color(44, 62, 80, 200));

        labelBarcosP1 = new JLabel(player1.getUsername() + ": 0 barcos", SwingConstants.CENTER);
        labelBarcosP1.setFont(new Font("Arial", Font.BOLD, 14));
        labelBarcosP1.setForeground(Color.WHITE);
        labelBarcosP1.setOpaque(true);
        labelBarcosP1.setBackground(new Color(52, 152, 219, 200));

        labelBarcosP2 = new JLabel(player2.getUsername() + ": 0 barcos", SwingConstants.CENTER);
        labelBarcosP2.setFont(new Font("Arial", Font.BOLD, 14));
        labelBarcosP2.setForeground(Color.WHITE);
        labelBarcosP2.setOpaque(true);
        labelBarcosP2.setBackground(new Color(231, 76, 60, 200));

        panelInfo.add(labelEstado);
        panelInfo.add(labelBarcosP1);
        panelInfo.add(labelBarcosP2);

        capas.add(panelInfo, Integer.valueOf(1));
    }

    private void configurarEventos() {
        for (int f = 0; f < 8; f++) {
            for (int c = 0; c < 8; c++) {
                final int fila = f;
                final int col  = c;

                tableroP1.getBoton(f, c).addActionListener(e ->
                    clickTablero(tableroP1, tableroP2, true, fila, col));

                tableroP2.getBoton(f, c).addActionListener(e ->
                    clickTablero(tableroP2, tableroP1, false, fila, col));
            }
        }
    }

    private void clickTablero(Tablero tableroClickeado, Tablero tableroOponente,
                               boolean esTableroP1, int f, int c) {
        if (faseColocacion) {
            if ((colocandoPlayer1 && esTableroP1) || (!colocandoPlayer1 && !esTableroP1)) {
                colocarBarcoEnTablero(tableroClickeado, f, c);
            }
        } else {
            if ((turnoPlayer1 && !esTableroP1) || (!turnoPlayer1 && esTableroP1)) {
                realizarAtaque(tableroClickeado, tableroOponente, f, c);
            }
        }
    }

    private void colocarBarcoEnTablero(Tablero tablero, int f, int c) {
        int cantidadBarcos = tiposBarcos.length;
        if (barcoActualIndex >= cantidadBarcos) return;

        String tipoBarco = tiposBarcos[barcoActualIndex];
        int    tamanio   = tamaniosBarcos[barcoActualIndex];

        if (tablero.puedeColocar(f, c, tamanio, horizontal)) {
            tablero.colocarBarco(f, c, tamanio, horizontal, tipoBarco);

            if (colocandoPlayer1) barcosColocadosP1[barcoActualIndex]++;
            else                  barcosColocadosP2[barcoActualIndex]++;

            barcoActualIndex++;
            progressBarco.setValue(barcoActualIndex);
            progressBarco.setString(barcoActualIndex + "/" + cantidadBarcos);

            if (barcoActualIndex >= cantidadBarcos) {
                if (colocandoPlayer1) {
                    colocandoPlayer1 = false;
                    barcoActualIndex = 0;
                    progressBarco.setValue(0);
                    progressBarco.setString("0/" + cantidadBarcos);
                    labelEstado.setText("Turno de " + player2.getUsername() + " - Coloca tus barcos");
         
                    if (modoJuego.equals("ARCADE")) {
                        tableroP1.setModoOculto(true);
                    }
                } else {
                    iniciarFaseCombate();
                }
            }

            actualizarEstado();
        }
    }

    private void iniciarFaseCombate() {
        faseColocacion = false;
        barcoActualIndex = 0;
        btnRotar.setEnabled(false);
        btnRendirse.setEnabled(true);

        if (modoJuego.equals("ARCADE")) {
            tableroP1.setModoOculto(true);
            tableroP2.setModoOculto(true);
        }

        actualizarColoresTableros();
        labelEstado.setText("\u2694\uFE0F COMBATE - Turno de: " + player1.getUsername());
        actualizarEstado();
    }

    private void realizarAtaque(Tablero tableroAtacado, Tablero tableroAtacante, int f, int c) {
        int resultado = tableroAtacado.atacar(f, c);


        String nombreAtacante = turnoPlayer1 ? player1.getUsername() : player2.getUsername();
        String nombreDefensor = turnoPlayer1 ? player2.getUsername() : player1.getUsername();

        switch (resultado) {
            case 0:
                labelEstado.setText("AGUA - " + nombreAtacante + " fallo. Turno de " + nombreDefensor);
                cambiarTurno();
                break;

            case 1:
                if (tableroAtacado.tieneBarcosSinHundir()) tableroAtacado.regenerarTablero();
                labelEstado.setText(" IMPACTO - " + nombreAtacante + " dio en un barco. Sigue disparando!");
                break;

            case 2:
                if (tableroAtacado.todosHundidos()) {
                    finalizarJuego(nombreAtacante, nombreDefensor, false);
                    return;
                }
                if (tableroAtacado.tieneBarcosSinHundir()) tableroAtacado.regenerarTablero();
                labelEstado.setText(" BARCO HUNDIDO - " + nombreAtacante + " hundo un barco! Sigue disparando!");
                break;
        }

        actualizarEstado();
        actualizarColoresTableros();
    }

    private void cambiarTurno() {
        if (turnoPlayer1) {
            tableroP2.limpiarFallos();
        } else {
            tableroP1.limpiarFallos();
        }
        turnoPlayer1 = !turnoPlayer1;
        actualizarColoresTableros();
    }

    private void actualizarEstado() {
        if (faseColocacion) {
            if (barcoActualIndex < nombresBarcos.length) {
                labelBarcoActual.setText("Colocar: " + nombresBarcos[barcoActualIndex]);
            }
        } else {
            labelBarcoActual.setText("Modo: " + modoJuego);
        }

        int barcosP1 = tableroP1.getBarcosRestantes();
        int barcosP2 = tableroP2.getBarcosRestantes();

        labelBarcosP1.setText(player1.getUsername() + ": " + barcosP1 + " barcos");
        labelBarcosP2.setText(player2.getUsername() + ": " + barcosP2 + " barcos");
    }

    private void rotarBarco() {
        horizontal = !horizontal;
        String orientacion = horizontal ? "Horizontal" : "Vertical";
        labelEstado.setText("Orientacion cambiada a: " + orientacion);
    }

    private void rendirse() {
        String jugadorActual = turnoPlayer1 ? player1.getUsername() : player2.getUsername();
        String oponente      = turnoPlayer1 ? player2.getUsername() : player1.getUsername();

        int confirm = JOptionPane.showConfirmDialog(frame,
            jugadorActual + " desea rendirse?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            finalizarJuego(oponente, jugadorActual, true);
        }
    }

    private void finalizarJuego(String nombreGanador, String nombrePerdedor, boolean porRendicion) {

        Player ganador  = nombreGanador.equals(player1.getUsername())  ? player1  : player2;
        Player perdedor = nombrePerdedor.equals(player1.getUsername()) ? player1 : player2;


        ganador.agregarPuntos(3);


        String logTexto = porRendicion
            ? nombrePerdedor + " se retiro. Gano " + nombreGanador + " en modo " + dificultad
            : nombreGanador + " hundio todos los barcos de " + nombrePerdedor + " en modo " + dificultad;


        ganador.agregarLog(logTexto);
        perdedor.agregarLog(logTexto);

        labelEstado.setText(porRendicion
            ?  nombrePerdedor + " se rindio. GANA: " + nombreGanador
            :  nombreGanador + " hundo todos los barcos! GANA: " + nombreGanador);

        btnRendirse.setEnabled(false);
        btnReiniciar.setEnabled(true);
        btnMenu.setEnabled(true);

        deshabilitarTableros();
    }

    private void deshabilitarTableros() {
        for (int f = 0; f < 8; f++) {
            for (int c = 0; c < 8; c++) {
                tableroP1.getBoton(f, c).setEnabled(false);
                tableroP2.getBoton(f, c).setEnabled(false);
            }
        }
    }

    private void reiniciarJuego() {
        frame.dispose();
        new BattleShipGUI(player1, player2, dificultad, modoJuego, menuReferencia).mostrar();
    }

    private void volverAlMenu() {
        frame.dispose();
        if (menuReferencia != null) menuReferencia.setVisible(true);
    }

    private int getCantidadBarcosPorDificultad() {
        switch (dificultad) {
            case "EASY":   return 5;
            case "NORMAL": return 4;
            case "EXPERT": return 2;
            case "GENIUS": return 1;
            default:       return 4;
        }
    }

    private static final Color COLOR_MI_TURNO  = new Color(0xA2CADF); 
    private static final Color COLOR_ATACAR    = new Color(0xFF6961); 
    private static final Color COLOR_NEUTRO    = new Color(0x5B9BD5);

    private void actualizarColoresTableros() {
        if (!faseColocacion) {
            if (turnoPlayer1) {

                tableroP1.setColorAgua(COLOR_MI_TURNO);
                tableroP1.setColorBordeTablero(new Color(0xA2CADF));
                tableroP2.setColorAgua(COLOR_ATACAR);
                tableroP2.setColorBordeTablero(new Color(0xFF6961));
            } else {
     
                tableroP1.setColorAgua(COLOR_ATACAR);
                tableroP1.setColorBordeTablero(new Color(0xFF6961));
                tableroP2.setColorAgua(COLOR_MI_TURNO);
                tableroP2.setColorBordeTablero(new Color(0xA2CADF));
            }
        } else {
            tableroP1.setColorAgua(COLOR_NEUTRO);
            tableroP1.resetColorBordeTablero();
            tableroP2.setColorAgua(COLOR_NEUTRO);
            tableroP2.resetColorBordeTablero();
        }
    }

    public void mostrar() {
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}