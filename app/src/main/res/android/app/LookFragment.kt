package layout

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by rshimura on 2017/07/08.
 */
public class LookFragment : Fragment() {
    companion object {
        fun getInstance(): LookFragment {
            return LookFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.something, container, false)
    }
}