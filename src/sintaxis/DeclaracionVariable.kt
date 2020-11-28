package sintaxis

import lexico.Token
import semantico.Simbolo
import semantico.TablaSimbolos
import java.util.*
import javax.swing.tree.DefaultMutableTreeNode

/**
 * Clase que representa la declaración de variable
 */
class DeclaracionVariable : Sentencia {
    /**
     * @return the visibilidad
     */
    /**
     * @param visibilidad
     * the visibilidad to set
     */
    var visibilidad: Token? = null
    /**
     * @return the tipo
     */
    /**
     * @param tipo
     * the tipo to set
     */
    var tipo: Token
    private var arreglo: Token? = null
    /**
     * @return the listaId
     */
    /**
     * @param listaId
     * the listaId to set
     */
    var listaId: ArrayList<Token>

    /**
     * @param visibilidad
     * @param tipo
     * @param listaId
     */
    constructor(visibilidad: Token?, tipo: Token, listaId: ArrayList<Token>) : super() {
        this.visibilidad = visibilidad
        this.tipo = tipo
        this.listaId = listaId
    }

    /**
     * @param tipo
     * @param listaId
     */
    constructor(tipo: Token, listaId: ArrayList<Token>) : super() {
        this.tipo = tipo
        this.listaId = listaId
    }

    /**
     * @param visibilidad
     * @param tipo
     * @param arreglo
     * @param listaId
     */
    constructor(visibilidad: Token?, tipo: Token, arreglo: Token?, listaId: ArrayList<Token>) : super() {
        this.visibilidad = visibilidad
        this.tipo = tipo
        this.arreglo = arreglo
        this.listaId = listaId
    }

    /**
     * @param tipo
     * @param arreglo
     * @param listaId
     */
    constructor(arreglo: Token?, listaId: ArrayList<Token>, tipo: Token) : super() {
        this.tipo = tipo
        this.arreglo = arreglo
        this.listaId = listaId
    }

    /**
     * Método para retornar el nodo de un arbol visual
     *
     * @return
     */
    override fun getArbolVisual(): DefaultMutableTreeNode{
        val nodo = DefaultMutableTreeNode("Declaracion variable")
        if (visibilidad != null) {
            nodo.add(DefaultMutableTreeNode(visibilidad!!.lexema))
        }
        if (arreglo != null) {
            nodo.add(DefaultMutableTreeNode(arreglo!!.lexema))
        }
        nodo.add(DefaultMutableTreeNode(tipo.lexema))
        for (id in listaId) {
            nodo.add(DefaultMutableTreeNode(id.lexema))
        }
        return nodo
    }



    override fun llenarTablaSimbolos(ts: TablaSimbolos?, ambito: Simbolo?) {
        for (token in listaId) {
            if (ambito != null &&  ts != null) {

                    ts.agregarSimbolo(token.lexema, tipo.lexema, ambito)

            }
        }
    }



    override fun analizarSemantica(errores: ArrayList<String?>?, ts: TablaSimbolos?, ambito: Simbolo?) {
        // TODO Auto-generated method stub
    }

    override fun traducir(identacion: String?, global: Boolean): String? {
        var visibilidad = ""
        if (this.visibilidad != null) {
            visibilidad = if (this.visibilidad!!.lexema == "visible") "public" else "private"
        }
        val arreglo = if (arreglo != null) "[]" else ""
        var variables = ""
        for (token in listaId) {
            variables = token.lexema + ", "
        }
        variables = variables.replace("<".toRegex(), "")
        variables = variables.replace(">".toRegex(), "")
        variables = variables.replace("-".toRegex(), "_")
        variables = variables.substring(0, variables.length - 2)
        var tipo = ""
        tipo = when (this.tipo.lexema) {
            "ltr" -> "char"
            "ntr" -> "int"
            "pntdec" -> "double"
            "ltrarr" -> "String"
            "binary" -> "boolean"
            else -> ""
        }
        return identacion + visibilidad + (if (global) " static " else "") + tipo + arreglo + " " + variables
    }
}