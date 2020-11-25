package sintaxis

import sintaxis.LlamadoFuncion
import sintaxis.ValorAsignacion
import sintaxis.Expresion
import javax.swing.tree.DefaultMutableTreeNode
import lexico.Categoria
import lexico.Token
import sintaxis.ExpresionAritmetica
import sintaxis.ExpresionLogica
import sintaxis.ExpresionRelacional
import java.util.ArrayList

/**
 * Clase que representa un termino
 */
class Termino {
    private var llamadoFuncion: LlamadoFuncion? = null
    private var valorAsignacion: ValorAsignacion? = null
    private var expresion: Expresion? = null
    var termino: Token? = null

    /**
     * Método constructor
     *
     * @param termino
     */
    constructor(termino: Token?) {
        this.termino = termino
    }

    constructor(llamadoFuncion: LlamadoFuncion?) {
        this.llamadoFuncion = llamadoFuncion
    }

    constructor(valorAsignacion: ValorAsignacion?) {
        this.valorAsignacion = valorAsignacion
    }

    constructor(expresion: Expresion?) {
        this.expresion = expresion
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
    override fun toString(): String {
        return "Termino [termino=$termino]"
    }



    fun llenarTablaSimbolos() {}

}