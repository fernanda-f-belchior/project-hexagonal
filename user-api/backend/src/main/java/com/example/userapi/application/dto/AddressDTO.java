package com.example.userapi.application.dto;


import lombok.*;

@Getter
@Setter
@ToString(exclude = "userDTO")
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {

    Long id;
    String postalCode;
    Long number;
    String street;
    String neighborhood;
    String city;
    String complement;
    Long userId;

}


