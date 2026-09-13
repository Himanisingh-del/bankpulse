package com.bankpulse.bankpulse.dto;

import com.bankpulse.bankpulse.entity.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    TransactionResponse toResponse(Transaction transaction);
}