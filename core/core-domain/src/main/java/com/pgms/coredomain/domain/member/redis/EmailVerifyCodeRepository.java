package com.pgms.coredomain.domain.member.redis;

import org.springframework.data.repository.CrudRepository;

public interface EmailVerifyCodeRepository extends CrudRepository<EmailVerifyCode, String> {
}
