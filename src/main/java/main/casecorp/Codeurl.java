package main.casecorp;

import util.ConstantValue;

public class Codeurl {

	public String chkUrl() {
		
		String serviceKey = ConstantValue.DART_SERVICE_KEY;
		String addr = "https://opendart.fss.or.kr/api/corpCode.xml?"
					+ "crtfc_key="+serviceKey;
		
		return addr;
	}
}
