

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */package Juego;

import LogicaJuego.Player;
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
    private JButton btnEspecial, btnDesactivarEspecial;

    private final Color vino = new Color(90, 0, 40);
    private final Color negro = new Color(0, 0, 0);
    private final Color movimientoColor = Color.PINK;
    private final Color especialColor = new Color(150, 0, 0);

    private Timer timerRuleta;
    private ImageIcon[] ruletaIcons;
    private int giroActual = 0, totalGiros = 0, velocidad = 150;

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
        cargarRuleta();
        girarRuleta();
    }

    // -------------------- INTERFAZ --------------------
    private void initUI() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(negro);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        add(panelPrincipal);

        // Panel superior
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

        // Panel derecho: ruleta + botones
        JPanel panelDerecha = new JPanel();
        panelDerecha.setBackground(negro);
        panelDerecha.setLayout(new BoxLayout(panelDerecha, BoxLayout.Y_AXIS));

        lblRuleta = new JLabel("Pieza Ruleta: ?");
        lblRuleta.setForeground(Color.WHITE);
        lblRuleta.setFont(new Font("Arial", Font.BOLD, 16));
        lblRuleta.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelDerecha.add(lblRuleta);
        panelDerecha.add(Box.createRigidArea(new Dimension(0,10)));

        btnEspecial = new JButton("Usar Especial");
        btnDesactivarEspecial = new JButton("Desactivar Especial");
        JButton[] botones = {btnEspecial, btnDesactivarEspecial};

        for(JButton b : botones){
            b.setFont(new Font("Arial", Font.BOLD, 16));
            b.setForeground(Color.WHITE);
            b.setBackground(vino);
            b.setFocusPainted(false);
            b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelDerecha.add(Box.createRigidArea(new Dimension(0,10)));
            panelDerecha.add(b);
        }

        panelPrincipal.add(panelDerecha, BorderLayout.EAST);

        // Panel tablero
        JPanel panelTablero = new JPanel(new GridLayout(6,6,2,2));
        panelTablero.setBackground(negro);
        for(int i=0;i<6;i++){
            for(int j=0;j<6;j++){
                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(80,80));
                btn.setFocusPainted(false);
                btn.setBackground((i+j)%2==0?Color.GRAY:Color.DARK_GRAY);
                btn.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
                casillas[i][j]=btn;
                panelTablero.add(btn);
                final int fila=i,col=j;
                btn.addActionListener(e -> manejarClick(fila,col));
            }
        }
        panelPrincipal.add(panelTablero, BorderLayout.CENTER);

        // Panel inferior
        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(negro);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(15,0,0,0));
        JButton btnRetirar = new JButton("Retirarse");
        JButton btnVolver = new JButton("Volver al menú");
        JButton[] botonesInf = {btnRetirar, btnVolver};
        for(JButton b: botonesInf){
            b.setFont(new Font("Arial", Font.BOLD, 16));
            b.setForeground(Color.WHITE);
            b.setBackground(vino);
            b.setFocusPainted(false);
            b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            b.setPreferredSize(new Dimension(150,40));
            panelInferior.add(b);
        }
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        btnVolver.addActionListener(e -> dispose());
        btnRetirar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, jugadorActual.getUsername()+" se ha retirado. "+
                    (jugadorActual==jugador1?jugador2.getUsername():jugador1.getUsername())+" gana.");
            dispose();
        });

        btnEspecial.addActionListener(e -> activarEspecial());
        btnDesactivarEspecial.addActionListener(e -> limpiarSeleccion());
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

    // -------------------- MANEJO DE CLICK --------------------
    private void manejarClick(int fila, int col){
        Pieza p = piezas[fila][col];
        Point pClick = new Point(fila,col);

        // Selección de pieza propia
        if(p!=null && p.getRetador()==jugadorActual && !(necroOpcion==2 && p instanceof Zombie)){
            seleccionarPieza(fila,col);
            if(!modoEspecial || !(piezaSeleccionada instanceof Necromancer))
                marcarMovimientoDisponible();
            return;
        }

        // Modo especial activo
        if(modoEspecial){
            if(piezaSeleccionada instanceof Lobo){
                manejarClickEspecialLobo(fila,col);
                return;
            }
            if(piezaSeleccionada instanceof Necromancer){
                manejarClickEspecialNecro(fila,col,p);
                return;
            }
        }

        // Movimiento normal
        if(piezaSeleccionada!=null && casillasDisponibles.contains(pClick)){
            manejarClickNormal(fila,col);
        }
        
        

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

                    if(destino != null){
                        destino.recibirDanio(z.getAtaque());
                        if(!destino.estaViva()){
                            // Zombie ocupa la casilla del enemigo
                            piezas[fila][col] = z;
                            piezas[filaZombie][colZombie] = null;
                            z.setPosicion(fila, col);

                            // Sprite según jugador
                            if(z.getRetador().getUsername().equals(jugador1.getUsername()))
                                z.setImagen("zombie.png");
                            else
                                z.setImagen("zombie2.png");
                        }
                    } else {
                        // Casilla vacía: solo mover zombie
                        piezas[fila][col] = z;
                        piezas[filaZombie][colZombie] = null;
                        z.setPosicion(fila, col);
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
    private void seleccionarPieza(int fila,int col){
        limpiarMarcado();
        if(botonSeleccionado!=null) botonSeleccionado.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
        piezaSeleccionada=piezas[fila][col];
        botonSeleccionado=casillas[fila][col];
        seleccionFila=fila;
        seleccionCol=col;
        botonSeleccionado.setBorder(BorderFactory.createLineBorder(Color.YELLOW,3));
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

    private void limpiarMarcado(){
        for(int i=0;i<6;i++) for(int j=0;j<6;j++)
            casillas[i][j].setBackground((i+j)%2==0?Color.GRAY:Color.DARK_GRAY);
        casillasDisponibles.clear();
        casillasEspeciales.clear();
        modoEspecial=false;
        necroOpcion=-1;
        lblAtaqueEspecial.setText("Ataque Especial: Ninguno");
    }

    private void limpiarSeleccion(){
        if(botonSeleccionado!=null) botonSeleccionado.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
        piezaSeleccionada=null;
        botonSeleccionado=null;
        limpiarMarcado();
    }

   private void resolverCombate(Pieza atacante, int filaDestino, int colDestino) {
    Pieza destino = piezas[filaDestino][colDestino];

    if (destino == null) {
        // Mover atacante a la casilla vacía
        piezas[filaDestino][colDestino] = atacante;
        piezas[seleccionFila][seleccionCol] = null;

        // Actualizar posición si es Zombie
        if(atacante instanceof Zombie){
            Zombie z = (Zombie) atacante;
            z.setPosicion(filaDestino, colDestino);
            // Ajustar sprite según jugador
            if(z.getRetador().getUsername().equals("Jugador1")) z.setImagen("zombie.png");
            else z.setImagen("zombie2.png");
        }

    } else if (destino.getRetador() != atacante.getRetador()) {
        destino.recibirDanio(atacante.getAtaque());

        if (!destino.estaViva()) {
            // El atacante ocupa la casilla del enemigo
            piezas[filaDestino][colDestino] = atacante;
            piezas[seleccionFila][seleccionCol] = null;

            // Actualizar posición si es Zombie
            if(atacante instanceof Zombie){
                Zombie z = (Zombie) atacante;
                z.setPosicion(filaDestino, colDestino);
                if(z.getRetador().getUsername().equals("Jugador1")) z.setImagen("zombie.png");
                else z.setImagen("zombie2.png");
            }
        }
    }

    actualizarTablero();
}



    private void finalizarTurno(){
        limpiarMarcado();
        limpiarSeleccion();
        cambiarTurno();
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


    // -------------------- RULETA --------------------
    private void cargarRuleta(){
        int w = 100, h = 100;
        ruletaIcons = new ImageIcon[]{
            new ImageIcon(getClass().getResource("/Recursos/lobo.png")),
            new ImageIcon(getClass().getResource("/Recursos/vampiro.png")),
            new ImageIcon(getClass().getResource("/Recursos/necromancer.png"))
        };
        for(int i=0;i<ruletaIcons.length;i++){
            Image img = ruletaIcons[i].getImage().getScaledInstance(w,h,Image.SCALE_SMOOTH);
            ruletaIcons[i]=new ImageIcon(img);
        }
        lblRuleta.setPreferredSize(new Dimension(w,h));
        lblRuleta.setHorizontalAlignment(SwingConstants.CENTER);
        lblRuleta.setVerticalAlignment(SwingConstants.CENTER);
    }

    private void girarRuleta(){
        if(timerRuleta!=null && timerRuleta.isRunning()) timerRuleta.stop();
        giroActual=0;
        totalGiros=calcularGirosExtra(jugadorActual)*ruletaIcons.length*2;
        velocidad=100;
        timerRuleta=new Timer(velocidad,null);
        timerRuleta.addActionListener(e->{
            lblRuleta.setIcon(ruletaIcons[giroActual%ruletaIcons.length]);
            giroActual++;
            if(giroActual>=totalGiros) timerRuleta.stop();
            else if(giroActual>totalGiros/2 && velocidad<500){
                velocidad+=10;
                timerRuleta.setDelay(velocidad);
            }
        });
        timerRuleta.start();
    }

    private int calcularGirosExtra(Player jugador){
        int perdidas=contarPiezasPerdidas(jugador);
        if(perdidas>=4) return 3;
        else if(perdidas>=2) return 2;
        else return 1;
    }

    private int contarPiezasPerdidas(Player jugador){
        int total=0;
        for(int i=0;i<6;i++)
            for(int j=0;j<6;j++)
                if(piezas[i][j]!=null && piezas[i][j].getRetador()==jugador) total++;
        return 6-total;
    }

}
