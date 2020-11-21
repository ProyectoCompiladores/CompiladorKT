package controlador;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.tree.DefaultTreeModel;

import gui.VentanaCompilador;
import lexico.AnalizadorLexico;
import lexico.Token;
import sintaxis.AnalizadorSintactico;
import sintaxis.ErrorSintactico;


/**
 * Esta clase se encarga de controlar la interfaz grafica y sus eventos
 */
public class ControladorVentana {
	private VentanaCompilador ventanaCompilador;
	File fichero;
	boolean guardado;

	/**
	 * Contructor del controlador de la ventana, recibe la ventana a controlar
	 * 
	 *  [ventanaCompilador]
	 */
	public ControladorVentana(VentanaCompilador ventanaCompilador) {
		this.ventanaCompilador = ventanaCompilador;
		organizar();
	}

	/**
	 * Metodo para poner numeros en el editor
	 */
	private void organizar() {
		ventanaCompilador.getLinea().setText("");
		String aux = "";
		for (int i = 1; i <= ventanaCompilador.getTextEditor().getLineCount(); i++) {
			aux += i + "\n";
		}
		ventanaCompilador.getLinea().setText(aux);
	}

	/**
	 * Metodo para cargar un archivo
	 */
	public void cargar() {
		String aux = "";
		String texto = "";
		try {
			JFileChooser file = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter(".MG", "mg");
			file.setFileFilter(filter);
			file.showOpenDialog(null);
			fichero = file.getSelectedFile();
			if (fichero != null) {
				FileReader archivos = new FileReader(fichero);
				BufferedReader buff = new BufferedReader(archivos);
				while ((aux = buff.readLine()) != null) {
					texto += aux + "\n";

				}
				buff.close();
			}
			ventanaCompilador.getTextEditor().setText(texto.substring(0, texto.length() - 1));
			ventanaCompilador.getEditor().setText(ventanaCompilador.getTextEditor().getText());
			organizar();

		} catch (IOException ex) {
			JOptionPane.showMessageDialog(null, ex + "" + "\nNo se ha encontrado el archivo", "Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (NullPointerException e2) {
			JOptionPane.showMessageDialog(null, "Se ha cancelado el cargado");
		}
	}

	/**
	 * Metodo para guardar un archivo
	 */
	public void guardar() {
		FileWriter archivos;
		if (guardado) {
			try {
				archivos = new FileWriter(fichero);
				BufferedWriter buff = new BufferedWriter(archivos);
				buff.write(ventanaCompilador.getEditor().getText());
				buff.close();

				JOptionPane.showMessageDialog(null, "Se ha guardado correctamente");
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, "No se ha guardado");
			} catch (NullPointerException e2) {
				JOptionPane.showMessageDialog(null, "Se ha cancelado el guardado");
			}
		} else {
			try {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Elija donde guardar");
				FileNameExtensionFilter filter = new FileNameExtensionFilter(".MG", "mg");
				fileChooser.setFileFilter(filter);

				int userSelection = fileChooser.showSaveDialog(null);
				File fileToSave = null;
				if (userSelection == JFileChooser.APPROVE_OPTION) {
					fileToSave = fileChooser.getSelectedFile();
				}

				archivos = new FileWriter(fileToSave.getAbsolutePath() + ".MG");
				BufferedWriter buff = new BufferedWriter(archivos);
				buff.write(ventanaCompilador.getEditor().getText());
				buff.close();

				JOptionPane.showMessageDialog(null, "Se ha guardado correctamente");
				fichero = fileToSave.getAbsoluteFile();
				guardado = true;
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, "No se ha guardao");
			} catch (NullPointerException e2) {
				JOptionPane.showMessageDialog(null, "Se ha cancelado el guardado");
			}
		}
	}

	/**
	 * Metodo para crear un nuevo archivo
	 */
	public void crear() {
		ventanaCompilador.getEditor().setText("");
		fichero = null;
		ventanaCompilador.getTextEditor().requestFocus();
		guardado = false;
		ventanaCompilador.getPanelSimbolos().removeAll();
		ventanaCompilador.getPanelErrores().removeAll();
	}

	/**
	 * Metodo para la ejecucion del compilador
	 */
	public void compilar() {
		ventanaCompilador.setCompilado(false);
		ventanaCompilador.setAnalizadorLexico(new AnalizadorLexico(ventanaCompilador.getEditor().getText()));
		ventanaCompilador.getAnalizadorLexico().analizar();
		agregarSimbolos();
		agregarErrores();
		if (ventanaCompilador.getAnalizadorLexico().getTablaErrores().size() == 0) {
			ventanaCompilador.setAnalizadorSintactico(
					new AnalizadorSintactico(ventanaCompilador.getAnalizadorLexico().getTablaSimbolos()));
			ventanaCompilador.getAnalizadorSintactico().analizar();
			agregarErroresSintacticos();

			if (ventanaCompilador.getAnalizadorSintactico().getTablaErrores().size() == 0) {
				ArrayList<String> errores = new ArrayList<>();
				JOptionPane.showMessageDialog(null, "No hay errores Sintacticos");
				}
			}
		}

	public void ejecutar() {
		if (ventanaCompilador.isCompilado()) {
			try {
				File f = new File("src/" + ventanaCompilador.getAnalizadorSintactico().getUnidadCompilacion()
						.getIdentificadorClase().getLexema().substring(1) + ".java");
				FileWriter fw = new FileWriter(f);
				fw.flush();
				fw.close();
				String comando = "javac.exe" + " "
						+ ventanaCompilador.getAnalizadorSintactico().getUnidadCompilacion().getIdentificadorClase()
								.getLexema().substring(1)
						+ ".java";
				String comando0 = "java.exe" + " " + ventanaCompilador.getAnalizadorSintactico().getUnidadCompilacion()
						.getIdentificadorClase().getLexema().substring(1);
				Runtime.getRuntime().exec(comando, null, new File("src/")).waitFor();
				Runtime.getRuntime().exec(comando0, null, new File("src/")).waitFor();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			JOptionPane.showMessageDialog(null, "El archivo aún no ha sido compilado o tuvo errores en su compilacion",
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}



	/**
	 * Metodo para agregar simbolos a la tabla de la interfaz
	 */
	private void agregarSimbolos() {
		String[] fila_simbolo = { "Lexema", "Columna", "Fila", "Categoria" };
		DefaultTableModel modelo_simbolo = new DefaultTableModel(fila_simbolo, 0);
		for (Token token : ventanaCompilador.getAnalizadorLexico().getTablaSimbolos()) {

			Object[] columnas = { token.getLexema(), token.getColumna(), token.getFila(),
					token.getCategoria().toString() };
			modelo_simbolo.addRow(columnas);
		}

		ventanaCompilador.getPanelSimbolos().removeAll();
		ventanaCompilador.setSimbolos(new JTable(modelo_simbolo));
		ventanaCompilador.getSimbolos().setBackground(ventanaCompilador.getEditor().getBackground());
		ventanaCompilador.getSimbolos().setForeground(ventanaCompilador.getEditor().getForeground());
		ventanaCompilador.getPanelSimbolos().add(new JScrollPane(ventanaCompilador.getSimbolos()), BorderLayout.CENTER);
	}

	/**
	 * Metodo para agregar errores a la tabla de la interfaz
	 */
	private void agregarErrores() {
		String[] fila_error = { "Lexema", "Columna", "Fila", "Categoria" };
		DefaultTableModel modelo_simbolo = new DefaultTableModel(fila_error, 0);
		for (Token token : ventanaCompilador.getAnalizadorLexico().getTablaErrores()) {
			Object[] columnas = { token.getLexema(), token.getColumna(), token.getFila(),
					token.getCategoria().toString() };
			modelo_simbolo.addRow(columnas);
		}

		ventanaCompilador.getPanelErrores().removeAll();
		ventanaCompilador.setErrores(new JTable(modelo_simbolo));
		ventanaCompilador.getErrores().setBackground(ventanaCompilador.getEditor().getBackground());
		ventanaCompilador.getErrores().setForeground(ventanaCompilador.getEditor().getForeground());
		ventanaCompilador.getPanelErrores().add(new JScrollPane(ventanaCompilador.getErrores()), BorderLayout.CENTER);

		for (Token token : ventanaCompilador.getAnalizadorLexico().getTablaErrores()) {
			DefaultHighlighter.DefaultHighlightPainter highlightPainter = new DefaultHighlighter.DefaultHighlightPainter(
					ventanaCompilador.getBackground().equals(Color.BLACK) ? new Color(180, 0, 0)
							: new Color(255, 0, 0));
			try {
				ventanaCompilador.getEditor().getHighlighter().addHighlight(token.getColumnaReal(),
						token.getColumnaReal() + token.getLexema().length(), highlightPainter);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}

		}

		JOptionPane.showMessageDialog(null, "Compilado con "
				+ ventanaCompilador.getAnalizadorLexico().getTablaErrores().size() + " errores lexicos");
	}

	/**
	 * Método para señalar errores al editor
	 */
	private void agregarErroresSintacticos() {
		String[] fila_error = { "Mensaje", "Columna", "Fila" };
		DefaultTableModel modelo_simbolo = new DefaultTableModel(fila_error, 0);
		for (ErrorSintactico errorSintactico : ventanaCompilador.getAnalizadorSintactico().getTablaErrores()) {
			Object[] columnas = { errorSintactico.getMensaje(), errorSintactico.getColumna(),
					errorSintactico.getFila() };
			modelo_simbolo.addRow(columnas);
		}

		ventanaCompilador.getPanelErroresSintacticos().removeAll();
		ventanaCompilador.setErroresSintacticos(new JTable(modelo_simbolo));
		ventanaCompilador.getErroresSintacticos().setBackground(ventanaCompilador.getEditor().getBackground());
		ventanaCompilador.getErroresSintacticos().setForeground(ventanaCompilador.getEditor().getForeground());
		ventanaCompilador.getPanelErroresSintacticos().add(new JScrollPane(ventanaCompilador.getErroresSintacticos()),
				BorderLayout.CENTER);

		for (ErrorSintactico errorSintactico : ventanaCompilador.getAnalizadorSintactico().getTablaErrores()) {
			DefaultHighlighter.DefaultHighlightPainter highlightPainter = new DefaultHighlighter.DefaultHighlightPainter(
					ventanaCompilador.getBackground().equals(Color.BLACK) ? new Color(180, 0, 0)
							: new Color(255, 0, 0));
			try {
				ventanaCompilador.getEditor().getHighlighter().addHighlight(errorSintactico.getColumnaReal(),
						errorSintactico.getColumnaReal() + 4, highlightPainter);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}

		}

		JOptionPane.showMessageDialog(null, "Compilado con "
				+ ventanaCompilador.getAnalizadorSintactico().getTablaErrores().size() + " errores sintacticos");
	}


	/**
	 * Insertar los numeros a partir de la escritura
	 */
	public void leerSaltoLinea() {
		ventanaCompilador.getTextEditor().setText(ventanaCompilador.getEditor().getText());
		organizar();
	}
}
