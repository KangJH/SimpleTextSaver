package home.text.simpletextsaver;

import java.text.NumberFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class ListItem_Main extends ArrayAdapter<AccountChunk> {

	OnClickListener mOnClickListener = null;
	public ListItem_Main(Context context, int resource,
			ArrayList<AccountChunk> objects, OnClickListener onClickListener) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		mResourceID = resource;
		mOnClickListener = onClickListener;
	}
	private int mResourceID = 0;
	
	public void toggleCheckbox(boolean val, View convertView) {
		//AccountChunk currItem = getItem(position);
		//currItem.blChecked = !currItem.blChecked;
		if(convertView != null) {
			((CheckBox)convertView.findViewById(R.id.listitem_checkbox)).setChecked(val);
		}
	}
	
	/*private void updateView(int index){
	    View v = yourListView.getChildAt(index - 
	       yourListView.getFirstVisiblePosition());
	    TextView someText = (TextView) v.findViewById(R.id.sometextview);
	    someText.setText("Hi! I updated you manually!");
	}*/
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
			convertView = inflater.inflate(mResourceID, null);
			holder = new ViewHolder();
			holder.tvContent = (TextView) convertView.findViewById(R.id.listitem_content);
			holder.tvExpense = (TextView) convertView.findViewById(R.id.listitem_expense);
			holder.tvDate = (TextView) convertView.findViewById(R.id.listitem_date);
			holder.cbCheck = (CheckBox) convertView.findViewById(R.id.listitem_checkbox);
			holder.cbCheck.setOnClickListener(mOnClickListener);
			convertView.setTag(holder);
		} 
		holder = (ViewHolder) convertView.getTag();
		AccountChunk currItem = getItem(position);
		holder.cbCheck.setTag(position);
		holder.tvContent.setText(currItem.strContent);
		holder.tvExpense.setText(NumberFormat.getCurrencyInstance().format(Long.parseLong(currItem.strExpense)));
		holder.tvDate.setText(currItem.strDate);
		holder.cbCheck.setChecked(currItem.blChecked);
		return convertView;
	}
	private class ViewHolder {
		TextView tvContent;
		TextView tvExpense;
		TextView tvDate;
		CheckBox cbCheck;
	}

}
