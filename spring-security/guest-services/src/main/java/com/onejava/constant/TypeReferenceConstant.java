package com.onejava.constant;

import com.fasterxml.jackson.core.type.TypeReference;
import com.onejava.entity.Guest;

import java.util.List;

public class TypeReferenceConstant {

    public static final TypeReference<List<Guest>> LIST_OF_GUEST_DATA = new TypeReference<>() {
    };

}
