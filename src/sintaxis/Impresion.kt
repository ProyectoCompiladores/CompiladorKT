package sintaxis

import lexico.Token
import semantico.Simbolo
import semantico.TablaSimbolos
import java.util.*
import javax.swing.tree.DefaultMutableTreeNode

/**
 * Clase que representa la expresion Cadena
 */
class Impresion
/**
 * Constructor para imprimir un dato
 *
 * @param escribir
 * @param termino
 */(private val escribir: Token, private val termino: Termino?) : Sentencia() {

    override fun getArbolVisual(): DefaultMutableTreeNode {
        val nodo = DefaultMutableTreeNode("Impresion")
        nodo.add(DefaultMutableTreeNode(escribir.lexema))
        if (termino != null) {
            nodo.add(termino.getArbolVisual())
        }
        return nodo
    }

    fun analizarSemantica() {}
    fun llenarTablaSimbolos() {}

    override fun analizarSemantica(errores: ArrayList<String?>?, ts: TablaSimbolos?, ambito: Simbolo?) {
        termino!!.analizarSemantica(errores!!, ts!!, ambito!!)
    }

    override fun llenarTablaSimbolos(ts: TablaSimbolos?, ambito: Simbolo?) {
        // TODO Auto-generated method stub
    }

    override fun traducir(identacion: String?, global: Boolean): String? {
        return identacion + "JOptionPane.showMessageDialog(null, " + termino!!.traducir() + ")"
    }
}