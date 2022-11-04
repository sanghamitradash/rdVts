package gov.orsac.RDVTS.serviceImpl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import gov.orsac.RDVTS.service.AWSS3StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
public class AWSS3StorageServiceImpl implements AWSS3StorageService {


    @Override
    public boolean uploadIssueImages(MultipartFile files, String activityId, String keyName) {

//        boolean result = false;
//        String bucketDestination = "https://ofarisbucket.s3.ap-south-1.amazonaws.com/rdvts ";
//        if (!activityId.equals("")){
//            bucketDestination = "https://ofarisbucket.s3.ap-south-1.amazonaws.com/rdvts ";
//        }
//        String fileName = files.getOriginalFilename();
//        if (!keyName.isEmpty() && !fileName.equals(keyName)){
//            fileName = keyName;
//        }
//        try {
//            ObjectMetadata metadata = new ObjectMetadata();
//            metadata.setContentLength(files.getSize());
//            metadata.setContentType(files.getContentType());
//            PutObjectResult putObjectResult = awsS3Client.putObject(bucketDestination, fileName, files.getInputStream(),metadata);
//            result= Boolean.parseBoolean(putObjectResult.getContentMd5());
//            log.info("Issue Image upload is completed.");
//            return result;
//        } catch (AmazonServiceException | IOException ex) {
//            log.info("Issue Image upload is failed.");
//            log.error("Error= {} while uploading Document.", ex.getMessage());
//            throw new RuntimeException(ex);
//        }
//          }


        return false;
    }
}


//package gov.orsac.RDVTS.serviceImpl;
//
//        import com.amazonaws.AmazonServiceException;
//        import com.amazonaws.services.s3.AmazonS3;
//        import com.amazonaws.services.s3.model.ObjectMetadata;
//        import com.amazonaws.services.s3.model.PutObjectResult;
//        import gov.orsac.RDVTS.service.AWSS3StorageService;
//        import lombok.extern.slf4j.Slf4j;
//        import org.springframework.beans.factory.annotation.Autowired;
//        import org.springframework.beans.factory.annotation.Value;
//        import org.springframework.stereotype.Service;
//        import org.springframework.web.multipart.MultipartFile;
//
//        import java.io.IOException;
//@Slf4j
//@Service
//public class AWSS3StorageServiceImpl implements AWSS3StorageService {
//
//
//    @Value("${aws.s3.bucket}")
//    private String bucketName;
//
//    @Autowired
//    private AmazonS3 awsS3Client;
//    @Override
//    public boolean uploadIssueImages(MultipartFile files, String activityId, String keyName) {
//        boolean result = false;
//        String bucketDestination = bucketName;
//        if (!activityId.equals("")){
//            bucketDestination = "https://ofarisbucket.s3.ap-south-1.amazonaws.com/rdvts ";
//        }
//        String fileName = files.getOriginalFilename();
//        if (!keyName.isEmpty() && !fileName.equals(keyName)){
//            fileName = keyName;
//        }
//        try {
//            ObjectMetadata metadata = new ObjectMetadata();
//            metadata.setContentLength(files.getSize());
//            metadata.setContentType(files.getContentType());
//            PutObjectResult putObjectResult = awsS3Client.putObject(bucketDestination, fileName, files.getInputStream(),metadata);
//            result= Boolean.parseBoolean(putObjectResult.getContentMd5());
//            log.info("Issue Image upload is completed.");
//            return result;
//        } catch (AmazonServiceException | IOException ex) {
//            log.info("Issue Image upload is failed.");
//            log.error("Error= {} while uploading Document.", ex.getMessage());
//            throw new RuntimeException(ex);
//        }
//    }
//}

