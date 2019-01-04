package services.ozzy.ozzystemperatures

import android.app.AlertDialog
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView

class MyRecyclerViewAdapter internal constructor(private val context: Context) :
    RecyclerView.Adapter<MyRecyclerViewAdapter.DataObjectHolder>() {

    inner class DataObjectHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        internal var sensorNameView: TextView = itemView.findViewById(R.id.sensorName) as TextView
        internal var sensorValueView: TextView = itemView.findViewById(R.id.sensorValue) as TextView

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val (first, _, third) = TempSensorHandler.getAll()[adapterPosition]

            val alert = AlertDialog.Builder(this@MyRecyclerViewAdapter.context)

            alert.setTitle("Set custom name")
            alert.setMessage("Here you can set custom name for this fucking thing.")

            val input = EditText(this@MyRecyclerViewAdapter.context)
            input.setText(first)
            input.setSingleLine()
            alert.setView(input)

            alert.setPositiveButton("Ok") { _, _ ->
                TempSensorHandler.updateName(third, input.text.toString())
            }

            alert.setNegativeButton("Cancel") { _, _ ->
                // Do nothing
            }

            alert.show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataObjectHolder {
        return DataObjectHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_view_element, parent, false))
    }

    override fun onBindViewHolder(holder: DataObjectHolder, position: Int) {
        val pairs = TempSensorHandler.getAll()

        holder.sensorNameView.text = pairs[position].first
        holder.sensorValueView.text = context.getString(R.string.degree_value, pairs[position].second.toPlainString())
    }

    override fun getItemCount(): Int {
        return TempSensorHandler.count()
    }
}