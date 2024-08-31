package app.core.controls;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.graphics.drawable.StateListDrawable;

public class Drawable {

	public void test() {

		GradientDrawable gd = new GradientDrawable();

	}

	public int getList_item_title_Color() {
		return Color.parseColor("#ffffff");
	}

	public int getList_background() {
		return Color.parseColor("#303030");
	}

	public int getList_background_pressed() {
		return Color.parseColor("#992416");
	}

	public int getList_divider() {
		int color=Color.parseColor("#272727");
		return color;
				//this.getSimpleGradient(color,color);
	}
	public int gray()
	{
		return this.getcolor("#d3d3d3 ");
	}
	public int red()
	{
		return this.getcolor("#ff0000 ");
	}
	private int getcolor(String clrCode)
	{
		return Color.parseColor("#272727");
	}

	public GradientDrawable getcounter_text_bg() {
		return setGradientColors(Color.parseColor("#626262"),Color.parseColor("#626262"));
	}
	
	public  StateListDrawable getList_selector()
	{
		StateListDrawable stateListDrawable= new StateListDrawable();
		int color=this.getList_background();
		int scolor=this.getList_background_pressed();
		stateListDrawable.addState(new int[]{android.R.attr.state_activated},this.getGradient(color,color));
		stateListDrawable.addState(new int[]{android.R.attr.state_pressed},this.getGradient(scolor,scolor));
		stateListDrawable.addState(new int[]{android.R.attr.state_activated},this.getGradient(scolor,scolor));
		
		return stateListDrawable;
	}
	

	public int getcounter_text_color() {
		return Color.parseColor("#c5c4c4");
	}

	private GradientDrawable getGradient(int bottomColor, int topColor) {
		GradientDrawable gradient = new GradientDrawable(
				Orientation.BOTTOM_TOP, new int[] { bottomColor, topColor });
		gradient.setShape(GradientDrawable.RECTANGLE);
		gradient.setGradientRadius(90);
		return gradient;
	}
	
	private GradientDrawable setGradientColors(int bottomColor, int topColor) {
		GradientDrawable gradient = new GradientDrawable(
				Orientation.BOTTOM_TOP, new int[] { bottomColor, topColor });
		gradient.setShape(GradientDrawable.RECTANGLE);
		gradient.setCornerRadius(2.f);
	
		return gradient;
	}
	private GradientDrawable getSimpleGradient(int bottomColor, int topColor)
	{
		GradientDrawable gradient = new GradientDrawable(
				Orientation.BOTTOM_TOP, new int[] { bottomColor, topColor });
		gradient.setShape(GradientDrawable.RECTANGLE);

		//gradient.setCornerRadius(10.f);
		return gradient;	
	}
	public GradientDrawable getSimpleGradientwithstroke(int strokeclr)
	{
		GradientDrawable gradient = new GradientDrawable();
		gradient.setShape(GradientDrawable.RECTANGLE);
		gradient.setStroke(1,Color.parseColor("#F6A00A"));

		//gradient.setCornerRadius(10.f);
		return gradient;
	}

}
