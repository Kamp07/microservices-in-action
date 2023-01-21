package ru.kampot.license.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Organization extends RepresentationModel<Organization> {
    String id;
    String name;
    String contactName;
    String contactEmail;
    String contactPhone;
}
