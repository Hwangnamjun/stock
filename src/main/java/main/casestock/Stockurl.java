package main.casestock;

import bean.Infobean;
import util.ConstantValue;

public class Stockurl {

	public String chkUrl(Infobean bean) {
		
		String serviceKey = ConstantValue.GOV_SERVICE_KEY;
		String pageRow = bean.getPageRow();
		String pageNo = bean.getPageNo();
		String startDate = bean.getStartDT();
		String endDate = bean.getEndDT();
		String stockCode = bean.getStockCode();

		String addr = "https://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService/getStockPriceInfo?"
					+ "serviceKey="+serviceKey
					+ "&numOfRows="+pageRow
					+ "&pageNo="+pageNo
					+ "&resultType=json"
					+ "&beginBasDt="+startDate
					+ "&endBasDt="+endDate
					+ "&likeSrtnCd="+stockCode
					;
		
		return addr;
	}
}
