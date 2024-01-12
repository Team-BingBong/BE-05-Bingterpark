package com.pgms.coredomain.domain.member.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pgms.coredomain.domain.member.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	boolean existsByEmail(String email);

	Slice<Member> findSliceBy(Pageable pageable);

	Optional<Member> findByEmail(String email);

	@Query("select m from Member m where m.email = :email and m.status = 'DELETED'")
	Optional<Member> findByEmailAndIsDeletedTrue(@Param("email") String email);

	@Query("select m from Member m where m.id = :id and m.status != 'DELETED'")
	Optional<Member> findByIdAndIsDeletedFalse(@Param("id") Long id);
}
