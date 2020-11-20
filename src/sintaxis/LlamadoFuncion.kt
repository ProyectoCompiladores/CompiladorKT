package sintaxis

import lexico.Token
import java.util.*
import javax.swing.tree.DefaultMutableTreeNode

class LlamadoFuncion : Sentencia {
    /**
     * @return the identificadorFuncion
     */
    /**
     * @param identificadorFuncion
     * the identificadorFuncion to set
     */
    /**
     * Clase que representa el llamado a una funcion
     */
    var identificadorFuncion: Token
    /**
     * @return the listaArgumentos
     */
    /**
     * @param listaArgumentos
     * the listaArgumentos to set
     */
    var listaArgumentos: ArrayList<Termino>? = null

    /**
     * Constructor que declara un llamado a funcion
     *
     * @param identificadorFuncion
     * @param listaArgumentos
     */
    constructor(identificadorFuncion: Token, listaArgumentos: ArrayList<Termino>?) : super() {
        this.identificadorFuncion = identificadorFuncion
        this.listaArgumentos = listaArgumentos
    }

    /**
     * Constructor que declara un llamado a funcion
     *
     * @param identificadorFuncion
     */
    constructor(identificadorFuncion: Token) : super() {
        this.identificadorFuncion = identificadorFuncion
    }

    override fun toString(): String {
        return ("LlamadoFuncion [identificadorFuncion=" + identificadorFuncion + ", listaArgumentos=" + listaArgumentos
                + "]")
    }

    override var arbolVisual: DefaultMutableTreeNode
        get() {
            val nodo = DefaultMutableTreeNode("Llamado funcion")
            nodo.add(DefaultMutableTreeNode(identificadorFuncion.lexema))
            if (listaArgumentos != null) {
                for (termino in listaArgumentos!!) {
                    nodo.add(termino.arbolVisual)
                }
            }
            return nodo
        }
        set(arbolVisual) {
            super.arbolVisual = arbolVisual
        }



}