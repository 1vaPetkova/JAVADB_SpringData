package softuni.exam.instagraphlite.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.dtos.seed.json.UserSeedDto;
import softuni.exam.instagraphlite.models.entities.Picture;
import softuni.exam.instagraphlite.models.entities.User;
import softuni.exam.instagraphlite.repository.PictureRepository;
import softuni.exam.instagraphlite.repository.UserRepository;
import softuni.exam.instagraphlite.service.UserService;
import softuni.exam.instagraphlite.util.FileUtil;
import softuni.exam.instagraphlite.util.ValidationUtil;

import java.io.IOException;
import java.util.Comparator;

import static softuni.exam.instagraphlite.common.Constants.*;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PictureRepository pictureRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final FileUtil fileUtil;

    public UserServiceImpl(UserRepository userRepository, PictureRepository pictureRepository,
                           ValidationUtil validationUtil, ModelMapper modelMapper,
                           Gson gson, FileUtil fileUtil) {
        this.userRepository = userRepository;
        this.pictureRepository = pictureRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.fileUtil = fileUtil;
    }

    @Override
    public boolean areImported() {
        return this.userRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return this.fileUtil.readFile(USERS_PATH);
    }

    @Override
    public String importUsers() throws IOException {
        StringBuilder sb = new StringBuilder();
        UserSeedDto[] userSeedDtos = this.gson.fromJson(readFromFileContent(), UserSeedDto[].class);
        for (UserSeedDto userSeedDto : userSeedDtos) {
            Picture picture = this.pictureRepository.findByPath(userSeedDto.getProfilePicture());
            if (picture == null
                    || !this.validationUtil.isValid(userSeedDto)
                    || this.userRepository.findByUsername(userSeedDto.getUsername()) != null) {
                getIncorrectDataMessage(sb, "User");
                continue;
            }
            User user = this.modelMapper.map(userSeedDto, User.class);
            user.setProfilePicture(picture);
            this.userRepository.save(user);
            getSuccessUserMessage(sb, user.getUsername());
        }

        return sb.toString().trim();
    }

    @Override
    public String exportUsersWithTheirPosts() {
        StringBuilder sb = new StringBuilder();
        this.userRepository.findAllOrderByPostsCountAndId()
                .forEach(user -> {
                            sb.append(
                                    String.format("User: %s\n" +
                                                    "Post count: %d\n",
                                            user.getUsername(), user.getPosts().size()));
                            user.getPosts()
                                    .stream()
                                    .sorted(Comparator.comparingDouble(f -> f.getPicture().getSize()))
                                    .forEach(post ->
                                            sb
                                                    .append(
                                                            String.format("==Post Details:\n" +
                                                                            "----Caption: %s\n" +
                                                                            "----Picture Size: %.2f\n",
                                                                    post.getCaption(), post.getPicture().getSize())
                                                    ));
                        }

                );
        return sb.toString().trim();
    }
}
