package com.example.contactutils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ContactAdapter(
    val list: ArrayList<String>,
    context: Context
) :
    RecyclerView.Adapter<ContactAdapter.ViewHolder>() {
    val context: Context = context
    private var mSelectedPosition = -1


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContactAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ContactAdapter.ViewHolder, position: Int) {
        holder.item_text!!.tag = position
        holder.check!!.tag = position
        holder.item_text!!.setText(list.get(position).toString())

        holder.linear!!.setOnClickListener {
            holder.check!!.performClick()
        }
        val pos = holder.check!!.getTag() as Int

        holder.check!!.setChecked(pos == mSelectedPosition)
        holder.check!!.setOnClickListener(View.OnClickListener { v ->
            val pos = v.tag as Int
            val Checked = v as CheckBox
            if (Checked.isChecked) {
                mSelectedPosition = pos
            } else {
                mSelectedPosition = -1
            }
            notifyDataSetChanged()
            val no=list.get(position)

            val prefs =
                context.getSharedPreferences("filename", Context.MODE_PRIVATE)
            val editor = prefs.edit()

            editor.putString("number",no)
            editor.apply()


        })


    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var item_text: TextView? = null
        var check: CheckBox? = null
        var linear: LinearLayout? = null

        init {
            item_text = view.findViewById(R.id.text)
            check = view.findViewById(R.id.check)
            linear = view.findViewById(R.id.linear)
        }
    }

}