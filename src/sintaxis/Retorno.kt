package sintaxis

import lexico.Token
import sintaxis.Termino
import sintaxis.Sentencia
import javax.swing.tree.DefaultMutableTreeNode
import java.util.ArrayList

/**
 * Clase que representa una sentencia de retorno
 */
class Retorno
/**
 * Constructor de clase
 *
 * @param retorno
 * @param termino
 */(
        /**
         * @param retorno
         * the retorno to set
         */
        var retorno: Token,
        /**
         * @param termino
         * the termino to set
         */
        var termino: Termino) : Sentencia() {
    /**
     * @return the retorno
     */
    /**
     * @return the termino
     */
    override var arbolVisual: DefaultMutableTreeNode
        get() {
            val nodo = DefaultMutableTreeNode("Retorno")
            nodo.add(DefaultMutableTreeNode(retorno.lexema))
            nodo.add(termino.arbolVisual)
            return nodo
        }
        set(arbolVisual) {
            super.arbolVisual = arbolVisual
        }


}