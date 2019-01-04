package services.ozzy.ozzystemperatures

import java.math.BigDecimal

data class TempSensorMessage(val sensorId: String, val sensorValue: BigDecimal) {

    constructor(id: String, data: Double) : this(id, BigDecimal(data))
    constructor(id: String, data: String) : this(id, BigDecimal(data))

}