package pl.lukaszbyjos.emotionshooterserver.controler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequestMapping("/photo/")
public interface PhotoController {

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    void handleFileUpload(@RequestParam("file") MultipartFile file,
                          RedirectAttributes redirectAttributes,
                          HttpServletRequest request) throws IOException;

    @RequestMapping(value = "/{photoName:.+}", method = RequestMethod.GET)
    ResponseEntity getSelectedPhoto(@PathVariable String photoName);

}
