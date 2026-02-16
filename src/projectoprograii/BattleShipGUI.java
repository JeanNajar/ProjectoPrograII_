package projectoprograii;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BattleShipGUI extends JPanel {

    Tablero tableroP1;
    Tablero tableroP2;

    JPanel panelControl;
    JPanel panelInfo;
    JPanel panelSelector;
    JLabel labelEstado;
    JLabel labelBarcosP1;
    JLabel labelBarcosP2;

    JButton btnRotar;
    JButton btnRendirse;
    JButton btnReiniciar;
    JButton btnMenu;

    private Player player1;
    private Player player2;

    private boolean turnoPlayer1     = true;
    private boolean faseColocacion   = true;
    private boolean colocandoPlayer1 = true;

    private int     barcoActualIndex = 0;
    private boolean horizontal       = true;

    private Tipobarco[] listaBarcos;
    private JButton[] botonesSelectorBarcos;

    private String dificultad = "NORMAL";
    private String modoJuego  = "TUTORIAL";

    private JFrame frameContenedor;
    private CardLayout cardLayoutPrincipal;
    private JPanel panelPrincipal;

    public BattleShipGUI(JFrame frame, CardLayout cardLayout, JPanel panelPrincipal,
                         Player p1, Player p2, String dificultad, String modo) {
        this.frameContenedor      = frame;
        this.cardLayoutPrincipal  = cardLayout;
        this.panelPrincipal       = panelPrincipal;
        this.player1              = p1;
        this.player2              = p2;
        this.dificultad           = dificultad;
        this.modoJuego            = modo;

        listaBarcos = construirListaBarcos(dificultad);

        setLayout(null);
        setBounds(0, 0, 1500, 850);

        ImageIcon iconfondo = new ImageIcon(getClass().getResource("/imagen/fondo_2.jpg"));
        JLabel fondo = new JLabel(iconfondo);
        fondo.setBounds(0, 0, 1500, 850);
        add(fondo);

        crearTableros();
        crearPanelControl();
        crearPanelInfo();
        crearPanelSelector();
        configurarEventos();
        actualizarEstado();
    }

    private Tipobarco[] construirListaBarcos(String dif) {
        if (dif.equals("EASY")) {
            return new Tipobarco[]{
                Tipobarco.DESTRUCTOR,
                Tipobarco.SUBMARINO,
                Tipobarco.ACORAZADO,
                Tipobarco.PORTAAVIONES,
                Tipobarco.DESTRUCTOR
            };
        }
        int cantidad;
        switch (dif) {
            case "EXPERT": cantidad = 2; break;
            case "GENIUS": cantidad = 1; break;
            default:       cantidad = 4; break;
        }
        Tipobarco[] lista = new Tipobarco[cantidad];
        for (int i = 0; i < cantidad; i++) {
            lista[i] = Tipobarco.values()[i];
        }
        return lista;
    }

    private void crearTableros() {
        tableroP1 = new Tablero(50,  150, player1.getUsername());
        tableroP2 = new Tablero(850, 150, player2.getUsername());

        add(tableroP1.getPanel());
        add(tableroP2.getPanel());

        setComponentZOrder(tableroP1.getPanel(), 0);
        setComponentZOrder(tableroP2.getPanel(), 0);
    }

    private void crearPanelControl() {
        panelControl = new JPanel();
        panelControl.setBounds(500, 450, 300, 160);
        panelControl.setLayout(new GridLayout(4, 1, 10, 10));
        panelControl.setOpaque(false);

        btnRotar = new JButton("Rotar Barco");
        btnRotar.setFont(new Font("Arial", Font.BOLD, 14));
        btnRotar.setBackground(new Color(52, 152, 219));
        btnRotar.setForeground(Color.BLACK);
        btnRotar.setFocusPainted(false);
        btnRotar.addActionListener(e -> rotarBarco());

        btnRendirse = new JButton("Rendirse");
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

        btnMenu = new JButton("Volver al Menu");
        btnMenu.setFont(new Font("Arial", Font.BOLD, 14));
        btnMenu.setBackground(new Color(155, 89, 182));
        btnMenu.setForeground(Color.BLACK);
        btnMenu.setFocusPainted(false);
        btnMenu.addActionListener(e -> volverAlMenu());

        panelControl.add(btnRotar);
        panelControl.add(btnRendirse);
        panelControl.add(btnReiniciar);
        panelControl.add(btnMenu);

        add(panelControl);
        setComponentZOrder(panelControl, 0);
    }

    private void crearPanelInfo() {
        panelInfo = new JPanel();
        panelInfo.setBounds(400, 50, 700, 80);
        panelInfo.setLayout(new GridLayout(3, 1, 5, 5));
        panelInfo.setOpaque(false);

        labelEstado = new JLabel("Fase: Colocacion - " + player1.getUsername(), SwingConstants.CENTER) {
            @Override
            public void setIcon(Icon icon) {
                // Bloquear cualquier intento de asignar íconos
            }
        };
        labelEstado.setFont(new Font("Arial", Font.BOLD, 20));
        labelEstado.setForeground(Color.WHITE);
        labelEstado.setOpaque(true);
        labelEstado.setBackground(new Color(44, 62, 80, 200));

        labelBarcosP1 = new JLabel(player1.getUsername() + ": 0 barcos", SwingConstants.CENTER) {
            @Override
            public void setIcon(Icon icon) {
                // Bloquear cualquier intento de asignar íconos
            }
        };
        labelBarcosP1.setFont(new Font("Arial", Font.BOLD, 14));
        labelBarcosP1.setForeground(Color.WHITE);
        labelBarcosP1.setOpaque(true);
        labelBarcosP1.setBackground(new Color(52, 152, 219, 200));

        labelBarcosP2 = new JLabel(player2.getUsername() + ": 0 barcos", SwingConstants.CENTER) {
            @Override
            public void setIcon(Icon icon) {
                // Bloquear cualquier intento de asignar íconos
            }
        };
        labelBarcosP2.setFont(new Font("Arial", Font.BOLD, 14));
        labelBarcosP2.setForeground(Color.WHITE);
        labelBarcosP2.setOpaque(true);
        labelBarcosP2.setBackground(new Color(231, 76, 60, 200));

        panelInfo.add(labelEstado);
        panelInfo.add(labelBarcosP1);
        panelInfo.add(labelBarcosP2);

        add(panelInfo);
        setComponentZOrder(panelInfo, 0);
    }

    private void crearPanelSelector() {
        panelSelector = new JPanel();
        panelSelector.setBounds(500, 150, 300, 280);
        panelSelector.setLayout(new GridLayout(listaBarcos.length + 1, 1, 5, 5));
        panelSelector.setOpaque(false);

        JLabel titulo = new JLabel("Selecciona el barco a colocar:", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 13));
        titulo.setForeground(Color.WHITE);
        titulo.setOpaque(true);
        titulo.setBackground(new Color(44, 62, 80, 200));
        panelSelector.add(titulo);

        botonesSelectorBarcos = new JButton[listaBarcos.length];

        for (int i = 0; i < listaBarcos.length; i++) {
            final int index = i;
            JButton btnBarco = new JButton(listaBarcos[i].getNombreCompleto());
            btnBarco.setFont(new Font("Arial", Font.BOLD, 16));
            btnBarco.setBackground(new Color(70, 130, 180));
            btnBarco.setForeground(Color.black);
            btnBarco.setFocusPainted(false);
            btnBarco.addActionListener(e -> seleccionarBarco(index));
            botonesSelectorBarcos[i] = btnBarco;
            panelSelector.add(btnBarco);
        }

        add(panelSelector);
        setComponentZOrder(panelSelector, 0);
        actualizarBotonesSelector();
    }

    private void seleccionarBarco(int index) {
        if (!faseColocacion) return;
        barcoActualIndex = index;
        actualizarEstado();
        actualizarBotonesSelector();
    }

    private void actualizarBotonesSelector() {
        for (int i = 0; i < botonesSelectorBarcos.length; i++) {
            if (i == barcoActualIndex) {
                botonesSelectorBarcos[i].setBackground(new Color(46, 204, 113));
            } else {
                botonesSelectorBarcos[i].setBackground(new Color(70, 130, 180));
            }
        }
    }

    private void configurarEventos() {
        for (int f = 0; f < 8; f++) {
            for (int c = 0; c < 8; c++) {
                final int fila = f;
                final int col  = c;
                tableroP1.getBoton(f, c).addActionListener(e ->
                    clickTablero(tableroP1, true, fila, col));
                tableroP2.getBoton(f, c).addActionListener(e ->
                    clickTablero(tableroP2, false, fila, col));
            }
        }
    }

    private void clickTablero(Tablero tableroClickeado, boolean esTableroP1, int f, int c) {
        if (faseColocacion) {
            if ((colocandoPlayer1 && esTableroP1) || (!colocandoPlayer1 && !esTableroP1)) {
                colocarBarcoEnTablero(tableroClickeado, f, c);
            }
        } else {
            if ((turnoPlayer1 && !esTableroP1) || (!turnoPlayer1 && esTableroP1)) {
                realizarAtaque(tableroClickeado, f, c);
            }
        }
    }

    private void colocarBarcoEnTablero(Tablero tablero, int f, int c) {
        if (barcoActualIndex >= listaBarcos.length) return;

        Tipobarco tipo    = listaBarcos[barcoActualIndex];
        int       tamanio = tipo.getTamanio();

        if (tablero.puedeColocar(f, c, tamanio, horizontal)) {
            tablero.colocarBarco(f, c, tamanio, horizontal, tipo.getCodigo());

            botonesSelectorBarcos[barcoActualIndex].setEnabled(false);
            botonesSelectorBarcos[barcoActualIndex].setBackground(Color.GRAY);

            boolean todosPuestos = true;
            for (JButton btn : botonesSelectorBarcos) {
                if (btn.isEnabled()) {
                    todosPuestos = false;
                    break;
                }
            }

            if (todosPuestos) {
                if (colocandoPlayer1) {
                    colocandoPlayer1 = false;
                    barcoActualIndex = 0;
                    labelEstado.setText("Turno de " + player2.getUsername() + " - Coloca tus barcos");
                    if (modoJuego.equals("ARCADE")) tableroP1.setModoOculto(true);
                    for (JButton btn : botonesSelectorBarcos) {
                        btn.setEnabled(true);
                        btn.setBackground(new Color(70, 130, 180));
                    }
                    actualizarBotonesSelector();
                } else {
                    iniciarFaseCombate();
                }
            } else {
                for (int i = 0; i < botonesSelectorBarcos.length; i++) {
                    if (botonesSelectorBarcos[i].isEnabled()) {
                        barcoActualIndex = i;
                        break;
                    }
                }
            }
            actualizarEstado();
            actualizarBotonesSelector();
        }
    }

    private void iniciarFaseCombate() {
        faseColocacion = false;
        barcoActualIndex = 0;
        btnRotar.setEnabled(false);
        btnRendirse.setEnabled(true);
        panelSelector.setVisible(false);

        if (modoJuego.equals("ARCADE")) {
            tableroP1.setModoOculto(true);
            tableroP2.setModoOculto(true);
        }

        actualizarColoresTableros();
        labelEstado.setText("COMBATE - Turno de: " + player1.getUsername());
        actualizarEstado();
    }

    private void realizarAtaque(Tablero tableroAtacado, int f, int c) {
        int resultado = tableroAtacado.atacar(f, c);

        String nombreAtacante = turnoPlayer1 ? player1.getUsername() : player2.getUsername();
        String nombreDefensor = turnoPlayer1 ? player2.getUsername() : player1.getUsername();

        switch (resultado) {
            case 0:
                labelEstado.setText("AGUA - " + nombreAtacante + " fallo. Turno de " + nombreDefensor);
                cambiarTurno();
                break;
            case 1:
                tableroAtacado.limpiarMarcas();
                if (tableroAtacado.tieneBarcosSinHundir()) tableroAtacado.regenerarTablero();
                labelEstado.setText("IMPACTO - " + nombreAtacante + " dio en un barco. Sigue disparando!");
                break;
            case 2:
                tableroAtacado.limpiarMarcas();
                if (tableroAtacado.todosHundidos()) {
                    finalizarJuego(nombreAtacante, nombreDefensor, false);
                    return;
                }
                if (tableroAtacado.tieneBarcosSinHundir()) tableroAtacado.regenerarTablero();
                labelEstado.setText("BARCO HUNDIDO - " + nombreAtacante + " hundo un barco! Sigue disparando!");
                break;
        }

        actualizarEstado();
        actualizarColoresTableros();
    }

    private void cambiarTurno() {
        turnoPlayer1 = !turnoPlayer1;
        actualizarColoresTableros();
    }

    private void actualizarEstado() {
        if (faseColocacion) {
            if (barcoActualIndex < listaBarcos.length) {
                labelEstado.setText("Colocando: " + listaBarcos[barcoActualIndex].getNombreCompleto() 
                    + " - " + (colocandoPlayer1 ? player1.getUsername() : player2.getUsername()));
            }
        } else {
            if (!labelEstado.getText().contains("AGUA") && 
                !labelEstado.getText().contains("IMPACTO") && 
                !labelEstado.getText().contains("HUNDIDO") &&
                !labelEstado.getText().contains("GANA")) {
                labelEstado.setText("COMBATE - Turno de: " + (turnoPlayer1 ? player1.getUsername() : player2.getUsername()));
            }
        }

        labelBarcosP1.setText(player1.getUsername() + ": " + tableroP1.getBarcosRestantes() + " barcos");
        labelBarcosP2.setText(player2.getUsername() + ": " + tableroP2.getBarcosRestantes() + " barcos");
    }

    private void rotarBarco() {
        horizontal = !horizontal;
        labelEstado.setText("Orientacion cambiada a: " + (horizontal ? "Horizontal" : "Vertical"));
    }

    private void rendirse() {
        String jugadorActual = turnoPlayer1 ? player1.getUsername() : player2.getUsername();
        String oponente      = turnoPlayer1 ? player2.getUsername() : player1.getUsername();

        int confirm = JOptionPane.showConfirmDialog(frameContenedor,
            jugadorActual + " desea rendirse?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            finalizarJuego(oponente, jugadorActual, true);
        }
    }

    private void finalizarJuego(String nombreGanador, String nombrePerdedor, boolean porRendicion) {
        Player ganador  = nombreGanador.equals(player1.getUsername())  ? player1 : player2;
        Player perdedor = nombrePerdedor.equals(player1.getUsername()) ? player1 : player2;

        ganador.agregarPuntos(3);

        String logTexto = porRendicion
            ? nombrePerdedor + " se retiro. Gano " + nombreGanador + " en modo " + dificultad
            : nombreGanador + " hundio todos los barcos de " + nombrePerdedor + " en modo " + dificultad;

        ganador.agregarLog(logTexto);
        perdedor.agregarLog(logTexto);

        labelEstado.setText(porRendicion
            ? nombrePerdedor + " se rindio. GANA: " + nombreGanador
            : nombreGanador + " hundo todos los barcos! GANA: " + nombreGanador);

        btnRendirse.setEnabled(false);
        btnReiniciar.setEnabled(true);

        deshabilitarTableros();
    }

    private void deshabilitarTableros() {
        for (int f = 0; f < 8; f++)
            for (int c = 0; c < 8; c++) {
                tableroP1.getBoton(f, c).setEnabled(false);
                tableroP2.getBoton(f, c).setEnabled(false);
            }
    }

    private void reiniciarJuego() {
        panelPrincipal.remove(this);
        BattleShipGUI nuevoJuego = new BattleShipGUI(frameContenedor, cardLayoutPrincipal,
            panelPrincipal, player1, player2, dificultad, modoJuego);
        panelPrincipal.add(nuevoJuego, "JUEGO");
        cardLayoutPrincipal.show(panelPrincipal, "JUEGO");
    }

    private void volverAlMenu() {
        cardLayoutPrincipal.show(panelPrincipal, "MENU");
    }

    private static final Color COLOR_MI_TURNO = new Color(0xA2CADF);
    private static final Color COLOR_ATACAR   = new Color(0xFF6961);
    private static final Color COLOR_NEUTRO   = new Color(0x5B9BD5);

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
}