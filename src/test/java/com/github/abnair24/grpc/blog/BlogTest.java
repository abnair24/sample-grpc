package com.github.abnair24.grpc.blog;

import com.github.abnair24.grpc.blog.client.BlogClient;
import com.proto.blog.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Instant;

public class BlogTest {

    BlogClient blogClient;

    @BeforeClass(alwaysRun = true)
    public void before() throws Exception {
        blogClient = new BlogClient();
    }

    @Test
    public void createAndSearchBlog() throws Exception{

        Blog blog = Blog.newBuilder()
                .setAuthorId("TW")
                .setTitle("gRPC - " + Instant.now().getEpochSecond())
                .setContent("Blog on gRPC testing")
                .build();

        CreateBlogRequest request = CreateBlogRequest.newBuilder().setBlog(blog).build();

        CreateBlogResponse response = blogClient.createBlog(request);

        String id = response.getBlog().getId();

        ReadBlogRequest readBlogRequest = ReadBlogRequest.newBuilder().setBlogId(id).build();

        ReadBlogResponse readBlogResponse = blogClient.readBlog(readBlogRequest);

        System.out.println(readBlogResponse.toString());
    }

    @Test
    public void listAllBlogs() throws Exception {

        ListBlogRequest request = ListBlogRequest.newBuilder().build();
        blogClient.listBlog(request)
                .forEachRemaining(
                        response -> System.out.println(response.toString())
                );
    }


    @AfterClass(alwaysRun = true)
    public void shutdownChannels() throws Exception {
        blogClient.shutdownChannel();
    }
}
