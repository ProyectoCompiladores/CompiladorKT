package sintaxis

import lexico.Token
import javax.swing.tree.DefaultMutableTreeNode

/**
 * Clase que representa el valor de asignacion
 */
class ValorAsignacion(
        /**
         * @param tipoDato the tipoDato to set
         */
        var tipoDato: Token) {
    /**
     * @return the tipoDato
     */
    override fun toString(): String {
        return "ValorAsignacion [tipoDato=$tipoDato]"
    }

    val arbolVisual: DefaultMutableTreeNode
        get() = DefaultMutableTreeNode(tipoDato.lexema)
}