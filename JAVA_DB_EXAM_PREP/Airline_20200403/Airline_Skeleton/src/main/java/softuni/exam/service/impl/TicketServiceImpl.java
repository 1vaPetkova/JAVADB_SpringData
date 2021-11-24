package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.seed.xml.TicketSeedDto;
import softuni.exam.models.dto.seed.xml.TicketSeedRootDto;
import softuni.exam.models.entities.Passenger;
import softuni.exam.models.entities.Plane;
import softuni.exam.models.entities.Ticket;
import softuni.exam.models.entities.Town;
import softuni.exam.repository.PassengerRepository;
import softuni.exam.repository.PlaneRepository;
import softuni.exam.repository.TicketRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TicketService;
import softuni.exam.util.FileUtil;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;

import static softuni.exam.common.Constants.*;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final PlaneRepository planeRepository;
    private final TownRepository townRepository;
    private final PassengerRepository passengerRepository;
    private final FileUtil fileUtil;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;

    public TicketServiceImpl(TicketRepository ticketRepository, PlaneRepository planeRepository,
                             TownRepository townRepository, PassengerRepository passengerRepository,
                             FileUtil fileUtil, ValidationUtil validationUtil, ModelMapper modelMapper,
                             XmlParser xmlParser) {
        this.ticketRepository = ticketRepository;
        this.planeRepository = planeRepository;
        this.townRepository = townRepository;
        this.passengerRepository = passengerRepository;
        this.fileUtil = fileUtil;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return this.ticketRepository.count() > 0;
    }

    @Override
    public String readTicketsFileContent() throws IOException {
        return this.fileUtil.readFile(TICKETS_PATH);
    }

    @Override
    public String importTickets() throws JAXBException {
        StringBuilder sb = new StringBuilder();
        TicketSeedRootDto ticketSeedRootDto = this.xmlParser.parseXml(TicketSeedRootDto.class, TICKETS_PATH);
        for (TicketSeedDto ticketSeedDto : ticketSeedRootDto.getTickets()) {
            Town fromTown = this.townRepository.findByName(ticketSeedDto.getFromTown().getName());
            Town toTown = this.townRepository.findByName(ticketSeedDto.getToTown().getName());
            Passenger passenger = this.passengerRepository
                    .findByEmail(ticketSeedDto.getPassenger().getEmail());
            Plane plane = this.planeRepository.findByRegisterNumber(ticketSeedDto.getPlane().getRegisterNumber());
            if (!this.validationUtil.isValid(ticketSeedDto)
                    || fromTown == null
                    || toTown == null
                    || passenger == null
                    || plane == null
                    || fromTown.equals(toTown)) {
                getIncorrectDataMessage(sb, "Ticket");
                continue;
            }
            Ticket ticket = this.modelMapper.map(ticketSeedDto, Ticket.class);
            ticket.setFromTown(fromTown);
            ticket.setToTown(toTown);
            ticket.setPassenger(passenger);
            ticket.setPlane(plane);
            this.ticketRepository.save(ticket);
            getSuccessTicketMessage(sb,fromTown.getName(),toTown.getName());
        }
        return sb.toString().trim();
    }
}
