package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;

import controlador.ControladorVentana;
import lexico.AnalizadorLexico;
import sintaxis.AnalizadorSintactico;

/**
 * Esta clase contiene la interfaz grafica del compilador
 */
public class VentanaCompilador extends JFrame implements ActionListener, KeyListener {

	private JPanel panelEditor, panelErrores, panelSimbolos, panelErroresSintacticos, panelErroresSemanticos, panel,
			panelArbol;
	private JTable errores, simbolos, erroresSintacticos;
	private JScrollPane scroll, scrollArbol, scrollSimbolos, scrollErrores, scrollErroresSintacticos,
			scrollErroresSemanticos;
	private JTree arbolVisual;
	private JMenu mnEjecutar, mnArchivo, mnTema;
	private JMenuBar menuBar;
	private JMenuItem mntmAbrir, mntmEjecutar, mntmCompilar, mntmNuevo, mntmGuardar, mntmOscuro, mntmClaro;
	private JTextArea textEditor, erroresSemanticos;
	private JEditorPane editor, linea;
	private ControladorVentana controladorVentana;
	private GroupLayout gl_panel;
	private AnalizadorLexico analizadorLexico;
	private AnalizadorSintactico analizadorSintactico;
	private boolean compilado;

	/**
	 * 
	 */
	private static final long serialVersionUID = 3622765441129668108L;

	/**
	 * Metodo constructor, aca se define el molde grafico del compilador
	 */
	public VentanaCompilador() {
		setResizable(false);
		setTitle("Compilador");
		//setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/res/cms.png")));
		setBounds(100, 100, 1280, 720);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		compilado = false;
		init();
		controladorVentana = new ControladorVentana(this);
		setVisible(true);
		setLocationRelativeTo(null);
	}

	/**
	 * Inicializa los componentes graficos de la interfaz y los organiza dentro de
	 * llea
	 */
	private void init() {

		// Molde principal
		getContentPane().setLayout(null);
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 1280, 720);
		tabbedPane.setBorder(new TitledBorder(null, "Compilador", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		panelSimbolos = new JPanel(new BorderLayout());
		panelSimbolos.setBounds(0, 0, 1280, 720);
		panelSimbolos.setBorder(new TitledBorder(null, "Simbolos", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		panelErroresSintacticos = new JPanel(new BorderLayout());
		panelErroresSintacticos.setBounds(0, 0, 1280, 720);
		panelErroresSintacticos.setBorder(
				new TitledBorder(null, "Errores sintacticos", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		panelErrores = new JPanel(new BorderLayout());
		panelErrores.setBounds(0, 0, 1280, 720);
		panelErrores.setBorder(new TitledBorder(null, "Errores", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		panelEditor = new JPanel(null);
		panelEditor.setBounds(0, 0, 1280, 720);
		panelEditor.setBorder(new TitledBorder(null, "Editor", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		panelErroresSemanticos = new JPanel(null);
		panelErroresSemanticos.setBounds(0, 0, 1280, 720);
		panelErroresSemanticos.setBorder(
				new TitledBorder(null, "Errores semánticos", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		panelArbol = new JPanel(null);
		panelArbol.setBounds(0, 0, 1280, 720);
		panelArbol.setBorder(new TitledBorder(null, "Arbol", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(tabbedPane);

		tabbedPane.addTab("Compilador", null, panelEditor, null);
		tabbedPane.addTab("Simbolos", null, panelSimbolos, null);
		tabbedPane.addTab("Errores", null, panelErrores, null);
		tabbedPane.addTab("Arbol", null, panelArbol, null);
		tabbedPane.addTab("Errores sintácticos", null, panelErroresSintacticos, null);
		tabbedPane.addTab("Errores semánticos", null, panelErroresSemanticos, null);

		// Arbol
		arbolVisual = new JTree(new DefaultMutableTreeNode("Arbol visual"));
		arbolVisual.setBounds(0, 0, 1280, 720);

		// Panel editor
		linea = new JEditorPane();
		linea.setText("1\n");
		linea.setEditable(false);

		editor = new JEditorPane();

		menuBar = new JMenuBar();
		menuBar.setBounds(0, 20, 170, 20);

		mnArchivo = new JMenu("Archivo");
		mnArchivo.setFont(new Font("Century Gothic", Font.BOLD, 13));
		menuBar.add(mnArchivo);

		mnEjecutar = new JMenu("Compilar");
		mnEjecutar.setFont(new Font("Century Gothic", Font.BOLD, 13));
		menuBar.add(mnEjecutar);

		mnTema = new JMenu("Tema");
		mnTema.setFont(new Font("Century Gothic", Font.BOLD, 13));
		menuBar.add(mnTema);

		mntmAbrir = new JMenuItem("Abrir");
		mntmAbrir.addActionListener(this);
		mnArchivo.add(mntmAbrir);

		mntmNuevo = new JMenuItem("Nuevo");
		mntmNuevo.addActionListener(this);
		mnArchivo.add(mntmNuevo);

		mntmGuardar = new JMenuItem("Guardar");
		mntmGuardar.addActionListener(this);
		mnArchivo.add(mntmGuardar);

		mntmClaro = new JMenuItem("Claro");
		mntmClaro.addActionListener(this);
		mnTema.add(mntmClaro);

		mntmOscuro = new JMenuItem("Oscuro");
		mntmOscuro.addActionListener(this);
		mnTema.add(mntmOscuro);

		mntmEjecutar = new JMenuItem("Ejecutar");
		mntmEjecutar.addActionListener(this);
		mnEjecutar.add(mntmEjecutar);
		
		mntmCompilar = new JMenuItem("Compilar");
		mntmCompilar.addActionListener(this);
		mnEjecutar.add(mntmCompilar);

		panelEditor.add(menuBar);

		scroll = new JScrollPane();
		scroll.setBounds(2, 37, 1253, 601);
		panelEditor.add(scroll);

		scrollArbol = new JScrollPane(arbolVisual);
		scrollArbol.setBounds(2, 37, 1253, 601);
		panelArbol.add(scrollArbol);

		panel = new JPanel();
		scroll.setViewportView(panel);

		gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addContainerGap()
						.addComponent(linea, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(editor, GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE).addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
				gl_panel.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
								.addComponent(editor, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
								.addComponent(linea, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE))
						.addContainerGap()));
		panel.setLayout(gl_panel);

		editor.addKeyListener(this);

		textEditor = new JTextArea();
		textEditor.setBounds(editor.getBounds());
		textEditor.setLineWrap(true);

		// Panel errores
		errores = new JTable();
		errores.setBounds(2, 37, 1253, 601);

		// Panel simbolos
		simbolos = new JTable();
		errores.setBounds(2, 37, 1253, 601);

		// Panel errores sintacticos
		erroresSintacticos = new JTable();
		erroresSintacticos.setBounds(2, 37, 1253, 601);

		// Panel errores semánticos
		erroresSemanticos = new JTextArea();
		erroresSemanticos.setBounds(2, 37, 1253, 601);
		erroresSemanticos.setLineWrap(true);

		scrollSimbolos = new JScrollPane(simbolos);
		scrollSimbolos.setBounds(2, 37, 1253, 601);
		panelSimbolos.add(scrollSimbolos, BorderLayout.CENTER);

		scrollErrores = new JScrollPane(errores);
		scrollErrores.setBounds(2, 37, 1253, 601);
		panelErrores.add(scrollErrores, BorderLayout.CENTER);

		scrollErroresSintacticos = new JScrollPane(erroresSintacticos);
		scrollErroresSintacticos.setBounds(2, 37, 1253, 601);
		panelErroresSintacticos.add(scrollErroresSintacticos, BorderLayout.CENTER);

		scrollErroresSemanticos = new JScrollPane(erroresSemanticos);
		scrollErroresSemanticos.setBounds(2, 37, 1253, 601);
		panelErroresSemanticos.add(erroresSemanticos);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == '\n' || e.getKeyChar() == '\b' || e.getKeyChar() == 127) {
			controladorVentana.leerSaltoLinea();
		} else {
			editor.getHighlighter().removeAllHighlights();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == mntmAbrir) {
			controladorVentana.cargar();
		} else if (e.getSource() == mntmGuardar) {
			controladorVentana.guardar();
		} else if (e.getSource() == mntmCompilar) {
			controladorVentana.compilar();
		} else if (e.getSource() == mntmEjecutar) {
			controladorVentana.ejecutar();
		} else if (e.getSource() == mntmOscuro) {
			controladorVentana.ponerOscuro();
		} else if (e.getSource() == mntmNuevo) {
			controladorVentana.crear();
		} else if (e.getSource() == mntmClaro) {
			controladorVentana.ponerClaro();
		}
	}

	public JPanel getPanelEditor() {
		return panelEditor;
	}

	public void setPanelEditor(JPanel panelEditor) {
		this.panelEditor = panelEditor;
	}

	public JPanel getPanelErrores() {
		return panelErrores;
	}

	public void setPanelErrores(JPanel panelErrores) {
		this.panelErrores = panelErrores;
	}

	public JPanel getPanelSimbolos() {
		return panelSimbolos;
	}

	public void setPanelSimbolos(JPanel panelSimbolos) {
		this.panelSimbolos = panelSimbolos;
	}

	public JMenu getMnEjecutar() {
		return mnEjecutar;
	}

	public void setMnEjecutar(JMenu mnEjecutar) {
		this.mnEjecutar = mnEjecutar;
	}

	public JMenu getMnArchivo() {
		return mnArchivo;
	}

	public void setMnArchivo(JMenu mnArchivo) {
		this.mnArchivo = mnArchivo;
	}

	public JMenuBar getMenuBarWindow() {
		return menuBar;
	}

	public void setMenuBar(JMenuBar menuBar) {
		this.menuBar = menuBar;
	}

	public JMenuItem getMntmAbrir() {
		return mntmAbrir;
	}

	public void setMntmAbrir(JMenuItem mntmAbrir) {
		this.mntmAbrir = mntmAbrir;
	}

	public JMenuItem getMntmEjecutar() {
		return mntmEjecutar;
	}

	public void setMntmEjecutar(JMenuItem mntmEjecutar) {
		this.mntmEjecutar = mntmEjecutar;
	}

	public JMenuItem getMntmNuevo() {
		return mntmNuevo;
	}

	public void setMntmNuevo(JMenuItem mntmNuevo) {
		this.mntmNuevo = mntmNuevo;
	}

	public JMenuItem getMntmGuardar() {
		return mntmGuardar;
	}

	public void setMntmGuardar(JMenuItem mntmGuardar) {
		this.mntmGuardar = mntmGuardar;
	}

	public JTextArea getTextEditor() {
		return textEditor;
	}

	public void setTextEditor(JTextArea textEditor) {
		this.textEditor = textEditor;
	}

	public JEditorPane getEditor() {
		return editor;
	}

	public void setEditor(JEditorPane editor) {
		this.editor = editor;
	}

	public ControladorVentana getControladorVentana() {
		return controladorVentana;
	}

	public void setControladorVentana(ControladorVentana controladorVentana) {
		this.controladorVentana = controladorVentana;
	}

	public JEditorPane getLinea() {
		return linea;
	}

	public void setLinea(JEditorPane linea) {
		this.linea = linea;
	}

	public JTable getErrores() {
		return errores;
	}

	public void setErrores(JTable errores) {
		this.errores = errores;
	}

	public JTable getSimbolos() {
		return simbolos;
	}

	public void setSimbolos(JTable simbolos) {
		this.simbolos = simbolos;
	}

	public JMenu getMnTema() {
		return mnTema;
	}

	public void setMnTema(JMenu mnTema) {
		this.mnTema = mnTema;
	}

	public JMenuItem getMntmOscuro() {
		return mntmOscuro;
	}

	public void setMntmOscuro(JMenuItem mntmOscuro) {
		this.mntmOscuro = mntmOscuro;
	}

	public JMenuItem getMntmClaro() {
		return mntmClaro;
	}

	public void setMntmClaro(JMenuItem mntmClaro) {
		this.mntmClaro = mntmClaro;
	}

	public AnalizadorLexico getAnalizadorLexico() {
		return analizadorLexico;
	}

	public void setAnalizadorLexico(AnalizadorLexico analizadorLexico) {
		this.analizadorLexico = analizadorLexico;
	}

	public AnalizadorSintactico getAnalizadorSintactico() {
		return analizadorSintactico;
	}

	public void setAnalizadorSintactico(AnalizadorSintactico analizadorSintactico) {
		this.analizadorSintactico = analizadorSintactico;
	}

	/**
	 * @return the panelArbol
	 */
	public JPanel getPanelArbol() {
		return panelArbol;
	}

	/**
	 * @param panelArbol
	 *            the panelArbol to set
	 */
	public void setPanelArbol(JPanel panelArbol) {
		this.panelArbol = panelArbol;
	}

	public JTree getArbolVisual() {
		return arbolVisual;
	}

	public void setArbolVisual(JTree arbolVisual) {
		this.arbolVisual = arbolVisual;
	}

	/**
	 * @return the scrollSimbolos
	 */
	public JScrollPane getScrollSimbolos() {
		return scrollSimbolos;
	}

	/**
	 * @param scrollSimbolos
	 *            the scrollSimbolos to set
	 */
	public void setScrollSimbolos(JScrollPane scrollSimbolos) {
		this.scrollSimbolos = scrollSimbolos;
	}

	/**
	 * @return the scrollErrores
	 */
	public JScrollPane getScrollErrores() {
		return scrollErrores;
	}

	/**
	 * @param scrollErrores
	 *            the scrollErrores to set
	 */
	public void setScrollErrores(JScrollPane scrollErrores) {
		this.scrollErrores = scrollErrores;
	}

	public JTable getErroresSintacticos() {
		return erroresSintacticos;
	}

	public void setErroresSintacticos(JTable erroresSintacticos) {
		this.erroresSintacticos = erroresSintacticos;
	}

	public JPanel getPanelErroresSintacticos() {
		return panelErroresSintacticos;
	}

	public void setPanelErroresSintacticos(JPanel panelErroresSintacticos) {
		this.panelErroresSintacticos = panelErroresSintacticos;
	}

	public JTextArea getErroresSemanticos() {
		return erroresSemanticos;
	}

	public void setErroresSemanticos(JTextArea erroresSemanticos) {
		this.erroresSemanticos = erroresSemanticos;
	}

	public JPanel getPanelErroresSemanticos() {
		return panelErroresSemanticos;
	}

	public void setPanelErroresSemanticos(JPanel panelErroresSemanticos) {
		this.panelErroresSemanticos = panelErroresSemanticos;
	}

	/**
	 * @return the compilado
	 */
	public boolean isCompilado() {
		return compilado;
	}

	/**
	 * @param compilado
	 *            the compilado to set
	 */
	public void setCompilado(boolean compilado) {
		this.compilado = compilado;
	}

}
