package cloud.rsps.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.afterburner.AfterburnerModule
import com.fasterxml.jackson.module.kotlin.kotlinModule

class JacksonObjectMapper : ObjectMapper() {
    init {
        registerModule(AfterburnerModule())
        registerModule(kotlinModule())
    }

    companion object {
        @JvmStatic
        fun getObjectMapper(): ObjectMapper = JacksonObjectMapper()
    }
}
