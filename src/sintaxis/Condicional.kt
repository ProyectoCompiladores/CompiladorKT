package sintaxis

import lexico.Token
import semantico.Simbolo
import semantico.TablaSimbolos
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
     * @param pregunta
     * @param expresionLogica
     * @param listaSentencia
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

    /**
     * Metodo que permite dibujar el arbol grafico de condicional
     */
    override fun getArbolVisual(): DefaultMutableTreeNode {
        val nodo = DefaultMutableTreeNode("Condicional")
        nodo.add(DefaultMutableTreeNode(pregunta.lexema))
        nodo.add(expresionLogica.getArbolVisual())
        for (sentencia in listaSentencia) {
            nodo.add(sentencia.getArbolVisual())
        }
        if (contrario != null) {
            nodo.add(DefaultMutableTreeNode(contrario!!.lexema))
            for (sentencia0 in listaSentencia0!!) {
                nodo.add(sentencia0.getArbolVisual())
            }
        }
        return nodo
    }

    override fun analizarSemantica(errores: ArrayList<String?>?, ts: TablaSimbolos?, ambito: Simbolo?) {
        expresionLogica.analizarSemantica(errores, ts, ambito)
        val nuevoAmbito = Simbolo(ambito!!.nombre + "condicional" + ambito.numeroCondicional, ambito.tipo, ambito.tipos)
        nuevoAmbito.ambitoPadre = ambito
        for (sentencia in listaSentencia) {
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
        nuevoAmbito.nombre = nuevoAmbito.nombre + "contrario" + ambito.numeroCondicional
        if (listaSentencia0 != null) {
            nuevoAmbito.retorno = false
            for (sentencia in listaSentencia0!!) {
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
    }

    override fun llenarTablaSimbolos(ts: TablaSimbolos?, ambito: Simbolo?) {}
    override fun traducir(identacion: String?, global: Boolean): String? {
        var sentencias = ""
        for (sentencia in listaSentencia) {
            sentencias += """
                ${sentencia.traducir(identacion, false)};
                
                """.trimIndent()
        }
        var sentencias0 = ""
        for (sentencia in listaSentencia0!!) {
            sentencias0 += """
                ${sentencia.traducir(identacion, false)};
                
                """.trimIndent()
        }
        return if (listaSentencia0 != null) {
            """
     ${identacion}if(${expresionLogica.traducir()}){
     $sentencias$identacion} else{
     $sentencias0$identacion}
     """.trimIndent()
        } else {
            """
     ${identacion}if(${expresionLogica.traducir()}){
     $sentencias$identacion}
     """.trimIndent()
        }
    }
}