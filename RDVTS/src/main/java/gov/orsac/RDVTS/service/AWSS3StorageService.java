package gov.orsac.RDVTS.service;

import org.springframework.web.multipart.MultipartFile;

public interface AWSS3StorageService {
    boolean uploadIssueImages(MultipartFile files, String activityId, String keyName);
}
