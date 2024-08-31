package slidemenu;


import android.annotation.SuppressLint;

//import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;


@SuppressLint("NewApi")
public abstract class IFragment extends Fragment {

	public View FragmentView=null;
	public abstract void Activate(View FragmentView);
//	public View InflateView(int resource,LayoutInflater inflater, ViewGroup container,
//            Bundle savedInstanceState)
//	{
//		 FragmentView = inflater.inflate(R.layout.fragment_photos, container, false);
//		 Activate(FragmentView);
//		 return FragmentView;
//	}
	public View InflateView(int resource,LayoutInflater inflater, ViewGroup container)
	{
		 FragmentView = inflater.inflate(resource, container, false);
		 Activate(FragmentView);
		 return FragmentView;
	}
	
}
