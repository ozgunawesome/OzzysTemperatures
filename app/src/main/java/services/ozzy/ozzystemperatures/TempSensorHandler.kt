package services.ozzy.ozzystemperatures

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Handler
import android.os.Message
import android.support.v7.widget.RecyclerView
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import kotlin.concurrent.timerTask

object TempSensorHandler : Handler() {
    private const val updateInterval: Long = 500 //milliseconds

    private val timer = Timer()
    private val sensorValues: MutableMap<String, BigDecimal> = HashMap()
    private val ignoreIds: MutableSet<String> = HashSet()
    private lateinit var preferences: SharedPreferences
    private lateinit var adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>

    fun setContext(context: Context) {
        preferences = context.getSharedPreferences("OzzysHeadunitTempApp", MODE_PRIVATE)
    }

    fun setAdapter(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        this.adapter = adapter
    }

    override fun handleMessage(msg: Message) {
        updateValue(msg.obj as TempSensorMessage)
    }

    private fun updateValue(x: TempSensorMessage) {
        if (ignoreIds.contains(x.sensorId)) return

        sensorValues[x.sensorId] = x.sensorValue.setScale(2, RoundingMode.HALF_EVEN);
        adapter.notifyDataSetChanged()

        ignoreIds.add(x.sensorId)
        timer.schedule(timerTask { ignoreIds.remove(x.sensorId) }, updateInterval)
    }

    private fun getSensorName(id: String): String {
        return preferences.getString(id, id)
    }

    fun updateName(id: String, name: String) {
        val editor = preferences.edit()
        editor.putString(id, name)
        editor.apply()

        adapter.notifyDataSetChanged()
    }

    fun getAll(): List<Triple<String, BigDecimal, String>> {
        return sensorValues
            .map { Triple(getSensorName(it.key), it.value, it.key) }
            .sortedBy { it.third }
    }

    fun count(): Int {
        return sensorValues.count()
    }
}
