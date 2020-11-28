package sintaxis

import lexico.Token
import semantico.Simbolo
import semantico.TablaSimbolos
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
    override fun getArbolVisual(): DefaultMutableTreeNode {
        val nodo = DefaultMutableTreeNode("Llamado funcion")
        nodo.add(DefaultMutableTreeNode(identificadorFuncion.lexema))
        if (listaArgumentos != null) {
            for (termino in listaArgumentos!!) {
                nodo.add(termino.getArbolVisual())
            }
        }
        return nodo
    }


    fun getTipos(errores: ArrayList<String?>, ts: TablaSimbolos, ambito: Simbolo): ArrayList<String> {
        val tipos = ArrayList<String>()
        if (listaArgumentos != null) {
            for (termino in listaArgumentos!!) {
                tipos.add(termino.getTipo(errores, ts, ambito)!!)
            }
        }
        return tipos
    }



    override fun analizarSemantica(errores: ArrayList<String?>?, ts: TablaSimbolos?, ambito: Simbolo?) {
        var funcion = false
        for (simbolo in ts!!.tablaSimbolos) {
            if (identificadorFuncion.lexema == simbolo.nombre && simbolo.isEsFuncion) {
                if (simbolo.tipos!!.containsAll(getTipos(errores!!, ts!!, ambito!!))) {
                    funcion = true
                    break
                }
            }
        }
        if (!funcion) {
            errores?.add(identificadorFuncion.lexema + " No se encontró la función invocada")
        }
        if (listaArgumentos != null) {
            for (termino in listaArgumentos!!) {
                termino.analizarSemantica(errores!!, ts!!, ambito!!)
            }
        }
    }

    override fun llenarTablaSimbolos(ts: TablaSimbolos?, ambito: Simbolo?) {
        // TODO Auto-generated method stub
    }

    override fun traducir(identacion: String?, global: Boolean): String? {
        var arguemntos = ""
        if (listaArgumentos != null) {
            for (termino in listaArgumentos!!) {
                arguemntos += termino.traducir() + ", "
            }
            arguemntos = arguemntos.substring(0, arguemntos.length - 2)
        }
        return identacion + identificadorFuncion.lexema + "(" + arguemntos + ")"
    }
}