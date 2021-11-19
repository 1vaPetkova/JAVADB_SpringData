package softuni.exam.instagraphlite.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.dtos.seed.xml.PostSeedDto;
import softuni.exam.instagraphlite.models.dtos.seed.xml.PostSeedRootDto;
import softuni.exam.instagraphlite.models.entities.Picture;
import softuni.exam.instagraphlite.models.entities.Post;
import softuni.exam.instagraphlite.models.entities.User;
import softuni.exam.instagraphlite.repository.PictureRepository;
import softuni.exam.instagraphlite.repository.PostRepository;
import softuni.exam.instagraphlite.repository.UserRepository;
import softuni.exam.instagraphlite.service.PostService;
import softuni.exam.instagraphlite.util.FileUtil;
import softuni.exam.instagraphlite.util.ValidationUtil;
import softuni.exam.instagraphlite.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;

import static softuni.exam.instagraphlite.common.Constants.*;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PictureRepository pictureRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    private final FileUtil fileUtil;

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository,
                           PictureRepository pictureRepository, ValidationUtil validationUtil,
                           ModelMapper modelMapper, XmlParser xmlParser, FileUtil fileUtil) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.pictureRepository = pictureRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.fileUtil = fileUtil;
    }

    @Override
    public boolean areImported() {
        return this.postRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return this.fileUtil.readFile(POSTS_PATH);
    }

    @Override
    public String importPosts() throws JAXBException {
        StringBuilder sb = new StringBuilder();
        PostSeedRootDto postSeedRootDto = this.xmlParser.parseXml(PostSeedRootDto.class, POSTS_PATH);
        for (PostSeedDto postSeedDto : postSeedRootDto.getPosts()) {
            User user = this.userRepository.findByUsername(postSeedDto.getUser().getUsername());
            Picture picture = this.pictureRepository.findByPath(postSeedDto.getPicture().getPath());
            if (user == null
                    || picture == null
                    || !this.validationUtil.isValid(postSeedDto)) {
                getIncorrectDataMessage(sb, "Post");
                continue;
            }
            Post post = this.modelMapper.map(postSeedDto, Post.class);
            post.setUser(user);
            post.setPicture(picture);
            this.postRepository.save(post);
            getSuccessPostMessage(sb, post.getUser().getUsername());
        }
        return sb.toString().trim();
    }
}
