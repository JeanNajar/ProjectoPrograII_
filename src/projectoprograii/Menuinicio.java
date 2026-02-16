package projectoprograii;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.net.URL;


public class Menuinicio extends JFrame {

    
    private class PanelConFondo extends JPanel {
    private Image imagen;

    public PanelConFondo(LayoutManager layout, String nombreArchivo) {
        super(layout);
        try {
            URL url = getClass().getResource("/imagen/" + nombreArchivo);
            if (url != null) {
                imagen = new ImageIcon(url).getImage();
            }
        } catch (Exception e) {}
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagen != null) {
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
    
    
    private static ArrayList<Player> jugadores = new ArrayList<>();

    private String dificultad = "NORMAL";
    private String modoJuego = "TUTORIAL";
    private Player jugadorActual = null;

    private JPanel panelPrincipal;
    private CardLayout cardLayout;

    static {
        
        jugadores.add(new Player("admin", "admin"));
     
    }

    public Menuinicio() {
        setTitle("Battleship - Menu Principal");
        setSize(600, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        panelPrincipal = new JPanel(cardLayout);

        panelPrincipal.add(crearPantallaInicio(),        "INICIO");
        panelPrincipal.add(crearPantallaLogin(),         "LOGIN");
        panelPrincipal.add(crearPantallaRegistro(),      "REGISTRO");
        panelPrincipal.add(crearPantallaMenuPrincipal(), "MENU");
        panelPrincipal.add(crearPantallaConfiguracion(), "CONFIG");
        panelPrincipal.add(crearPantallaReportes(),      "REPORTES");
        panelPrincipal.add(crearPantallaPerfil(),        "PERFIL");

        add(panelPrincipal);
        cardLayout.show(panelPrincipal, "INICIO");
    }

   
    private JPanel crearPantallaInicio() {
      JPanel panel = new PanelConFondo(new GridBagLayout(), "menu_background.png");
       panel.setOpaque(true);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

       

        JButton btnLogin = crearBoton("Login", new Color(52, 152, 219));
        btnLogin.setForeground(Color.BLACK);
        btnLogin.addActionListener(e -> cardLayout.show(panelPrincipal, "LOGIN"));
        gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(btnLogin, gbc);

        JButton btnRegistro = crearBoton("Crear Cuenta", new Color(0x00cc00));
        btnRegistro.setForeground(Color.BLACK);
        btnRegistro.addActionListener(e -> cardLayout.show(panelPrincipal, "REGISTRO"));
        gbc.gridx = 1;
        panel.add(btnRegistro, gbc);

        JButton btnSalir = crearBoton("Salir", new Color(231, 76, 60));
        btnSalir.setForeground(Color.BLACK);
        btnSalir.addActionListener(e -> System.exit(0));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(btnSalir, gbc);

        return panel;
    }


    private JPanel crearPantallaLogin() {
        JPanel panel = new PanelConFondo(new GridBagLayout(), "menus_background.png");
        panel.setOpaque(true);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("LOGIN");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titulo, gbc);

        JLabel lblUser = new JLabel("Username:");
        lblUser.setForeground(Color.WHITE);
        gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(lblUser, gbc);

        JTextField txtUser = new JTextField(15);
        gbc.gridx = 1;
        panel.add(txtUser, gbc);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(lblPass, gbc);

        JPasswordField txtPass = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(txtPass, gbc);

        JLabel lblMensaje = new JLabel("", SwingConstants.CENTER);
        lblMensaje.setForeground(new Color(231, 76, 60));
        lblMensaje.setFont(new Font("Arial", Font.BOLD, 13));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(lblMensaje, gbc);

        JButton btnEntrar = crearBoton("Entrar", new Color(52, 152, 219));
        btnEntrar.setForeground(Color.BLACK);
        btnEntrar.addActionListener(e -> {
            String u = txtUser.getText().trim();
            String p = new String(txtPass.getPassword());
            if (validarLogin(u, p)) {
                lblMensaje.setText("");
                txtUser.setText("");
                txtPass.setText("");

                panelPrincipal.remove(panelPrincipal.getComponentCount() - 1);
                panelPrincipal.add(crearPantallaPerfil(), "PERFIL");
                cardLayout.show(panelPrincipal, "MENU");
            } else {
                lblMensaje.setText("Usuario o contrasena incorrectos");
            }
        });
        gbc.gridy = 4;
        panel.add(btnEntrar, gbc);

        JButton btnVolver = crearBoton("Volver", new Color(149, 165, 166));
        btnVolver.setForeground(Color.BLACK);
        btnVolver.addActionListener(e -> { lblMensaje.setText(""); cardLayout.show(panelPrincipal, "INICIO"); });
        gbc.gridy = 5;
        panel.add(btnVolver, gbc);

        return panel;
    }

    
    private JPanel crearPantallaRegistro() {
        JPanel panel = new PanelConFondo(new GridBagLayout(), "menus_background.png");
        panel.setOpaque(true);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("CREAR CUENTA");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titulo, gbc);

        JLabel lblUser = new JLabel("Username:");
        lblUser.setForeground(Color.WHITE);
        gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(lblUser, gbc);

        JTextField txtUser = new JTextField(15);
        gbc.gridx = 1;
        panel.add(txtUser, gbc);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(lblPass, gbc);

        JPasswordField txtPass = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(txtPass, gbc);

        JLabel lblConfirm = new JLabel("Confirmar:");
        lblConfirm.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(lblConfirm, gbc);

        JPasswordField txtConfirm = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(txtConfirm, gbc);

        JLabel lblMensaje = new JLabel("", SwingConstants.CENTER);
        lblMensaje.setForeground(new Color(231, 76, 60));
        lblMensaje.setFont(new Font("Arial", Font.BOLD, 13));
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panel.add(lblMensaje, gbc);

        JButton btnCrear = crearBoton("Crear Cuenta", new Color(46, 204, 113));
        btnCrear.setForeground(Color.BLACK);
        btnCrear.addActionListener(e -> {
            String u = txtUser.getText().trim();
            String p = new String(txtPass.getPassword());
            String c = new String(txtConfirm.getPassword());

            if (u.isEmpty() || p.isEmpty()) {
                lblMensaje.setText("Rellena todos los campos");
            } else if (!p.equals(c)) {
                lblMensaje.setText("Las contrasenas no coinciden");
            } else if (existeJugador(u)) {
                lblMensaje.setText("El username ya existe");
            } else {
                Player nuevo = new Player(u, p);
                jugadores.add(nuevo);
                jugadorActual = nuevo;
                lblMensaje.setText("");
                txtUser.setText(""); txtPass.setText(""); txtConfirm.setText("");
                cardLayout.show(panelPrincipal, "MENU");
            }
        });
        gbc.gridy = 5;
        panel.add(btnCrear, gbc);

        JButton btnVolver = crearBoton("Volver", new Color(149, 165, 166));
        btnVolver.setForeground(Color.BLACK);
        btnVolver.addActionListener(e -> { lblMensaje.setText(""); cardLayout.show(panelPrincipal, "INICIO"); });
        gbc.gridy = 6;
        panel.add(btnVolver, gbc);

        return panel;
    }




    private JPanel crearPantallaMenuPrincipal() {
        JPanel panel = new PanelConFondo(new GridBagLayout(), "menus_background.png");
        panel.setOpaque(true);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JLabel titulo = new JLabel("MENU PRINCIPAL");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);
        gbc.gridy = 0;
        panel.add(titulo, gbc);

        JButton btnJugar = crearBoton("Jugar Battleship", new Color(52, 152, 219));
        btnJugar.setForeground(Color.BLACK);
        btnJugar.addActionListener(e -> iniciarJuego());
        gbc.gridy = 1;
        panel.add(btnJugar, gbc);

        JButton btnConfig = crearBoton("Configuracion", new Color(46, 204, 113));
        btnConfig.setForeground(Color.BLACK);
        btnConfig.addActionListener(e -> cardLayout.show(panelPrincipal, "CONFIG"));
        gbc.gridy = 2;
        panel.add(btnConfig, gbc);

     
        JButton btnReportes = crearBoton("Reportes", new Color(230, 126, 34));
        btnReportes.setForeground(Color.BLACK);
        btnReportes.addActionListener(e -> {
        
            int idx = indexOfCard("REPORTES");
            if (idx >= 0) panelPrincipal.remove(idx);
            panelPrincipal.add(crearPantallaReportes(), "REPORTES");
            cardLayout.show(panelPrincipal, "REPORTES");
        });
        gbc.gridy = 3;
        panel.add(btnReportes, gbc);

  
        JButton btnPerfil = crearBoton("Mi Perfil", new Color(155, 89, 182));
        btnPerfil.setForeground(Color.BLACK);
        btnPerfil.addActionListener(e -> {
            int idx = indexOfCard("PERFIL");
            if (idx >= 0) panelPrincipal.remove(idx);
            panelPrincipal.add(crearPantallaPerfil(), "PERFIL");
            cardLayout.show(panelPrincipal, "PERFIL");
        });
        gbc.gridy = 4;
        panel.add(btnPerfil, gbc);

        JButton btnCerrar = crearBoton("Cerrar Sesion", new Color(231, 76, 60));
        btnCerrar.setForeground(Color.BLACK);
        btnCerrar.addActionListener(e -> {
            jugadorActual = null;
            cardLayout.show(panelPrincipal, "INICIO");
        });
        gbc.gridy = 5;
        panel.add(btnCerrar, gbc);

        return panel;
    }


    private JPanel crearPantallaConfiguracion() {
        JPanel panel = new PanelConFondo(new GridBagLayout(), "menus_background.png");
        panel.setOpaque(true);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("CONFIGURACION");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titulo, gbc);

        JLabel lblDif = new JLabel("Dificultad:");
        lblDif.setForeground(Color.WHITE);
        gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(lblDif, gbc);

        String[] difs = {"EASY", "NORMAL", "EXPERT", "GENIUS"};
        JComboBox<String> comboDif = new JComboBox<>(difs);
        comboDif.setSelectedItem(dificultad);
        comboDif.addActionListener(e -> dificultad = (String) comboDif.getSelectedItem());
        gbc.gridx = 1;
        panel.add(comboDif, gbc);

        JLabel lblModo = new JLabel("Modo:");
        lblModo.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(lblModo, gbc);

        String[] modos = {"TUTORIAL", "ARCADE"};
        JComboBox<String> comboModo = new JComboBox<>(modos);
        comboModo.setSelectedItem(modoJuego);
        comboModo.addActionListener(e -> modoJuego = (String) comboModo.getSelectedItem());
        gbc.gridx = 1;
        panel.add(comboModo, gbc);

        JButton btnVolver = crearBoton("Volver", new Color(149, 165, 166));
        btnVolver.setForeground(Color.BLACK);
        btnVolver.addActionListener(e -> cardLayout.show(panelPrincipal, "MENU"));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(btnVolver, gbc);

        return panel;
    }


    private JPanel crearPantallaReportes() {
        JPanel panel = new PanelConFondo(new GridBagLayout(), "menus_background.png");
        panel.setOpaque(true);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JLabel titulo = new JLabel("REPORTES");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);
        gbc.gridy = 0;
        panel.add(titulo, gbc);

        // boton reporte
        JButton btnUltimos = crearBoton("Mis ultimos 10 juegos", new Color(52, 152, 219));
        btnUltimos.setForeground(Color.BLACK);
        btnUltimos.addActionListener(e -> mostrarUltimos10Juegos());
        gbc.gridy = 1;
        panel.add(btnUltimos, gbc);

        // boton ranking
        JButton btnRanking = crearBoton("Ranking de jugadores", new Color(230, 126, 34));
        btnRanking.setForeground(Color.BLACK);
        btnRanking.addActionListener(e -> mostrarRanking());
        gbc.gridy = 2;
        panel.add(btnRanking, gbc);

        JButton btnVolver = crearBoton("Volver al Menu", new Color(149, 165, 166));
        btnVolver.setForeground(Color.BLACK);
        btnVolver.addActionListener(e -> cardLayout.show(panelPrincipal, "MENU"));
        gbc.gridy = 3;
        panel.add(btnVolver, gbc);

        return panel;
    }


    private void mostrarUltimos10Juegos() {
        if (jugadorActual == null) return;

        String[] logs = jugadorActual.getLogs();
        JPanel panelLogs = new JPanel(new GridLayout(11, 1, 3, 3));
        panelLogs.setBackground(new Color(44, 62, 80));

        JLabel lbl = new JLabel("Ultimos juegos de: " + jugadorActual.getUsername());
        lbl.setForeground(Color.YELLOW);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        panelLogs.add(lbl);

 
        int numero = 1;
        for (String log : logs) {
            String texto = numero + "- " + (log != null ? log : "");
            JLabel fila = new JLabel(texto);
            fila.setForeground(Color.WHITE);
            fila.setFont(new Font("Arial", Font.PLAIN, 12));
            panelLogs.add(fila);
            numero++;
        }

        JOptionPane.showMessageDialog(this, panelLogs, "Mis Ultimos 10 Juegos", JOptionPane.PLAIN_MESSAGE);
    }


    private void mostrarRanking() {
   
        ArrayList<Player> ordenados = new ArrayList<>(jugadores);


        for (int i = 0; i < ordenados.size() - 1; i++) {
            for (int j = 0; j < ordenados.size() - 1 - i; j++) {
                if (ordenados.get(j).getPuntos() < ordenados.get(j + 1).getPuntos()) {
                    Player temp = ordenados.get(j);
                    ordenados.set(j, ordenados.get(j + 1));
                    ordenados.set(j + 1, temp);
                }
            }
        }

        JPanel panelRanking = new JPanel(new GridLayout(ordenados.size() + 1, 1, 3, 3));
        panelRanking.setBackground(new Color(44, 62, 80));

        JLabel encabezado = new JLabel("  # | Username            | Puntos");
        encabezado.setForeground(Color.YELLOW);
        encabezado.setFont(new Font("Monospaced", Font.BOLD, 13));
        panelRanking.add(encabezado);

        int pos = 1;
        for (Player p : ordenados) {
            String linea = String.format("  %d | %-20s | %d", pos, p.getUsername(), p.getPuntos());
            JLabel fila = new JLabel(linea);
            fila.setForeground(pos == 1 ? Color.YELLOW : Color.WHITE);
            fila.setFont(new Font("Monospaced", Font.PLAIN, 13));
            panelRanking.add(fila);
            pos++;
        }

        JOptionPane.showMessageDialog(this, panelRanking, "Ranking de Jugadores", JOptionPane.PLAIN_MESSAGE);
    }

 
    private JPanel crearPantallaPerfil() {
        JPanel panel = new PanelConFondo(new GridBagLayout(), "menus_background.png");
        panel.setOpaque(true);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JLabel titulo = new JLabel("MI PERFIL");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);
        gbc.gridy = 0;
        panel.add(titulo, gbc);

        // ver datos
        JButton btnVer = crearBoton("Ver mis datos", new Color(52, 152, 219));
        btnVer.setForeground(Color.BLACK);
        btnVer.addActionListener(e -> verDatos());
        gbc.gridy = 1;
        panel.add(btnVer, gbc);

        // modificar datos
        JButton btnModificar = crearBoton("Modificar mis datos", new Color(46, 204, 113));
        btnModificar.setForeground(Color.BLACK);
        btnModificar.addActionListener(e -> modificarDatos());
        gbc.gridy = 2;
        panel.add(btnModificar, gbc);

        // eliminar cuenta
        JButton btnEliminar = crearBoton("Eliminar mi cuenta", new Color(231, 76, 60));
        btnEliminar.setForeground(Color.BLACK);
        btnEliminar.addActionListener(e -> eliminarCuenta());
        gbc.gridy = 3;
        panel.add(btnEliminar, gbc);

        JButton btnVolver = crearBoton("Volver al Menu", new Color(149, 165, 166));
        btnVolver.setForeground(Color.BLACK);
        btnVolver.addActionListener(e -> cardLayout.show(panelPrincipal, "MENU"));
        gbc.gridy = 4;
        panel.add(btnVolver, gbc);

        return panel;
    }

    private void verDatos() {
        if (jugadorActual == null) return;

        JPanel p = new JPanel(new GridLayout(3, 1, 5, 5));
        p.setBackground(new Color(44, 62, 80));

        JLabel u = new JLabel("Username: " + jugadorActual.getUsername());
        JLabel pw = new JLabel("Password: " + jugadorActual.getPassword());
        JLabel pts = new JLabel("Puntos: " + jugadorActual.getPuntos());

        for (JLabel lbl : new JLabel[]{u, pw, pts}) {
            lbl.setForeground(Color.WHITE);
            lbl.setFont(new Font("Arial", Font.PLAIN, 14));
            p.add(lbl);
        }

        JOptionPane.showMessageDialog(this, p, "Mis Datos", JOptionPane.PLAIN_MESSAGE);
    }

    private void modificarDatos() {
        if (jugadorActual == null) return;

        JPanel p = new JPanel(new GridLayout(4, 2, 5, 5));
        p.setBackground(new Color(44, 62, 80));

        JLabel lu = new JLabel("Nuevo username:");
        JLabel lp = new JLabel("Nueva password:");
        JLabel lc = new JLabel("Confirmar password:");
        lu.setForeground(Color.WHITE); lp.setForeground(Color.WHITE); lc.setForeground(Color.WHITE);

        JTextField txtUser = new JTextField(jugadorActual.getUsername());
        JPasswordField txtPass = new JPasswordField();
        JPasswordField txtConfirm = new JPasswordField();
        JLabel lblError = new JLabel("");
        lblError.setForeground(new Color(231, 76, 60));

        p.add(lu); p.add(txtUser);
        p.add(lp); p.add(txtPass);
        p.add(lc); p.add(txtConfirm);
        p.add(new JLabel("")); p.add(lblError);

        int opcion = JOptionPane.showConfirmDialog(this, p, "Modificar Datos", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (opcion == JOptionPane.OK_OPTION) {
            String nuevoUser = txtUser.getText().trim();
            String nuevaPass = new String(txtPass.getPassword());
            String confirmar = new String(txtConfirm.getPassword());

            if (nuevoUser.isEmpty()) return;

       
            boolean userOcupado = false;
            for (Player p2 : jugadores) {
                if (p2.getUsername().equals(nuevoUser) && p2 != jugadorActual) {
                    userOcupado = true;
                    break;
                }
            }

            if (userOcupado) {
                JOptionPane.showMessageDialog(this, "Ese username ya esta en uso", "Error", JOptionPane.PLAIN_MESSAGE);
                return;
            }

            jugadorActual.setUsername(nuevoUser);

            if (!nuevaPass.isEmpty()) {
                if (!nuevaPass.equals(confirmar)) {
                    JOptionPane.showMessageDialog(this, "Las contrasenas no coinciden", "Error", JOptionPane.PLAIN_MESSAGE);
                    return;
                }
                jugadorActual.setPassword(nuevaPass);
            }
        }
    }

    private void eliminarCuenta() {
        if (jugadorActual == null) return;

        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Seguro que deseas eliminar tu cuenta? Esta accion no se puede deshacer.",
            "Confirmar eliminacion", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            jugadores.remove(jugadorActual);
            jugadorActual = null;
            cardLayout.show(panelPrincipal, "INICIO");
        }
    }


    private void iniciarJuego() {
        JPanel inputPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        inputPanel.setBackground(new Color(44, 62, 80));
        JLabel lbl = new JLabel("Username del oponente:");
        lbl.setForeground(Color.WHITE);
        JTextField txtOponente = new JTextField();
        inputPanel.add(lbl);
        inputPanel.add(txtOponente);

        int opcion = JOptionPane.showConfirmDialog(this, inputPanel, "Seleccionar Oponente",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (opcion != JOptionPane.OK_OPTION) return;

        String oponente = txtOponente.getText().trim();

        if (oponente.isEmpty() || oponente.equalsIgnoreCase("EXIT")) return;
        if (!existeJugador(oponente)) return;
        if (oponente.equals(jugadorActual.getUsername())) return;

     
        Player jugadorOponente = buscarJugador(oponente);

        this.setVisible(false);

        BattleShipGUI juego = new BattleShipGUI(
            jugadorActual, jugadorOponente, dificultad, modoJuego, this
        );
        juego.mostrar();
    }


 
    public static ArrayList<Player> getJugadores() {
        return jugadores;
    }

    private boolean validarLogin(String username, String password) {
        for (Player p : jugadores) {
            if (p.getUsername().equals(username) && p.getPassword().equals(password)) {
                jugadorActual = p;
                return true;
            }
        }
        return false;
    }

    private boolean existeJugador(String username) {
        for (Player p : jugadores) {
            if (p.getUsername().equals(username)) return true;
        }
        return false;
    }

    private Player buscarJugador(String username) {
        for (Player p : jugadores) {
            if (p.getUsername().equals(username)) return p;
        }
        return null;
    }


    private int indexOfCard(String nombre) {
        for (int i = 0; i < panelPrincipal.getComponentCount(); i++) {
            Component c = panelPrincipal.getComponent(i);
            if (c.getName() != null && c.getName().equals(nombre)) return i;
        }
        return -1;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(250, 50));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(color.brighter()); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(color); }
        });
        return btn;
    }
}