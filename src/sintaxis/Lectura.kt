package sintaxis

import lexico.Token
import java.util.*
import javax.swing.tree.DefaultMutableTreeNode

/**
 * Clase que representa la expresion Cadena
 */
class Lectura
/**
 * Constructor para leer un dato
 *
 *  [idVariable]
 *  [opAsignacion]
 *  [leer]
 *  [tipoDato]
 */(private val idVariable: Token, private val opAsignacion: Token, private val leer: Token, private val tipoDato: Token) : Sentencia() {
    override var arbolVisual: DefaultMutableTreeNode
        get() {
            val nodo = DefaultMutableTreeNode("Lectura")
            nodo.add(DefaultMutableTreeNode(idVariable.lexema))
            nodo.add(DefaultMutableTreeNode(opAsignacion.lexema))
            nodo.add(DefaultMutableTreeNode(leer.lexema))
            nodo.add(DefaultMutableTreeNode(tipoDato.lexema))
            return nodo
        }
        set(arbolVisual) {
            super.arbolVisual = arbolVisual
        }

}