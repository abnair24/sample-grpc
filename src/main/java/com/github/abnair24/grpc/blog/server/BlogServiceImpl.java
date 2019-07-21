package com.github.abnair24.grpc.blog.server;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.proto.blog.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;

@Slf4j
public class BlogServiceImpl extends BlogServiceGrpc.BlogServiceImplBase {

    private MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
    private MongoDatabase mongoDatabase = mongoClient.getDatabase("testdb");
    private MongoCollection<Document>mongoCollection = mongoDatabase.getCollection("blog");

    @Override
    public void createBlog(CreateBlogRequest request, StreamObserver<CreateBlogResponse> responseObserver) {
        log.info("Creating blog :");

        Blog blog = request.getBlog();
        Document document = new Document("author_id",blog.getAuthorId())
                .append("title",blog.getTitle())
                .append("content",blog.getContent());

        log.info("Inserting blog : {}",blog.getTitle());
        mongoCollection.insertOne(document);

        String id = document.getObjectId("_id").toString();
        log.info("Inserted blog : {}",blog.getTitle(),id);


        CreateBlogResponse createBlogResponse = CreateBlogResponse.newBuilder()
                .setBlog(blog
                        .toBuilder()
                        .setId(id)
                .build())
                .build();

        responseObserver.onNext(createBlogResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void readBlog(ReadBlogRequest request, StreamObserver<ReadBlogResponse> responseObserver) {
        log.info("Reading blog :");

        String blogId = request.getBlogId();

        log.info("Searching blog with Id :{}",blogId);

        Document result = mongoCollection.find(eq("_id",new ObjectId(blogId)))
                    .first();

        if(result == null) {
            log.info("Not found the blog :{}",blogId);
            responseObserver.onError(Status.NOT_FOUND
            .withDescription("blog not found")
            .asRuntimeException());
        } else {
            log.info("Found blog..");
            Blog blog = Blog.newBuilder()
                    .setAuthorId(result.getString("author_id"))
                    .setTitle(result.getString("title"))
                    .setContent(result.getString("content"))
                    .setId(result.getObjectId("_id").toString())
                    .build();

            ReadBlogResponse readBlogResponse = ReadBlogResponse
                    .newBuilder()
                    .setBlog(blog)
                    .build();

            responseObserver.onNext(readBlogResponse);

            responseObserver.onCompleted();
        }

    }

    @Override
    public void updateBlog(UpdateBlogRequest request, StreamObserver<UpdateBlogResponse> responseObserver) {

        log.info("Updating blog");

        Blog blog = request.getBlog();
        String blogId = blog.getId();

        log.info("Searching the blog to update");

        Document result = mongoCollection.find(eq("_id",new ObjectId(blogId)))
                .first();

        if(result == null){
            log.info("Blog not found to update : {}",blogId);
            responseObserver.onError(Status.NOT_FOUND
            .withDescription("Blog not found")
            .asRuntimeException());
        } else {
            Document updateDocument = new Document("author_id",blog.getAuthorId())
                    .append("title",blog.getTitle())
                    .append("content",blog.getContent())
                    .append("_id",new ObjectId(blogId));

            mongoCollection.replaceOne(eq("_id",result.getObjectId("_id")),updateDocument);

            log.info("Updated the content of blog : {}",blogId);

            Blog updateBlog= Blog.newBuilder()
                    .setAuthorId(updateDocument.getString("author_id"))
                    .setTitle(updateDocument.getString("title"))
                    .setContent(updateDocument.getString("content"))
                    .setId(updateDocument.getObjectId("_id").toString())
                    .build();


            UpdateBlogResponse updateBlogResponse = UpdateBlogResponse
                    .newBuilder()
                    .setBlog(updateBlog)
                    .build();

            responseObserver.onNext(updateBlogResponse);

            responseObserver.onCompleted();
        }

    }

    @Override
    public void deleteBlog(DeleteBlogRequest request, StreamObserver<DeleteBlogResponse> responseObserver) {
        log.info("Deleting blog");

        String blogId = request.getBlogId();

        DeleteResult result = mongoCollection.deleteOne(eq("_id",new ObjectId(blogId)));

        if(result.getDeletedCount() == 0) {
            log.info("Blog to be deleted not found : {}",blogId);
            responseObserver.onError(Status.NOT_FOUND
            .withDescription("Blog not found to delete")
            .asRuntimeException());
        } else {
            log.info("Blog deleted ");



            DeleteBlogResponse deleteBlogResponse = DeleteBlogResponse
                    .newBuilder()
                    .setBlogId(blogId)
                    .build();

            responseObserver.onNext(deleteBlogResponse);

            responseObserver.onCompleted();
        }
    }

    @Override
    public void listBlog(ListBlogRequest request, StreamObserver<ListBlogResponse> responseObserver) {
        log.info("Listing all the blogs");

        mongoCollection
                .find()
                .iterator()
                .forEachRemaining(document -> responseObserver
                        .onNext(ListBlogResponse
                                .newBuilder()
                                .setBlog(Blog
                                        .newBuilder()
                                        .setAuthorId(document.getString("author_id"))
                                        .setContent(document.getString("content"))
                                        .setTitle(document.getString("title"))
                                        .setId(document.getObjectId("_id").toString())
                                        .build())
                                .build()));

        responseObserver.onCompleted();
    }
}
