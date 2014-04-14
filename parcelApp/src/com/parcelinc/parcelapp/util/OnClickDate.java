package com.parcelinc.parcelapp.util;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.TextView;

import com.parcelinc.parcelapp.db.DateUtil;

public class OnClickDate implements OnClickListener, OnDateSetListener {

	private Context contexto;
	
	private TextView txt;

	public OnClickDate(Context contexto, TextView txt) {
		this.contexto = contexto;
		this.txt = txt;
		this.txt.setOnClickListener(this);
		setCalendar(Calendar.getInstance());
	}

	@Override
	public void onClick(View v) {
		Calendar cal = getCalendar();

		DatePickerDialog dialogo = new DatePickerDialog(contexto, this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

		dialogo.show();
	}

	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {
		setDate(year, month, day);
	}
	
	public Calendar getCalendar() {
		return (Calendar) txt.getTag();
	}
	
	public void setCalendar(Calendar cal) {
		txt.setTag(cal);
		refreshField(cal);
	}
	
	public void setDate(int year, int monthOfYear, int dayOfMonth) {
		Calendar cal = getCalendar();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, monthOfYear);
		cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		refreshField(cal);
	}
	
	private void refreshField(Calendar cal) {
		txt.setText(DateUtil.toString(cal));
	}

}
