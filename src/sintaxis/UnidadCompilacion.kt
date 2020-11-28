package sintaxis

import lexico.Token
import semantico.TablaSimbolos
import java.util.*
import javax.swing.tree.DefaultMutableTreeNode

/**
 * Clase que representa la unidad de compilacion, (Raiz del arbol de analisis lexico)
 */
class UnidadCompilacion
/**
 * Constructor de la unidad de compilacion
 *
 * @param palabraReservadaClase:
 * Palabra reservada del lexico {Clase}
 * @param identificadorClase
 * @param cuerpoClase
 */(
        /**
         * @param palabraReservadaClase
         * the palabraReservadaClase to set
         */
        // Variables
        var palabraReservadaClase: Token,
        /**
         * @param identificadorClase
         * the identificadorClase to set
         */
        var identificadorClase: Token,
        /**
         * @param cuerpoClase
         * the cuerpoClase to set
         */
        var cuerpoClase: CuerpoClase) {
    /**
     * @return the palabraReservadaClase
     */
    /**
     * @return the identificadorClase
     */
    /**
     * @return the cuerpoClase
     */

    /*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
    override fun toString(): String {
        return ("UnidadCompilacion [palabraReservadaClase=" + palabraReservadaClase + ", identificadorClase="
                + identificadorClase + ", cuerpoClase=" + cuerpoClase + "]")
    }

    /**
     * Método para retornar el nodo de un arbol visual
     * @return
     */
    val arbolVisual: DefaultMutableTreeNode
        get() {
            val nodo = DefaultMutableTreeNode("Unidad de compilacion")
            nodo.add(DefaultMutableTreeNode(palabraReservadaClase.lexema))
            nodo.add(DefaultMutableTreeNode(identificadorClase.lexema))
            nodo.add(cuerpoClase.arbolVisual)
            return nodo
        }

    fun analizarSemantica(ts: TablaSimbolos?, errores: ArrayList<String?>?) {
        cuerpoClase.analizarSemantica(errores!!, ts)
    }

    fun llenarTablaSimbolos(ts: TablaSimbolos?) {
        cuerpoClase.llenarTablaSimbolos(ts!!)
    }

    fun traducir(): String {
        return """
               import javax.swing.JOptionPane;
               public class ${identificadorClase.lexema.substring(1)}{
               ${cuerpoClase.traducir("\t")}}
               """.trimIndent()
    }
}