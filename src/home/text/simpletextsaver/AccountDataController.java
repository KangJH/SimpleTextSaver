package home.text.simpletextsaver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.jar.Pack200.Packer;

import android.os.Environment;
import android.util.Log;

public class AccountDataController extends Object implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<AccountChunk> mAccountChunks = null;
	private File					mShareFile = null;
	private final String SHARE_FOLDER_NAME = Environment.
            getExternalStorageDirectory()+"AccountBook";
	private final String SHARE_FILE_NAME = "my_accout.txt";
	private final String TAG = "AccountDataController";
	private static AccountDataController mMyInstance = null; 
	static public AccountDataController getInstance() {
		if(mMyInstance == null) {
			mMyInstance = new AccountDataController();
		}
		return mMyInstance;
	}
	private AccountDataController() {
		mAccountChunks = new ArrayList<AccountChunk>();
		File dir = makeDirectory(SHARE_FOLDER_NAME);
		mShareFile = makeFile(dir, (SHARE_FOLDER_NAME + SHARE_FILE_NAME));
	}
	
	public ArrayList<AccountChunk> getAccountChunks() {
		return mAccountChunks;
	}
	
	public void updateData(AccountChunk data, boolean syncToFile) {
		for(AccountChunk obj : mAccountChunks) {
			if(obj.strTimeByMilliSec.equals(data.strTimeByMilliSec)) {
				obj.copy(data);
				break;
			}
		}
		if(syncToFile) {
			save();
		}
	}
	
	public void updateData(ArrayList<AccountChunk> updatingData_array, boolean syncToFile) {
		for(AccountChunk data : updatingData_array) {
			for(AccountChunk obj : mAccountChunks) {
				if(obj.strTimeByMilliSec.equals(data.strTimeByMilliSec)) {
					obj.copy(data);
					break;
				}
			}
		}
		if(syncToFile) {
			save();
		}
	}

	public void insertData(AccountChunk data) {
		if(mAccountChunks != null) {
			mAccountChunks.add(data);
		}
		save();
	}
	
	public void insertData(ArrayList<AccountChunk> insertingData_array) {
		if(mAccountChunks != null) {
			mAccountChunks.addAll(insertingData_array);
		}
		save();
	}
	
	public void removeData(AccountChunk data) {
		Iterator<AccountChunk> it = mAccountChunks.iterator();
		while(it.hasNext()){
			AccountChunk obj = it.next();
			if(obj.strTimeByMilliSec.equals(data.strTimeByMilliSec)) {
				it.remove();
			}
		}
		save();
	}
	
	public void removeData(ArrayList<AccountChunk> removalData_array) {
		for(AccountChunk data : removalData_array) {
			Iterator<AccountChunk> it = mAccountChunks.iterator();
			while(it.hasNext()){
				AccountChunk obj = it.next();
				if(obj.strTimeByMilliSec.equals(data.strTimeByMilliSec)) {
					it.remove();
				}
			}
		}
		save();
	}
	
	public boolean save() {
		boolean ret = false;
		if(mAccountChunks != null) {
			if(mAccountChunks.size() > 0) {
				if(!isFileExist(mShareFile)) {
					File dir = makeDirectory(SHARE_FOLDER_NAME);
					mShareFile = makeFile(dir, (SHARE_FOLDER_NAME + SHARE_FILE_NAME));
				}
				String buffer = new String();
				for(AccountChunk obj : mAccountChunks) {
					String oneLine = obj.strContent + "/" + obj.strExpense + "/" + obj.strDate + "/" + obj.strTimeByMilliSec + "\n";
					buffer += oneLine;
				}
				ret = writeFile(mShareFile, buffer.getBytes());
			} else {
				ret = deleteFile(mShareFile);
			}
		}
		return ret;
	}
	
	public void load() {
		if(mAccountChunks != null) {
			byte[] buffer = readFile(mShareFile);
			if(buffer != null) {
				String strBuffer = new String(buffer);
				StringTokenizer tokens_lf = new StringTokenizer(strBuffer, "\n");
				while(tokens_lf.hasMoreTokens()) {
					String oneLine = tokens_lf.nextToken();
					StringTokenizer tokens_slash = new StringTokenizer(oneLine, "/");
					AccountChunk newChunk = new AccountChunk();
					newChunk.strContent = tokens_slash.nextToken();
					newChunk.strExpense = tokens_slash.nextToken();
					newChunk.strDate = tokens_slash.nextToken();
					newChunk.strTimeByMilliSec = tokens_slash.nextToken();
					mAccountChunks.add(newChunk);
				}
			}
		}
		
	}
	
	/**
     * 디렉토리 생성 
     * @return dir
     */
    private File makeDirectory(String dir_path){
        File dir = new File(dir_path);
        if (!dir.exists())
        {
            dir.mkdirs();
            Log.i( TAG , "!dir.exists" );
        }else{
            Log.i( TAG , "dir.exists" );
        }
 
        return dir;
    }
 
    /**
     * 파일 생성
     * @param dir
     * @return file 
     */
    private File makeFile(File dir , String file_path){
        File file = null;
        boolean isSuccess = false;
        if(dir.isDirectory()){
            file = new File(file_path);
            if(file!=null&&!file.exists()){
                Log.i( TAG , "!file.exists" );
                try {
                    isSuccess = file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally{
                    Log.i(TAG, "파일생성 여부 = " + isSuccess);
                }
            }else{
                Log.i( TAG , "file.exists" );
            }
        }
        return file;
    }
 
    /**
     * (dir/file) 절대 경로 얻어오기
     * @param file
     * @return String
     */
    private String getAbsolutePath(File file){
        return ""+file.getAbsolutePath();
    }
 
    /**
     * (dir/file) 삭제 하기
     * @param file
     */
    private boolean deleteFile(File file){
        boolean result;
        if(file!=null&&file.exists()){
            file.delete();
            result = true;
        }else{
            result = false;
        }
        return result;
    }
 
    /**
     * 파일여부 체크 하기
     * @param file
     * @return
     */
    private boolean isFile(File file){
        boolean result;
        if(file!=null&&file.exists()&&file.isFile()){
            result=true;
        }else{
            result=false;
        }
        return result;
    }
 
    /**
     * 디렉토리 여부 체크 하기
     * @param dir
     * @return
     */
    private boolean isDirectory(File dir){
        boolean result;
        if(dir!=null&&dir.isDirectory()){
            result=true;
        }else{
            result=false;
        }
        return result;
    }
 
    /**
     * 파일 존재 여부 확인 하기
     * @param file
     * @return
     */
    private boolean isFileExist(File file){
        boolean result;
        if(file!=null&&file.exists()){
            result=true;
        }else{
            result=false;
        }
        return result;
    }
     
    /**
     * 파일 이름 바꾸기
     * @param file
     */
    private boolean reNameFile(File file , File new_name){
        boolean result;
        if(file!=null&&file.exists()&&file.renameTo(new_name)){
            result=true;
        }else{
            result=false;
        }
        return result;
    }
     
    /**
     * 디렉토리에 안에 내용을 보여 준다.
     * @param file
     * @return
     */
    private String[] getList(File dir){
        if(dir!=null&&dir.exists())
            return dir.list();
        return null;
    }
 
    /**
     * 파일에 내용 쓰기
     * @param file
     * @param file_content
     * @return
     */
    private boolean writeFile(File file , byte[] file_content){
        boolean result;
        FileOutputStream fos;
        if(file!=null&&file.exists()&&file_content!=null){
            try {
                fos = new FileOutputStream(file);
                try {
                    fos.write(file_content);
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            result = true;
        }else{
            result = false;
        }
        return result;
    }
 
    /**
     * 파일 읽어 오기 
     * @param file
     */
    private byte[] readFile(File file){
    	byte[] ret = null;
        int readcount=0;
        if(file!=null&&file.exists()){
            try {
                FileInputStream fis = new FileInputStream(file);
                readcount = (int)file.length();
                byte[] buffer = new byte[readcount];
                fis.read(buffer);
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ret;
    }
     
    /**
     * 파일 복사
     * @param file
     * @param save_file
     * @return
     */
    private boolean copyFile(File file , String save_file){
        boolean result;
        if(file!=null&&file.exists()){
            try {
                FileInputStream fis = new FileInputStream(file);
                FileOutputStream newfos = new FileOutputStream(save_file);
                int readcount=0;
                byte[] buffer = new byte[1024];
                while((readcount = fis.read(buffer,0,1024))!= -1){
                    newfos.write(buffer,0,readcount);
                }
                newfos.close();
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            result = true;
        }else{
            result = false;
        }
        return result;
    }
	
	
	
	
	
	/*public boolean saveToFile() {
		boolean ret = false;
		if(mAccountChunks != null) {
			ret = true;
			removeFile(getShareFile());
			for(AccountChunk obj : mAccountChunks) {
				if(!saveToFile(obj.strContent, Long.parseLong(obj.strExpense), obj.strDate, Long.parseLong(obj.strTimeByMilliSec))){
					ret = false;
					break;
				}
			}
		}
		return ret;
	}
	
	public void loadFromFile() {
		loadAccountChunkFromFile(getShareFile().getAbsolutePath());
	}
	
	public boolean insertData(AccountChunk data) {
		boolean ret = false;
		return ret;
	}
	
	public boolean removeData(String id) {
		boolean ret = false;
		return ret;
	}
	
	private boolean removeFile(File file) {
		boolean ret = false;
		if(file != null) {
			ret = file.delete();
		}
		return ret;
	}
	
	private boolean saveToFile(String content, long price, String date, long id) {
		boolean ret = false;
		if(content != null && content.length() > 0 && price > 0) {
			String buffer = content + "/" + price + "/" + date + "/" + id + "\n";
			File file = getShareFile();
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(file, true);
				fos.write(buffer.getBytes());
				fos.close();
				ret = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}
	
	private void loadAccountChunkFromFile(String path) {
		if(mAccountChunks != null) {
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
						mAccountChunks.add(newChunk);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private File getShareFile() {
		File file = null;
		if(mContext != null) {
	        String filepath = Environment.getExternalStorageDirectory().getPath();
	        File folder = new File(filepath, SHARE_FOLDER_NAME);
	        if (!folder.exists()) {
	        	folder.mkdirs();
	        }//System.currentTimeMillis()
	        file = new File(folder.getAbsolutePath(), SHARE_FILE_NAME);
		}
        return file;
    }*/
	
	
	
	
	

}
