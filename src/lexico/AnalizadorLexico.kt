package lexico

import java.util.*

/**
 * Clase que permite realizar un analisis lexico en el codigo fuente
 */
class AnalizadorLexico(private val codigoFuente: String) {
    private var charActual: Char
    private val finCodigo: Char
    private var posActual = 0
    private var colActual = 0
    private var filaActual = 0

    /**
     * @return the tablaSimbolos
     */
    val tablaSimbolos: ArrayList<Token>

    /**
     * @return the tablaErrores
     */
    val tablaErrores: ArrayList<Token>
    private val reservedWords: ArrayList<String>

    /**
     * Metodo que almacena las palabras reservadas
     */
    private fun llenarPalabras() {
        // palabra reservada para indicar una clase
        reservedWords.add("clase")
        //palabra para indicar un metodo
        reservedWords.add("funapp")
        // palabras reservadas para visibilidad
        reservedWords.add("visible")
        reservedWords.add("oculto")
        // palabra reservada para indicar un metodo
        reservedWords.add("funaapp")
        // palabras reservadas de tipos de dato o retorno
        reservedWords.add("ltr")
        reservedWords.add("ltrarr")
        reservedWords.add("binary")
        reservedWords.add("pntdec")
        reservedWords.add("ntr")
        reservedWords.add("sr")
        // palabras reservadas para valores booleanos
        reservedWords.add("v")
        reservedWords.add("f")
        // palabra reservada para fin de sentencia
        reservedWords.add("fin")
        // palabra reservada para incrementar o decrementar variables numericas
        reservedWords.add("inc")
        reservedWords.add("dec")
        // palabras reservadas para ciclo
        reservedWords.add("ciclo")
        reservedWords.add("mientras")
        // Palabras para sentencia condicional
        reservedWords.add("pregunta")
        reservedWords.add("contrario")
        // Identificar arreglo
        reservedWords.add("\$arr()")
        // Retorno
        reservedWords.add("devolver")
        // Lectura y escritura
        reservedWords.add("leer")
        reservedWords.add("imprimir")
    }

    /**
     * Metodo general para analizar con metodos de predicado
     */
    fun analizar() {
        while (charActual != finCodigo) {
            if (charActual == ' ' || charActual == '\n' || charActual == '\t' || charActual == '\r') {
                obtenerSiguienteCaracter()
                continue
            }
            if (esEntero()) continue
            if (esReal()) continue
            if (esCadenaCaracteres()) continue
            if (esComentarioLinea()) continue
            if (esOperadorRelacional()) continue
            if (esComentarioBloque()) continue
            if (esIdentificadorVariable()) continue
            if (esIdentificadorMetodo()) continue
            if (esIdentificadorClase()) continue
            if (esOperadorLogico()) continue
            if (esOperadorAsignacion()) continue
            if (esOperadorAritmetico()) continue
            if (esCaracter()) continue
            if (esAgrupadorIzquierdo()) continue
            if (esAgrupadorDerecho()) continue
            if (esParentesisIzquierdo()) continue
            if (esParentesisDerecho()) continue
            if (esArreglo()) continue
            if (esSeparador()) continue
            if (esIdentificador()) continue
            if (esPunto()) {
                continue
            }
            reportarError("" + charActual, filaActual, colActual, posActual)
            obtenerSiguienteCaracter()
        }
    }

    /**
     * Metodo para devolver el proceso de metodos de predicado
     *
     * @param posInicial
     * posicion hasta donde devolverse
     */
    private fun hacerBacktracking(posInicial: Int) {
        posActual = posInicial
        charActual = codigoFuente[posActual]
    }

    /**
     * Metodo que continÃºa con el siguiente caracter del codigo fuente
     */
    private fun obtenerSiguienteCaracter() {
        if (posActual == codigoFuente.length - 1) {
            charActual = finCodigo
        } else {
            if (charActual == '\n') {
                filaActual++
                colActual = 0
            } else {
                colActual++
            }
            posActual++
            charActual = codigoFuente[posActual]
        }
    }

    /**
     * Metodo que almacena un simbolo del sistema
     *
     * @param lexema
     * el lexema que se almacenarÃ¡
     * @param fila
     * fila donde inicio el simbolo
     * @param columna
     * columna donde se inicio el simbolo
     * @param categoria
     * categorÃ­a del sÃ­mbolo
     */
    private fun almacenarSimbolo(lexema: String, fila: Int, columna: Int, categoria: Categoria) {
        tablaSimbolos.add(Token(lexema, fila, columna, categoria))
    }

    /**
     * Metodo que almacena un codigo desconocido del sistema
     *
     * @param lexema
     * el lexema de error
     * @param fila
     * la fila donde origino el error
     * @param columna
     * la columna donde se origio el error
     */
    private fun reportarError(lexema: String, fila: Int, columna: Int, posInicial: Int) {
        tablaErrores.add(Token(lexema, fila, columna, posInicial - fila, Categoria.DESCONOCIDO))
    }
    // METODOS PREDICADO
    /**
     * Metodo para determinar si lo ingresado en codigo es un nÃºmero real
     *
     * @return
     */
    private fun esEntero(): Boolean {
        if (!Character.isDigit(charActual)) {
            return false
        }

        // Transicion
        var lexema = ""
        val filaInicial = filaActual
        val colInicial = colActual
        val posInicial = posActual
        lexema += charActual
        obtenerSiguienteCaracter()

        // Bucle
        while (Character.isDigit(charActual)) {
            lexema += charActual
            obtenerSiguienteCaracter()
        }
        if (charActual == '.') {
            hacerBacktracking(posInicial)
            return false
        }

        // AA
        almacenarSimbolo(lexema, filaInicial, colInicial, Categoria.ENTERO)
        return true
    }

    /**
     * Metodo para determinar si lo ingresado en codigo es una cadena de
     * caracteres
     *
     * @return
     */
    private fun esCadenaCaracteres(): Boolean {
        // Transicion
        var lexema = ""
        val filaInicial = filaActual
        val colInicial = colActual
        val posInicial = posActual
        return if (charActual == '$') {
            lexema += charActual
            obtenerSiguienteCaracter()
            while (charActual != '$') {
                if (charActual == '\'') {
                    lexema += charActual
                    obtenerSiguienteCaracter()
                    return if (!(charActual == 'n' || charActual == 't' || charActual == '$' || charActual == 'f' || charActual == 'b' || charActual == '\'' || charActual == 'r')) {
                        reportarError(lexema, filaInicial, colInicial, posInicial)
                        true
                    } else {
                        lexema += charActual
                        obtenerSiguienteCaracter()
                        continue
                    }
                } else if (charActual == finCodigo) {
                    reportarError(lexema, filaInicial, colInicial, posInicial)
                    return true
                } else {
                    lexema += charActual
                    obtenerSiguienteCaracter()
                }
            }
            lexema += charActual
            obtenerSiguienteCaracter()
            almacenarSimbolo(lexema, filaInicial, colInicial, Categoria.CADENA_CARACTERES)
            true
        } else {
            hacerBacktracking(posInicial)
            false
        }
    }

    /**
     * Metodo para determinar si lo ingresado en codigo es un caracter
     *
     * @return
     */
    private fun esCaracter(): Boolean {
        if (charActual != '#') {
            return false
        }
        var lexema = ""
        val filaInicial = filaActual
        val colInicial = colActual
        val posInicial = posActual
        lexema += charActual
        obtenerSiguienteCaracter()
        if (charActual == '\'') {
            lexema += charActual
            obtenerSiguienteCaracter()
            if (charActual == '#' || charActual == 'n' || charActual == 'b' || charActual == 't' || charActual == 'r') {
                lexema += charActual
                obtenerSiguienteCaracter()
                return if (charActual == '#') {
                    lexema += charActual
                    obtenerSiguienteCaracter()
                    almacenarSimbolo(lexema, filaInicial, colInicial, Categoria.CARACTER)
                    true
                } else {
                    reportarError(lexema, filaInicial, colInicial, posInicial)
                    true
                }
            }
        } else if (charActual != '#') {
            lexema += charActual
            obtenerSiguienteCaracter()
            return if (charActual == '#') {
                lexema += charActual
                obtenerSiguienteCaracter()
                almacenarSimbolo(lexema, filaInicial, colInicial, Categoria.CARACTER)
                true
            } else {
                lexema += charActual
                reportarError(lexema, filaInicial, colInicial, posInicial)
                true
            }
        }
        return true
    }

    /**
     * Metodo para determinar si lo ingresado en codigo es un operador relacional
     *
     * @return
     */
    private fun esOperadorRelacional(): Boolean {
        var lexema = ""
        val filaInicial = filaActual
        val colInicial = colActual
        val posInicial = posActual
        return if (charActual == '(') {
            lexema += charActual
            obtenerSiguienteCaracter()
            when (charActual) {
                '<', '>' -> {
                    lexema += charActual
                    obtenerSiguienteCaracter()
                    if (charActual == 'e') {
                        lexema += charActual
                        obtenerSiguienteCaracter()
                        if (charActual == 's') {
                            lexema += charActual
                            obtenerSiguienteCaracter()
                            if (charActual == ')') {
                                lexema += charActual
                                obtenerSiguienteCaracter()
                                almacenarSimbolo(lexema, filaInicial, colInicial, Categoria.OPERADOR_RELACIONAL)
                            } else {
                                reportarError(lexema, filaInicial, colInicial, posInicial)
                            }
                            true
                        } else {
                            reportarError(lexema, filaInicial, colInicial, posInicial)
                            true
                        }
                    } else {
                        if (charActual == ')') {
                            lexema += charActual
                            obtenerSiguienteCaracter()
                            almacenarSimbolo(lexema, filaInicial, colInicial, Categoria.OPERADOR_RELACIONAL)
                            true
                        } else {
                            reportarError(lexema, filaInicial, colInicial, posInicial)
                            true
                        }
                    }
                }
                'e' -> {
                    lexema += charActual
                    obtenerSiguienteCaracter()
                    if (charActual == 's') {
                        lexema += charActual
                        obtenerSiguienteCaracter()
                        if (charActual == ')') {
                            lexema += charActual
                            obtenerSiguienteCaracter()
                            almacenarSimbolo(lexema, filaInicial, colInicial, Categoria.OPERADOR_RELACIONAL)
                        } else {
                            reportarError(lexema, filaInicial, colInicial, posInicial)
                        }
                        true
                    } else {
                        reportarError(lexema, filaInicial, colInicial, posInicial)
                        true
                    }
                }
                else -> {
                    hacerBacktracking(posInicial)
                    false
                }
            }
        } else if (charActual == 'N') {
            lexema += charActual
            obtenerSiguienteCaracter()
            if (charActual == 'O') {
                lexema += charActual
                obtenerSiguienteCaracter()
                if (charActual == 'T') {
                    lexema += charActual
                    obtenerSiguienteCaracter()
                    if (charActual == '(') {
                        lexema += charActual
                        obtenerSiguienteCaracter()
                        if (charActual == 'e') {
                            lexema += charActual
                            obtenerSiguienteCaracter()
                            if (charActual == 's') {
                                lexema += charActual
                                obtenerSiguienteCaracter()
                                if (charActual == ')') {
                                    lexema += charActual
                                    obtenerSiguienteCaracter()
                                    almacenarSimbolo(lexema, filaInicial, colInicial, Categoria.OPERADOR_RELACIONAL)
                                } else {
                                    reportarError(lexema, filaInicial, colInicial, posInicial)
                                }
                            } else {
                                reportarError(lexema, filaInicial, colInicial, posInicial)
                            }
                        } else {
                            reportarError(lexema, filaInicial, colInicial, posInicial)
                        }
                    } else {
                        hacerBacktracking(posInicial)
                        return false
                    }
                } else {
                    reportarError(lexema, filaInicial, colInicial, posInicial)
                }
            } else {
                reportarError(lexema, filaInicial, colInicial, posInicial)
            }
            true
        } else {
            false
        }
    }

    /**
     * Metodo para determinar si lo ingresado en codigo es un nÃºmero real
     *
     * @return
     */
    private fun esReal(): Boolean {
        var lexema = ""
        val filaInicial = filaActual
        val colInicial = colActual
        val posInicial = posActual
        return if (Character.isDigit(charActual)) {
            lexema += charActual
            obtenerSiguienteCaracter()
            while (Character.isDigit(charActual)) {
                lexema += charActual
                obtenerSiguienteCaracter()
            }
            if (charActual == '.') {
                lexema += charActual
                obtenerSiguienteCaracter()
                while (Character.isDigit(charActual)) {
                    lexema += charActual
                    obtenerSiguienteCaracter()
                }
                almacenarSimbolo(lexema, filaInicial, colInicial, Categoria.REAL)
                true
            } else {
                hacerBacktracking(posInicial)
                false
            }
        } else {
            false
        }
    }

    /**
     * Analiza si la parte de codigo analizado es un comentario de linea
     *
     * @return
     */
    private fun esComentarioLinea(): Boolean {
        var lexema = ""
        val colInicial = colActual
        val filaInicial = filaActual
        val posInicial = posActual
        return if (charActual == '-') {
            lexema += charActual
            obtenerSiguienteCaracter()
            if (charActual == '-') {
                lexema += charActual
                obtenerSiguienteCaracter()
                while (charActual != '\n') {
                    if (charActual == finCodigo) {
                        reportarError(lexema, filaInicial, colInicial, posInicial)
                    } else {
                        lexema += charActual
                        obtenerSiguienteCaracter()
                    }
                }
                true
            } else {
                hacerBacktracking(posInicial)
                false
            }
        } else {
            false
        }
    }

    /**
     * Identifica si la parte de codigo analizado es comentario de bloque
     *
     * @return
     */
    private fun esComentarioBloque(): Boolean {
        var lexema = ""
        val filaInicial = filaActual
        val colInicial = colActual
        val posInicial = posActual
        if (charActual == '-') {
            lexema += charActual
            obtenerSiguienteCaracter()
            if (charActual == '{') {
                lexema += charActual
                obtenerSiguienteCaracter()
                do {
                    while (charActual != '}') {
                        lexema += charActual
                        obtenerSiguienteCaracter()
                        if (charActual == finCodigo) {
                            reportarError(lexema, filaInicial, colInicial, posInicial)
                            return true
                        }
                    }
                    while (charActual == '}') {
                        lexema += charActual
                        obtenerSiguienteCaracter()
                        if (charActual == finCodigo) {
                            reportarError(lexema, filaInicial, colInicial, posInicial)
                            return false
                        }
                    }
                } while (charActual != '-')
                return if (charActual == '-') {
                    obtenerSiguienteCaracter()
                    true
                } else {
                    reportarError(lexema, filaInicial, colInicial, posInicial)
                    true
                }
            } else {
                hacerBacktracking(posInicial)
            }
        }
        return false
    }

    /**
     * Metodo para determinar si lo ingresado por codigo es un identificador de
     * variable
     *
     * @return
     */
    private fun esIdentificadorVariable(): Boolean {
        var lexema = ""
        val filaInicial = filaActual
        val colInicial = colActual
        val posInicial = posActual
        return if (charActual == '<') {
            lexema += charActual
            obtenerSiguienteCaracter()
            while (Character.getType(charActual) == Character.LOWERCASE_LETTER.toInt()) {
                lexema += charActual
                obtenerSiguienteCaracter()
            }
            while (charActual == '-') {
                if (charActual == '-') {
                    lexema += charActual
                    obtenerSiguienteCaracter()
                } else {
                    reportarError(lexema, filaInicial, colInicial, posInicial)
                    return true
                }
                while (Character.getType(charActual) == Character.LOWERCASE_LETTER.toInt()) {
                    lexema += charActual
                    obtenerSiguienteCaracter()
                }
            }
            if (charActual == '>') {
                lexema += charActual
                obtenerSiguienteCaracter()
                almacenarSimbolo(lexema, filaInicial, colInicial, Categoria.IDENTIFICADOR_VARIABLE)
            } else {
                hacerBacktracking(posInicial)
                return false
            }
            true
        } else {
            hacerBacktracking(posInicial)
            false
        }
    }

    /**
     * Metodo para determinar si lo ingresado por codigo es un identificador de
     * metodo
     *
     * @return
     */
    private fun esIdentificadorMetodo(): Boolean {
        var lexema = ""
        val filaInicial = filaActual
        val colInicial = colActual
        val posInicial = posActual
        return if (charActual == 'f') {
            lexema += charActual
            obtenerSiguienteCaracter()
            if (charActual == 'u') {
                lexema += charActual
                obtenerSiguienteCaracter()
                if (charActual == 'n') {
                    lexema += charActual
                    obtenerSiguienteCaracter()
                    if (Character.getType(charActual) == Character.LOWERCASE_LETTER.toInt()) {
                        lexema += charActual
                        obtenerSiguienteCaracter()
                        while (Character.getType(charActual) == Character.LOWERCASE_LETTER.toInt()) {
                            lexema += charActual
                            obtenerSiguienteCaracter()
                        }
                        if (lexema.substring(lexema.length - 2) == "ar" || lexema.substring(lexema.length) == "er" || lexema.substring(lexema.length) == "ir" || lexema.substring(lexema.length) == "or" || lexema.substring(lexema.length) == "ur") {
                            almacenarSimbolo(lexema, filaInicial, colInicial, Categoria.IDENTIFICADOR_METODO)
                        } else {
                            hacerBacktracking(posInicial)
                            return false
                        }
                        true
                    } else {
                        reportarError(lexema, filaInicial, colInicial, posInicial)
                        true
                    }
                } else {
                    reportarError(lexema, filaInicial, colInicial, posInicial)
                    true
                }
            } else {
                hacerBacktracking(posInicial)
                false
            }
        } else {
            false
        }
    }

    /**
     * Metodo para determinar si lo ingresado por codigo es un identificador de
     * metodo
     *
     * @return
     */
    private fun esIdentificadorClase(): Boolean {
        var lexema = ""
        val filaInicial = filaActual
        val colInicial = colActual
        val posInicial = posActual
        return if (charActual == '@') {
            lexema += charActual
            obtenerSiguienteCaracter()
            if (Character.getType(charActual) == Character.UPPERCASE_LETTER.toInt()) {
                lexema += charActual
                obtenerSiguienteCaracter()
                while (Character.getType(charActual) == Character.LOWERCASE_LETTER.toInt()) {
                    lexema += charActual
                    obtenerSiguienteCaracter()
                }
                almacenarSimbolo(lexema, filaInicial, colInicial, Categoria.IDENTIFICADOR_CLASE)
                true
            } else {
                reportarError(lexema, filaInicial, colInicial, posInicial)
                true
            }
        } else {
            false
        }
    }

    /**
     * Metodo para hallar identificador
     */
    private fun esIdentificador(): Boolean {
        var lexema = ""
        val filaInicial = filaActual
        val colInicial = colActual
        return if (charActual == '_' || Character.getType(charActual) == Character.LOWERCASE_LETTER.toInt()) {
            while (Character.getType(charActual) == Character.LOWERCASE_LETTER.toInt()) {
                lexema += charActual
                obtenerSiguienteCaracter()
            }
            while (charActual == '_') {
                lexema += charActual
                obtenerSiguienteCaracter()
                while (Character.getType(charActual) == Character.LOWERCASE_LETTER.toInt()) {
                    lexema += charActual
                    obtenerSiguienteCaracter()
                }
            }
            if (esPalabraReservada(lexema)) {
                almacenarSimbolo(lexema, filaInicial, colInicial, Categoria.PALABRA_RESERVADA)
            } else {
                reportarError(lexema, filaInicial, colInicial, posActual)
            }
            true
        } else {
            false
        }
    }

    /**
     * Metodo para determinar si un identificador es palabra reservada
     *
     * @param lexema
     * el identificador encontrado
     * @return
     */
    private fun esPalabraReservada(lexema: String): Boolean {
        return reservedWords.contains(lexema)
    }

    private fun esOperadorAritmetico(): Boolean {
        var lexema = ""
        val filaInicial = filaActual
        val colInicial = colActual
        val posInicial = posActual
        return if (charActual == '(') {
            lexema += charActual
            obtenerSiguienteCaracter()
            if (charActual == '+' || charActual == '-' || charActual == '*' || charActual == '/' || charActual == '%') {
                lexema += charActual
                obtenerSiguienteCaracter()
                if (charActual == ')') {
                    lexema += charActual
                    obtenerSiguienteCaracter()
                    almacenarSimbolo(lexema, filaInicial, colInicial, Categoria.OPERADOR_ARITMETICO)
                    true
                } else {
                    reportarError(lexema, filaInicial, colInicial, posInicial)
                    true
                }
            } else {
                hacerBacktracking(posInicial)
                false
            }
        } else {
            false
        }
    }

    /**
     * Metodo para determinar si lo ingresado en codigo es un operador de
     * asignacion
     *
     * @return
     */
    private fun esOperadorAsignacion(): Boolean {
        var lexema = ""
        val filaInicial = filaActual
        val colInicial = colActual
        val posInicial = posActual
        return if (charActual == 'e') {
            lexema += charActual
            obtenerSiguienteCaracter()
            if (charActual == 's') {
                lexema += charActual
                obtenerSiguienteCaracter()
                if (charActual == '(') {
                    lexema += charActual
                    obtenerSiguienteCaracter()
                    if (charActual == '+' || charActual == '-' || charActual == '*' || charActual == '/' || charActual == '%') {
                        lexema += charActual
                        obtenerSiguienteCaracter()
                        if (charActual == ')') {
                            lexema += charActual
                            obtenerSiguienteCaracter()
                            almacenarSimbolo(lexema, filaInicial, colInicial, Categoria.OPERADOR_ASIGNACION)
                            true
                        } else {
                            reportarError(lexema, filaInicial, colInicial, posInicial)
                            true
                        }
                    } else {
                        reportarError(lexema, filaInicial, colInicial, posInicial)
                        true
                    }
                } else {
                    almacenarSimbolo(lexema, filaInicial, colInicial, Categoria.OPERADOR_ASIGNACION)
                    true
                }
            } else {
                reportarError(lexema, filaInicial, colInicial, posInicial)
                true
            }
        } else {
            false
        }
    }

    /**
     * Metodo que permite determinar si una fraccion del codigo es un agrupador
     * derecho
     *
     * @return
     */
    private fun esAgrupadorIzquierdo(): Boolean {
        var lexema = ""
        val filaInicial = filaActual
        val colInicial = colActual
        val posInicial = posActual
        if (charActual == '-') {
            lexema += charActual
            obtenerSiguienteCaracter()
            if (charActual == '>') {
                lexema += charActual
                obtenerSiguienteCaracter()
                almacenarSimbolo(lexema, filaInicial, colInicial, Categoria.AGRUPADOR_IZQUIERDO)
                return true
            } else {
                hacerBacktracking(posInicial)
            }
        }
        return false
    }

    /**
     * Metodo que permite determinar si una fraccion del codigo es un agrupador
     * izquierdo
     *
     * @return
     */
    private fun esAgrupadorDerecho(): Boolean {
        var lexema = ""
        val filaInicial = filaActual
        val colInicial = colActual
        val posInicial = posActual
        if (charActual == '<') {
            lexema += charActual
            obtenerSiguienteCaracter()
            if (charActual == '-') {
                lexema += charActual
                obtenerSiguienteCaracter()
                almacenarSimbolo(lexema, filaInicial, colInicial, Categoria.AGRUPADOR_DERECHO)
                return true
            } else {
                hacerBacktracking(posInicial)
            }
        }
        return false
    }

    /**
     * Metodo que permite determinar si una fraccion del codigo es un parentesis
     * izquierdoG
     *
     * @return
     */
    private fun esParentesisIzquierdo(): Boolean {
        var lexema = ""
        val filaInicial = filaActual
        val colInicial = colActual
        if (charActual == '{') {
            lexema += charActual
            obtenerSiguienteCaracter()
            if (charActual == '{') {
                lexema += charActual
                obtenerSiguienteCaracter()
                almacenarSimbolo(lexema, filaInicial, colInicial, Categoria.PARENTESIS_IZQUIERDO)
                return true
            }
        }
        return false
    }

    /**
     * Metodo que permite determinar si una fraccion del codigo es un parentesis
     * derecho
     *
     * @return
     */
    private fun esParentesisDerecho(): Boolean {
        var lexema = ""
        val filaInicial = filaActual
        val colInicial = colActual
        if (charActual == '}') {
            lexema += charActual
            obtenerSiguienteCaracter()
            if (charActual == '}') {
                lexema += charActual
                obtenerSiguienteCaracter()
                almacenarSimbolo(lexema, filaInicial, colInicial, Categoria.PARENTESIS_DERECHO)
                return true
            }
        }
        return false
    }

    /**
     * Metodo que permite determinar si una fraccion del codigo es un operador
     * logico
     *
     * @return
     */
    private fun esOperadorLogico(): Boolean {
        var lexema = ""
        val filaInicial = filaActual
        val colInicial = colActual
        val posInicial = posActual
        if (charActual == 'A') {
            lexema += charActual
            obtenerSiguienteCaracter()
            return if (charActual == 'N') {
                lexema += charActual
                obtenerSiguienteCaracter()
                if (charActual == 'D') {
                    lexema += charActual
                    obtenerSiguienteCaracter()
                    almacenarSimbolo(lexema, filaInicial, colInicial, Categoria.OPERADOR_LOGICO)
                    true
                } else {
                    reportarError(lexema, filaInicial, colInicial, posInicial)
                    true
                }
            } else {
                reportarError(lexema, filaInicial, colInicial, posInicial)
                true
            }
        } else if (charActual == 'O') {
            lexema += charActual
            obtenerSiguienteCaracter()
            return if (charActual == 'R') {
                lexema += charActual
                obtenerSiguienteCaracter()
                almacenarSimbolo(lexema, filaInicial, colInicial, Categoria.OPERADOR_LOGICO)
                true
            } else {
                reportarError(lexema, filaInicial, colInicial, posInicial)
                true
            }
        } else if (charActual == 'N') {
            lexema += charActual
            obtenerSiguienteCaracter()
            return if (charActual == 'O') {
                lexema += charActual
                obtenerSiguienteCaracter()
                if (charActual == 'T') {
                    lexema += charActual
                    obtenerSiguienteCaracter()
                    almacenarSimbolo(lexema, filaInicial, colInicial, Categoria.OPERADOR_LOGICO)
                    true
                } else {
                    reportarError(lexema, filaInicial, colInicial, posInicial)
                    true
                }
            } else {
                reportarError(lexema, filaInicial, colInicial, posInicial)
                true
            }
        }
        return false
    }

    /**
     * Metodo para identificar si se esta asignando un arreglo
     *
     * @return
     */
    private fun esArreglo(): Boolean {
        var lexema = ""
        val colInicial = colActual
        val filaInicial = filaActual
        val posInicial = posActual
        return if (charActual == '$') {
            lexema += charActual
            obtenerSiguienteCaracter()
            if (charActual == 'a') {
                lexema += charActual
                obtenerSiguienteCaracter()
                if (charActual == 'r') {
                    lexema += charActual
                    obtenerSiguienteCaracter()
                    if (charActual == 'r') {
                        lexema += charActual
                        obtenerSiguienteCaracter()
                        if (charActual == '(') {
                            lexema += charActual
                            obtenerSiguienteCaracter()
                            while (Character.isDigit(charActual)) {
                                lexema += charActual
                                obtenerSiguienteCaracter()
                            }
                            if (charActual == ')') {
                                lexema += charActual
                                obtenerSiguienteCaracter()
                                almacenarSimbolo(lexema, filaInicial, colInicial, Categoria.ARREGLO)
                                obtenerSiguienteCaracter()
                                true
                            } else {
                                hacerBacktracking(posInicial)
                                false
                            }
                        } else {
                            reportarError(lexema, filaInicial, colInicial, posInicial)
                            true
                        }
                    } else {
                        reportarError(lexema, filaInicial, colInicial, posInicial)
                        true
                    }
                } else {
                    reportarError(lexema, filaInicial, colInicial, posInicial)
                    true
                }
            } else {
                hacerBacktracking(posInicial)
                false
            }
        } else {
            false
        }
    }

    /**
     * Metodo que identifica si el simbolo ingresado es un separador
     *
     * @return
     */
    private fun esSeparador(): Boolean {
        return if (charActual == '|') {
            almacenarSimbolo("" + charActual, filaActual, colActual, Categoria.SEPARADOR)
            obtenerSiguienteCaracter()
            true
        } else {
            false
        }
    }

    /**
     * Metodo paara identificar si es un invocador a elementos de objetos
     *
     * @return
     */
    private fun esPunto(): Boolean {
        return if (charActual == '\\') {
            almacenarSimbolo("" + charActual, filaActual, colActual, Categoria.PUNTO)
            obtenerSiguienteCaracter()
            true
        } else {
            false
        }
    }

    /**
     *
     * @param codigoFuente
     * el codigo fuente a analizar
     */
    init {
        charActual = codigoFuente[0]
        tablaSimbolos = ArrayList()
        tablaErrores = ArrayList()
        reservedWords = ArrayList()
        finCodigo = 0.toChar()
        llenarPalabras()
    }
}