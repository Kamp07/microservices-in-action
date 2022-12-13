package ru.kampot.license.service;

import ru.kampot.license.model.License;

import java.util.Locale;

public interface LicenseService {
    License getLicense(String licenseId, String organizationId);

    String createLicense(License license, String organizationId);

    String updateLicense(License license, String organizationId);

    String deleteLicense(String licenseId, String organizationId);
}
