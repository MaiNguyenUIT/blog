package com.example.backend;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.example.backend.serviceImpl.CloudinaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class CloudinaryServiceTest {
    @Mock
    private Cloudinary mockCloudinary;

    @Mock
    private Uploader mockUploader;

    @InjectMocks
    private CloudinaryService cloudinaryService;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        when(mockCloudinary.uploader()).thenReturn(mockUploader);
    }

    @Test
    public void testUploadFile() throws Exception {

        byte[] content = "fake image".getBytes();
        MultipartFile file = new MockMultipartFile("file", "avatar.jpg", "image/jpeg", content);

        Map<String, Object> fakeResult = new HashMap<>();
        fakeResult.put("secure_url", "https://res.cloudinary.com/test/image/upload/avatar.jpg");

        when(mockUploader.upload(eq(content), anyMap())).thenReturn(fakeResult);


        String result = cloudinaryService.uploadFile(file);

        assertEquals("https://res.cloudinary.com/test/image/upload/avatar.jpg", result);
    }
}
