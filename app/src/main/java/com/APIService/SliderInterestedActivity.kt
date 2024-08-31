package com.APIService

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fieldforce.harmonkardonff.R
import com.smarteist.autoimageslider.SliderView
import com.vspl.docopd.API.Apiintefacec
import retrofit2.Call

class SliderInterestedActivity : AppCompatActivity() {
    var sliderView: SliderView? = null
    var images = intArrayOf(R.drawable.imageone, R.drawable.imagetwo)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slider_interested)
        sliderView = findViewById(R.id.slider)
        val adapter = SliderAdaptertwo(this@SliderInterestedActivity, images)

        // below method is used to set auto cycle direction in left to
        // right direction you can change according to requirement.
        sliderView!!.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR)

        // below method is used to
        // setadapter to sliderview.
        sliderView!!.setSliderAdapter(adapter)

        // below method is use to set
        // scroll time in seconds.
        sliderView!!.setScrollTimeInSec(3)

        // to set it scrollable automatically
        // we use below method.
        sliderView!!.setAutoCycle(true)

        // to start autocycle below method is used.
        sliderView!!.startAutoCycle()

        SignInAPI();
    }


    fun SignInAPI()
    {
        val pd = ProgressDialog(this)
        pd.setMessage("Loading...")
        pd.setCancelable(false)
        pd.show()
        //  val apiService = ApiClientForAPI.getClient()!!.SignIn()
        val apiService = ApiClient.getClient().create(Apiintefacec::class.java)
        val call = apiService.submit_employee_details("1188","Ashutosh Dubey","ashu@gmail.com","123456789","SDE","Male", "harman_isd")

        call.enqueue(object : retrofit2.Callback<SubmitEmpDetail> {
            override fun onResponse(call: Call<SubmitEmpDetail>, response: retrofit2.Response<SubmitEmpDetail>)
            {

                Log.e("SignUpRePonse", response.code().toString()+"ndgjkdn")
                if (response.isSuccessful() && response.body() != null)
                {

                    Log.e("SignUpRePonse", response.message().toString()+"ndgjkdn")


                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<SubmitEmpDetail>, t: Throwable)
            {

                Toast.makeText(this@SliderInterestedActivity, "Server Error" , Toast.LENGTH_LONG).show()
            }

        })

    }
}