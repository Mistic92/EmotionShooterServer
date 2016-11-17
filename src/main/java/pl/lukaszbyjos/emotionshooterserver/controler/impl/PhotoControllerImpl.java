package pl.lukaszbyjos.emotionshooterserver.controler.impl;

import com.google.api.services.vision.v1.model.FaceAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.lukaszbyjos.emotionshooterserver.controler.PhotoController;
import pl.lukaszbyjos.emotionshooterserver.domain.EmotionStatus;
import pl.lukaszbyjos.emotionshooterserver.domain.NewPhotoInfo;
import pl.lukaszbyjos.emotionshooterserver.domain.VisionResponse;
import pl.lukaszbyjos.emotionshooterserver.service.ResponsesBackupService;
import pl.lukaszbyjos.emotionshooterserver.service.StorageService;
import pl.lukaszbyjos.emotionshooterserver.service.VisionService;
import pl.lukaszbyjos.emotionshooterserver.websocket.PhotoChangeHandler;
import rx.schedulers.Schedulers;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;

@Slf4j
@RestController
public class PhotoControllerImpl implements PhotoController {

    @Autowired
    private VisionService visionService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private PhotoChangeHandler photoChangeHandler;
    @Autowired
    private ResponsesBackupService responsesBackupService;

    private String baseUrl;


    @Override
    public void handleFileUpload(@RequestParam("file") MultipartFile file,
                                 RedirectAttributes redirectAttributes,
                                 HttpServletRequest request) throws IOException {
        log.info("Get file with name: " + file.getOriginalFilename());
        if (!file.isEmpty()) {
            final String fileName = storageService.store(file);
            if (baseUrl == null)
                baseUrl = String.format("%s://%s:%d/api/photo/", request.getScheme(), request.getServerName(), request.getServerPort());
            final String fileDownloadUrl = baseUrl + fileName;
            photoChangeHandler.sendProcessingInfo(NewPhotoInfo.builder().photoUrl(fileDownloadUrl).build());
            visionService.getPhotoData(file.getBytes())
                    .subscribeOn(Schedulers.io())
                    .subscribe(visionResponse -> {
                        log.info("Vision photoUrl: " + visionResponse.toString());
                        visionResponse = remapVisionResponse(visionResponse);
                        photoChangeHandler.sendNewPhotoData(visionResponse);
                        visionResponse.setPhotoUrl(fileDownloadUrl);
                        responsesBackupService.saveResponse(visionResponse);
                    });
        }
    }

    @Override
    public ResponseEntity getSelectedPhoto(@PathVariable String photoName) {
        if (!photoName.isEmpty()) {
            final Path filePath = storageService.load(photoName);
            try {
                InputStreamResource inputStream = new InputStreamResource(new FileInputStream(filePath.toFile()));
                return ResponseEntity.ok(inputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.notFound().build();
    }

    private VisionResponse remapVisionResponse(VisionResponse visionResponse) {
        if (visionResponse.getAnnotationList() != null) {
            FaceAnnotation faceAnn = visionResponse.getAnnotationList().get(0);
            String anger = EmotionStatus.valueOf(faceAnn.getAngerLikelihood()).getTranslation();
            String blurred = EmotionStatus.valueOf(faceAnn.getBlurredLikelihood()).getTranslation();
            String joy = EmotionStatus.valueOf(faceAnn.getJoyLikelihood()).getTranslation();
            String sorrow = EmotionStatus.valueOf(faceAnn.getSorrowLikelihood()).getTranslation();
            String suprise = EmotionStatus.valueOf(faceAnn.getSurpriseLikelihood()).getTranslation();
            String headwear = EmotionStatus.valueOf(faceAnn.getHeadwearLikelihood()).getTranslation();
            String underexp = EmotionStatus.valueOf(faceAnn.getUnderExposedLikelihood()).getTranslation();
            FaceAnnotation f = new FaceAnnotation();
            f.setAngerLikelihood(anger);
            f.setJoyLikelihood(joy);
            f.setBlurredLikelihood(blurred);
            f.setSorrowLikelihood(sorrow);
            f.setSurpriseLikelihood(suprise);
            f.setHeadwearLikelihood(headwear);
            f.setUnderExposedLikelihood(underexp);
            visionResponse.setAnnotationList(Collections.singletonList(f));
        }
        return visionResponse;
    }

}

