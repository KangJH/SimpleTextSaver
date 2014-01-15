package home.text.simpletextsaver;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener {
	private EditText mContent = null;
	private EditText mExpense = null;
	private String mExpenseText = new String();
	private final String won_mark = UnitoStr("20A9");
	private AccountDataController mAccountDataController = null;
	private final int RESULT_CODE = 0xDECA;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContent = (EditText) findViewById(R.id.content_input);
		mExpense = (EditText) findViewById(R.id.price_input);
		mExpense.addTextChangedListener( new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(s.length() > 0) {
					if(!s.toString().equals(mExpenseText)) {
						mExpense.removeTextChangedListener(this);
						String formated = NumberFormat.getCurrencyInstance().format(getNumbericFromCurrency(s.toString()));
						mExpenseText = formated;
						mExpense.setText(formated);
						mExpense.setSelection(formated.length());
	
						mExpense.addTextChangedListener(this);
					}
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		findViewById(R.id.send_btn).setOnClickListener(this);
		findViewById(R.id.show_btn).setOnClickListener(this);
		showKeyboard();

		mAccountDataController = AccountDataController.getInstance();
		mAccountDataController.load();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	protected void onActivityResult (int requestCode, int resultCode, Intent data) {
		if(requestCode == RESULT_CODE) {
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.send_btn:
			String content = mContent.getText().toString();
			long price = getNumbericFromCurrency(mExpenseText);
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			if(content != null && content.length() > 0 && price > 0) {
				AccountChunk obj = new AccountChunk();
				obj.strContent = content;
				obj.strExpense = Long.toString(price);
				obj.strDate = sdf.format(date);
				obj.strTimeByMilliSec = Long.toString(calendar.getTimeInMillis());
				mAccountDataController.insertData(obj);
				mContent.setText("");
				mExpense.setText("");
				hideKeyboard();
				mContent.requestFocus();
			} else {
				 new AlertDialog.Builder(this).setTitle("Warning")
	                .setMessage("Field is not complete.")
	                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							//dismiss();
						}
					}).show();
			}
			break;
		case R.id.show_btn:
			Intent intent = new Intent(this, ListViewActivity.class);
			//intent.putExtra(getResources().getString(R.string.intent_share_data), mAccountDataController);
			startActivityForResult(intent, RESULT_CODE);
			break;
		default:
			break;
		}
	}
	
	private String UnitoStr(String uni)
	{
		String str = "" ;

		StringTokenizer str1 = new StringTokenizer(uni,"\\u") ;

		while(str1.hasMoreTokens())
		{
			String str2 = str1.nextToken() ;
			int i = Integer.parseInt(str2,16) ;
			str += (char)i ;
		}
		return str ;
	}
	
	private long getNumbericFromCurrency(String input) {
		long ret = 0;
		if(input != null && input.length() > 0) {
			String cleanString = input.replaceAll("["+won_mark+",]", "");
			ret = Long.parseLong(cleanString);
		}
		return ret;
	}

	
	
	private void showKeyboard() {
		/*InputMethodManager imm = (InputMethodManager)
				getSystemService(Context.INPUT_METHOD_SERVICE);
		if(imm != null){
			imm.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
		}*/

		mContent.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(mContent, 0); 
            }
        },200);
	}
	
	private void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager)
				getSystemService(Context.INPUT_METHOD_SERVICE);
		if(imm != null){
			//imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
			imm.hideSoftInputFromWindow(mExpense.getWindowToken(), 0);
		}
		//getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
}
