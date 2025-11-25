package com.example.userapi.port;

import com.example.userapi.application.response.PostalCodeAddressResponse;

public interface PostalCodeAddressPort {
    public PostalCodeAddressResponse fetchAddressByPostalCode(String postalCode);

}
