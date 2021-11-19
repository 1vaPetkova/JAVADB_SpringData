package mostwanted.domain.dtos.races;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;
import java.util.Set;

@XmlRootElement(name = "race")
@XmlAccessorType(XmlAccessType.FIELD)
public class RaceImportDto {

    @XmlElement
    private Integer laps;
    @XmlElement(name = "district-name")
    private String districtName;
    @XmlElementWrapper(name = "entries")
    @XmlElement(name = "entry")
    private Set<EntryIdDto> entries;

    @NotNull
    public Integer getLaps() {
        return laps;
    }

    public void setLaps(Integer laps) {
        this.laps = laps;
    }

    @NotBlank
    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public Set<EntryIdDto> getEntries() {
        return entries;
    }

    public void setEntries(Set<EntryIdDto> entries) {
        this.entries = entries;
    }
}
