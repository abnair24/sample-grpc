package com.github.abnair24.grpc.blog.client;

import com.github.abnair24.grpc.BaseClient;
import com.github.abnair24.grpc.blog.stubs.BlogServiceConnection;
import com.proto.blog.*;

import java.util.Iterator;

public class BlogClient extends BaseClient {

    private BlogServiceConnection blogServiceConnection;
    private BlogServiceGrpc.BlogServiceBlockingStub blockingStub;

    public BlogClient() {
        blogServiceConnection = new BlogServiceConnection();
        blockingStub = blogServiceConnection.getConnection();
    }

    @Override
    public void shutdownChannel() {
        blogServiceConnection.shutdown();
    }

    public CreateBlogResponse createBlog(CreateBlogRequest createBlogRequest) throws Exception {
        CreateBlogResponse createBlogResponse = blockingStub.createBlog(createBlogRequest);
       // blogServiceConnection.close();
        return createBlogResponse;
    }

    public ReadBlogResponse readBlog(ReadBlogRequest readBlogRequest) throws Exception {

        ReadBlogResponse readBlogResponse = blockingStub.readBlog(readBlogRequest);
      //  blogServiceConnection.close();
        return readBlogResponse;
    }

    public UpdateBlogResponse updateBlog(UpdateBlogRequest updateBlogRequest) throws Exception {

        UpdateBlogResponse updateBlogResponse = blockingStub.updateBlog(updateBlogRequest);
        //blogServiceConnection.close();
        return updateBlogResponse;
    }

    public DeleteBlogResponse deleteBlog(DeleteBlogRequest deleteBlogRequest) throws Exception {

        DeleteBlogResponse deleteBlogResponse = blockingStub.deleteBlog(deleteBlogRequest);
       // blogServiceConnection.close();
        return deleteBlogResponse;
    }

    public Iterator<ListBlogResponse> listBlog(ListBlogRequest listBlogRequest) throws Exception {

        Iterator<ListBlogResponse> listBlogResponses = blockingStub.listBlog(listBlogRequest);
      //  blogServiceConnection.close();
        return listBlogResponses;
    }

}
