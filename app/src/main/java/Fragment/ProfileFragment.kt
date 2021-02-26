package Fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.khanakhajana.R
import org.w3c.dom.Text


class ProfileFragment : Fragment() {
    lateinit var name:TextView
    lateinit var number:TextView
    lateinit var email:TextView
    lateinit var address:TextView
    lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_profile, container, false)
        sharedPreferences=activity?.getSharedPreferences(getString(R.string.preference_file),Context.MODE_PRIVATE)!!
        name=view.findViewById(R.id.dbName)
        number=view.findViewById(R.id.dbNumber)
        email=view.findViewById(R.id.dbEmail)
        address=view.findViewById(R.id.dbAddress)
        name.setText(sharedPreferences.getString("name","Name"))
        number.setText(sharedPreferences.getString("mobile_number","Number"))
        email.setText(sharedPreferences.getString("email","Email"))
        address.setText(sharedPreferences.getString("address","Address"))
        return view
    }


}