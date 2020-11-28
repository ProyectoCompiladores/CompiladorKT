package sintaxis

import lexico.Token
import semantico.Simbolo
import semantico.TablaSimbolos
import java.util.*
import javax.swing.tree.DefaultMutableTreeNode

/**
 * Clase que representa la expresion logica
 */
class ExpresionLogica : Expresion {
    /**
     * @return the expresionRelacional
     */
    /**
     * @param expresionRelacional the expresionRelacional to set
     */
    var expresionRelacional: ExpresionRelacional? = null
    /**
     * @return the opLogico
     */
    /**
     * @param opLogico the opLogico to set
     */
    var opLogico: Token? = null
    /**
     * @return the expresionLogica
     */
    /**
     * @param expresionLogica the expresionLogica to set
     */
    var expresionLogica: ExpresionLogica? = null

    /**
     * Constructor que crea expresion logica con solo una expresion logica
     *
     * @param expresionLogica
     */
    constructor(expresionLogica: ExpresionLogica?) : super() {
        this.expresionLogica = expresionLogica
    }

    /**
     * Constructor que crea expresion logica con solo una expresion relacional
     *
     * @param expresionRelacional
     */
    constructor(expresionRelacional: ExpresionRelacional?) : super() {
        this.expresionRelacional = expresionRelacional
    }

    /**
     * Constructor que crea expresion logica con caso contrario
     *
     * @param expresionRelacional
     * @param operadorLogico
     * @param expresionLogica
     */
    constructor(expresionRelacional: ExpresionRelacional?, opLogico: Token?, expresionLogica: ExpresionLogica?) : super() {
        this.expresionRelacional = expresionRelacional
        this.opLogico = opLogico
        this.expresionLogica = expresionLogica
    }

    /**
     * Metodo del arbol grafico
     */
    override fun getArbolVisual(): DefaultMutableTreeNode {
        val nodo = DefaultMutableTreeNode("Expresion Logica")
        if (expresionRelacional != null) {
            nodo.add(expresionRelacional!!.getArbolVisual())
        }
        if (expresionLogica != null) {
            if (opLogico != null) {
                nodo.add(DefaultMutableTreeNode(opLogico!!.lexema))
            }
            return expresionLogica!!.getArbolVisual(nodo)
        }
        return nodo
    }


    fun getArbolVisual(nodo: DefaultMutableTreeNode): DefaultMutableTreeNode {
        if (expresionRelacional != null) {
            nodo.add(expresionRelacional!!.getArbolVisual())
        }
        if (expresionLogica != null) {
            if (opLogico != null) {
                nodo.add(DefaultMutableTreeNode(opLogico!!.lexema))
            }
            return expresionLogica!!.getArbolVisual(nodo)
        }
        return nodo
    }


    override fun analizarSemantica(errores: ArrayList<String?>?, ts: TablaSimbolos?, ambito: Simbolo?) {
        if (expresionRelacional != null) {
            expresionRelacional!!.analizarSemantica(errores, ts, ambito)
        }
        if (expresionLogica != null) {
            expresionLogica!!.analizarSemantica(errores, ts, ambito)
        }
    }

    override fun llenarTablaSimbolos(ts: TablaSimbolos?) {
        // TODO Auto-generated method stub
    }

    override fun traducir(): String {
        var operador = ""
        if (opLogico != null) {
            when (opLogico!!.lexema) {
                "AND" -> operador = "&&"
                "OR" -> operador = "||"
                "NOT" -> operador = "!"
                else -> {
                }
            }
        }
        return if (expresionLogica != null) {
            expresionRelacional!!.traducir() + operador + expresionLogica!!.traducir()
        } else {
            expresionRelacional!!.traducir()
        }
    }
}