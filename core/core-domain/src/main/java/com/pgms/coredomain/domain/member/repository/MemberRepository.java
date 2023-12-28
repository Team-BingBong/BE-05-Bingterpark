package com.pgms.coredomain.domain.member.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pgms.coredomain.domain.member.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Slice<Member> findSliceBy(Pageable pageable);

	boolean existsByRoleId(Long roleId);
}
