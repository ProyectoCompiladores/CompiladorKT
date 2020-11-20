package sintaxis

import lexico.Categoria
import lexico.Token
import java.util.*

class AnalizadorSintactico(
        /**
         * Clase que representa el analizador sintactico
         */
        var tablaSimbolos: ArrayList<Token>) {
    /**
     * @return the tablaSimbolos
     */
    /**
     *  [tablaSimbolos]
     * the tablaSimbolos to set
     */
    /**
     * @return the tablaErrores
     */
    /**
     *  [tablaErrores]
     * the tablaErrores to set
     */
    var tablaErrores: ArrayList<ErrorSintactico>
    private var posActual = 0
    private var tokenActual: Token
    var unidadCompilacion: UnidadCompilacion? = null

    /**
     * Metodo que analiza el codigo sintacticamente
     */
    fun analizar() {
        unidadCompilacion = esUnidadDeCompilacion()
        if (unidadCompilacion != null) {
            tablaErrores.clear()
        }
    }

    /**
     * Metodo que verifica si es unidad de compilacion
     *
     * <[UnidadCompilacion]>::= clase identificadorClase grupadorIzquierdo
     * <[CuerpoClase]> agrupadorDerechoa
     *
     * @return unidadDeCompilacion{@link UnidadCompilacion}
     */
    private fun esUnidadDeCompilacion(): UnidadCompilacion? {
        val unidadCompilacion: UnidadCompilacion
        if (tokenActual.lexema == "clase") {
            val clase = tokenActual
            obtenerSiguienteToken()
            if (tokenActual.categoria == Categoria.IDENTIFICADOR_CLASE) {
                val identificadorClase = tokenActual
                obtenerSiguienteToken()
                if (tokenActual.categoria == Categoria.AGRUPADOR_IZQUIERDO) {
                    obtenerSiguienteToken()
                    val cuerpoClase = esCuerpoClase()
                    if (cuerpoClase != null) {
                        if (tokenActual.categoria == Categoria.AGRUPADOR_DERECHO) {
                            unidadCompilacion = UnidadCompilacion(clase, identificadorClase, cuerpoClase)
                            return unidadCompilacion
                        } else {
                            reportarError("Falta agrupador derecho", tokenActual.fila, tokenActual.columna,
                                    tokenActual.columnaReal)
                        }
                    } else {
                        reportarError("Falta cuerpo de clase", tokenActual.fila, tokenActual.columna,
                                tokenActual.columnaReal)
                    }
                } else {
                    reportarError("Falta agrupador izquierdo", tokenActual.fila, tokenActual.columna,
                            tokenActual.columnaReal)
                }
            } else {
                reportarError("Falta identificador de clase", tokenActual.fila, tokenActual.columna,
                        tokenActual.columnaReal)
            }
        } else {
            reportarError("Falta La Palabra Reservada Clase", tokenActual.fila, tokenActual.columna,
                    tokenActual.columnaReal)
        }
        return null
    }

    /**
     * Metodo que verifica si es cuerpo de clase
     *
     * <[CuerpoClase]>::= <[Funcion]> [<[CuerpoClase]>] |
     * <[DeclaracionVariable]> [<[CuerpoClase]>]
     *
     * @return cuerpoClase{@link CuerpoClase}
     */
    private fun esCuerpoClase(): CuerpoClase? {
        val posInicial = posActual
        val funcion = esFuncion()
        if (funcion != null) {
            obtenerSiguienteToken()
            val cuerpoClase = esCuerpoClase()
            return cuerpoClase?.let { CuerpoClase(funcion, it) } ?: CuerpoClase(funcion)
        } else {
            hacerBactracking(posInicial)
            val declaracionVariable = esDeclaracionVariable()
            if (declaracionVariable != null) {
                obtenerSiguienteToken()
                val cuerpoClase1 = esCuerpoClase()
                return cuerpoClase1?.let { CuerpoClase(declaracionVariable, it) }
                        ?: CuerpoClase(declaracionVariable)
            }
        }
        return null
    }

    /**
     * Metodo que verifica si es una funcion
     *
     * <[Funcion]>::= visibilidad <[TipoRetorno]> funapp
     * identificadorFuncion parentesisIzquierdo <"Lista"[Parametro]>
     * parentesisDerecho agrupadorIzquierdo <"Lista" [Sentencia]>
     * agrupadorDerecho
     *
     * @return funcion{@link Funcion}
     */
    private fun esFuncion(): Funcion? {
        val visibilidad = esVisibilidad()
        if (visibilidad != null) {
            obtenerSiguienteToken()
            val tipoRetorno = esTipoRetorno()
            if (tipoRetorno != null) {
                obtenerSiguienteToken()
                if (tokenActual.lexema == "funapp") {
                    val funcion = tokenActual
                    obtenerSiguienteToken()
                    if (tokenActual.categoria == Categoria.IDENTIFICADOR_METODO) {
                        val idFuncion = tokenActual
                        obtenerSiguienteToken()
                        if (tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO) {
                            obtenerSiguienteToken()
                            val listaParametros = esListaParametro()
                            if (listaParametros != null) {
                                if (tokenActual.categoria == Categoria.PARENTESIS_DERECHO) {
                                    obtenerSiguienteToken()
                                    if (tokenActual.categoria == Categoria.AGRUPADOR_IZQUIERDO) {
                                        obtenerSiguienteToken()
                                        val listaSentencias = esListaSentencia()
                                        if (listaSentencias != null) {
                                            if (tokenActual.categoria == Categoria.AGRUPADOR_DERECHO) {
                                                return Funcion(visibilidad, tipoRetorno, idFuncion, funcion,
                                                        listaParametros, listaSentencias)
                                            } else {
                                                reportarError("Falta Agrupador Derecho", tokenActual.fila,
                                                        tokenActual.columna, tokenActual.columnaReal)
                                            }
                                        } else {
                                            if (tokenActual.categoria == Categoria.AGRUPADOR_DERECHO) {
                                                return Funcion(visibilidad, tipoRetorno, idFuncion, listaParametros,
                                                        funcion)
                                            } else {
                                                reportarError("Falta Agrupador Derecho", tokenActual.fila,
                                                        tokenActual.columna, tokenActual.columnaReal)
                                            }
                                        }
                                    } else {
                                        reportarError("Falta Agrupador Izquierdo", tokenActual.fila,
                                                tokenActual.columna, tokenActual.columnaReal)
                                    }
                                } else {
                                    reportarError("Falta parentesis derecho", tokenActual.fila,
                                            tokenActual.columna, tokenActual.columnaReal)
                                }
                            } else {
                                if (tokenActual.categoria == Categoria.PARENTESIS_DERECHO) {
                                    obtenerSiguienteToken()
                                    if (tokenActual.categoria == Categoria.AGRUPADOR_IZQUIERDO) {
                                        obtenerSiguienteToken()
                                        val listaSentencias = esListaSentencia()
                                        if (listaSentencias != null) {
                                            if (tokenActual.categoria == Categoria.AGRUPADOR_DERECHO) {
                                                return Funcion(visibilidad, tipoRetorno, idFuncion, funcion,
                                                        listaParametros, listaSentencias)
                                            } else {
                                                reportarError("Falta Agrupador Derecho", tokenActual.fila,
                                                        tokenActual.columna, tokenActual.columnaReal)
                                            }
                                        } else {
                                            if (tokenActual.categoria == Categoria.AGRUPADOR_DERECHO) {
                                                return Funcion(visibilidad, tipoRetorno, idFuncion, listaParametros,
                                                        funcion)
                                            } else {
                                                reportarError("Falta Agrupador Derecho", tokenActual.fila,
                                                        tokenActual.columna, tokenActual.columnaReal)
                                            }
                                        }
                                    } else {
                                        reportarError("Falta Agrupador Izquierdo", tokenActual.fila,
                                                tokenActual.columna, tokenActual.columnaReal)
                                    }
                                } else {
                                    reportarError("Falta parentesis derecho", tokenActual.fila,
                                            tokenActual.columna, tokenActual.columnaReal)
                                }
                            }
                        } else {
                            reportarError("Falta Parentesis Derecho", tokenActual.fila, tokenActual.columna,
                                    tokenActual.columnaReal)
                        }
                    } else {
                        reportarError("Falta Identificador Metodo", tokenActual.fila, tokenActual.columna,
                                tokenActual.columnaReal)
                    }
                } else {
                    reportarError("Falta La Palabra Reservada funapp", tokenActual.fila, tokenActual.columna,
                            tokenActual.columnaReal)
                }
            } else {
                reportarError("Falta Tipo De Retorno", tokenActual.fila, tokenActual.columna,
                        tokenActual.columnaReal)
            }
        } else {
            val tipoRetorno = esTipoRetorno()
            if (tipoRetorno != null) {
                obtenerSiguienteToken()
                if (tokenActual.lexema == "funapp") {
                    val funcion = tokenActual
                    obtenerSiguienteToken()
                    if (tokenActual.categoria == Categoria.IDENTIFICADOR_METODO) {
                        val idFuncion = tokenActual
                        obtenerSiguienteToken()
                        if (tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO) {
                            obtenerSiguienteToken()
                            val listaParametros = esListaParametro()
                            if (listaParametros != null) {
                                obtenerSiguienteToken()
                                if (tokenActual.categoria == Categoria.PARENTESIS_DERECHO) {
                                    obtenerSiguienteToken()
                                    if (tokenActual.categoria == Categoria.AGRUPADOR_IZQUIERDO) {
                                        obtenerSiguienteToken()
                                        val listaSentencias = esListaSentencia()
                                        if (listaSentencias != null) {
                                            obtenerSiguienteToken()
                                            if (tokenActual.categoria == Categoria.AGRUPADOR_DERECHO) {
                                                return Funcion(visibilidad, tipoRetorno, idFuncion, funcion,
                                                        listaParametros, listaSentencias)
                                            }
                                        } else {
                                            if (tokenActual.categoria == Categoria.AGRUPADOR_DERECHO) {
                                                return Funcion(visibilidad, tipoRetorno, idFuncion, listaParametros,
                                                        funcion)
                                            } else {
                                                reportarError("Falta Agrupador Derecho", tokenActual.fila,
                                                        tokenActual.columna, tokenActual.columnaReal)
                                            }
                                        }
                                    } else {
                                        reportarError("Falta Agrupador Izquierdo", tokenActual.fila,
                                                tokenActual.columna, tokenActual.columnaReal)
                                    }
                                } else {
                                    reportarError("Falta parentesis derecho", tokenActual.fila,
                                            tokenActual.columna, tokenActual.columnaReal)
                                }
                            } else {
                                if (tokenActual.categoria == Categoria.PARENTESIS_DERECHO) {
                                    obtenerSiguienteToken()
                                    if (tokenActual.categoria == Categoria.AGRUPADOR_IZQUIERDO) {
                                        obtenerSiguienteToken()
                                        val listaSentencias = esListaSentencia()
                                        if (listaSentencias != null) {
                                            obtenerSiguienteToken()
                                            if (tokenActual.categoria == Categoria.AGRUPADOR_DERECHO) {
                                                return Funcion(visibilidad, tipoRetorno, idFuncion, funcion,
                                                        listaParametros, listaSentencias)
                                            }
                                        } else {
                                            if (tokenActual.categoria == Categoria.AGRUPADOR_DERECHO) {
                                                return Funcion(visibilidad, tipoRetorno, idFuncion, listaParametros,
                                                        funcion)
                                            } else {
                                                reportarError("Falta Agrupador Derecho", tokenActual.fila,
                                                        tokenActual.columna, tokenActual.columnaReal)
                                            }
                                        }
                                    } else {
                                        reportarError("Falta Agrupador Izquierdo", tokenActual.fila,
                                                tokenActual.columna, tokenActual.columnaReal)
                                    }
                                } else {
                                    reportarError("Falta parentesis derecho", tokenActual.fila,
                                            tokenActual.columna, tokenActual.columnaReal)
                                }
                            }
                        } else {
                            reportarError("Falta Parentesis Derecho", tokenActual.fila, tokenActual.columna,
                                    tokenActual.columnaReal)
                        }
                    } else {
                        reportarError("Falta Identificador Metodo", tokenActual.fila, tokenActual.columna,
                                tokenActual.columnaReal)
                    }
                } else {
                    reportarError("Falta La Palabra Reservada funapp", tokenActual.fila, tokenActual.columna,
                            tokenActual.columnaReal)
                }
            } else {
                reportarError("Falta Tipo De Retorno", tokenActual.fila, tokenActual.columna,
                        tokenActual.columnaReal)
            }
        }
        return null
    }

    /**
     * Metodo que verifica si es visibilidad
     *
     * <{link Visibilidad}>::= visible | oculto
     *
     * @return visibilidad{@link Visibilidad}
     */
    private fun esVisibilidad(): Token? {
        return if (tokenActual.lexema == "visible" || tokenActual.lexema == "oculto") {
            tokenActual
        } else null
    }

    /**
     * Metodo que verifica si es un tipo de retorno
     *
     * <[TipoRetorno]>::= sr | [<{link TipoDato}>]
     *
     * @return tipoRetorno{@link TipoRetorno}
     */
    private fun esTipoRetorno(): TipoRetorno? {
        val retorno = esTipoDato()
        if (retorno != null) {
            return TipoRetorno(retorno)
        } else if (tokenActual.lexema == "sr") {
            return TipoRetorno(tokenActual)
        }
        return null
    }

    /**
     * Metodo que verifica si es un tipo de dato
     *
     * <{link TipoDato}>::= ntr | ltr | ltrarr | binary | pntdec
     *
     * @return tipoDato{@link TipoDato}
     */
    private fun esTipoDato(): Token? {
        return if (tokenActual.lexema == "ltr" || tokenActual.lexema == "ntr" || tokenActual.lexema == "ltrarr" || tokenActual.lexema == "pntdec" || tokenActual.lexema == "binary") {
            tokenActual
        } else null
    }

    /**
     * Metodo que verifica si es un parametro
     *
     * <"Lista"[Parametro]>::= <[Parametro]> ["|"
     * <"Lista"[Parametro]>] <[Parametro]>::= <{link TipoDato}>
     * [$arr()] identificadorVariable
     *
     * @return parametro{@link Parametro}
     */
    private fun esParametro(): Parametro? {
        val parametro: Parametro
        val posInicial = posActual
        val tipoDatp = esTipoDato()
        if (tipoDatp != null) {
            val tipoDato = tokenActual
            obtenerSiguienteToken()
            if (tokenActual.lexema == "\$arr()") {
                val arr = tokenActual
                obtenerSiguienteToken()
                if (tokenActual.categoria == Categoria.IDENTIFICADOR_VARIABLE) {
                    parametro = Parametro(tipoDato, arr, tokenActual)
                    return parametro
                } else {
                    reportarError("Debe seguir identificador de variable", tokenActual.fila,
                            tokenActual.columna, tokenActual.columnaReal)
                }
            } else if (tokenActual.categoria == Categoria.IDENTIFICADOR_VARIABLE) {
                parametro = Parametro(tipoDato, tokenActual)
                return parametro
            } else {
                hacerBactracking(posInicial)
            }
        }
        return null
    }

    /**
     * Metodo que verifica si es una sentencia
     *
     * <"List"[Sentencia]>::= <[Sentencia]>[<"List"[Sentencia]>]
     * <[Sentencia]>::= <[Condicional]> | <[Ciclo]> |
     * <[Retorno]> | <[Impresion]> | <[Lectura]> |
     * <[DeclaracionVariable]> | <[AsignacionVariable]> |
     * <[SentenciaIncremento]> | <{link SentenciDecremento}>|
     * <[LlamadoFuncion]>
     *
     * @return sentencia{@link Sentencia}
     */
    private fun esSentencia(): Sentencia? {
        val posInicial = posActual
        var sentencia: Sentencia?
        sentencia = esCondicional()
        if (sentencia != null) {
            return sentencia
        }
        hacerBactracking(posInicial)
        sentencia = esCiclo()
        if (sentencia != null) {
            return sentencia
        }
        hacerBactracking(posInicial)
        sentencia = esRetorno()
        if (sentencia != null) {
            return sentencia
        }
        hacerBactracking(posInicial)
        sentencia = esImpresion()
        if (sentencia != null) {
            return sentencia
        }
        hacerBactracking(posInicial)
        sentencia = esLectura()
        if (sentencia != null) {
            return sentencia
        }
        hacerBactracking(posInicial)
        sentencia = esDeclaracionVariable()
        if (sentencia != null) {
            return sentencia
        }
        hacerBactracking(posInicial)
        sentencia = esAsignacionVariable()
        if (sentencia != null) {
            return sentencia
        }
        hacerBactracking(posInicial)
        sentencia = esSentenciaIncremento()
        if (sentencia != null) {
            return sentencia
        }
        hacerBactracking(posInicial)
        sentencia = esSentenciaDecremento()
        if (sentencia != null) {
            return sentencia
        }
        hacerBactracking(posInicial)
        sentencia = esLlamadoFuncion()
        if (sentencia != null) {
            return sentencia
        }
        hacerBactracking(posInicial)
        sentencia = esRetorno()
        return sentencia
    }

    /**
     * Metodo que verifica si es un condicional
     *
     * <[Condicional]>::= pregunta parentesisIzquierdo
     * <[ExpresionLogica]> parentesisDerecho agrupadorIzquierdo <"Lista"
     * [Sentencia]> agrupadorDerecho [contrario agrupadorIzquierdo <"Lista"
     * [Sentencia]> agrupadorDerecho]
     *
     * @return condicional{@link Condicional}
     */
    private fun esCondicional(): Condicional? {
        val condicional: Condicional
        if (tokenActual.lexema == "pregunta") {
            val pregunta = tokenActual
            obtenerSiguienteToken()
            if (tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO) {
                obtenerSiguienteToken()
                val expresionLogica = esExpresionLogica()
                if (expresionLogica != null) {
                    if (tokenActual.categoria == Categoria.PARENTESIS_DERECHO) {
                        obtenerSiguienteToken()
                        if (tokenActual.categoria == Categoria.AGRUPADOR_IZQUIERDO) {
                            obtenerSiguienteToken()
                            val listaSentencia = esListaSentencia() // lista sentencia
                            if (listaSentencia != null) {
                                if (tokenActual.categoria == Categoria.AGRUPADOR_DERECHO) {
                                    obtenerSiguienteToken()
                                    if (tokenActual.lexema == "contrario") {
                                        val contrario = tokenActual
                                        obtenerSiguienteToken()
                                        if (tokenActual.categoria == Categoria.AGRUPADOR_IZQUIERDO) {
                                            obtenerSiguienteToken()
                                            val listaSentencia1 = esListaSentencia() // lista
                                            // sentencia
                                            if (listaSentencia1 != null) {
                                                if (tokenActual.categoria == Categoria.AGRUPADOR_DERECHO) {
                                                    condicional = Condicional(pregunta, expresionLogica,
                                                            listaSentencia, contrario, listaSentencia1)
                                                    return condicional
                                                } else {
                                                    reportarError("Falta agrupador derecho", tokenActual.fila,
                                                            tokenActual.columna, tokenActual.columnaReal)
                                                }
                                            } else {
                                                reportarError("Falta lista sentencia", tokenActual.fila,
                                                        tokenActual.columna, tokenActual.columnaReal)
                                            }
                                        } else {
                                            reportarError("Falta agrupador izquierdo", tokenActual.fila,
                                                    tokenActual.columna, tokenActual.columnaReal)
                                        }
                                    } else {
                                        condicional = Condicional(pregunta, expresionLogica, listaSentencia)
                                        hacerBactracking(posActual - 1)
                                        return condicional
                                    }
                                } else {
                                    reportarError("Falta agrupador derecho", tokenActual.fila,
                                            tokenActual.columna, tokenActual.columnaReal)
                                }
                            } else {
                                reportarError("Falta lista sentencia", tokenActual.fila, tokenActual.columna,
                                        tokenActual.columnaReal)
                            }
                        } else {
                            reportarError("Falta agrupador izquierdo", tokenActual.fila, tokenActual.columna,
                                    tokenActual.columnaReal)
                        }
                    } else {
                        reportarError("Falta parentesis derecho", tokenActual.fila, tokenActual.columna,
                                tokenActual.columnaReal)
                    }
                } else {
                    reportarError("Falta expresion logica", tokenActual.fila, tokenActual.columna,
                            tokenActual.columnaReal)
                }
            } else {
                reportarError("Falta parentesis izquierdo", tokenActual.fila, tokenActual.columna,
                        tokenActual.columnaReal)
            }
        }
        return null
    }

    /**
     * Metodo que verifica si es una expresion
     *
     * <[Expresion]> ::= <[ExpresionLogica]> |
     * <[ExpresionRelacional]> | <[ExpresionRelacional]>
     *
     * @return expresion{@link Expresion}
     */
    private fun esExpresion(): Expresion? {
        val posInicial = posActual
        var expresion: Expresion? = esExpresionLogica()
        if (expresion != null) {
            if ((expresion as ExpresionLogica).expresionRelacional!!.expRelacional != null) {
                return expresion
            }
        }
        hacerBactracking(posInicial)
        expresion = esExpresionRelacional()
        if (expresion != null) {
            if (expresion.expRelacional != null) {
                return expresion
            }
        }
        hacerBactracking(posInicial)
        expresion = esExpresionAritmetica()
        if (expresion != null) {
            return expresion
        }
        hacerBactracking(posInicial)
        expresion = esExpresionCadena()
        return expresion
    }

    /**
     * Metodo que verifica si es una expresion logica
     *
     * <[ExpresionLogica]>::= <[ExpresionRelacional]> [operadorLogico
     * <[ExpresionLogica]>] | parentesisIzquierdo <[ExpresionLogica]>
     * parentesisDerecho
     *
     * @return expresionLogica{@link ExpresionLogica}
     */
    private fun esExpresionLogica(): ExpresionLogica? {
        val posInicial = posActual
        val expresionRelacional = esExpresionRelacional()
        if (expresionRelacional != null) {
            if (tokenActual.categoria == Categoria.OPERADOR_LOGICO) {
                val opLogico = tokenActual
                obtenerSiguienteToken()
                val expresionLogica = esExpresionLogica()
                if (expresionLogica != null) {
                    return ExpresionLogica(expresionRelacional, opLogico, expresionLogica)
                } else {
                    reportarError("Falta expresión lógica", tokenActual.fila, tokenActual.columna,
                            tokenActual.columnaReal)
                }
            } else {
                return ExpresionLogica(expresionRelacional)
            }
        } else {
            hacerBactracking(posInicial)
            if (tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO) {
                obtenerSiguienteToken()
                val expresionLogica = esExpresionLogica()
                if (expresionLogica != null) {
                    if (tokenActual.categoria == Categoria.PARENTESIS_DERECHO) {
                        obtenerSiguienteToken()
                        return expresionLogica
                    } else {
                        reportarError("Falta parentesis derecho", tokenActual.fila, tokenActual.columna,
                                tokenActual.columnaReal)
                    }
                } else {
                    reportarError("Falta expresión lógica", tokenActual.fila, tokenActual.columna,
                            tokenActual.columnaReal)
                }
            } else {
                reportarError("Falta Parentesis Izquierdo", tokenActual.fila, tokenActual.columna,
                        tokenActual.columnaReal)
            }
        }
        return null
    }

    /**
     * Metodo que verifica si es una expresion relacional
     *
     * <[ExpresionRelacional]>::= <[ExpresionAritmetica]>
     * [operadorRelacional <[ExpresionRelacional]>] | parentesisIzquierdo
     * <[ExpresionRelacional]> parentesisDerecho
     *
     * @return expresionLogica{@link ExpresionRelacional}
     */
    private fun esExpresionRelacional(): ExpresionRelacional? {
        val posInicial = posActual
        val expresionAritmetica = esExpresionAritmetica()
        if (expresionAritmetica != null) {
            if (tokenActual.categoria == Categoria.OPERADOR_RELACIONAL) {
                val opeRelacional = tokenActual
                obtenerSiguienteToken()
                val expresionRelacional = esExpresionRelacional()
                if (expresionRelacional != null) {
                    return ExpresionRelacional(expresionAritmetica, opeRelacional, expresionRelacional)
                } else {
                    reportarError("Falta Expresion Relacional", tokenActual.fila, tokenActual.columna,
                            tokenActual.columnaReal)
                }
            } else {
                return ExpresionRelacional(expresionAritmetica)
            }
        } else {
            hacerBactracking(posInicial)
            if (tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO) {
                obtenerSiguienteToken()
                val expRelacional = esExpresionRelacional()
                if (expRelacional != null) {
                    obtenerSiguienteToken()
                    if (tokenActual.categoria == Categoria.PARENTESIS_DERECHO) {
                        return ExpresionRelacional(expRelacional)
                    } else {
                        reportarError("Falta Parentesis Derecho", tokenActual.fila, tokenActual.columna,
                                tokenActual.columnaReal)
                    }
                } else {
                    reportarError("Falta Expresion Relacional", tokenActual.fila, tokenActual.columna,
                            tokenActual.columnaReal)
                }
            } else {
                reportarError("Falta Parentesis Izquierdo", tokenActual.fila, tokenActual.columna,
                        tokenActual.columnaReal)
            }
        }
        return null
    }

    /**
     * Metodo que verifica si es una expresion aritmetica
     *
     * <[ExpresionAritmetica]>::= <[Termino]> [operadorAritmetico
     * <[ExpresionAritmetica]>] | parentesisIzquierdo
     * <[ExpresionAritmetica]> parentesisDerecho | parentesisIzquierdo
     * <[Termino]> [operadorAritmetico <[ExpresionAritmetica]>]
     * parentesisDerecho
     *
     * @return expresionAritmetica{@link ExpresionAritmetica}
     */
    private fun esExpresionAritmetica(): ExpresionAritmetica? {
        if (tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO) {
            obtenerSiguienteToken()
            val termino = esTermino()
            if (termino != null) {
                obtenerSiguienteToken()
                if (tokenActual.categoria == Categoria.PARENTESIS_DERECHO) {
                    obtenerSiguienteToken()
                    val opeAritmetico = tokenActual
                    if (opeAritmetico.categoria == Categoria.OPERADOR_ARITMETICO) {
                        obtenerSiguienteToken()
                        val expAritmetica = esExpresionAritmetica()
                        if (expAritmetica != null) {
                            return ExpresionAritmetica(termino, opeAritmetico, expAritmetica)
                        } else {
                            reportarError("Falta Expresion Aritmetica", tokenActual.fila, tokenActual.columna,
                                    tokenActual.columnaReal)
                        }
                    } else {
                        return ExpresionAritmetica(termino)
                    }
                } else {
                    reportarError("Falta Parentesis Derecho", tokenActual.fila, tokenActual.columna,
                            tokenActual.columnaReal)
                }
            } else {
                val expresionAritmetica = esExpresionAritmetica()
                if (expresionAritmetica != null) {
                    obtenerSiguienteToken()
                    if (tokenActual.categoria == Categoria.PARENTESIS_DERECHO) {
                        return ExpresionAritmetica(expresionAritmetica)
                    } else {
                        reportarError("Falta Parentesis Derecho", tokenActual.fila, tokenActual.columna,
                                tokenActual.columnaReal)
                    }
                } else {
                    reportarError("Falta Expresion Aritmetica O Termino", tokenActual.fila,
                            tokenActual.columna, tokenActual.columnaReal)
                }
            }
        } else {
            if (tokenActual.categoria != Categoria.PARENTESIS_DERECHO) {
                val termino = esTermino()
                if (termino != null) {
                    obtenerSiguienteToken()
                    val opArt = tokenActual
                    if (opArt.categoria == Categoria.OPERADOR_ARITMETICO) {
                        obtenerSiguienteToken()
                        val expresionAritmetica = esExpresionAritmetica()
                        if (expresionAritmetica != null) {
                            return ExpresionAritmetica(termino, opArt, expresionAritmetica)
                        } else {
                            reportarError("Falta Expresion Aritmetica", tokenActual.fila, tokenActual.columna,
                                    tokenActual.columnaReal)
                        }
                    } else {
                        return ExpresionAritmetica(termino)
                    }
                } else {
                    reportarError("Falta Termino", tokenActual.fila, tokenActual.columna,
                            tokenActual.columnaReal)
                }
            }
        }
        return null
    }

    /**
     * Metodo que verifica si es un termino
     *
     * <"Lista" [Termino]>::= <[Termino]> ["|" <"Lista"
     * [Termino]>] <[Termino]>::= identificadorVariable |
     * <[LlamadoFuncion]> | <[ValorAsignacion]> | <[Expresion]>
     *
     * @return termino{@link Termino}
     */
    private fun esTermino(): Termino? {
        if (tokenActual.categoria == Categoria.IDENTIFICADOR_VARIABLE) {
            return Termino(tokenActual)
        }
        val llamadoFuncion = esLlamadoFuncion()
        if (llamadoFuncion != null) {
            return Termino(llamadoFuncion)
        }
        val valorAsignacion = esValorAsignacion()
        if (valorAsignacion != null) {
            return Termino(valorAsignacion)
        }
        val expresion = esExpresion()
        return expresion?.let { Termino(it) }
    }

    /**
     * Metodo que verifica si es un valor de asignacion
     *
     * <[ValorAsignacion]>::= entero | real | caracter | cadena | booleano
     *
     * @return valorAsignacion{@link ValorAsignacion}
     */
    private fun esValorAsignacion(): ValorAsignacion? {
        return if (tokenActual.categoria == Categoria.ENTERO || tokenActual.categoria == Categoria.REAL || tokenActual.categoria == Categoria.CARACTER || tokenActual.categoria == Categoria.CADENA_CARACTERES || tokenActual.lexema == "v" || tokenActual.lexema == "f") {
            ValorAsignacion(tokenActual)
        } else null
    }

    /**
     * Metodo que verifica si es un llamado a una funcion
     *
     * <[LlamadoFuncion]>::= identificadorFuncion parentesisIzquierdo <"Lista"
     * [Termino]> parentesisDerecho fin
     *
     * @return llamadoFuncion{@link LlamadoFuncion}
     */
    private fun esLlamadoFuncion(): LlamadoFuncion? {
        if (tokenActual.categoria == Categoria.IDENTIFICADOR_METODO) {
            val identificadorFuncion = tokenActual
            obtenerSiguienteToken()
            if (tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO) {
                obtenerSiguienteToken()
                val listaArgumentos = esListaArgumento()
                if (listaArgumentos != null) {
                    if (tokenActual.categoria == Categoria.PARENTESIS_DERECHO) {
                        return LlamadoFuncion(identificadorFuncion, listaArgumentos)
                    } else {
                        reportarError("Debe seguir parentesis derecho o lista de terminos", tokenActual.fila,
                                tokenActual.columna, tokenActual.columnaReal)
                    }
                } else if (tokenActual.categoria == Categoria.PARENTESIS_DERECHO) {
                    return LlamadoFuncion(identificadorFuncion)
                } else {
                    reportarError("Debe seguir parentesis derecho o lista de terminos", tokenActual.fila,
                            tokenActual.columna, tokenActual.columnaReal)
                }
            } else {
                reportarError("Falta parentesisIzquierdo", tokenActual.fila, tokenActual.columna,
                        tokenActual.columnaReal)
            }
        } else {
            reportarError("Falta Identificador Metodo", tokenActual.fila, tokenActual.columna,
                    tokenActual.columnaReal)
        }
        return null
    }

    /**
     * Metodo que verifica si es un ciclo
     *
     * <[Ciclo]>::= ciclo mientras parentesisIzquierdo
     * <[ExpresionLogica]> parentesisDerecho agrupadorIzquierdo
     * [<"List"[Sentencia]>] agrupadorDerecho
     *
     * @return ciclo{@link Ciclo}
     */
    private fun esCiclo(): Ciclo? {
        if (tokenActual.lexema == "ciclo") {
            val ciclo = tokenActual
            obtenerSiguienteToken()
            if (tokenActual.lexema == "mientras") {
                val mientras = tokenActual
                obtenerSiguienteToken()
                if (tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO) {
                    obtenerSiguienteToken()
                    val expresionLogica = esExpresionLogica()
                    if (expresionLogica != null) {
                        if (tokenActual.categoria == Categoria.PARENTESIS_DERECHO) {
                            obtenerSiguienteToken()
                            if (tokenActual.categoria == Categoria.AGRUPADOR_IZQUIERDO) {
                                obtenerSiguienteToken()
                                val listaSentencia = esListaSentencia()
                                if (listaSentencia != null) {
                                    if (tokenActual.categoria == Categoria.AGRUPADOR_DERECHO) {
                                        return Ciclo(ciclo, mientras, expresionLogica, listaSentencia)
                                    } else {
                                        reportarError("Falta agrupador derecho", tokenActual.fila,
                                                tokenActual.columna, tokenActual.columnaReal)
                                    }
                                } else {
                                    if (tokenActual.categoria == Categoria.AGRUPADOR_DERECHO) {
                                        return Ciclo(ciclo, mientras, expresionLogica)
                                    } else {
                                        reportarError("Falta agrupador derecho", tokenActual.fila,
                                                tokenActual.columna, tokenActual.columnaReal)
                                    }
                                }
                            } else {
                                reportarError("Falta agrupador izquierdo", tokenActual.fila,
                                        tokenActual.columna, tokenActual.columnaReal)
                            }
                        } else {
                            reportarError("Falta parentesis derecho", tokenActual.fila, tokenActual.columna,
                                    tokenActual.columnaReal)
                        }
                    } else {
                    }
                } else {
                    reportarError("Falta parentesis izquierdo", tokenActual.fila, tokenActual.columna,
                            tokenActual.columnaReal)
                }
            } else {
                reportarError("Falta palabrea reserrvada mientras", tokenActual.fila, tokenActual.columna,
                        tokenActual.columnaReal)
            }
        }
        return null
    }

    /**
     * Metodo que verifica si es un retorno
     *
     * <[Retorno]>::= devolver <[Termino]> fin
     *
     * @return retorno [Retorno]
     */
    private fun esRetorno(): Retorno? {
        if (tokenActual.lexema == "devolver") {
            val retorno = tokenActual
            obtenerSiguienteToken()
            val posInicial = posActual
            val termino = esTermino()
            if (termino != null) {
                obtenerSiguienteToken()
                if (tokenActual.lexema == "fin") {
                    return Retorno(retorno, termino)
                } else {
                    hacerBactracking(posInicial)
                    val expresion = esExpresion()
                    if (expresion != null) {
                        if (tokenActual.lexema == "fin") {
                            return Retorno(retorno, Termino(expresion))
                        } else {
                            reportarError("Fala fin de sentencia", tokenActual.fila, tokenActual.columna,
                                    tokenActual.columnaReal)
                        }
                    } else {
                        reportarError("Fala fin de sentencia", tokenActual.fila, tokenActual.columna,
                                tokenActual.columnaReal)
                    }
                }
            } else {
                reportarError("Falta el termino a devolver", tokenActual.fila, tokenActual.columna,
                        tokenActual.columnaReal)
            }
        } else {
            reportarError("Falta Palabra Clave Devolver", tokenActual.fila, tokenActual.columna,
                    tokenActual.columnaReal)
        }
        return null
    }

    /**
     * Metodo que verifica si es una impresion
     *
     * <[Impresion]>::= imprimir parentesisIzquierdo <[] Termino}>
     * parentesisDerecho fin
     *
     * @return impresion{@link Impresion}
     */
    private fun esImpresion(): Impresion? {
        val escribir = tokenActual
        if (escribir.lexema == "imprimir") {
            obtenerSiguienteToken()
            if (tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO) {
                obtenerSiguienteToken()
                val posBack = posActual
                var termino = esTermino()
                if (termino != null) {
                    obtenerSiguienteToken()
                    if (tokenActual.categoria == Categoria.PARENTESIS_DERECHO) {
                        obtenerSiguienteToken()
                        if (tokenActual.lexema == "fin") {
                            return Impresion(escribir, termino)
                        } else {
                            reportarError("Falta la sentencia Fin", tokenActual.fila, tokenActual.columna,
                                    tokenActual.columnaReal)
                        }
                    } else {
                        if (tokenActual.lexema == "(+)") {
                            hacerBactracking(posBack)
                            val expresion: Expresion? = esExpresionCadena()
                            if (expresion != null) {
                                termino = Termino(expresion)
                                if (tokenActual.categoria == Categoria.PARENTESIS_DERECHO) {
                                    obtenerSiguienteToken()
                                    if (tokenActual.lexema == "fin") {
                                        return Impresion(escribir, termino)
                                    } else {
                                        reportarError("Falta la sentencia Fin", tokenActual.fila,
                                                tokenActual.columna, tokenActual.columnaReal)
                                    }
                                } else {
                                    reportarError("Falta el Parentesis Derecho", tokenActual.fila,
                                            tokenActual.columna, tokenActual.columnaReal)
                                }
                            }
                        } else {
                            reportarError("Falta el Parentesis Derecho", tokenActual.fila,
                                    tokenActual.columna, tokenActual.columnaReal)
                        }
                    }
                } else {
                    reportarError("Falta el termino", tokenActual.fila, tokenActual.columna,
                            tokenActual.columnaReal)
                }
            } else {
                reportarError("Falta el Parentesis Izquierdo", tokenActual.fila, tokenActual.columna,
                        tokenActual.columnaReal)
            }
        } else {
            reportarError("Falta la sentencia imprimir", tokenActual.fila, tokenActual.columna,
                    tokenActual.columnaReal)
        }
        return null
    }

    /**
     * Metodo que verifica si es una comcatenacion de cadena
     *
     * <[ExpresionCadena]>::= <[] Termino}> [ (+)
     * <[ExpresionCadena]> ] fin
     *
     * @return expresionCadena{@link ExpresionCadena}
     */
    private fun esExpresionCadena(): ExpresionCadena? {
        if (tokenActual.categoria != Categoria.PARENTESIS_DERECHO) {
            val termino = esTermino()
            if (termino != null) {
                obtenerSiguienteToken()
                if (tokenActual.lexema == "(+)") {
                    obtenerSiguienteToken()
                    val expresionCadena = esExpresionCadena()
                    if (expresionCadena != null) {
                        return ExpresionCadena(termino, expresionCadena)
                    } else {
                        reportarError("Falta expresion cadena", tokenActual.fila, tokenActual.columna,
                                tokenActual.columnaReal)
                    }
                } else {
                    return ExpresionCadena(termino)
                }
            } else {
                reportarError("Falta Termino", tokenActual.fila, tokenActual.columna,
                        tokenActual.columnaReal)
            }
        }
        return null
    }

    /**
     * Metodo que verifica si es una sentencia de lectura
     *
     * <[Lectura]>::= idVariable operadorAsignacion leer parentesisIzquierdo
     * <{link TipoDato}> parentesisDerecho fin
     *
     * @return lectura{@link Lectura}
     */
    private fun esLectura(): Lectura? {
        if (tokenActual.categoria == Categoria.IDENTIFICADOR_VARIABLE) {
            val idVariable = tokenActual
            obtenerSiguienteToken()
            if (tokenActual.categoria == Categoria.OPERADOR_ASIGNACION) {
                val opAsignacion = tokenActual
                obtenerSiguienteToken()
                if (tokenActual.lexema == "leer") {
                    val leer = tokenActual
                    obtenerSiguienteToken()
                    if (tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO) {
                        obtenerSiguienteToken()
                        val tipoDato = esTipoDato()
                        if (tipoDato != null) {
                            obtenerSiguienteToken()
                            if (tokenActual.categoria == Categoria.PARENTESIS_DERECHO) {
                                obtenerSiguienteToken()
                                if (tokenActual.lexema == "fin") {
                                    return Lectura(idVariable, opAsignacion, leer, tipoDato)
                                } else {
                                    reportarError("Falta fin de sentencia", tokenActual.fila,
                                            tokenActual.columna, tokenActual.columnaReal)
                                }
                            } else {
                                reportarError("Falta parentesis derecho", tokenActual.fila,
                                        tokenActual.columna, tokenActual.columnaReal)
                            }
                        } else {
                            reportarError("Falta tipo de dato", tokenActual.fila, tokenActual.columna,
                                    tokenActual.columnaReal)
                        }
                    } else {
                        reportarError("Falta parentesis izquierdo", tokenActual.fila, tokenActual.columna,
                                tokenActual.columnaReal)
                    }
                } else {
                    reportarError("Falta la palabra reservada leer", tokenActual.fila, tokenActual.columna,
                            tokenActual.columnaReal)
                }
            } else {
                reportarError("Falta operador de asignacion", tokenActual.fila, tokenActual.columna,
                        tokenActual.columnaReal)
            }
        } else {
            reportarError("Falta Identificador Variable", tokenActual.fila, tokenActual.columna,
                    tokenActual.columnaReal)
        }
        return null
    }

    /**
     * Metodo que identifica si es una declaracion de campo
     *
     * <[ DeclaracionVariable]>::= [<{link Visibilidad}>] tipoDato [ arreglo]
     * lista identificadorVariable fin
     *
     * @return declaracionnCampo{@link DeclaracionVariable}
     */
    private fun esDeclaracionVariable(): DeclaracionVariable? {
        val visibilidad = esVisibilidad()
        if (visibilidad != null) {
            obtenerSiguienteToken()
            val tipoDato = esTipoDato()
            if (tipoDato != null) {
                obtenerSiguienteToken()
                if (tokenActual.categoria == Categoria.ARREGLO) {
                    val arreglo = tokenActual
                    obtenerSiguienteToken()
                    val listaIdentificador = esListaIdentificadores()
                    if (listaIdentificador != null) {
                        obtenerSiguienteToken()
                        if (tokenActual.lexema == "fin") {
                            return DeclaracionVariable(visibilidad, tipoDato, arreglo, listaIdentificador)
                        } else {
                            reportarError("Falta fin de sentencia", tokenActual.fila, tokenActual.columna,
                                    tokenActual.columnaReal)
                        }
                    } else {
                        reportarError("Falta algun identificador", tokenActual.fila, tokenActual.columna,
                                tokenActual.columnaReal)
                    }
                } else {
                    val listaIdentificador = esListaIdentificadores()
                    if (listaIdentificador != null) {
                        if (tokenActual.lexema == "fin") {
                            return DeclaracionVariable(visibilidad, tipoDato, listaIdentificador)
                        } else {
                            reportarError("Falta fin de sentencia", tokenActual.fila, tokenActual.columna,
                                    tokenActual.columnaReal)
                        }
                    } else {
                        reportarError("Falta algun identificador", tokenActual.fila, tokenActual.columna,
                                tokenActual.columnaReal)
                    }
                }
            } else {
                reportarError("Falta el tipo de dato", tokenActual.fila, tokenActual.columna,
                        tokenActual.columnaReal)
            }
        } else {
            val tipoDato = esTipoDato()
            if (tipoDato != null) {
                obtenerSiguienteToken()
                if (tokenActual.categoria == Categoria.ARREGLO) {
                    val arreglo = tokenActual
                    obtenerSiguienteToken()
                    val listaIdentificador = esListaIdentificadores()
                    if (listaIdentificador != null) {
                        obtenerSiguienteToken()
                        if (tokenActual.lexema == "fin") {
                            return DeclaracionVariable(arreglo, listaIdentificador, tipoDato)
                        } else {
                            reportarError("Falta fin de sentencia", tokenActual.fila, tokenActual.columna,
                                    tokenActual.columnaReal)
                        }
                    } else {
                        reportarError("Falta algun identificador", tokenActual.fila, tokenActual.columna,
                                tokenActual.columnaReal)
                    }
                } else {
                    val listaIdentificador = esListaIdentificadores()
                    if (listaIdentificador != null) {
                        if (tokenActual.lexema == "fin") {
                            return DeclaracionVariable(tipoDato, listaIdentificador)
                        } else {
                            reportarError("Falta fin de sentencia", tokenActual.fila, tokenActual.columna,
                                    tokenActual.columnaReal)
                        }
                    } else {
                        reportarError("Falta algun identificador", tokenActual.fila, tokenActual.columna,
                                tokenActual.columnaReal)
                    }
                }
            } else {
                reportarError("Falta el tipo de dato", tokenActual.fila, tokenActual.columna,
                        tokenActual.columnaReal)
            }
        }
        return null
    }

    /**
     * Metodo que identifica si es una asignacion de variable
     *
     * <[AsignacionVariable]>::= identificadorVariable operadorAsignacion
     * <[Termino]> fin
     *
     * @return declaracionCampo{@link DeclaracionVariable}
     */
    private fun esAsignacionVariable(): AsignacionVariable? {
        if (tokenActual.categoria == Categoria.IDENTIFICADOR_VARIABLE) {
            val identificador = tokenActual
            obtenerSiguienteToken()
            if (tokenActual.categoria == Categoria.OPERADOR_ASIGNACION) {
                val operadorAsignacion = tokenActual
                obtenerSiguienteToken()
                val posBack = posActual
                var termino = esTermino()
                if (termino != null) {
                    obtenerSiguienteToken()
                    if (tokenActual.lexema == "fin") {
                        return AsignacionVariable(identificador, operadorAsignacion, termino)
                    } else {
                        if (tokenActual.categoria == Categoria.OPERADOR_ARITMETICO || tokenActual.categoria == Categoria.OPERADOR_LOGICO || tokenActual.categoria == Categoria.OPERADOR_RELACIONAL) {
                            hacerBactracking(posBack)
                            val expresion = esExpresion()
                            if (expresion != null) {
                                termino = Termino(expresion)
                                if (tokenActual.lexema == "fin") {
                                    return AsignacionVariable(identificador, operadorAsignacion, termino)
                                } else {
                                    reportarError("Falta fin de sentencia", tokenActual.fila,
                                            tokenActual.columna, tokenActual.columnaReal)
                                }
                            } else {
                                reportarError("Falta termino", tokenActual.fila, tokenActual.columna,
                                        tokenActual.columnaReal)
                            }
                        } else {
                            reportarError("Falta fin de sentencia", tokenActual.fila, tokenActual.columna,
                                    tokenActual.columnaReal)
                        }
                    }
                } else {
                    reportarError("Falta termino", tokenActual.fila, tokenActual.columna,
                            tokenActual.columnaReal)
                }
            } else {
                reportarError("Falta operador asignación", tokenActual.fila, tokenActual.columna,
                        tokenActual.columnaReal)
            }
        } else {
            reportarError("Falta Identificador Variable", tokenActual.fila, tokenActual.columna,
                    tokenActual.columnaReal)
        }
        return null
    }

    /**
     * Metodo que identifica si es una sentencia de incremento
     *
     * <[SentenciaIncremento]>::= identificadorVariable inc fin
     *
     * @return sentenciaIncremento [SentenciaIncremento]
     */
    private fun esSentenciaIncremento(): SentenciaIncremento? {
        if (tokenActual.categoria == Categoria.IDENTIFICADOR_VARIABLE) {
            val indentificadorVariable = tokenActual
            obtenerSiguienteToken()
            if (tokenActual.lexema == "inc") {
                val incremento = tokenActual
                obtenerSiguienteToken()
                if (tokenActual.lexema == "fin") {
                    return SentenciaIncremento(indentificadorVariable, incremento)
                } else {
                    reportarError("Falta Fin", tokenActual.fila, tokenActual.columna,
                            tokenActual.columnaReal)
                }
            } else {
                reportarError("Falta incremento inc", tokenActual.fila, tokenActual.columna,
                        tokenActual.columnaReal)
            }
        }
        return null
    }

    /**
     * Metodo que identifica si es una sentencia de decremento
     *
     * <[SentenciaDecremento]>::= identificadorVariable dec fin
     *
     * @return sentenciaDecremento [SentenciaDecremento]
     */
    private fun esSentenciaDecremento(): SentenciaIncremento? {
        if (tokenActual.categoria == Categoria.IDENTIFICADOR_VARIABLE) {
            val indentificadorVariable = tokenActual
            obtenerSiguienteToken()
            if (tokenActual.lexema == "dec") {
                val incremento = tokenActual
                obtenerSiguienteToken()
                if (tokenActual.lexema == "fin") {
                    return SentenciaIncremento(indentificadorVariable, incremento)
                } else {
                    reportarError("Falta Fin", tokenActual.fila, tokenActual.columna,
                            tokenActual.columnaReal)
                }
            } else {
                reportarError("Falta decremento dec", tokenActual.fila, tokenActual.columna,
                        tokenActual.columnaReal)
            }
        }
        return null
    }

    /**
     * Metodo para lista de identificadores
     *
     * @return
     */
    private fun esListaIdentificadores(): ArrayList<Token>? {
        val listaId = ArrayList<Token>()
        if (tokenActual.categoria == Categoria.IDENTIFICADOR_VARIABLE) {
            listaId.add(tokenActual)
            obtenerSiguienteToken()
            while (tokenActual.categoria == Categoria.SEPARADOR) {
                obtenerSiguienteToken()
                if (tokenActual.categoria == Categoria.IDENTIFICADOR_VARIABLE) {
                    listaId.add(tokenActual)
                    obtenerSiguienteToken()
                } else {
                    reportarError("Falta identificador", tokenActual.fila, tokenActual.columna,
                            tokenActual.columnaReal)
                }
            }
            return if (listaId.size > 0) listaId else null
        }
        return null
    }

    /**
     * Metodo para lista de parametros
     *
     * @return
     */
    private fun esListaParametro(): ArrayList<Parametro>? {
        val listaParametro = ArrayList<Parametro>()
        val parametro = esParametro()
        if (parametro != null) {
            listaParametro.add(parametro)
            obtenerSiguienteToken()
            while (tokenActual.categoria == Categoria.SEPARADOR) {
                obtenerSiguienteToken()
                val parametro0 = esParametro()
                if (parametro0 != null) {
                    listaParametro.add(parametro0)
                    obtenerSiguienteToken()
                } else {
                    reportarError("Falta parametro", tokenActual.fila, tokenActual.columna,
                            tokenActual.columnaReal)
                }
            }
            return if (listaParametro.size > 0) listaParametro else null
        }
        return null
    }

    /**
     * Metodo para lista de parametros
     *
     * @return
     */
    private fun esListaArgumento(): ArrayList<Termino>? {
        val listaArgumento = ArrayList<Termino>()
        val termino = esTermino()
        if (termino != null) {
            listaArgumento.add(termino)
            obtenerSiguienteToken()
            while (tokenActual.categoria == Categoria.SEPARADOR) {
                obtenerSiguienteToken()
                val termino0 = esTermino()
                if (termino0 != null) {
                    listaArgumento.add(termino0)
                    obtenerSiguienteToken()
                } else {
                    reportarError("Falta parametro", tokenActual.fila, tokenActual.columna,
                            tokenActual.columnaReal)
                }
            }
            return if (listaArgumento.size > 0) listaArgumento else null
        }
        return null
    }

    /**
     * Método para identificar una lista de sentencias
     *
     * @return
     */
    private fun esListaSentencia(): ArrayList<Sentencia>? {
        val listaSentencia = ArrayList<Sentencia>()
        while (true) {
            val sentencia = esSentencia()
            if (sentencia != null) {
                listaSentencia.add(sentencia)
                obtenerSiguienteToken()
            } else {
                break
            }
        }
        return if (listaSentencia.size > 0) listaSentencia else null
    }

    /**
     * Obtiene el siguiente token del la lista de codigos
     */
    fun obtenerSiguienteToken() {
        tokenActual = if (posActual < tablaSimbolos.size - 1) {
            posActual++
            tablaSimbolos[posActual]
        } else {
            Token("", 0, 0, Categoria.FIN_CODIGO)
        }
    }

    /**
     * Metodo para volver a una instancia anterior del codigo
     *
     * @param posInicial:
     * posicion donde se inicio el analisis
     */
    fun hacerBactracking(posInicial: Int) {
        posActual = posInicial
        tokenActual = tablaSimbolos[posActual]
    }

    /**
     * Metodo para reportar un error sintactico
     *
     * @param mensaje:
     * El mensaje con el error
     * @param fila:
     * fila del error
     * @param columna:
     * columna del error
     */
    private fun reportarError(mensaje: String, fila: Int, columna: Int, columnaReal: Int) {
        tablaErrores.add(ErrorSintactico(mensaje, fila, columna, columnaReal))
    }

    init {
        tokenActual = tablaSimbolos[posActual]
        tablaErrores = ArrayList()
    }
}