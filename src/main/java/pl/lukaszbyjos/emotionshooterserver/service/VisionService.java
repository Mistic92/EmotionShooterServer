package pl.lukaszbyjos.emotionshooterserver.service;

import pl.lukaszbyjos.emotionshooterserver.domain.VisionResponse;
import rx.Observable;

public interface VisionService {
        public Observable<VisionResponse> getPhotoData(final byte[] bytes);
}
