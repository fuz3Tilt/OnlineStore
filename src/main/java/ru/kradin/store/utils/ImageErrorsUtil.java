package ru.kradin.store.utils;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

public class ImageErrorsUtil {

    public static void addErrorsIfExist(MultipartFile imageToUpload, BindingResult bindingResult, String objectName){
        if (imageToUpload.getSize() > 5000000){
            FieldError error = new FieldError(objectName, "imageToUpload", "Размер файла не должен превышать 5MB");
            bindingResult.addError(error);
        }

        if (!imageToUpload.getContentType().equals("image/jpeg")){
            FieldError error = new FieldError(objectName, "imageToUpload", "Изображение должно быть формата JPEG");
            bindingResult.addError(error);
        }

        if (imageToUpload.isEmpty()){
            FieldError error = new FieldError(objectName, "imageToUpload", "Не выбрано изображение к загрузке");
            bindingResult.addError(error);
        }

    }
}
