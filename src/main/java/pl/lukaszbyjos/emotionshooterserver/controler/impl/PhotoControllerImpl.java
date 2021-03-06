package pl.lukaszbyjos.emotionshooterserver.controler.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.lukaszbyjos.emotionshooterserver.controler.PhotoController;
import pl.lukaszbyjos.emotionshooterserver.domain.NewPhotoInfo;
import pl.lukaszbyjos.emotionshooterserver.service.*;
import pl.lukaszbyjos.emotionshooterserver.websocket.PhotoChangeHandler;
import rx.schedulers.Schedulers;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

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
    @Autowired
    private FunnyTextService funnyTextService;
    @Autowired
    private ImageDrawService imageDrawService;

    @Value("${effects.enabled}")
    private boolean effectsEnabled;

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
            photoChangeHandler.sendProcessingInfo(NewPhotoInfo.builder().processing(true).build());
            visionService.getPhotoData(file.getBytes())
                    .subscribeOn(Schedulers.io())
                    .subscribe(visionResponse -> {
                        String fileDownloadUrl;
                        if (effectsEnabled) {
                            imageDrawService.createColorfullImage(visionResponse, storageService.load(fileName));
                            String newFileName = fileName.substring(0, fileName.lastIndexOf(".jpg")) + "_fun.jpg";
                            fileDownloadUrl = baseUrl + newFileName;
                        } else {
                            fileDownloadUrl = baseUrl + fileName;
                        }
                        visionResponse.setPhotoUrl(fileDownloadUrl);
                        photoChangeHandler.sendNewPhotoData(visionResponse);
                        log.info("Vision photoUrl: " + visionResponse.toString());
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


}

