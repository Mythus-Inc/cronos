package util;

import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

public class MensagemProvider extends ResourceBundle {

	private static MensagemProvider instance;

	private MensagemProvider() {

	}

	public static MensagemProvider getInstance() {
		if (instance == null) {
			instance = new MensagemProvider();
			instance.load(Locale.getDefault());
		}
		return instance;
	}

	public void load(Locale locale) {
		setParent(getBundle("messages_pt_BR"));
	}

	public String getMensagem(String key, Object... objects) {
		return String.format(getString(key), objects);
	}

	@Override
	public Enumeration<String> getKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Object handleGetObject(String key) {
		// TODO Auto-generated method stub
		return null;
	}
}
