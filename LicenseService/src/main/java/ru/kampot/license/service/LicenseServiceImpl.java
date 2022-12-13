package ru.kampot.license.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.kampot.license.model.License;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class LicenseServiceImpl implements LicenseService {
    @Override
    public License getLicense(String licenseId, String organizationId) {
        return License.builder()
                .id(new Random().nextLong(1000))
                .licenseId(licenseId)
                .organizationId(organizationId)
                .description("Software Product")
                .productName("Game Engine")
                .licenseType("full")
                .build();
    }

    @Override
    public String createLicense(License license, String organizationId) {
        String responseMessage = null;
        if (license != null) {
            license.setOrganizationId(organizationId);
            responseMessage = String.format("License created: %s", license);
        }
        return responseMessage;
    }

    @Override
    public String updateLicense(License license, String organizationId) {
        String responseMessage = null;
        if (license != null) {
            license.setOrganizationId(organizationId);
            responseMessage = String.format("License Updated: %s", license);
        }
        return responseMessage;
    }

    @Override
    public String deleteLicense(String licenseId, String organizationId) {
        return String.format("Deleting license with id %s for the organization %s",
                licenseId, organizationId);
    }
}
