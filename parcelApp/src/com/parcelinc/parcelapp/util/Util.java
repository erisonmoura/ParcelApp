package com.parcelinc.parcelapp.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

import com.parcelinc.parcelapp.db.DateUtil;
import com.parcelinc.parcelapp.db.UsuarioDataBase;
import com.parcelinc.parcelapp.pojo.Conta;
import com.parcelinc.parcelapp.pojo.Usuario;

public class Util {

	public static final long SERIAL_VERSION_UID = 1L;
	
	public static void setTextUnderline(TextView txt, String conteudo){
		SpannableString texto = new SpannableString(conteudo);
		texto.setSpan(new UnderlineSpan(), 0, texto.length(), 0);			
		txt.setText(texto);
	}
	
	public static String prepareFiltro(String filtroMes) {
		return "%" + filtroMes.trim() + DateUtil.SEPARATOR + "%";
	}

	public static String doubleToString(double valor) {
		String vl = String.format("%.3f", valor); 
		return vl.replace(',', '.');
	}

	public static double stringToDouble(String valor) {
		// TODO Definir e utilizar um DecimalFormat adequado
		return Double.parseDouble(valor);
	}
	
	private static UsuarioDataBase getUsuarioDB(Context contexto) {
		return UsuarioDataBase.getInstance(contexto);
	}
	
	public static List<Usuario> consultarUsuariosValidos(Conta conta, Context contexto) {
		List<Usuario> usuariosValidos = new ArrayList<Usuario>();
		for (Usuario usuario : getUsuarioDB(contexto).getList()) {
			for(Long idUsuario : conta.getIdsUsuario()) {
				if (usuario.getId().equals(idUsuario)) {
					usuariosValidos.add(usuario);
				}
			}
		}
		return usuariosValidos;
	}

}
