package org.willroberts.android.petphone;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
//import android.widget.Toast;

public class FileChooser extends ListActivity {
    /** Called when the activity is first created. */
	private File currentDir;
	private FileArrayAdapter adapter;
	boolean hasPetPhoneDir = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/* 
 * TODO Should check if sdcard exists before using it        
 */
        currentDir = new File("/sdcard/");
        fill(currentDir);
    }
/**
** The fill method is going to work like this:
** Were going to get an array of all the files and dirs in the current directory.
** Were going to create 2 ListArrays. One for folders and one for files.
** Were going to sort files and dirs into the appropriate ListArray.
** Were going to sort the ListArrays alphabetically and pass to one ListArray.
** Were going to pass this ListArray to our custom ArrayAdapter
 * 
 */
    private void fill(File f)
    {
        File[]dirs = f.listFiles();
         this.setTitle("Current Dir: "+f.getName());
         List<Option>dir = new ArrayList<Option>();
         List<Option>fls = new ArrayList<Option>();
         adapter = new FileArrayAdapter(FileChooser.this,R.layout.file_view,dir);
 		 this.setListAdapter(adapter);
         try{
             for(File ff: dirs)
             {
                if(ff.isDirectory()){
                    dir.add(new Option(ff.getName(),"Folder",ff.getAbsolutePath()));
                    /****************
                     * ************** If the user has a folder called petphone, 
                     * ************** make it the current dir.
                     */
                     if(ff.getName().equalsIgnoreCase("petphone") && !hasPetPhoneDir){
//                         Toast.makeText(this,"Folder: "+ff.getName()+"\n"+ff.getAbsolutePath(),5000).show();
                    	 hasPetPhoneDir = true;
                         currentDir = new File(ff.getAbsolutePath());
                         fill(currentDir);
                      }
                }
                else
                {
                    fls.add(new Option(ff.getName(),"="+ff.length(),ff.getAbsolutePath()));
                }
             }
         }catch(Exception e)
         {
             
         }
         Collections.sort(dir);
         Collections.sort(fls);
         dir.addAll(fls);
         if(!f.getName().equalsIgnoreCase("sdcard"))
             dir.add(0,new Option("..","Parent Directory",f.getParent()));
    }
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Option o = adapter.getItem(position);
		if(o.getData().equalsIgnoreCase("folder")||o.getData().equalsIgnoreCase("parent directory")){
				currentDir = new File(o.getPath());
				fill(currentDir);
		}else
		{
			onFileClick(o);
		}
	}// onListItemClick
    private void onFileClick(Option o)
    {
    	
    	Intent data = new Intent();
    	data.putExtra("mypet", o.getPath());
    	data.putExtra("retName", o.getName());
    	data.putExtra("retData", o.getData());
		setResult(RESULT_OK, data);
		super.finish();
    }

}

