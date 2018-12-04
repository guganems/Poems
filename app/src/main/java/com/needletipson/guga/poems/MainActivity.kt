package com.needletipson.guga.poems

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.needletipson.guga.poems.api.Api
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val random: Random

        val retrofit = Retrofit.Builder()
            .baseUrl("http://poemsapi.ga/public/api/poem/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api: Api = retrofit.create(Api::class.java)

        var url = ""

        fun randomPoem(){
            var randId = (1..18).shuffled().last()
            url = randId.toString()

            val call = api.getPoems(url)

            call.enqueue(object : Callback<List<Poems>> {
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

        randomPoem()

        randombtn.setOnClickListener {
            randomPoem()
        }
    }
}
