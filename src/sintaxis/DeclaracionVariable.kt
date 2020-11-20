package sintaxis

import lexico.Token
import java.util.*
import javax.swing.tree.DefaultMutableTreeNode

/**
 * Clase que representa la declaraci�n de variable
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
     * M�todo para retornar el nodo de un arbol visual
     *
     * @return
     */
    override var arbolVisual: DefaultMutableTreeNode
        get() {
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
        set(arbolVisual) {
            super.arbolVisual = arbolVisual
        }

}