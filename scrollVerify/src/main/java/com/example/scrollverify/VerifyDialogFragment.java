package com.example.scrollverify;

import com.example.scrollverify.ScrollVerifyView.OnVerifyListener;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

public class VerifyDialogFragment extends DialogFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = inflater.inflate(R.layout.dialog_fragment_verify, null);
		ScrollVerifyView verifyView = (ScrollVerifyView) view.findViewById(R.id.verify);
		verifyView.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.image));
		verifyView.setOnVerifyListener(new OnVerifyListener() {

			@Override
			public void success() {
				// TODO Auto-generated method stub
				getDialog().dismiss();
			}

			@Override
			public void fail() {
				// TODO Auto-generated method stub
				Toast.makeText(getContext(), "—È÷§ ß∞‹£¨«Î÷ÿ ‘", Toast.LENGTH_SHORT).show();
			}
		});
		return view;
	}
}
