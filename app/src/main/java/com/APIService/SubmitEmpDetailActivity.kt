package com.APIService

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.fieldforce.asyntask.WebService
import com.fieldforce.harmonkardonff.MainActivity
import com.fieldforce.harmonkardonff.R
import com.fieldforce.profile.MyProfileModel

import com.fieldforce.webasyntask.ConnGetReqAsyResParser
import com.google.gson.Gson
import com.smarteist.autoimageslider.SliderView
import com.vspl.docopd.API.Apiintefacec
import com.vspl.docopd.API.Apiintefacec1
import org.json.JSONObject
import retrofit2.Call
class SubmitEmpDetailActivity : AppCompatActivity() {
    var sliderView: SliderView? = null
    var images = intArrayOf(R.drawable.imageone, R.drawable.imagetwo)
    var interested_btn:Button?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slider_interested)
        sliderView = findViewById(R.id.slider)
        interested_btn=findViewById(R.id.interested_btn)
        val adapter = SliderAdaptertwo(this, images)

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

        interested_btn!!.setOnClickListener {
            SignInAPI();
        }

    }
    fun SignInAPI()
    {
        val pd = ProgressDialog(this)
        pd.setMessage("Loading...")
        pd.setCancelable(false)
        pd.show()


        //  schemeModel.InsertOrUpdate()

        //  val apiService = ApiClientForAPI.getClient()!!.SignIn()
        val apiService = ApiClient.getClient().create(Apiintefacec::class.java)

        val call = apiService.submit_employee_details(MainActivity.MyInfo.EmployeeCode,MainActivity.MyInfo.ISPName.toString(),"",MainActivity.MyInfo.Pmobile1,MainActivity.MyInfo.CurrentStore,"Male","harman_isd")

        call.enqueue(object : retrofit2.Callback<SubmitEmpDetail> {
            override fun onResponse(call: Call<SubmitEmpDetail>, response: retrofit2.Response<SubmitEmpDetail>)
            {


                if (response.isSuccessful() && response.body() != null)
                {

                    Log.e("SignUpRePonse", response.body()!!.status+"ndgjkdn")
                    val intent = Intent(this@SubmitEmpDetailActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<SubmitEmpDetail>, t: Throwable)
            {

                Toast.makeText(this@SubmitEmpDetailActivity, "Server Error" , Toast.LENGTH_LONG).show()
            }

        })

    }


    fun SubmitNO()
    {
        val pd = ProgressDialog(this)
        pd.setMessage("Loading...")
        pd.setCancelable(false)
        pd.show()

        //  schemeModel.InsertOrUpdate()

        //  val apiService = ApiClientForAPI.getClient()!!.SignIn()
        val apiService = ApiClient.getClient().create(Apiintefacec1::class.java)

        val call = apiService.submit_employee_details_NO(MainActivity.MyInfo.EmployeeCode,MainActivity.MyInfo.ISPName.toString(),"",MainActivity.MyInfo.Pmobile1,MainActivity.MyInfo.CurrentStore,"Male","harman_isd")

        call.enqueue(object : retrofit2.Callback<SubmitEmpDetail> {
            override fun onResponse(call: Call<SubmitEmpDetail>, response: retrofit2.Response<SubmitEmpDetail>)
            {


                if (response.isSuccessful() && response.body() != null)
                {

                    Log.e("SignUpRePonse", response.body()!!.status+"ndgjkdn")
                    val intent = Intent(this@SubmitEmpDetailActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<SubmitEmpDetail>, t: Throwable)
            {

                Toast.makeText(this@SubmitEmpDetailActivity, "Server Error" , Toast.LENGTH_LONG).show()
            }

        })

    }

    override fun onBackPressed() {
        SubmitNO()
    }
    var server = WebService()
    var gson = Gson()



}