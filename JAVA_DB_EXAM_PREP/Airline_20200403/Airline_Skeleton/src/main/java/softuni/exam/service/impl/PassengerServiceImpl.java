package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.seed.json.PassengerSeedDto;
import softuni.exam.models.entities.Passenger;
import softuni.exam.models.entities.Town;
import softuni.exam.repository.PassengerRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.PassengerService;
import softuni.exam.util.FileUtil;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;

import static softuni.exam.common.Constants.*;

@Service
public class PassengerServiceImpl implements PassengerService {
    private final PassengerRepository passengerRepository;
    private final TownRepository townRepository;
    private final FileUtil fileUtil;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final Gson gson;

    public PassengerServiceImpl(PassengerRepository passengerRepository, TownRepository townRepository, FileUtil fileUtil,
                                ValidationUtil validationUtil, ModelMapper modelMapper,
                                Gson gson) {
        this.passengerRepository = passengerRepository;
        this.townRepository = townRepository;
        this.fileUtil = fileUtil;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return this.passengerRepository.count() > 0;
    }

    @Override
    public String readPassengersFileContent() throws IOException {
        return this.fileUtil.readFile(PASSENGERS_PATH);
    }

    @Override
    public String importPassengers() throws IOException {
        StringBuilder sb = new StringBuilder();
        PassengerSeedDto[] passengerSeedDtos = this.gson.fromJson(readPassengersFileContent(), PassengerSeedDto[].class);
        for (PassengerSeedDto passengerSeedDto : passengerSeedDtos) {
            Town town = this.townRepository.findByName(passengerSeedDto.getTown());
            if (!this.validationUtil.isValid(passengerSeedDto)
                    || town == null) {
                getIncorrectDataMessage(sb, "Passenger");
                continue;
            }
            Passenger passenger = this.modelMapper.map(passengerSeedDto, Passenger.class);
            passenger.setTown(town);
            this.passengerRepository.save(passenger);
            getSuccessPassengerMessage(sb, passenger.getLastName(), passenger.getEmail());
        }
        return sb.toString().trim();
    }

    @Override
    public String getPassengersOrderByTicketsCountDescendingThenByEmail() {
        StringBuilder sb = new StringBuilder();
       this.passengerRepository
                .findAllOrderByTicketsCountDescAndEmail()
                .forEach(p->sb.append(p.toString()));
//       this.passengerRepository
//                .findAllOrderedByTicketsCountAndEmail()
//                .forEach(p->sb.append(String.format("Passenger %s %s\n" +
//                        "\tEmail - %s\n" +
//                        "\tPhone - %s" +
//                        "\tNumber of tickets - %d\n",
//                        p.getFirstName(),p.getLastName(),
//                        p.getEmail(),p.getPhoneNumber(),p.getTickets().size())));

        return sb.toString().trim();
    }
}
