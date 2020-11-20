package sintaxis

import lexico.Token
import javax.swing.tree.DefaultMutableTreeNode

/**
 * Clase que representa el parametro
 */
class Parametro {
    /**
     * @return the tipoDato
     */
    /**
     * @param tipoDato
     * the tipoDato to set
     */
    var tipoDato: Token
    /**
     * @return the arreglo
     */
    /**
     * @param arreglo
     * the arreglo to set
     */
    var arreglo: Token? = null
    /**
     * @return the idenVariable
     */
    /**
     * @param idenVariable
     * the idenVariable to set
     */
    var idenVariable: Token

    /**
     * Constructor que tiene ell tipo de dato y el identificador de variable
     *
     * @param tipoDato
     * @param idenVariable
     */
    constructor(tipoDato: Token, idenVariable: Token) : super() {
        this.tipoDato = tipoDato
        this.idenVariable = idenVariable
    }

    /**
     * Constructor que tiene tipo de dato, arreglo y un identificador de variable
     *
     * @param tipoDato
     * @param arreglo
     * @param idenVariable
     */
    constructor(tipoDato: Token, arreglo: Token?, idenVariable: Token) : super() {
        this.tipoDato = tipoDato
        this.arreglo = arreglo
        this.idenVariable = idenVariable
    }

    val arbolVisual: DefaultMutableTreeNode
        get() {
            val nodo = DefaultMutableTreeNode("Parametros")
            nodo.add(DefaultMutableTreeNode(tipoDato.lexema))
            if (arreglo != null) {
                nodo.add(DefaultMutableTreeNode(arreglo!!.lexema))
            }
            nodo.add(DefaultMutableTreeNode(idenVariable.lexema))
            return nodo
        }

    fun analizarSemantica() {}
    fun llenarTablaSimbolos() {}
    fun traducir(): String {
        val arreglo = if (arreglo != null) "[]" else ""
        var variable = ""
        variable = idenVariable.lexema
        variable = variable.replace("<".toRegex(), "")
        variable = variable.replace(">".toRegex(), "")
        variable = variable.replace("-".toRegex(), "_")
        var tipo = ""
        tipo = when (tipoDato.lexema) {
            "ltr" -> "char"
            "ntr" -> "int"
            "pntdec" -> "double"
            "ltrarr" -> "String"
            "binary" -> "boolean"
            else -> ""
        }
        return "$tipo$arreglo $variable"
    }
}