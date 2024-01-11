package com.pgms.coredomain.domain.booking;

import lombok.Getter;

@Getter
public enum CardIssuer {
	IBK_BC("기업 BC", "3K"),
	GWANGJUBANK("광주은행", "46"),
	LOTTE("롯데카드", "71"),
	KDBBANK("KDB산업은행", "30"),
	BC("BC카드", "31"),
	SAMSUNG("삼성카드", "51"),
	SAEMAUL("새마을금고", "38"),
	SHINHAN("신한카드", "41"),
	SHINHYEOP("신협", "62"),
	CITI("씨티카드", "36"),
	WOORI_BC("우리BC카드(BC 매입)", "33"),
	WOORI("우리카드(우리 매입)", "W1"),
	POST("우체국예금보험", "37"),
	SAVINGBANK("저축은행중앙회", "39"),
	JEONBUKBANK("전북은행", "35"),
	JEJUBANK("제주은행", "42"),
	KAKAOBANK("카카오뱅크", "15"),
	KBANK("케이뱅크", "3A"),
	TOSSBANK("토스뱅크", "24"),
	HANA("하나카드", "21"),
	HYUNDAI("현대카드", "61"),
	KOOKMIN("KB국민카드", "11"),
	NONGHYEOP("NH농협카드", "91"),
	SUHYEOP("Sh수협은행", "34"),
	DINERS("다이너스 클럽", "6D"),
	MASTER("마스터카드", "4M"),
	UNIONPAY("유니온페이", "3C"),
	AMEX("아메리칸 익스프레스", "7A"),
	JCB("JCB", "4J"),
	VISA("VISA", "4V");

	private final String name;
	private final String officialCode;

	CardIssuer(String name, String officialCode) {
		this.name = name;
		this.officialCode = officialCode;
	}

	public static CardIssuer fromOfficialCode(String officialCode) {
		for (CardIssuer cardIssuer : CardIssuer.values()) {
			if (cardIssuer.officialCode.equals(officialCode)) {
				return cardIssuer;
			}
		}
		throw new IllegalArgumentException("다음 카드 발급사를 찾을 수 없습니다 : " + officialCode);
	}
}

