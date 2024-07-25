package com.example.server.image.service;

import com.example.server.feed.repository.FeedJpaRepository;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    private final FeedJpaRepository feedJpaRepository;

    public String saveFile(MultipartFile file) throws IOException {
        String imageId = UUID.randomUUID().toString();
        BufferedImage original = ImageIO.read(file.getInputStream());

        File imageFile = File.createTempFile(imageId, ".jpg");
        File thumbnailFile = File.createTempFile(imageId, ".jpg");

        try {
            Thumbnails.of(original).scale(1.0d).outputFormat("jpg").toFile(imageFile);
            Thumbnails.of(original).crop(Positions.CENTER).size(500, 500).outputFormat("jpg").toFile(thumbnailFile);

            uploadToS3(imageFile, "feed/" + imageId + ".jpg");
            uploadToS3(thumbnailFile, "thumbnail/" + imageId + ".jpg");
        } finally {
            imageFile.delete();
            thumbnailFile.delete();
        }

        return imageId+".jpg";
    }

    private void uploadToS3(File file, String key) throws IOException {
        try (InputStream inputStream = file.toURI().toURL().openStream()) {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType("image/jpeg")
                    .build();

            s3Client.putObject(putObjectRequest,
                    software.amazon.awssdk.core.sync.RequestBody.fromInputStream(inputStream, file.length()));
        }
    }
}
