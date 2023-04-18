package action;

import java.text.NumberFormat;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import bean.Resultbean;

public class Ajaxaction implements Action {

	NumberFormat numberFormat = NumberFormat.getInstance();
	
	@Override
	public Actionforward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		
		ArrayList<Resultbean> arr = (ArrayList<Resultbean>)session.getAttribute("result");
		int begin = Integer.parseInt(request.getParameter("begin"));
		int end = Integer.parseInt(request.getParameter("end"));
		
		JSONArray jArr = new JSONArray();
		
		for(int i = begin; i < end; i++) {
			
			JSONObject subJson = new JSONObject();
			subJson.put("기준일자",arr.get(i).getBaseYmd());
			subJson.put("기업명",arr.get(i).getCorpName());
			subJson.put("주식코드",arr.get(i).getStockCode());
			subJson.put("종가",formatting(arr.get(i).getClpr()));
			subJson.put("전일대비등락",formatting(arr.get(i).getVs()));
			subJson.put("등락비율",arr.get(i).getFltRate());
			subJson.put("시가",formatting(arr.get(i).getMkp()));
			subJson.put("고가",formatting(arr.get(i).getHipr()));
			subJson.put("저가",formatting(arr.get(i).getLopr()));
			subJson.put("거래량",formatting(arr.get(i).getTrqu()));
			subJson.put("거래대금",formatting(arr.get(i).getTrprc()));
			subJson.put("당기순이익",formatting(arr.get(i).getThsIncome()));
			subJson.put("전기순이익",formatting(arr.get(i).getFrmIncome()));
			subJson.put("전전기순이익",formatting(arr.get(i).getBfefrmIncome()));
			subJson.put("당기자산총계",formatting(arr.get(i).getThsAssets()));
			subJson.put("전기자산총계",formatting(arr.get(i).getFrmAssets()));
			subJson.put("전전기자산총계",formatting(arr.get(i).getBfefrmAssets()));
			subJson.put("당기부채총계",formatting(arr.get(i).getThsDebt()));
			subJson.put("전기부채총계",formatting(arr.get(i).getFrmDebt()));
			subJson.put("전전기부채총계",formatting(arr.get(i).getBfefrmDebt()));
			subJson.put("당기자본총계",formatting(arr.get(i).getThsTotCap()));
			subJson.put("전기자본총계",formatting(arr.get(i).getFrmTotCap()));
			subJson.put("전전기자본총계",formatting(arr.get(i).getBfefrmTotCap()));
			subJson.put("상장주식수",formatting(arr.get(i).getLstGstCnt()));
			subJson.put("시가총액",formatting(arr.get(i).getMktTotAmt()));
			subJson.put("EPS",formatting(arr.get(i).getEps()));
			subJson.put("PER",formatting(arr.get(i).getPer()));
			subJson.put("BPS",formatting(arr.get(i).getBps()));
			subJson.put("PBR",formatting(arr.get(i).getPbr()));
			jArr.add(subJson);
		}

		response.setContentType("text/html; charset=UTF-8");
		response.getWriter().print(jArr.toString());
		
		return null;
	}
	
	private String formatting(Long num) {
		
		return numberFormat.format(num);
	}
}
