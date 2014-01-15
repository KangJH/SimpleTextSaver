package home.text.simpletextsaver;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;

public class ListViewActivity extends Activity implements OnClickListener, OnItemClickListener {
	ListView		mMainListView = null;
	ListItem_Main	mMainAdapter = null;
	CheckBox		mAllCheckbox = null;
	//String			mShareFilePath = null;
	AccountDataController mAccountDataController = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview_main);
		findViewById(R.id.listview_delete_btn).setOnClickListener(this);
		mAllCheckbox = (CheckBox) findViewById(R.id.listview_all_checkbox);
		mAllCheckbox.setOnClickListener(this);
		//read data from file
		mAccountDataController = AccountDataController.getInstance();//(AccountDataController) getIntent().getExtras().getSerializable(getResources().getString(R.string.intent_share_data));
		
		mMainListView = (ListView)findViewById(R.id.listview_main);
		mMainAdapter = new ListItem_Main(this, R.layout.listitem_main, mAccountDataController.getAccountChunks(), this);
        mMainListView.setAdapter(mMainAdapter);
	}
	public void onDestroy() {
		super.onDestroy();
		//mAccountDataController.save();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.listview_delete_btn: {
			ArrayList<AccountChunk> arr = mAccountDataController.getAccountChunks();
			ArrayList<AccountChunk> delArr = new ArrayList<AccountChunk>(); 
			for(AccountChunk obj : arr) {
				if(obj.blChecked) {
					delArr.add(obj);
				}
			}
			mAccountDataController.removeData(delArr);
			mMainAdapter.notifyDataSetChanged();
			if(mAllCheckbox.isChecked()) {
				mAllCheckbox.setChecked(false);
			}
			break;
		}
		case R.id.listview_all_checkbox: {
			boolean newCheckVal = mAllCheckbox.isChecked();
			ArrayList<AccountChunk> arr = mAccountDataController.getAccountChunks();
			for(AccountChunk obj : arr) {
				obj.blChecked = newCheckVal;
			}
			mAccountDataController.updateData(arr, false);
			mMainAdapter.notifyDataSetChanged();
			break;
		}
		case R.id.listitem_checkbox:
			ArrayList<AccountChunk> arr = mAccountDataController.getAccountChunks();
			int position = (Integer) v.getTag();
			if(position < arr.size()) {
				arr.get(position).blChecked = !arr.get(position).blChecked;
				View itemView = mMainListView.getChildAt(position - mMainListView.getFirstVisiblePosition());
				mMainAdapter.toggleCheckbox(arr.get(position).blChecked, itemView);
			}
			break;
		default:
			break;
		}
	}
	
	/*private void loadAccountChunkFromFile(String path) {
		if(path != null && path.length() > 0) {
			File file = new File(path);
			FileInputStream fis = null;
			String strBuffer = null;
			try {
				fis = new FileInputStream(file);
				byte[] buffer = new byte[fis.available()];
				fis.read(buffer);
				fis.close();
				strBuffer = new String(buffer);
				StringTokenizer tokens_lf = new StringTokenizer(strBuffer, "\n");
				while(tokens_lf.hasMoreTokens()) {
					String oneLine = tokens_lf.nextToken();
					StringTokenizer tokens_slash = new StringTokenizer(oneLine, "/");
					AccountChunk newChunk = new AccountChunk();
					newChunk.strContent = tokens_slash.nextToken();
					newChunk.strExpense = tokens_slash.nextToken();
					newChunk.strDate = tokens_slash.nextToken();
					newChunk.strTimeByMilliSec = tokens_slash.nextToken();
					if(mAccountChunkList == null) {
						mAccountChunkList = new ArrayList<AccountChunk>();
					}
					mAccountChunkList.add(newChunk);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}*/

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
}
