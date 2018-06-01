package vn.tiki.android.androidhometest

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import vn.tiki.android.androidhometest.data.api.ApiServices
import vn.tiki.android.androidhometest.data.api.response.Deal
import vn.tiki.android.androidhometest.di.initDependencies
import vn.tiki.android.androidhometest.di.inject
import vn.tiki.android.androidhometest.di.releaseDependencies

class MainActivity : AppCompatActivity() {

  val apiServices by inject<ApiServices>()
  private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var itemAdapter: RecyclerAdapter
    private lateinit var task : AsyncTask<Unit, Unit, List<Deal>>


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    initDependencies(this)

    setContentView(R.layout.activity_main)

      gridLayoutManager = GridLayoutManager(this, 2)
      recyclerView.layoutManager = gridLayoutManager
      recyclerView.setHasFixedSize(true)
      recyclerView.itemAnimator = DefaultItemAnimator()
       itemAdapter = RecyclerAdapter(this)
      recyclerView.adapter  = itemAdapter
      task = LoadServiceAsync(itemAdapter, apiServices).execute()

//      object : AsyncTask<Unit, Unit, List<Deal>>() {
//      override fun doInBackground(vararg params: Unit?): List<Deal> {
//        return apiServices.getDeals()
//      }
//
//      override fun onPostExecute(result: List<Deal>?) {
//        super.onPostExecute(result)
//        result.orEmpty()
//            .forEach { deal ->
//              println(deal)
//            }
//          itemAdapter.addItem(result)
//
//      }
//    }.execute()
  }

  override fun onDestroy() {
    super.onDestroy()
      recyclerView.adapter = null
      task.cancel(true)
    releaseDependencies()
  }

    override fun onStop() {
        super.onStop()
        recyclerView.adapter = null

    }

  override fun onRestart() {
    super.onRestart()
    recyclerView.adapter = itemAdapter
  }
}
