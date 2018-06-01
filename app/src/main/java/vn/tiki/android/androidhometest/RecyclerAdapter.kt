package vn.tiki.android.androidhometest

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.recycleview_item.view.*
import vn.tiki.android.androidhometest.data.api.response.Deal
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.squareup.picasso.PicassoProvider
import vn.tiki.android.androidhometest.util.getImage
import java.util.*


class RecyclerAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    companion object {
        var mItems: MutableList<Deal>? = mutableListOf<Deal>()
        var mListTimer: MutableList<CountDownTimer>? = mutableListOf()
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
        mItems?.addAll(list!!.toMutableList())
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mItems?.get(position) ?: return
        Picasso.get().load(item.productThumbnail)
                .placeholder(R.drawable.abc_item_background_holo_dark)
                .fit()
                .into(holder.imageView)
        holder.txtName.text = item.productName
        holder.txtPrice.text = item.productPrice.toString()
        if (holder.timer != null) {
            holder.timer!!.cancel();
        }
        val period = item.endDate.time - Calendar.getInstance().time.time


        holder.timer = object : CountDownTimer(period, COUNTDOWN_INTERVAL) {
            override fun onFinish() {
                //Remove item
                mItems!!.remove(item)
                notifyDataSetChanged()
            }

            override fun onTick(millisUntilFinished: Long) {
                Log.d("RecyclerAdapter", "onTick $millisUntilFinished")
                holder.txtCountdown.text = (millisUntilFinished.div(1000)).toString()
            }

        }.start()
        holder.timer?.let {  mListTimer?.add(it) }
    }


    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
            super.onDetachedFromRecyclerView(recyclerView)
        mListTimer.orEmpty().forEach { timer -> timer.cancel() }
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var timer: CountDownTimer? = null
        val imageView: ImageView = view.imageView
        val txtName: TextView = view.tv_name
        val txtPrice: TextView = view.tv_price
        val txtCountdown: TextView = view.tv_countdown!!
    }

}