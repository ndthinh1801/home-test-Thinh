package vn.tiki.android.androidhometest

import android.os.AsyncTask
import vn.tiki.android.androidhometest.data.api.ApiServices
import vn.tiki.android.androidhometest.data.api.response.Deal
import java.lang.ref.WeakReference

class LoadServiceAsync(recyclerAdapter: RecyclerAdapter, apiServices: ApiServices) : AsyncTask<Unit, Unit, List<Deal>>() {

    private val adapterReference : WeakReference<RecyclerAdapter> = WeakReference(recyclerAdapter)
    private val apiServicesReference : WeakReference<ApiServices> = WeakReference(apiServices)

    override fun doInBackground(vararg params: Unit?): List<Deal>? {
        return apiServicesReference.get()?.getDeals()
    }

    override fun onPostExecute(result: List<Deal>?) {
        result.orEmpty()
                .forEach { deal ->
                    println(deal)
                }
        adapterReference.get()?.addItem(result)

    }
}