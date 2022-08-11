package utilities

import org.slf4j.LoggerFactory

class Logger(private val moduleName: String, private val msg: String) {
    fun debug() {
        LoggerFactory.getLogger(moduleName).debug(msg)
    }

    fun error() {
        LoggerFactory.getLogger(moduleName).error(msg)
    }

    fun info() {
        LoggerFactory.getLogger(moduleName).info(msg)
    }
}