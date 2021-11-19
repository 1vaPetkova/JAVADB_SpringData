package mostwanted.domain.dtos.races;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "race-entries")
@XmlAccessorType(XmlAccessType.FIELD)
public class EntryImportRootDto {

    @XmlElement(name = "race-entry")
    List<EntryImportDto> raceEntries;

    public List<EntryImportDto> getRaceEntries() {
        return raceEntries;
    }

    public void setRaceEntries(List<EntryImportDto> raceEntries) {
        this.raceEntries = raceEntries;
    }
}
