package org.example.board.springboot.web;

import org.example.board.springboot.domain.posts.Posts;
import org.example.board.springboot.domain.posts.PostsRepository;
import org.example.board.springboot.web.dto.PostsSaveRequestDto;
import org.example.board.springboot.web.dto.PostsUpdateRequestDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @After
    public void tearDown() throws Exception{
        postsRepository.deleteAll();
    }

    @Test
    public void postResgist() throws Exception{

        String title = "test1";
        String content = "notice";
        String author = "admin";

        PostsSaveRequestDto postsSaveRequestDto = PostsSaveRequestDto.builder().title(title)
                .content(content).author(author).build();

        StringBuffer url = new StringBuffer();
        url.append("http://localhost:");
        url.append(port);
        url.append("/api/v1/posts");

        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url.toString(), postsSaveRequestDto, Long.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> posts = postsRepository.findAll();
        assertThat(posts.get(0).getTitle()).isEqualTo(title);
        assertThat(posts.get(0).getContent()).isEqualTo(content);


    }


    @Test
    public void postsModify() throws Exception{

        //before data
        Posts savePosts = postsRepository.save(Posts.builder().title("title").content("content").author("author").build());

        Long updatedId = savePosts.getId();
        //update data
        String title = "수정제목";
        String content = "수정내용";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder().title(title).content(content).build();

        String url = "http://localhost:" + port + "/api/v1/posts/" + updatedId;

        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);
        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> allList = postsRepository.findAll();
        assertThat(allList.get(0).getTitle()).isEqualTo(title);
        assertThat(allList.get(0).getContent()).isEqualTo(content);

    }

    @Test
    public void BaseTimeEntityRegist(){

        LocalDateTime now = LocalDateTime.of(2020,10,10,22,52,0);
        postsRepository.save(Posts.builder().title("title").content("content").author("admin").build());

        List<Posts> postsList = postsRepository.findAll();

        Posts posts = postsList.get(0);

        System.out.println(">>> createDate = " + posts.getCreatedDate() + " , modifyDate = " + posts.getModifiedDate());

        assertThat(posts.getCreatedDate()).isAfter(now);
        assertThat(posts.getModifiedDate()).isAfter(now);
    }

}
