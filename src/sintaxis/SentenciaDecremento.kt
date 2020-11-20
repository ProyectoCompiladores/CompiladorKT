package sintaxis

import lexico.Token
import java.util.*
import javax.swing.tree.DefaultMutableTreeNode

class SentenciaDecremento
/**
 * Constructor de la sentencia decremento
 *
 *  [identificadorVariable]
 *  [decremento]
 */(
        /**
         * @param identificadorVariable
         * the identificadorVariable to set
         */
        var identificadorVariable: Token,
        /**
         * @param decremento
         * the decremento to set
         */
        var decremento: Token) : Sentencia() {
    /**
     * @return the identificadorVariable
     */
    /**
     * @return the decremento
     */
    override var arbolVisual: DefaultMutableTreeNode
        get() {
            val nodo = DefaultMutableTreeNode("Decremento")
            nodo.add(DefaultMutableTreeNode(identificadorVariable.lexema))
            nodo.add(DefaultMutableTreeNode(decremento.lexema))
            return nodo
        }
        set(arbolVisual) {
            super.arbolVisual = arbolVisual
        }

}