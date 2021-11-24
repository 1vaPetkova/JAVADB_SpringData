package hiberspring.service.impl;

import com.google.gson.Gson;
import hiberspring.domain.dtos.seed.json.BranchSeedDto;
import hiberspring.domain.entities.Branch;
import hiberspring.domain.entities.Town;
import hiberspring.repository.BranchRepository;
import hiberspring.repository.TownRepository;
import hiberspring.service.BranchService;
import hiberspring.util.FileUtil;
import hiberspring.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static hiberspring.common.Constants.*;

@Service
public class BranchServiceImpl implements BranchService {
    private final BranchRepository branchRepository;
    private final TownRepository townRepository;
    private final FileUtil fileUtil;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    public BranchServiceImpl(BranchRepository branchRepository, TownRepository townRepository, FileUtil fileUtil,
                             ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.branchRepository = branchRepository;
        this.townRepository = townRepository;
        this.fileUtil = fileUtil;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public Boolean branchesAreImported() {
        return this.branchRepository.count() > 0;
    }

    @Override
    public String readBranchesJsonFile() throws IOException {
        return this.fileUtil.readFile(BRANCHES_PATH);
    }

    @Override
    public String importBranches(String branchesFileContent) throws IOException {
        StringBuilder sb = new StringBuilder();
        BranchSeedDto[] branchSeedDtos = this.gson.fromJson(readBranchesJsonFile(), BranchSeedDto[].class);
        for (BranchSeedDto branchSeedDto : branchSeedDtos) {
            Town town = this.townRepository.findByName(branchSeedDto.getTown()).orElse(null);
            if (!this.validationUtil.isValid(branchSeedDto)
                    || town == null) {
                getIncorrectDataMessage(sb);
                continue;
            }
            Branch branch = this.modelMapper.map(branchSeedDto, Branch.class);
            branch.setTown(town);
            this.branchRepository.save(branch);
            getSuccessBranchMessage(sb, branch.getName());
        }
        return sb.toString().trim();
    }
}
