package com.example.nghia.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.effect.Effect;
import android.media.effect.EffectContext;
import android.media.effect.EffectFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nghia.adapter.FilterAdapterFactory;
import com.example.nghia.imageediter.Edittor;
import com.example.nghia.imageediter.R;
import com.example.nghia.utttilities.GLToolbox;
import com.example.nghia.utttilities.RecyclerItemClickListener;
import com.example.nghia.utttilities.TextureRenderer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class EffectsFilterFragment extends android.app.Fragment{
	public interface Send{
		public void sendata(int x);
	}

	Send send;
	private RecyclerView recList;

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.imf_effect_factory, container, false);
		LinearLayoutManager layoutManager
				= new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

		recList = (RecyclerView) rootView.findViewById(R.id.rc_filter);
		recList.setHasFixedSize(true);
		recList.setLayoutManager(layoutManager);

		FilterAdapterFactory filterAdapter = new FilterAdapterFactory(getActivity());
		recList.setAdapter(filterAdapter);

		recList.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
				new RecyclerItemClickListener.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				Log.d("MY","Chay vao chon Item Effect");
				Log.d("MY",String.valueOf(position));
				send.sendata(position);

			}
		}));
		Log.d("MY","Chay vao tao Fragment");

		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		send= (Send) activity;
	}
}
