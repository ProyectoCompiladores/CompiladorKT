package sintaxis

import lexico.Token
import java.util.*
import javax.swing.tree.DefaultMutableTreeNode

/**
 * Clase que representa la expresion Cadena
 */
class Impresion
/**
 * Constructor para imprimir un dato
 *
 *  [escribir]
 *  [termino]
 */(private val escribir: Token, private val termino: Termino?) : Sentencia() {
    override var arbolVisual: DefaultMutableTreeNode
        get() {
            val nodo = DefaultMutableTreeNode("Impresion")
            nodo.add(DefaultMutableTreeNode(escribir.lexema))
            if (termino != null) {
                nodo.add(termino.arbolVisual)
            }
            return nodo
        }
        set(arbolVisual) {
            super.arbolVisual = arbolVisual
        }
}