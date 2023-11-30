package android.com.testaeonapp

import android.com.testaeonapp.adapter.TransactionAdapter
import android.com.testaeonapp.databinding.ActivityMainBinding
import android.com.testaeonapp.retrofit.AuthApi
import android.com.testaeonapp.retrofit.AuthRequest
import android.com.testaeonapp.retrofit.AuthResponse
import android.com.testaeonapp.retrofit.TransactionApi
import android.os.Bundle
import android.widget.SearchView.OnQueryTextListener
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: TransactionAdapter
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = TransactionAdapter()
        binding.rcView.layoutManager = LinearLayoutManager(this)
        binding.rcView.adapter = adapter


        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://easypay.world/api-test/").client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val authApi = retrofit.create(AuthApi::class.java)
        val transactionApi = retrofit.create(TransactionApi::class.java)




        var user: AuthResponse? = null

        CoroutineScope(Dispatchers.IO).launch {
            user = authApi.getAuth(
                AuthRequest(
                    "demo",
                    "12345"
                )
            )
            runOnUiThread {
                supportActionBar?.title = user?.response?.token
            }
        }


        binding.sv.setOnQueryTextListener(object : OnQueryTextListener{
            override fun onQueryTextSubmit(text: String?): Boolean {
                CoroutineScope(Dispatchers.IO).launch {
                    val payments = text?.let { transactionApi.getTransactions(user?.response?.token ?: "") }
                    runOnUiThread {
                        binding.apply {
                            adapter.submitList(payments?.response)
                        }
                    }
                }
                return true
            }

            override fun onQueryTextChange(text: String?): Boolean {
                return true
            }

        })
    }
}