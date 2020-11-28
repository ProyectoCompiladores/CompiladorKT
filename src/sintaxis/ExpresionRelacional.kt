package sintaxis

import lexico.Token
import semantico.Simbolo
import semantico.TablaSimbolos
import java.util.*
import javax.swing.tree.DefaultMutableTreeNode

/**
 * Clase que representa la sentencia
 */
class ExpresionRelacional : Expresion {
    /**
     * @return the expAritmetica
     */
    /**
     * @param expAritmetica
     * the expAritmetica to set
     */
    var expAritmetica: ExpresionAritmetica? = null
    /**
     * @return the opRelacional
     */
    /**
     * @param opRelacional
     * the opRelacional to set
     */
    var opRelacional: Token? = null
    /**
     * @return the expRelacional
     */
    /**
     * @param expRelacional
     * the expRelacional to set
     */
    var expRelacional: ExpresionRelacional? = null

    /**
     * Constructor con expresion aritmetica
     *
     * @param expAritmetica
     */
    constructor(expAritmetica: ExpresionAritmetica?) : super() {
        this.expAritmetica = expAritmetica
    }

    /**
     * Constructor con expresion aritmetica y operador relacional
     *
     * @param expAritmetica
     * @param opRelacional
     * @param expRelacional
     */
    constructor(expAritmetica: ExpresionAritmetica?, opRelacional: Token?,
                expRelacional: ExpresionRelacional?) : super() {
        this.expAritmetica = expAritmetica
        this.opRelacional = opRelacional
        this.expRelacional = expRelacional
    }

    /**
     * Constructor con expresion relacional
     *
     * @param expRelacional
     */
    constructor(expRelacional: ExpresionRelacional?) : super() {
        this.expRelacional = expRelacional
    }

    override fun getArbolVisual(): DefaultMutableTreeNode {
        val nodo = DefaultMutableTreeNode("Expresion Relacional")
        if (expAritmetica != null) {
            nodo.add(expAritmetica!!.getArbolVisual())
            if (expRelacional != null) {
                nodo.add(DefaultMutableTreeNode(opRelacional!!.lexema))
                return expRelacional!!.getArbolVisual(nodo)
            }
        } else {
            if (expRelacional != null) {
                return expRelacional!!.getArbolVisual(nodo)
            }
        }
        return nodo
    }

    fun getArbolVisual(nodo: DefaultMutableTreeNode): DefaultMutableTreeNode {
        if (expAritmetica != null) {
            nodo.add(expAritmetica!!.getArbolVisual())
            if (expRelacional != null) {
                nodo.add(DefaultMutableTreeNode(opRelacional!!.lexema))
                return expRelacional!!.getArbolVisual(nodo)
            }
        } else {
            if (expRelacional != null) {
                return expRelacional!!.getArbolVisual(nodo)
            }
        }
        return nodo
    }



    override fun analizarSemantica(errores: ArrayList<String?>?, ts: TablaSimbolos?, ambito: Simbolo?) {
        if (expRelacional != null) {
            expRelacional!!.analizarSemantica(errores, ts, ambito)
        }
        if (expAritmetica != null) {
            expAritmetica!!.analizarSemantica(errores, ts, ambito)
        }
    }

    override fun llenarTablaSimbolos(ts: TablaSimbolos?) {}
    override fun traducir(): String {
        var operador = ""
        if (opRelacional != null) {
            when (opRelacional!!.lexema) {
                "(>)" -> operador = ">"
                "(<)" -> operador = "<"
                "NOT(es)" -> operador = "!="
                "(es)" -> operador = "=="
                "(>es)" -> operador = ">="
                "(<es)" -> operador = "<="
                else -> {
                }
            }
        }
        return if (expRelacional != null) {
            expAritmetica!!.traducir() + operador + expRelacional!!.traducir()
        } else {
            expAritmetica!!.traducir()
        }
    }
}