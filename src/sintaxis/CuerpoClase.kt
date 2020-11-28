package sintaxis

import semantico.Simbolo
import semantico.TablaSimbolos
import java.util.*
import javax.swing.tree.DefaultMutableTreeNode

/**
 * Clase que representa el cuerpo de la clase, (Funciones y variables)
 */
class CuerpoClase {
    /**
     * @return the funcion
     */
    /**
     * @param funcion
     * the funcion to set
     */
    // Variables
    var funcion: Funcion? = null
    /**
     * @return the declaracionVariable
     */
    /**
     * @param declaracionVariable
     * the declaracionVariable to set
     */
    var declaracionVariable: DeclaracionVariable? = null
    /**
     * @return the cuerpoClase
     */
    /**
     * @param cuerpoClase
     * the cuerpoClase to set
     */
    var cuerpoClase: CuerpoClase? = null

    /**
     * Constructor con una funcion y posibilidad de agregar mas cuerpos de clase
     *
     * @param funcion
     * @param cuerpoClase
     */
    constructor(funcion: Funcion?, cuerpoClase: CuerpoClase?) : super() {
        this.funcion = funcion
        this.funcion = funcion
        this.cuerpoClase = cuerpoClase
    }

    /**
     * Constructor con una funcion y posibilidad de agregar mas cuerpos de clase
     *
     * @param declaracionVariable
     * @param cuerpoClase
     */
    constructor(declaracionVariable: DeclaracionVariable?, cuerpoClase: CuerpoClase?) : super() {
        this.declaracionVariable = declaracionVariable
        this.cuerpoClase = cuerpoClase
    }

    /**
     * Constructor con solo una funcion
     *
     * @param funcion
     */
    constructor(funcion: Funcion?) : super() {
        this.funcion = funcion
    }

    /**
     * Constructor con solo una declaracion de variable
     *
     * @param declaracionVariable
     */
    constructor(declaracionVariable: DeclaracionVariable?) : super() {
        this.declaracionVariable = declaracionVariable
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
    override fun toString(): String {
        return if (funcion != null) {
            if (cuerpoClase != null) "CuerpoClase [funcion=" + funcion + "cuerpoClase=" + cuerpoClase + "]" else {
                "CuerpoClase [funcion=$funcion]"
            }
        } else if (declaracionVariable != null) {
            if (cuerpoClase != null) "CuerpoClase [declaracionVariable=" + declaracionVariable + "cuerpoClase=" + cuerpoClase + "]" else {
                "CuerpoClase [declaracionVariable=$declaracionVariable]"
            }
        } else {
            ""
        }
    }

    /**
     * Método para un nodo proviniente de otro cuerpo de clase
     *
     * @param node
     * @return
     */
    fun getArbolVisual(nodo: DefaultMutableTreeNode): DefaultMutableTreeNode {
        if (funcion != null) {
            nodo.add(funcion!!.getArbolVisual())
            if (cuerpoClase != null) {
                return cuerpoClase!!.getArbolVisual(nodo)
            }
        } else if (declaracionVariable != null) {
            nodo.add(declaracionVariable!!.getArbolVisual())
            if (cuerpoClase != null) {
                return cuerpoClase!!.getArbolVisual(nodo)
            }
        }
        return nodo
    }

    /**
     * Método para retornar de otro cuerpo de clase
     *
     * @param node
     * @return
     */
    val arbolVisual: DefaultMutableTreeNode
        get() {
            val nodo = DefaultMutableTreeNode("Cuerpo clase")
            if (funcion != null) {
                nodo.add(funcion!!.getArbolVisual())
                if (cuerpoClase != null) {
                    return cuerpoClase!!.getArbolVisual(nodo)
                }
            } else if (declaracionVariable != null) {
                nodo.add(declaracionVariable!!.getArbolVisual())
                if (cuerpoClase != null) {
                    return cuerpoClase!!.getArbolVisual(nodo)
                }
            }
            return nodo
        }

    fun analizarSemantica(errores: ArrayList<String?>, ts: TablaSimbolos?) {
        if (funcion != null) {
            funcion!!.analizarSemantica(errores, ts)
            if (cuerpoClase != null) {
                cuerpoClase!!.analizarSemantica(errores, ts)
            }
        } else if (declaracionVariable != null) {

            if (cuerpoClase != null) {
                cuerpoClase!!.analizarSemantica(errores, ts)
            }

        }
    }

    fun llenarTablaSimbolos(ts: TablaSimbolos) {
        if (funcion != null) {
            val tipos = ArrayList<String>()
            if (funcion!!.listaParametros != null) {
                for (param in funcion!!.listaParametros!!) {
                    tipos.add(param.tipoDato.lexema)
                }
            }
            funcion!!.ambito = ts.agregarFuncion(funcion!!.identificadorFuncion.lexema,
                    funcion!!.tipoRetorno.tipoRetorno.lexema, tipos)
            funcion!!.llenarTablaSimbolos(ts)
            if (cuerpoClase != null) {
                cuerpoClase!!.llenarTablaSimbolos(ts)
            }
        } else if (declaracionVariable != null) {
            declaracionVariable!!.llenarTablaSimbolos(ts,null)
            if (cuerpoClase != null) {
                cuerpoClase!!.llenarTablaSimbolos(ts)
            }

        }
    }

    fun traducir(identacion: String?): String {
        var codigo = ""
        if (funcion != null) {
            codigo += """
                ${funcion!!.traducir(identacion!!)}
                
                """.trimIndent()
            if (cuerpoClase != null) {
                codigo += cuerpoClase!!.traducir(identacion)
            }
        } else if (declaracionVariable != null) {
            codigo += """
                ${declaracionVariable!!.traducir(identacion!!, true)};
                
                """.trimIndent()
            if (cuerpoClase != null) {
                codigo += cuerpoClase!!.traducir(identacion)
            }
        }
        return codigo
    }
}