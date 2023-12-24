package com.pgms.coredomain.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pgms.coredomain.domain.member.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
