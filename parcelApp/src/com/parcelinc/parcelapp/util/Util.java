package com.parcelinc.parcelapp.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
	
	private static final Locale local;
	private static final DecimalFormatSymbols symbols;
	private static final DecimalFormat decimal;
	
	static {
		local = new Locale("pt", "BR");
		symbols = new DecimalFormatSymbols(local);
		decimal = new DecimalFormat();
		decimal.setDecimalFormatSymbols(symbols);

		decimal.setGroupingUsed(false);
		decimal.setMinimumFractionDigits(2);
		decimal.setDecimalSeparatorAlwaysShown(false);
	}
	
	public static void setTextUnderline(TextView txt, String conteudo){
		SpannableString texto = new SpannableString(conteudo);
		texto.setSpan(new UnderlineSpan(), 0, texto.length(), 0);			
		txt.setText(texto);
	}
	
	public static String prepareFiltro(String filtroMes) {
		return "%" + filtroMes.trim() + DateUtil.SEPARATOR + "%";
	}

	public static String doubleToString(double valor) {
		return decimal.format(valor);
	}

	public static double stringToDouble(String valor) throws ParseException {
		String vl = valor.replace('.', ',');
		return decimal.parse(vl).doubleValue();
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
