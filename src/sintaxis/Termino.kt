package sintaxis

import sintaxis.LlamadoFuncion
import sintaxis.ValorAsignacion
import sintaxis.Expresion
import javax.swing.tree.DefaultMutableTreeNode
import semantico.TablaSimbolos
import semantico.Simbolo
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

    /**
     * Arbol de Termino
     *
     * @return nodo
     */
    fun getArbolVisual(): DefaultMutableTreeNode? {
            val nodo = DefaultMutableTreeNode("Termino")
            if (termino != null) {
                nodo.add(DefaultMutableTreeNode(termino!!.lexema))
                return nodo
            }
            if (llamadoFuncion != null) {
                nodo.add(llamadoFuncion!!.getArbolVisual())
                return nodo
            }
            if (valorAsignacion != null) {
                nodo.add(valorAsignacion!!.arbolVisual)
                return nodo
            }
            if (expresion != null) {
                nodo.add(expresion!!.getArbolVisual())
                return nodo
            }
            return nodo
        }

    fun analizarSemantica(errores: ArrayList<String?>, ts: TablaSimbolos, ambito: Simbolo) {
        if (termino != null) {
            for (simbolo in ts.tablaSimbolos) {
                if (simbolo.ambito != null) {
                    if (termino!!.lexema == simbolo.nombre && !simbolo.isEsFuncion
                            && simbolo.ambito == ambito) {
                        return
                    }
                } else {
                    if (termino!!.lexema == simbolo.nombre && !simbolo.isEsFuncion) {
                        return
                    }
                }
            }
            if (ambito.ambitoPadre != null) {
                analizarSemantica(errores, ts, ambito.ambitoPadre!!)
            } else {
                errores.add(termino!!.lexema + " No se encontró la variable invocada")
            }
        }
        if (llamadoFuncion != null) {
            for (simbolo in ts.tablaSimbolos) {
                if (llamadoFuncion!!.identificadorFuncion.lexema == simbolo.nombre && simbolo.isEsFuncion) {
                    return
                } else {
                    if (ambito.ambitoPadre != null) {
                        analizarSemantica(errores, ts, ambito.ambitoPadre!!)
                    } else {
                        errores.add(llamadoFuncion!!.identificadorFuncion.lexema
                                + " No se encontró la función invocada")
                    }
                }
            }
        }
        if (valorAsignacion != null) {
            return
        }
        if (expresion != null) {
            expresion!!.analizarSemantica(errores, ts, ambito)
            return
        }
    }

    fun llenarTablaSimbolos() {}
    fun getTipo(errores: ArrayList<String?>, ts: TablaSimbolos, ambito: Simbolo): String? {
        if (termino != null) {
            for (simbolo in ts.tablaSimbolos) {
                if (termino!!.lexema == simbolo.nombre && !simbolo.isEsFuncion
                        && simbolo.ambito == ambito) {
                    return simbolo.tipo
                }
            }
            if (ambito.ambitoPadre != null) {
                val tipo = getTipo(errores, ts, ambito.ambitoPadre!!)
                if (tipo != null) {
                    return tipo
                }
            } else {
                errores.add(termino!!.lexema + " No se encontró la variable invocada")
            }
        } else if (llamadoFuncion != null) {
            for (simbolo in ts.tablaSimbolos) {
                if (llamadoFuncion!!.identificadorFuncion.lexema == simbolo.nombre && simbolo.isEsFuncion) {
                    return simbolo.tipo
                } else {
                    if (ambito.ambitoPadre != null) {
                        getTipo(errores, ts, ambito.ambitoPadre!!)
                    } else {
                        errores.add(llamadoFuncion!!.identificadorFuncion.lexema
                                + " No se encontró la función invocada")
                    }
                }
            }
        } else if (valorAsignacion != null) {
            return when (valorAsignacion!!.tipoDato.categoria) {
                Categoria.CARACTER -> "ltr"
                Categoria.ENTERO -> "ntr"
                Categoria.REAL -> "pntdec"
                Categoria.CADENA_CARACTERES -> "ltrarr"
                else -> {
                    if (valorAsignacion!!.tipoDato.lexema == "v" || valorAsignacion!!.tipoDato.lexema == "f") {
                        "binary"
                    } else null
                }
            }
        } else if (expresion != null) {
            if (expresion!!.javaClass == ExpresionAritmetica::class.java) {
                return (expresion as ExpresionAritmetica).getTipo(errores, ts, ambito)
            } else if (expresion!!.javaClass == ExpresionLogica::class.java || expresion!!.javaClass == ExpresionRelacional::class.java) {
                return "binary"
            }
        }
        return null
    }

    fun traducir(): String {
        var termino = ""
        if (this.termino != null) {
            termino = this.termino!!.lexema.replace("<".toRegex(), "").replace(">".toRegex(), "").replace("-".toRegex(), "_")
        } else if (llamadoFuncion != null) {
            termino = llamadoFuncion!!.traducir("", false)!!
        } else if (valorAsignacion != null) {
            if (valorAsignacion!!.tipoDato.lexema == "v") {
                termino = "true"
            } else if (valorAsignacion!!.tipoDato.lexema == "f") {
                termino = "false"
            } else {
                termino = valorAsignacion!!.tipoDato.lexema
                termino = termino.replace("[$]".toRegex(), "\"")
                termino = termino.replace("'".toRegex(), "\\\\")
                termino = termino.replace("#".toRegex(), "'")
            }
        } else if (expresion != null) {
            termino = expresion!!.traducir()!!
        }
        return termino
    }
}