package softuni.exam.domain.dto.seed.xml;

import javax.validation.constraints.NotBlank;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class PictureSeedDto {

    private String url;

    @NotBlank
    public String getUrl() {
        return url;
    }


    public void setUrl(String url) {
        this.url = url;
    }
}
