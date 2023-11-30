package android.com.testaeonapp

import android.com.testaeonapp.adapter.TransactionAdapter
import android.com.testaeonapp.databinding.FragmentTransactionsBinding
import android.com.testaeonapp.retrofit.TransactionApi
import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date


class TransactionsFragment : Fragment() {
    private lateinit var adapter: TransactionAdapter
    private lateinit var binding: FragmentTransactionsBinding
    private lateinit var transactionApi: TransactionApi
    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransactionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRetrofit()
        initRcView()
        viewModel.token.observe(viewLifecycleOwner){ token ->
            CoroutineScope(Dispatchers.IO).launch {
                val allPayments = transactionApi.getTransactions(token)
                val payments = allPayments.response.filter {
                    it.title?.trim() != "" && it.amount?.trim() != "" &&
                    it.created != null && it.id != null && it.title!= null && it.amount != null
                }
                payments.forEach {
                    val date = it.created?.toLong()?.let { it1 -> Date(it1.times(1000L)) } // *1000 = миллисекунды
                    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    sdf.timeZone = TimeZone.getTimeZone("GMT+3")
                    it.created = sdf.format(date)
                }
                requireActivity().runOnUiThread {
                    adapter.submitList(payments)
                }
            }
        }
    }

    private fun initRetrofit(){
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://easypay.world/api-test/").client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
        transactionApi = retrofit.create(TransactionApi::class.java)
    }

    private fun initRcView() = with(binding){
        adapter = TransactionAdapter()
        rcView.layoutManager = LinearLayoutManager(context)
        rcView.adapter = adapter
        binding.buttonExit.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}
