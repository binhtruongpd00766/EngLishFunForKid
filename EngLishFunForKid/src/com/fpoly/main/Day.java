package com.fpoly.main;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.fpoly.adapter.ListAdapter;
import com.fpoly.db.Mydatabase;
import com.fpoly.englishfunforkid.R;
import com.fpoly.object.English;

@SuppressLint("NewApi")
public class Day extends Fragment {
	Activity root;
	Mydatabase mydb;
	ImageView IvPreviousDay, IvList, IvRepeatDay, IvNextDay, IvDay, IvAudioDay,
			IvTextOnOffDay;
	TextView textOnOffDay;
	int stt = 64;
	private SQLiteDatabase database;
	ArrayList<English> list;
	Context context;
	MediaPlayer mp;
	ArrayList<English>listNew;
	AlertDialog listDialog;
	ListAdapter apdater;

	public Day() {
	}

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		root = getActivity();

		View rootView = inflater.inflate(R.layout.screen_day, container, false);
		mydb = new Mydatabase(root.getApplicationContext());

		list = mydb.GetAllInEnglish();
		Log.d("------raa", list.size() + "");
			init(rootView);
			
		return rootView;
	}
	public void init(View rootView) {
		IvPreviousDay = (ImageView) rootView.findViewById(R.id.IvPreviousDay);
		IvList = (ImageView) rootView.findViewById(R.id.IvList);
		IvRepeatDay = (ImageView) rootView.findViewById(R.id.IvRepeatDay);
		IvNextDay = (ImageView) rootView.findViewById(R.id.IvNextDay);
		IvDay = (ImageView) rootView.findViewById(R.id.IvDay);
		textOnOffDay = (TextView) rootView.findViewById(R.id.textOnOffDay);
		IvAudioDay = (ImageView) rootView.findViewById(R.id.IvAudioDay);
		IvTextOnOffDay = (ImageView) rootView.findViewById(R.id.IvTextOnOffDay);
		showScreenDay(64);
		IvNextDay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				stt++;
				if (stt == 71)

				{
					stt = 64;
				}
				showScreenDay(stt);
			}
		});
		

		IvPreviousDay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				stt--;
				if (stt == 63) {
					stt = 70;

				} // }
				showScreenDay(stt);
				

			}
		});
		IvAudioDay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				showScreenDay(stt);

			}
		});
		IvRepeatDay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				playAudio(list.get(stt - 1).getAudio());
			}
		});
		
		IvTextOnOffDay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				hide();

			}
		});
		// show list view
				IvList.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						AlertDialog.Builder builder = new AlertDialog.Builder(root);
						mydb = new Mydatabase(root);
						listNew = new ArrayList<English>();
						for (int i = 63; i < 70; i++) {

							listNew.add(list.get(i));

						}
						ListView listTail = new ListView(root);
						listTail.setAdapter(new ListAdapter(root
								.getApplicationContext(), listNew));
						listTail.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> arg0, View arg1,
									int position, long arg3) {

								if (listDialog.isShowing()) {
									listDialog.dismiss();

								}

								showScreenDay(position + 64);

							}
						});
						builder.setView(listTail);
						listDialog = builder.create();
						listDialog.show();

					}
				});
	}
	//
	boolean kt = true;

	
	public void hide() {
		if (kt == true) {
			textOnOffDay.setText("");
			kt = false;
		} else {
			textOnOffDay.setText(list.get(stt - 1).getDecription());
			
			kt = true;

		}
	}

	// show image
	public void loadDataFromAssetColor(String nameImageColor) {
		AssetManager assetManager = getActivity().getAssets();
		// load image
		try {
			// get input stream
			InputStream ims = assetManager.open("images/" + nameImageColor  + ".jpg");
			Bitmap bitmap = BitmapFactory.decodeStream(ims);
			
			// set image to ImageView
			IvDay.setImageBitmap(bitmap);
		} catch (IOException ex) {
			return;
		}

	}

	// show screen
	public void showScreenDay(int stt) {
		English english = new English();
		english = list.get(stt -1 );
		textOnOffDay.setText(english.getDecription());
		playAudio(english.getAudio());
		loadDataFromAssetColor(english.getImage());
		Log.d("--------",english.getImage());

	}

	// Play Audio
	public void playAudio(String nameAudio) {
		AssetManager assetManager = getActivity().getAssets();
		mp = new MediaPlayer();
		try {
			AssetFileDescriptor descriptor = assetManager.openFd("audio/"
					+ nameAudio + ".mp3");
			mp.setDataSource(descriptor.getFileDescriptor(),
					descriptor.getStartOffset(), descriptor.getLength());
			descriptor.close();
			mp.prepare();

			mp.start();
			mp.setVolume(100, 100);

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Stop Audio
	public void stop() {
		mp.stop();
	}

}
