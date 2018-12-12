package com.needletipson.guga.poems

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.needletipson.guga.poems.api.Api
import kotlinx.android.     synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://poemsapi.ga/public/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api: Api = retrofit.create(Api::class.java)

        var url: String

        fun getCount(){
            url = "poems"

            var poemsCountFromAPI: Int

            val call = api.getPoemsCount(url)

            call.enqueue(object : Callback<List<Count>> {
                override fun onResponse(call: Call<List<Count>>, response: Response<List<Count>>) {
                    val poemsCount = response.body()

                    val count = arrayOfNulls<String>(poemsCount!!.size)

                    for (i in poemsCount.indices) {
                        count[i] = poemsCount[i].count.toString()
                    }

                    poemsCountFromAPI = count[0]!!.toInt()
                    randomPoem(poemsCountFromAPI)
                }

                override fun onFailure(call: Call<List<Count>>, t: Throwable) {
                    Toast.makeText(
                        applicationContext, t.message, Toast
                            .LENGTH_SHORT
                    ).show()
                }

                fun randomPoem(count: Int){
                    val randId = (1..count).shuffled().last()
                    url = "poem/" + randId.toString()

                    val callPoems = api.getPoems(url)

                    callPoems.enqueue(object : Callback<List<Poems>> {
                        override fun onResponse(call: Call<List<Poems>>, response: Response<List<Poems>>) {
                            val poems = response.body()

                            val poemTitle = arrayOfNulls<String>(poems!!.size)
                            val poemAuthor = arrayOfNulls<String>(poems.size)
                            val poemContent = arrayOfNulls<String>(poems.size)

                            for (i in poems.indices) {
                                poemTitle[i] = poems[i].title
                                poemAuthor[i] = poems[i].author
                                poemContent[i] = poems[i].content
                            }

                            titleTxt.text = poemTitle[0]
                            author.text = poemAuthor[0]
                            poemContentTxt.text = poemContent[0]
                        }

                        override fun onFailure(call: Call<List<Poems>>, t: Throwable) {
                            Toast.makeText(
                                applicationContext, t.message, Toast
                                    .LENGTH_SHORT
                            ).show()
                        }
                    })
                }
            })
        }


        getCount()

        randombtn.setOnClickListener {
            getCount()
        }
    }
}
