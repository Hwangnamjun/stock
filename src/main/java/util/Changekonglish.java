package util;

import java.util.HashMap;
import java.util.Map;

public class Changekonglish {

	public String changeWord(String word) {
		
		Map<Character,String> matchingWord = new HashMap<Character,String>();
		
		matchingWord.put('A', "에이");
		matchingWord.put('B', "비");
		matchingWord.put('C', "씨");
		matchingWord.put('D', "디");
		matchingWord.put('E', "이");
		matchingWord.put('F', "에프");
		matchingWord.put('G', "지");
		matchingWord.put('H', "에이치");
		matchingWord.put('I', "아이");
		matchingWord.put('J', "제이");
		matchingWord.put('K', "케이");
		matchingWord.put('L', "엘");
		matchingWord.put('M', "엠");
		matchingWord.put('N', "앤");
		matchingWord.put('O', "오");
		matchingWord.put('P', "피");
		matchingWord.put('Q', "큐");
		matchingWord.put('R', "알");
		matchingWord.put('S', "에스");
		matchingWord.put('T', "티");
		matchingWord.put('U', "유");
		matchingWord.put('V', "브이");
		matchingWord.put('W', "더블유");
		matchingWord.put('X', "엑스");
		matchingWord.put('Y', "와이");
		matchingWord.put('Z', "지");
		
		
		char[] changing = word.toCharArray();
		StringBuffer result = new StringBuffer();
		
		for(int i = 0; i < changing.length; i++) {
			if(matchingWord.containsKey(changing[i])) {
				result.append(matchingWord.get(changing[i]));
			} else {
				result.append(changing[i]);
			}
		}
		return result.toString();
	}
}
