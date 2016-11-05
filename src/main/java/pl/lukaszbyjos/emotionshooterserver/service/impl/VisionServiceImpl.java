package pl.lukaszbyjos.emotionshooterserver.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionScopes;
import com.google.api.services.vision.v1.model.*;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.lukaszbyjos.emotionshooterserver.domain.VisionResponse;
import pl.lukaszbyjos.emotionshooterserver.service.VisionService;
import rx.Observable;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
@Slf4j
public class VisionServiceImpl implements VisionService {

    private Vision vision;

    @PostConstruct
    @SuppressWarnings("unused")
    private void init() {
        try {
            vision = getVisionService();
        } catch (IOException | GeneralSecurityException e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public Observable<VisionResponse> getPhotoData(byte[] bytes) {

        return Observable.fromCallable(() -> {
            AnnotateImageRequest request =
                    new AnnotateImageRequest()
                            .setImage(new Image().encodeContent(bytes))
                            .setFeatures(ImmutableList.of(
                                    new Feature()
                                            .setType("FACE_DETECTION")
                                            .setMaxResults(5)));
            AnnotateImageResponse response = null;
            try {
                Vision.Images.Annotate annotate =
                        vision.images()
                                .annotate(new BatchAnnotateImagesRequest().setRequests(ImmutableList.of(request)));

                // Due to a bug: requests to Vision API containing large images fail when GZipped.
                annotate.setDisableGZipContent(true);
                BatchAnnotateImagesResponse batchResponse = annotate.execute();
                assert batchResponse.getResponses().size() == 1;
                response = batchResponse.getResponses().get(0);
                if (response.getFaceAnnotations() == null) {
                    throw new IOException(
                            response.getError() != null
                                    ? response.getError().getMessage()
                                    : "Unknown error getting image annotations");
                } else {
                    List<FaceAnnotation> faceAnnotation = response.getFaceAnnotations();
                    return VisionResponse.builder().annotationList(faceAnnotation).build();
                }
            } catch (IOException e) {
                log.error(e.getLocalizedMessage(), e);
            }

            return VisionResponse.builder().build();
        });
    }

    private Vision getVisionService() throws IOException, GeneralSecurityException {
        GoogleCredential credential =
                GoogleCredential.getApplicationDefault().createScoped(VisionScopes.all());
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        return new Vision.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, credential)
                .setApplicationName("emotionshooter-147916")
                .build();
    }

}

