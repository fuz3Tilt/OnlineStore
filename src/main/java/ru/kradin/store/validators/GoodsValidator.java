package ru.kradin.store.validators;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;
import ru.kradin.store.enums.Status;

import java.util.Objects;

public class GoodsValidator {
    private int id;
    @NotEmpty(message = "Название не может быть пустым")
    @Size(min = 2, max = 50, message = "Название должно быть в пределах 2-50 символов")
    private String name;
    @NotEmpty(message = "Описание не может быть пустым")
    @Size(min = 2, max = 500, message = "Описание должно быть в пределах 2-500 символов")
    private String description;
    private MultipartFile imageToUpload;
    private String imageName;
    private Status status;
    @Min(value = 0, message = "Цена должна быть больше 0")
    private long price;
    @Min(value = 0,message = "Не выбран каталог для товара")
    private int catalogId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartFile getImageToUpload() {
        return imageToUpload;
    }

    public void setImageToUpload(MultipartFile imageToUpload) {
        this.imageToUpload = imageToUpload;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(int catalogId) {
        this.catalogId = catalogId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (! (o instanceof GoodsValidator that)) return false;
        return getPrice() == that.getPrice() && getCatalogId() == that.getCatalogId() && getName().equals(that.getName()) && getDescription().equals(that.getDescription()) && getStatus() == that.getStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription(), getStatus(), getPrice(), getCatalogId());
    }
}
