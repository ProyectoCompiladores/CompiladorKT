package sintaxis

import lexico.Token
import java.util.*
import javax.swing.tree.DefaultMutableTreeNode

/**
 * Clase que representa las asignaciones de variable.
 */
class AsignacionVariable
/**
 * Constructor de la asignaci�n de variable
 *
 * @param identificadorVariable
 * @param operadorAsignacion
 * @param termino
 */(
        /**
         * @param identificadorVariable
         * the identificadorVariable to set
         */
        var identificadorVariable: Token, var operadorAsignacion: Token,
        /**
         * @param termino
         * the termino to set
         */
        var termino: Termino) : Sentencia() {

    /**
     * M�todo para obtener el �rbol de visual de la asignaci�n de variable.
     */
    override var arbolVisual: DefaultMutableTreeNode
        get() {
            val nodo = DefaultMutableTreeNode("Asignaci�n variable")
            nodo.add(DefaultMutableTreeNode(identificadorVariable.lexema))
            nodo.add(DefaultMutableTreeNode(operadorAsignacion.lexema))
            nodo.add(termino.arbolVisual)
            return nodo
        }
        set(arbolVisual) {
            super.arbolVisual = arbolVisual
        }



}