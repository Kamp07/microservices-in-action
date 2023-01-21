package ru.kampot.license.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kampot.license.model.License;

import java.util.List;

public interface LicenseRepository extends JpaRepository<License, String> {
    List<License> findByOrganizationId(String organizationId);
    License findByOrganizationIdAndLicenseId(String organizationId, String licenseId);
}
