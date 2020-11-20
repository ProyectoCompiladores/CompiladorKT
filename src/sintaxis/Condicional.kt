package sintaxis

import lexico.Token
import sintaxis.Ciclo
import sintaxis.Condicional
import java.util.*
import javax.swing.tree.DefaultMutableTreeNode

class Condicional : Sentencia {
    private var pregunta: Token
    private var expresionLogica: ExpresionLogica
    private var listaSentencia: ArrayList<Sentencia>
    private var contrario: Token? = null
    private var listaSentencia0: ArrayList<Sentencia>? = null

    /**
     * Constructor que crea condicional sin caso contrario
     *
     *  [pregunta]
     *  [expresionLogica]
     *  [listaSentencia]
     */
    constructor(pregunta: Token, expresionLogica: ExpresionLogica, listaSentencia: ArrayList<Sentencia>) : super() {
        this.pregunta = pregunta
        this.expresionLogica = expresionLogica
        this.listaSentencia = listaSentencia
    }

    /**
     * Constructor que crea condicional con caso contrario
     *
     * @param pregunta
     * @param expresionLogica
     * @param listaSentencia
     * @param contrario
     * @param listaSentencia0
     */
    constructor(pregunta: Token, expresionLogica: ExpresionLogica, listaSentencia: ArrayList<Sentencia>,
                contrario: Token?, listaSentencia0: ArrayList<Sentencia>?) : super() {
        this.pregunta = pregunta
        this.expresionLogica = expresionLogica
        this.listaSentencia = listaSentencia
        this.contrario = contrario
        this.listaSentencia0 = listaSentencia0
    }

    override fun toString(): String {
        return if (contrario != null) {
            ("Condicional [pregunta=" + pregunta + ", expresionLogica=" + expresionLogica + ", listaSentencia="
                    + listaSentencia + ", contrario=" + contrario + ", listaSentencia0=" + listaSentencia0 + "]")
        } else {
            ("Condicional [pregunta=" + pregunta + ", expresionLogica=" + expresionLogica + ", listaSentencia="
                    + listaSentencia)
        }
    }

}
