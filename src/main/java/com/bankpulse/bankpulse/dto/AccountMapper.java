package com.bankpulse.bankpulse.dto;

import com.bankpulse.bankpulse.entity.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountResponse toResponse(Account account);
}