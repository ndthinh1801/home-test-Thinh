package vn.tiki.android.androidhometest

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.recycleview_item.view.*
import vn.tiki.android.androidhometest.data.api.response.Deal
import android.os.CountDownTimer
import android.widget.ImageView
import android.widget.TextView
import vn.tiki.android.androidhometest.util.getImage


class RecyclerAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    companion object {
        var mItems: MutableList<Deal>? = mutableListOf<Deal>()
        var mMapTask: MutableMap<Deal, CountDownTimer> = mutableMapOf<Deal, CountDownTimer>()
        const val COUNTDOWN_INTERVAL: Long = 1000

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.recycleview_item, parent, false))
    }

    override fun getItemCount(): Int {
        return mItems?.size ?: 0
    }

    fun addItem(list: List<Deal>?) {
        mItems!!.addAll(list!!.toMutableList())
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mItems?.get(position) ?: return
        holder.imageView.setImageDrawable(context.assets.getImage(item.productThumbnail))
        holder.txtName.text = item.productName
        holder.txtPrice.text = item.productPrice.toString()
        var countDownTimer = object : CountDownTimer(item.millisUntilFinished, COUNTDOWN_INTERVAL) {
            override fun onFinish() {
                //Remove item
                mItems!!.remove(item)
                //Release all timer
                releaseAllTask()
                mMapTask.clear()
                //Reload all item and timer
                notifyDataSetChanged()
            }

            override fun onTick(millisUntilFinished: Long) {
                item.millisUntilFinished = millisUntilFinished
                holder.txtCountdown.text = (millisUntilFinished.div(1000)).toString()
            }

        }
        mMapTask.put(item, countDownTimer)
        countDownTimer.start()

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.imageView
        val txtName: TextView = view.tv_name
        val txtPrice: TextView = view.tv_price
        val txtCountdown: TextView = view.tv_countdown!!
    }

    fun releaseAllTask() {
        mMapTask.forEach { _, task -> task.cancel() }
    }
}