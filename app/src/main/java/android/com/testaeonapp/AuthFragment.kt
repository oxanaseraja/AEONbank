package android.com.testaeonapp

import android.com.testaeonapp.databinding.FragmentAuthBinding
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import android.com.testaeonapp.retrofit.AuthApi
import android.com.testaeonapp.retrofit.AuthRequest
import androidx.fragment.app.activityViewModels
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthFragment : Fragment() {

    private lateinit var binding: FragmentAuthBinding
    private lateinit var authApi: AuthApi
    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRetrofit()
        binding.apply {
            buttonEnter.setOnClickListener {
                findNavController().navigate(R.id.action_AuthFragment_to_TransactionsFragment)
            }
            buttonSignIn.setOnClickListener {
                getAuth(
                    AuthRequest(
                        textLogin.text.trim().toString(),
                        textPassword.text.trim().toString()
                    )
                )
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
        authApi = retrofit.create(AuthApi::class.java)
    }

    private fun getAuth(authRequest: AuthRequest){
        CoroutineScope(Dispatchers.IO).launch{
            val authResponse = authApi.getAuth(authRequest)
            val message = authResponse.error?.error_msg
            requireActivity().runOnUiThread {
                binding.error.text = message
                val token = authResponse.response?.token
                if(!token.isNullOrEmpty()){
                    binding.buttonEnter.visibility = View.VISIBLE
                    viewModel.token.value = token
                }
            }

        }
    }

}