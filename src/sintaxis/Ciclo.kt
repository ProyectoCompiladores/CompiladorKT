package sintaxis

import lexico.Token
import semantico.Simbolo
import semantico.TablaSimbolos
import sintaxis.Ciclo
import sintaxis.Condicional
import java.util.*
import javax.swing.tree.DefaultMutableTreeNode

/**
 * Clase que representa un cilclo
 */
class Ciclo : Sentencia {
    /**
     * @return the ciclo
     */
    /**
     * @param ciclo
     * the ciclo to set
     */
    var ciclo: Token
    /**
     * @return the mientras
     */
    /**
     * @param mientras
     * the mientras to set
     */
    var mientras: Token
    /**
     * @return the expresionLogica
     */
    /**
     * @param expresionLogica
     * the expresionLogica to set
     */
    var expresionLogica: ExpresionLogica
    /**
     * @return the sentencias
     */
    /**
     * @param sentencias
     * the sentencias to set
     */
    var sentencias: ArrayList<Sentencia>? = null

    /**
     * Constructor con sintencias
     *
     * @param ciclo
     * @param mientras
     * @param expresionLogica
     * @param sentencias
     */
    constructor(ciclo: Token, mientras: Token, expresionLogica: ExpresionLogica, sentencias: ArrayList<Sentencia>?) : super() {
        this.ciclo = ciclo
        this.mientras = mientras
        this.expresionLogica = expresionLogica
        this.sentencias = sentencias
    }

    /**
     * Constructor sin sentencias
     *
     * @param ciclo
     * @param mientras
     * @param expresionLogica
     */
    constructor(ciclo: Token, mientras: Token, expresionLogica: ExpresionLogica) : super() {
        this.ciclo = ciclo
        this.mientras = mientras
        this.expresionLogica = expresionLogica
    }

    /**
     * Método de árbol visual
     *
     */
    override fun getArbolVisual(): DefaultMutableTreeNode {
        val nodo = DefaultMutableTreeNode("Ciclo")
        nodo.add(DefaultMutableTreeNode(ciclo.lexema))
        nodo.add(DefaultMutableTreeNode(mientras.lexema))
        nodo.add(expresionLogica.getArbolVisual())
        if (sentencias != null) {
            for (sentencia in sentencias!!) {
                nodo.add(sentencia.getArbolVisual())
            }
        }
        return nodo
    }

    override fun analizarSemantica(errores: ArrayList<String?>?, ts: TablaSimbolos?, ambito: Simbolo?) {
        expresionLogica.analizarSemantica(errores, ts, ambito)
        val nuevoAmbito = Simbolo(ambito!!.nombre + "ciclo" + ambito.numeroCiclo, ambito.tipo,
                ambito.tipos)
        nuevoAmbito.ambitoPadre = ambito
        for (sentencia in sentencias!!) {
            if (!nuevoAmbito.retorno) {
                sentencia.analizarSemantica(errores, ts, nuevoAmbito)
                if (sentencia.javaClass == Ciclo::class.java) {
                    nuevoAmbito.numeroCiclo = nuevoAmbito.numeroCiclo + 1
                } else if (sentencia.javaClass == Condicional::class.java) {
                    nuevoAmbito.numeroCondicional = nuevoAmbito.numeroCondicional + 1
                }
            } else {
                errores?.add("La función " + ambito.nombre + " ya ha retornado y el código es inalcanzable")
            }
        }
    }

    override fun llenarTablaSimbolos(ts: TablaSimbolos?, ambito: Simbolo?) {}
    override fun traducir(identacion: String?, global: Boolean): String? {
        var sentencias = ""
        for (sentencia in this.sentencias!!) {
            sentencias += """
                ${sentencia.traducir(identacion, false)};
                
                """.trimIndent()
        }
        return """
            ${identacion}while(${expresionLogica.traducir()}){
            $sentencias$identacion}
            """.trimIndent()
    }
}