//package org.study.tracker.utils;
//
//import jakarta.persistence.AttributeConverter;
//import org.study.tracker.RoleEnum;
//
//public class RoleConverter implements AttributeConverter<RoleEnum, String> {
//  @Override
//  public String convertToDatabaseColumn(RoleEnum role) {
//    return role.toString();
//  }
//
//  @Override
//  public RoleEnum convertToEntityAttribute(String role) {
//    return RoleEnum.valueOf(role);
//  }
//}
