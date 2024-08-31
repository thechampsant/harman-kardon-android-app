package app.core.controls;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SearchDialogControl {

	Context context;
	RelativeLayout lyt;
	//RelativeLayout listlyt;
	LinearLayout searchHeader;
	public ListView lv;
	public EditText SearchBox;
	public Button Back;
	public TextView msg;

	public SearchDialogControl(Context _context) {
		context = _context;
	}
    public int getID()
    {
    	int random = (int )(Math.random() * 50 + 1);
    	return random;
    }
	public View getSearchLayout() {
		lyt = new RelativeLayout(context);
		this.setSearchHeader();
		this.setBackButton();
		this.setSearchBox();
		this.setMessageText();
		this.setlistview();
		assembleLayout();
		return lyt;
	}

	private void setlistview() {
	    //listlyt = new RelativeLayout(context);
		lv = new ListView(context);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.BELOW,searchHeader.getId());
		lv.setLayoutParams(lp);
		//listlyt.addView(lv);
	}

	private void setBackButton() {
		Back = new Button(context);
		Back.setText("Back");
		Back.setTypeface(Typeface.DEFAULT_BOLD);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(150,
				70);
		lp.addRule(lyt.ALIGN_PARENT_BOTTOM);
		lp.addRule(lyt.ALIGN_PARENT_RIGHT);
		Back.setLayoutParams(lp);

	}

	private void setSearchBox() {
		SearchBox = new EditText(context);
		SearchBox.setHint("Tap to search!");

	}

	private void setMessageText() {
		msg = new TextView(context);
		msg.setText("Sorry No Result found!");
		msg.setVisibility(View.GONE);
	}

	private void setSearchHeader() {
		searchHeader = new LinearLayout(context);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(lyt.ALIGN_PARENT_TOP);
		lp.setMargins(0,0,0,10);
		searchHeader.setId(getID());
		searchHeader.setOrientation(LinearLayout.VERTICAL);
		searchHeader.setLayoutParams(lp);
	}

	private void assembleLayout() {
		lyt.addView(searchHeader);
		searchHeader.addView(SearchBox);
		searchHeader.addView(msg);
		lyt.addView(lv);
		lyt.addView(Back);
	}
}
