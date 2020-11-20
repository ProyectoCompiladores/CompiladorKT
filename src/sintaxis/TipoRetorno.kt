package sintaxis

import lexico.Token
import javax.swing.tree.DefaultMutableTreeNode

/**
 * Clase que representa el tipo de retorno
 */
class TipoRetorno
/**
 * Constructor sin retorno o con tipo de dato
 *
 * @param tipoDato
 */(
        /**
         * @param tipoRetorno the tipoRetorno to set
         */
        var tipoRetorno: Token) {
    /**
     * @return the tipoRetorno
     */
    val arbolVisual: DefaultMutableTreeNode
        get() = DefaultMutableTreeNode(tipoRetorno.lexema)

    fun analizarSemantica() {}
    fun llenarTablaSimbolos() {}
}