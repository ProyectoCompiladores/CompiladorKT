package sintaxis

import lexico.Token
import java.util.*
import javax.swing.tree.DefaultMutableTreeNode

class SentenciaIncremento
/**
 * Constructor de la sentencia incremento
 *
 *  [identificadorVariable]
 *  [incremento]
 */(
        /**
         * @param identificadorVariable
         * the identificadorVariable to set
         */
        var identificadorVariable: Token,
        /**
         * @param incremento
         * the incremento to set
         */
        var incremento: Token) : Sentencia() {
    /**
     * @return the identificadorVariable
     */
    /**
     * @return the incremento
     */
    override var arbolVisual: DefaultMutableTreeNode
        get() {
            val nodo = DefaultMutableTreeNode("Incremento")
            nodo.add(DefaultMutableTreeNode(identificadorVariable.lexema))
            nodo.add(DefaultMutableTreeNode(incremento.lexema))
            return nodo
        }
        set(arbolVisual) {
            super.arbolVisual = arbolVisual
        }

}