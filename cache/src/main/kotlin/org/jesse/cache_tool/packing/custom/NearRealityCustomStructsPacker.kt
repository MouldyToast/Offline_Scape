package org.jesse.cache_tool.packing.custom

import mgi.types.config.StructDefinitions

object NearRealityCustomStructsPacker {

    @JvmStatic
    fun pack() {
        StructDefinitions.get(500).copy(10500).apply {
            this.parameters[689] = "Strykewyrms"
            this.parameters[690] = 10500
            this.pack()
        }

        StructDefinitions.get(500).copy(10501).apply {
            this.parameters[689] = "Primal Items"
            this.parameters[690] = 10501
            this.pack()
        }

        StructDefinitions.get(500).copy(10502).apply {
            this.parameters[689] = "Wild Mole"
            this.parameters[690] = 10502
            this.pack()
        }
    }

}