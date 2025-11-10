

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */package Juego;

import LogicaJuego.Player;
import MenuPrincipal.MenuPrincipal;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.Timer;

public class Tablero extends JFrame {

    private JButton[][] casillas = new JButton[6][6];
    private Pieza[][] piezas = new Pieza[6][6];

    private Player jugador1, jugador2;
    private Player jugadorActual;

    private int seleccionFila = -1, seleccionCol = -1;
    private Pieza piezaSeleccionada = null;
    private JButton botonSeleccionado = null;

    private boolean modoEspecial = false;
    private List<Point> casillasDisponibles = new ArrayList<>();
    private List<Point> casillasEspeciales = new ArrayList<>();

    private int necroOpcion = -1;
    private Pieza zombieSeleccionado = null;

    private JLabel lblTurno, lblAtaqueEspecial, lblRuleta;
    private JButton btnEspecial, btnDesactivarEspecial, btnPararRuleta;
    

    private final Color vino = new Color(90, 0, 40);
    private final Color negro = new Color(0, 0, 0);
    private final Color movimientoColor = Color.ORANGE;
    private final Color especialColor = Color.MAGENTA;

    // --- Ruleta ---
    private RuletaPanel ruletaPanel;
    private Timer timerRuleta;
    private boolean girandoRuleta = false;
    private String piezaActualRuleta = null;
    private double velocidadInercia;
    private boolean ruletaDetenida = false; // indica si ya se puede seleccionar piezas

    private JPanel panelDerecha;

    public Tablero(Player j1, Player j2) {
        jugador1 = j1;
        jugador2 = j2;
        jugadorActual = jugador1;

        setTitle("Vampire Wargame - Tablero");
        setSize(900, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        initUI();
        inicializarPiezas();
        iniciarRuleta(); // configura la ruleta lista para girar
        girarRuleta();
    }

    private void initUI() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(negro);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        add(panelPrincipal);

        // --- Panel superior ---
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(negro);
        lblTurno = new JLabel("Turno: " + jugadorActual.getUsername(), SwingConstants.CENTER);
        lblTurno.setForeground(Color.WHITE);
        lblTurno.setFont(new Font("Arial", Font.BOLD, 20));
        lblAtaqueEspecial = new JLabel("Ataque Especial: Ninguno", SwingConstants.CENTER);
        lblAtaqueEspecial.setForeground(Color.WHITE);
        lblAtaqueEspecial.setFont(new Font("Arial", Font.BOLD, 16));
        panelSuperior.add(lblTurno, BorderLayout.CENTER);
        panelSuperior.add(lblAtaqueEspecial, BorderLayout.SOUTH);
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);

        // ---------- PANEL DERECHO ----------
        panelDerecha = new JPanel();
        panelDerecha.setBackground(negro);
        panelDerecha.setLayout(new BoxLayout(panelDerecha, BoxLayout.Y_AXIS));
        panelDerecha.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Label Ruleta
        lblRuleta = new JLabel("Pieza Ruleta: ?");
        lblRuleta.setForeground(Color.WHITE);
        lblRuleta.setFont(new Font("Arial", Font.BOLD, 16));
        lblRuleta.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelDerecha.add(lblRuleta);
        panelDerecha.add(Box.createRigidArea(new Dimension(0, 10)));

        panelPrincipal.add(panelDerecha, BorderLayout.EAST);

        // --- Panel Tablero ---
        JPanel panelTablero = new JPanel(new GridLayout(6, 6, 2, 2));
        panelTablero.setBackground(negro);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(80, 80));
                btn.setFocusPainted(false);
                btn.setBackground((i + j) % 2 == 0 ? Color.GRAY : Color.DARK_GRAY);
                btn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                casillas[i][j] = btn;
                panelTablero.add(btn);
                final int fila = i, col = j;
                btn.addActionListener(e -> manejarClick(fila, col));
            }
        }
        panelPrincipal.add(panelTablero, BorderLayout.CENTER);

        // --- Panel inferior ---
        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(negro);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        JButton btnRetirar = new JButton("Retirarse");
        JButton btnVolver = new JButton("Volver al menú");
        JButton[] botonesInf = {btnRetirar, btnVolver};
        for (JButton b : botonesInf) {
            b.setFont(new Font("Arial", Font.BOLD, 16));
            b.setForeground(Color.WHITE);
            b.setBackground(vino);
            b.setFocusPainted(false);
            b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            b.setPreferredSize(new Dimension(150, 40));
            panelInferior.add(b);
        }
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        btnVolver.addActionListener(e -> {
    new MenuPrincipal(jugadorActual, null).setVisible(true); // pasar logica si la tienes
    dispose();
});

btnRetirar.addActionListener(e -> {
    JOptionPane.showMessageDialog(this, jugadorActual.getUsername() + " se ha retirado. " +
            (jugadorActual == jugador1 ? jugador2.getUsername() : jugador1.getUsername()) + " gana.");
    new MenuPrincipal(jugadorActual, null).setVisible(true); // pasar logica si la tienes
    dispose();
});


    }

    // -------------------- INICIALIZACIÓN --------------------
    private void inicializarPiezas(){
        piezas[0][0]=new Lobo(jugador2,"lobo2.png");
        piezas[0][1]=new Vampiro(jugador2,"vampiro2.png");
        piezas[0][2]=new Necromancer(jugador2,"necromancer2.png");
        piezas[0][3]=new Necromancer(jugador2,"necromancer2.png");
        piezas[0][4]=new Vampiro(jugador2,"vampiro2.png");
        piezas[0][5]=new Lobo(jugador2,"lobo2.png");

        piezas[5][0]=new Lobo(jugador1,"lobo.png");
        piezas[5][1]=new Vampiro(jugador1,"vampiro.png");
        piezas[5][2]=new Necromancer(jugador1,"necromancer.png");
        piezas[5][3]=new Necromancer(jugador1,"necromancer.png");
        piezas[5][4]=new Vampiro(jugador1,"vampiro.png");
        piezas[5][5]=new Lobo(jugador1,"lobo.png");

        actualizarTablero();
    }
    
    // -------------------- RULETA --------------------
    private void iniciarRuleta() {
        // Crear icons
        // Crear icons en orden: Vampiro - Lobo - Necromancer - Vampiro - Lobo - Necromancer
    ImageIcon[] icons = new ImageIcon[6];
    icons[0] = new ImageIcon(getClass().getResource("/Recursos/vampiro.png"));
    icons[1] = new ImageIcon(getClass().getResource("/Recursos/lobo.png"));
    icons[2] = new ImageIcon(getClass().getResource("/Recursos/necromancer.png"));
    icons[3] = new ImageIcon(getClass().getResource("/Recursos/vampiro.png"));
    icons[4] = new ImageIcon(getClass().getResource("/Recursos/lobo.png"));
    icons[5] = new ImageIcon(getClass().getResource("/Recursos/necromancer.png"));

// Escalar imágenes
    for (int i = 0; i < icons.length; i++) {
    Image img = icons[i].getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
    icons[i] = new ImageIcon(img);
}


        // Crear ruleta
        ruletaPanel = new RuletaPanel(icons);
        ruletaPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelDerecha.add(ruletaPanel);
        panelDerecha.add(Box.createRigidArea(new Dimension(0, 10)));

        // Botón Parar Ruleta
        btnPararRuleta = new JButton("Parar Ruleta");
        btnPararRuleta.setFont(new Font("Arial", Font.BOLD, 14));
        btnPararRuleta.setForeground(Color.WHITE);
        btnPararRuleta.setBackground(Color.DARK_GRAY);
        btnPararRuleta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPararRuleta.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelDerecha.add(btnPararRuleta);
        panelDerecha.add(Box.createRigidArea(new Dimension(0, 15)));

        btnPararRuleta.addActionListener(e -> {
            if (girandoRuleta) {
                detenerRuletaConInercia();
                btnPararRuleta.setEnabled(false);
            }
        });

        // Botones especiales
        btnEspecial = new JButton("Usar Especial");
        btnDesactivarEspecial = new JButton("Desactivar Especial");
        JButton[] botones = {btnEspecial, btnDesactivarEspecial};
        for (JButton b : botones) {
            b.setFont(new Font("Arial", Font.BOLD, 14));
            b.setBackground(new Color(60, 0, 0));
            b.setForeground(Color.WHITE);
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            b.setMaximumSize(new Dimension(180, 40));
            panelDerecha.add(b);
            panelDerecha.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        btnEspecial.addActionListener(e -> activarEspecial());
        btnDesactivarEspecial.addActionListener(e -> limpiarMarcado());

    }

    public void girarRuleta() {
        if (timerRuleta != null && timerRuleta.isRunning()) timerRuleta.stop();
        girandoRuleta = true;
        double velocidadRuleta = 20 + Math.random() * 10;

        timerRuleta = new Timer(30, e -> {
            ruletaPanel.setAngulo(ruletaPanel.getAngulo() + velocidadRuleta);
            ruletaPanel.setAnguloPiezas(ruletaPanel.getAnguloPiezas() - velocidadRuleta);
        });
        timerRuleta.start();
    }

    public void detenerRuletaConInercia() {
        if (timerRuleta != null && timerRuleta.isRunning()) timerRuleta.stop();
        velocidadInercia = 15 + Math.random() * 10;

        Timer timerInercia = new Timer(30, null);
        long startTime = System.currentTimeMillis();

        timerInercia.addActionListener(ev -> {
    long elapsed = System.currentTimeMillis() - startTime;
    if (elapsed < 2000) {
        ruletaPanel.setAngulo(ruletaPanel.getAngulo() + velocidadInercia);
        ruletaPanel.setAnguloPiezas(ruletaPanel.getAnguloPiezas() - velocidadInercia);
        velocidadInercia *= 0.95;
    } else {
        timerInercia.stop();
        girandoRuleta = false;

        int indice = ruletaPanel.getIndiceSeleccionado();
        String[] tipos = {"Vampiro", "Lobo", "Necromancer", "Vampiro", "Lobo", "Necromancer"};
        piezaActualRuleta = tipos[indice];


        lblRuleta.setText("Pieza Ruleta: " + piezaActualRuleta);
        JOptionPane.showMessageDialog(this, "PIEZA DISPONIBLE: " + piezaActualRuleta);

        // ⚡ Habilitar selección de piezas
        ruletaDetenida = true;
    }
});

        timerInercia.start();
    }
// Validar pieza según ruleta
private boolean validarPiezaSeleccionada(Pieza p){
    if(piezaActualRuleta==null) return true;
    if(!p.getTipo().equals(piezaActualRuleta)){
        JOptionPane.showMessageDialog(this,"Pieza no disponible. Solo puedes usar " + piezaActualRuleta);
        return false;
    }
    return true;
}

// Giros extra según piezas perdidas
private int calcularGirosExtra(Player jugador){
    int perdidas = contarPiezasPerdidas(jugador);
    if(perdidas >= 4) return 3;
    else if(perdidas >= 2) return 2;
    else return 1;
}

    private int contarPiezasPerdidas(Player jugador) {
        return contarPiezasPerdidasRecursivo(jugador, 0, 0, 0);
    }

// Método auxiliar recursivo
    private int contarPiezasPerdidasRecursivo(Player jugador, int fila, int col, int total) {
    if (fila >= 6) return 6 - total; // base: total perdido = 6 - total en tablero
    if (col >= 6) return contarPiezasPerdidasRecursivo(jugador, fila + 1, 0, total);

    if (piezas[fila][col] != null && piezas[fila][col].getRetador() == jugador) total++;

    return contarPiezasPerdidasRecursivo(jugador, fila, col + 1, total);
}

    // -------------------- MANEJO DE CLICK --------------------
    // Método principal que se llama al hacer clic en el tablero
private void manejarClick(int fila, int col) {
    Pieza p = piezas[fila][col];
    Point pClick = new Point(fila, col);

    // 1) Si no hay piezaSeleccionada: intentar seleccionar (ruleta/propiedad/tipo lo valida en seleccionarPieza)
    if (piezaSeleccionada == null) {
        seleccionarPieza(fila, col);
        return;
    }

    // 2) Caso especial: si ya hay un Necromancer seleccionado y estamos en modoEspecial -> necroOpcion == 2 (ataque por zombie)
    //    Permitir seleccionar un zombie propio para luego realizar el ataque por zombie.
    if (modoEspecial && piezaSeleccionada instanceof Necromancer && necroOpcion == 2) {
        // Si el clic es sobre un zombie propio, lo seleccionamos como zombieSeleccionado y marcamos sus casillas de ataque
        if (p != null && p instanceof Zombie && p.getRetador() == jugadorActual) {
            // quitar borde de selección anterior si había (ej: necromancer)
            if (botonSeleccionado != null) botonSeleccionado.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            // marcar zombie seleccionado
            zombieSeleccionado = (Zombie) p;
            botonSeleccionado = casillas[fila][col];
            botonSeleccionado.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
            marcarCasillasAtaqueZombie(fila, col);
            lblAtaqueEspecial.setText("Ataque Especial: Zombie seleccionado");
            return;
        }

        // Si se hace clic en una casilla marcada como objetivo del attack por zombie, delegar al handler
        if (casillasEspeciales.contains(pClick)) {
            manejarClickEspecialNecro(fila, col, p);
            return;
        }

        // Si no es ni zombie propio ni casilla objetivo: informar y salir
        JOptionPane.showMessageDialog(this, "Selecciona un Zombie propio o una casilla marcada para atacar.");
        return;
    }

    // 3) Si hay una pieza seleccionada y el clic es sobre otra de tus piezas -> cambiar selección a esa nueva pieza
    if (p != null && p.getRetador() == jugadorActual) {
        // permite cambiar selección rápida a otra pieza propia
        seleccionarPieza(fila, col);
        return;
    }

    // 4) Si llegamos aquí, intentamos mover/atacar hacia la casilla (vacía o enemigo)
    moverPieza(fila, col);
}



private void moverPieza(int fila, int col) {
    // Si no hay pieza seleccionada, nada que hacer
    if (piezaSeleccionada == null) return;

    // Si estamos en modo especial, delegar a los manejadores correspondientes
    if (modoEspecial) {
        if (piezaSeleccionada instanceof Lobo) {
            // Lobo usa su handler especial (verifica casillasEspeciales dentro del método)
            manejarClickEspecialLobo(fila, col);
            return;
        }
        if (piezaSeleccionada instanceof Necromancer) {
            // Necromancer tiene varios sub-modos (conjurar, ataque por zombie, ataque a 2)
            // pasar la pieza que hay en la casilla para que el handler lo interprete
            Pieza p = piezas[fila][col];
            manejarClickEspecialNecro(fila, col, p);
            return;
        }
    }

    // NO modo especial: validar que la casilla destino esté entre las disponibles
    Point dest = new Point(fila, col);
    boolean valido = false;
    for (Point pt : casillasDisponibles) {
        if (pt.equals(dest)) {
            valido = true;
            break;
        }
    }
    if (!valido) {
        JOptionPane.showMessageDialog(this, "Movimiento inválido. Selecciona una casilla válida.");
        return;
    }

    // Si es movimiento/ataque normal: usar resolverCombate que ya tienes
    resolverCombate(piezaSeleccionada, fila, col);

    // Limpiar selección y marcados (resolverCombate ya actualiza el tablero)
    if (botonSeleccionado != null) {
        botonSeleccionado.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    }
    piezaSeleccionada = null;
    botonSeleccionado = null;
    seleccionFila = -1;
    seleccionCol = -1;
    limpiarMarcado();

    // Finalizar turno (gira ruleta, limpia selección y cambia turno)
    finalizarTurno();
}





    private void manejarClickNormal(int fila, int col){
        resolverCombate(piezaSeleccionada,fila,col);
        finalizarTurno();
    }

    private void manejarClickEspecialLobo(int fila,int col){
        Point pClick = new Point(fila,col);
        if(casillasEspeciales.contains(pClick)){
            resolverCombate(piezaSeleccionada,fila,col);
            finalizarTurno();
        }
    }

    // -------------------- MANEJO DE CLICK NECROMANCER --------------------
private void manejarClickEspecialNecro(int fila, int col, Pieza p){
    Point pClick = new Point(fila,col);

    // 1️⃣ Conjurar Zombie
    if(necroOpcion == 1 && piezas[fila][col] == null && casillasEspeciales.contains(pClick)){
        Necromancer necro = (Necromancer) piezaSeleccionada;
        Zombie nuevoZombie = necro.conjurarZombie(fila, col);

        // Colocar zombie en el tablero
        piezas[fila][col] = nuevoZombie;

        JOptionPane.showMessageDialog(this,
            "Zombie conjurado por " + jugadorActual.getUsername() + " en (" + fila + "," + col + ")");

        zombieSeleccionado = null;
        actualizarTablero();
        finalizarTurno();
        return;
    }

    // 2️⃣ Selección de zombie para ataque por zombie
    if(necroOpcion == 2 && p instanceof Zombie && p.getRetador() == jugadorActual){
        zombieSeleccionado = (Zombie) p;
        marcarCasillasAtaqueZombie(fila, col);
        lblAtaqueEspecial.setText("Ataque Especial: Zombie seleccionado");
        return;
    }

    // 3️⃣ Ataque especial (opción 0 o ataque por zombie)
    if(casillasEspeciales.contains(pClick)){
        Pieza objetivo = piezas[fila][col];

        if(objetivo != null && objetivo.getRetador() != jugadorActual){
            if(necroOpcion == 0){
                // Ataque normal de Necromancer
                resolverCombate(piezaSeleccionada, fila, col);
            } 
            else if(necroOpcion == 2 && zombieSeleccionado != null){
                // Usar la misma instancia del zombie para atacar
                int filaZombie = -1, colZombie = -1;

                // Buscar la posición actual del zombieSeleccionado
                for(int i=0; i<6; i++)
                    for(int j=0; j<6; j++)
                        if(piezas[i][j] == zombieSeleccionado){
                            filaZombie = i;
                            colZombie = j;
                        }

                if(filaZombie != -1 && colZombie != -1){
                    Zombie z = (Zombie) zombieSeleccionado;

                    Pieza destino = piezas[fila][col];

                    if (destino != null) {
    destino.recibirDanio(z.getAtaque());
    if (!destino.estaViva()) {
        // Eliminar la pieza enemiga, PERO NO mover el zombie: el zombie SE QUEDA en su casilla original
        piezas[fila][col] = null; // ya estaba destino, ahora eliminada
        // no hacer piezas[fila][col] = z;
        JOptionPane.showMessageDialog(this, "Zombie ha eliminado la pieza enemiga y permanece en su casilla.");
    } else {
        JOptionPane.showMessageDialog(this, "Zombie ha atacado. Vida restante del objetivo: " + destino.getVida());
    }
} else {
    // Casilla vacía: mover el zombie como antes
    piezas[fila][col] = z;
    piezas[filaZombie][colZombie] = null;
    z.setPosicion(fila, col);

    // Sprite según jugador
    if (z.getRetador().getUsername().equals(jugador1.getUsername()))
        z.setImagen("zombie.png");
    else
        z.setImagen("zombie2.png");
}

                }
            }
        }

        zombieSeleccionado = null;
        actualizarTablero();
        finalizarTurno();
    }
}



    // -------------------- MÉTODOS AUXILIARES --------------------
   // Método para seleccionar una pieza válida
private void seleccionarPieza(int fila, int col) {
    // 1️⃣ Bloquear si la ruleta no se ha detenido
    if (!ruletaDetenida) {
        JOptionPane.showMessageDialog(this, "Primero debes detener la ruleta antes de seleccionar una pieza.");
        return;
    }

    Pieza p = piezas[fila][col];

    // 2️⃣ Validar que la pieza exista
    if (p == null) {
        JOptionPane.showMessageDialog(this, "No hay ninguna pieza en esa casilla.");
        return;
    }

    // 3️⃣ No permitir seleccionar piezas del rival
    if (p.getRetador() != jugadorActual) {
        JOptionPane.showMessageDialog(this, "No puedes seleccionar piezas del rival.");
        return;
    }

    // 4️⃣ Validar si la selección es válida según el modo y la ruleta
    boolean seleccionValida = false;

    if (modoEspecial && piezaSeleccionada instanceof Necromancer && necroOpcion == 2) {
        // 💀 Ataque por Zombie: permitir seleccionar zombies propios
        if (p instanceof Zombie) {
            seleccionValida = true;
        }
    } else {
        // 🧛‍♂️ Turno normal: permitir solo pieza que salió en la ruleta
        if (piezaActualRuleta == null || p.getTipo().equals(piezaActualRuleta)) {
            seleccionValida = true;
        }
    }

    if (!seleccionValida) {
        JOptionPane.showMessageDialog(this, "Pieza no disponible. Solo puedes usar " + piezaActualRuleta);
        return;
    }

    // 5️⃣ Limpiar selección previa (si había una marcada)
    if (botonSeleccionado != null) {
        botonSeleccionado.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    }

    // 6️⃣ Marcar la nueva selección
    piezaSeleccionada = p;
    botonSeleccionado = casillas[fila][col];
    seleccionFila = fila;
    seleccionCol = col;
    botonSeleccionado.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));

    // 7️⃣ Marcar movimientos disponibles (solo si no es modo especial del Necromancer)
    if (!modoEspecial || !(piezaSeleccionada instanceof Necromancer)) {
        marcarMovimientoDisponible();
    }
}







    private void marcarMovimientoDisponible(){
        limpiarMarcado();
        casillasDisponibles.clear();

        List<int[]> posibles = new ArrayList<>();

        if(modoEspecial && piezaSeleccionada instanceof Lobo){
            // Se maneja directamente en la acción especial
        } else {
            int[] offsets = {-1,0,1};
            for(int df: offsets){
                for(int dc: offsets){
                    if(df==0 && dc==0) continue;
                    int nf = seleccionFila + df;
                    int nc = seleccionCol + dc;
                    if(nf>=0 && nf<6 && nc>=0 && nc<6 &&
                            (piezas[nf][nc]==null || piezas[nf][nc].getRetador()!=jugadorActual)){
                        posibles.add(new int[]{nf,nc});
                    }
                }
            }
        }

        for(int[] c: posibles){
            casillas[c[0]][c[1]].setBackground(movimientoColor);
            casillasDisponibles.add(new Point(c[0],c[1]));
        }
    }

    private void marcarCasillasAtaqueZombie(int filaZombie,int colZombie){
        casillasEspeciales.clear();
        List<int[]> adj = zombieSeleccionado.obtenerCasillasEspecial(piezas,filaZombie,colZombie,0);
        for(int[] c: adj){
            casillasEspeciales.add(new Point(c[0],c[1]));
            casillas[c[0]][c[1]].setBackground(especialColor);
        }
    }
    
    private void limpiarMarcado() {
        limpiarMarcadoRecursivo(0, 0);
        casillasDisponibles.clear();
        casillasEspeciales.clear();
        modoEspecial = false;
        necroOpcion = -1;
        lblAtaqueEspecial.setText("Ataque Especial: Ninguno");
    }

// Método auxiliar recursivo
    private void limpiarMarcadoRecursivo(int fila, int col) {
        if (fila >= 6) return; // base
        if (col >= 6) { limpiarMarcadoRecursivo(fila + 1, 0); return; }

        casillas[fila][col].setBackground((fila + col) % 2 == 0 ? Color.GRAY : Color.DARK_GRAY);

        limpiarMarcadoRecursivo(fila, col + 1); // siguiente columna
    }
    private void limpiarSeleccion(){
        if(botonSeleccionado!=null) botonSeleccionado.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
        piezaSeleccionada=null;
        botonSeleccionado=null;
        limpiarMarcado();
    }

   private void resolverCombate(Pieza atacante, int filaDestino, int colDestino) {
    Pieza destino = piezas[filaDestino][colDestino];

    // Caso 1: casilla destino VACÍA -> mover atacante
    if (destino == null) {
        piezas[filaDestino][colDestino] = atacante;
        piezas[seleccionFila][seleccionCol] = null;

        // Actualizar posición si es Zombie (solo si se MOVIÓ)
        if (atacante instanceof Zombie) {
            Zombie z = (Zombie) atacante;
            z.setPosicion(filaDestino, colDestino);
            if (z.getRetador().getUsername().equals("Jugador1")) z.setImagen("zombie.png");
            else z.setImagen("zombie2.png");
        }
    } 
    // Caso 2: hay enemigo -> aplicar daño, pero si muere NO mover al atacante (queda en su casilla)
    else if (destino.getRetador() != atacante.getRetador()) {
        destino.recibirDanio(atacante.getAtaque());

        if (!destino.estaViva()) {
            // Eliminar la pieza enemiga (la casilla queda vacía)
            piezas[filaDestino][colDestino] = null;

            // IMPORTANTE: no mover al atacante; queda en (seleccionFila, seleccionCol)
            // Si necesitas algún efecto (animación, mensaje), hazlo aquí:
            JOptionPane.showMessageDialog(this, "Pieza eliminada. El atacante permanece en su lugar.");
        } else {
            // Si el enemigo no muere, no hay cambio en posiciones
            JOptionPane.showMessageDialog(this,
            "SE ATACÓ A " + destino.getTipo() + 
            " Y SE LE QUITARON " + atacante.getAtaque() + " PUNTOS.\n" +
            "LE QUEDAN " + destino.getEscudo() + " DE ESCUDO Y " + destino.getVida() + " DE VIDA.");

        }
    }

    // Actualizar UI
    actualizarTablero();
}




    private void finalizarTurno() {
    limpiarMarcado();
    limpiarSeleccion();
    cambiarTurno();

    // Reiniciar selección y ruleta
    botonSeleccionado = null;
    piezaSeleccionada = null;
    piezaActualRuleta = null;
    ruletaDetenida = false;

    // Habilitar botón de parar ruleta al inicio del nuevo turno
    btnPararRuleta.setEnabled(true);

    // Girar ruleta automáticamente al iniciar turno
    girarRuleta();
}

    private void actualizarTablero(){
        for(int i=0;i<6;i++)
            for(int j=0;j<6;j++)
                casillas[i][j].setIcon(piezas[i][j]!=null?piezas[i][j].getImagen():null);
    }


    private void cambiarTurno(){
        jugadorActual=(jugadorActual==jugador1?jugador2:jugador1);
        lblTurno.setText("Turno: "+jugadorActual.getUsername());
    }

    // -------------------- ESPECIALES --------------------
    private void activarEspecial() {
    if(piezaSeleccionada == null){
        JOptionPane.showMessageDialog(this,"Selecciona primero una pieza propia.");
        return;
    }

    modoEspecial = true;
    casillasEspeciales.clear();

    if(piezaSeleccionada instanceof Lobo){
        // Lobo especial
        List<int[]> especiales = piezaSeleccionada.obtenerCasillasEspecial(piezas, seleccionFila, seleccionCol, 0);
        for(int[] c : especiales){
            casillasEspeciales.add(new Point(c[0], c[1]));
            casillas[c[0]][c[1]].setBackground(especialColor);
        }
        lblAtaqueEspecial.setText("Ataque Especial: Lobo (2 casillas adyacentes)");
        return;
    }

    if(piezaSeleccionada instanceof Necromancer){
        String[] opciones={"Ataque a 2","Conjurar Zombie","Ataque por Zombie"};
        necroOpcion = JOptionPane.showOptionDialog(this,"Selecciona ataque especial",
                "Necromancer",JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,opciones,opciones[0]);

        switch(necroOpcion){
            case 0: // Ataque a 2
                List<int[]> casillasAtaque = piezaSeleccionada.obtenerCasillasEspecial(piezas, seleccionFila, seleccionCol, necroOpcion);
                for(int[] c: casillasAtaque){
                    casillasEspeciales.add(new Point(c[0], c[1]));
                    casillas[c[0]][c[1]].setBackground(especialColor);
                }
                lblAtaqueEspecial.setText("Ataque Especial: Ataque a 2");
                break;

            case 1: // Conjurar Zombie
    ArrayList<int[]> casillasConjurar = ((Necromancer) piezaSeleccionada)
            .obtenerCasillasConjurar(piezas, seleccionFila, seleccionCol);

    for (int[] c : casillasConjurar) {
        casillasEspeciales.add(new Point(c[0], c[1]));
        casillas[c[0]][c[1]].setBackground(Color.GREEN); // Verde = conjurar
    }

    lblAtaqueEspecial.setText("Ataque Especial: Conjurar Zombie");
    break;

            case 2: // Ataque por Zombie
                boolean tieneZombies = false;
                for(int i=0;i<6;i++)
                    for(int j=0;j<6;j++)
                        if(piezas[i][j] instanceof Zombie && piezas[i][j].getRetador()==jugadorActual)
                            tieneZombies = true;

                if(!tieneZombies){
                    JOptionPane.showMessageDialog(this,"No hay Zombies conjurados");
                    modoEspecial=false;
                    return;
                }
                // Marcar todos los zombies del jugador
                for(int i=0;i<6;i++)
                    for(int j=0;j<6;j++)
                        if(piezas[i][j] instanceof Zombie && piezas[i][j].getRetador()==jugadorActual){
                            casillasEspeciales.add(new Point(i,j));
                            casillas[i][j].setBackground(Color.CYAN); // Azul = seleccionar Zombie
                        }
                lblAtaqueEspecial.setText("Ataque Especial: Selecciona un Zombie");
                break;
        }
    }
}

}