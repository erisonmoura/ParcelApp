package com.parcelinc.parcelapp.util;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

public class Util {

	public static void setTextUnderline(TextView txt, String conteudo){
		SpannableString texto = new SpannableString(conteudo);
		texto.setSpan(new UnderlineSpan(), 0, texto.length(), 0);			
		txt.setText(texto);
	}
	
}
