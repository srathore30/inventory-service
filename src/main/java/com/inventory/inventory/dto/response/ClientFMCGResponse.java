package com.inventory.inventory.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ClientFMCGResponse {
    private Long id;
    private String clientCode;
    private String clientFirstName;
    private String clientLastName;
    private String password;
    private String email;
    private Long mobile;
    private String address;
    private Long region;
    private Long memberId;
    private Long state;
    private Long city;
    private Double topUpBalance;
}
