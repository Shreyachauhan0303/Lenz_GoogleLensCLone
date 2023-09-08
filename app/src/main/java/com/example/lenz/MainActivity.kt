package com.example.lenz

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.firebase.ml.vision.FirebaseVision
import com.android.volley.toolbox.Volley
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler
import org.json.JSONException
import org.json.JSONObject

class MainActivity(var imageBitmap: Bitmap) : AppCompatActivity() {
    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
    }

    private lateinit var captureIV: ImageView
    private lateinit var snapButton: Button
    private lateinit var getSearchResultBtn: Button
    private lateinit var resultsRv: RecyclerView
    private lateinit var searchRVAdapter: SearchRVAdapter
    private val searchRVModelArrayList: ArrayList<SearchRVModel> = ArrayList()
    private lateinit var loadingPb: ProgressBar
    private lateinit var title: String
    private lateinit var link: String
    private lateinit var displayedLink: String
    private lateinit var snippet: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        captureIV = findViewById(R.id.image)
        snapButton = findViewById(R.id.idBtnSnap)
        getSearchResultBtn = findViewById(R.id.idBtnResults)
        loadingPb=findViewById(R.id.idPBLoading)
        resultsRv = findViewById(R.id.idRVSearchResults)
        searchRVAdapter = SearchRVAdapter(searchRVModelArrayList, this)
        val manager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        resultsRv.layoutManager = manager
        resultsRv.adapter = searchRVAdapter



        snapButton.setOnClickListener {
            searchRVModelArrayList.clear()
            searchRVAdapter.notifyDataSetChanged()
            takePictureIntent()
        }

        getSearchResultBtn.setOnClickListener {
            getResults()
        }
    }


    // Replace this with your button click listener.
    fun getResults() {
        val image = FirebaseVisionImage.fromBitmap(imageBitmap)
        val labeler: FirebaseVisionImageLabeler = FirebaseVision.getInstance().onDeviceImageLabeler

        labeler.processImage(image)
            .addOnSuccessListener(OnSuccessListener<List<FirebaseVisionImageLabel>> { labels ->
                val searchQuery = labels[0].text
                searchData(searchQuery)
            })
            .addOnFailureListener(OnFailureListener {
                Toast.makeText(this@MainActivity, "Fail to detect image..", Toast.LENGTH_SHORT)
                    .show()
            })
    }

    private fun searchData(searchQuery: String) {
        val apiKey = "Enter your API key here"
        val url =
            "https://serpapi.com/search.json?q=${searchQuery.trim()}&location=Delhi,India&hl=en&gl=us&google_domain=google.com&api_key=$apiKey"

        val queue: RequestQueue = Volley.newRequestQueue(this@MainActivity)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val organicResultsArray = response.getJSONArray("organic_results")
                    for (i in 0 until organicResultsArray.length()) {
                        val organicObj = organicResultsArray.getJSONObject(i)
                        title = organicObj.optString("title", "")
                        link = organicObj.optString("link", "")
                        displayedLink = organicObj.optString("displayed_link", "")
                        snippet = organicObj.optString("snippet", "")
                        searchRVModelArrayList.add(
                            SearchRVModel(
                                title,
                                link,
                                displayedLink,
                                snippet
                            )
                        )
                    }
                    searchRVAdapter.notifyDataSetChanged()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                Toast.makeText(
                    this@MainActivity,
                    "No Result found for the search query..",
                    Toast.LENGTH_SHORT
                )
                    .show()
            })

        queue.add(jsonObjectRequest)
    }

    fun takePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val extras = data?.extras
            if (extras != null) {
                imageBitmap = extras.get("data") as Bitmap
                captureIV.setImageBitmap(imageBitmap)
            }
        }

    }


}
