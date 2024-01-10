package com.pgms.apievent.factory.member;

import com.pgms.coredomain.domain.member.Member;
import com.pgms.coredomain.domain.member.enums.Gender;

public class MemberFactory {
	public static Member createMember() {
		return Member.builder()
			.email("test@naver.com")
			.password("encodedPassword")
			.name("tester")
			.phoneNumber("01011112222")
			.birthDate("19990926")
			.gender(Gender.MALE)
			.streetAddress("서울특별시 송파구 올림픽로 300")
			.detailAddress("롯데월드타워앤드롯데월드몰")
			.zipCode("05551")
			.build();
	}
}
