package com.github.abnair24.grpc.blog.server;

import com.mongodb.client.MongoClient;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.proto.blog.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;

import java.time.Duration;

import static com.mongodb.client.model.Filters.eq;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;


public class BlogServiceImpl extends BlogServiceGrpc.BlogServiceImplBase {

    public  GenericContainer mongo= null;
    private MongoClient mongoClient = null;
    private MongoDatabase mongoDatabase = null;
    private MongoCollection<Document>mongoCollection = null;

    public BlogServiceImpl() {
        mongo = new GenericContainer("mongo:latest")
                .withExposedPorts(27017)
                .waitingFor(new HttpWaitStrategy()
                        .forPort(27017)
                        .forStatusCodeMatching(r -> r == HTTP_OK || r == HTTP_UNAUTHORIZED))
                .withStartupTimeout(Duration.ofMinutes(1));
        mongo.start();

        mongoClient = MongoClients.create("mongodb://"+mongo.getContainerIpAddress()+ ":"+ mongo.getMappedPort(27017));
        mongoDatabase = mongoClient.getDatabase("testdb");
        mongoCollection = mongoDatabase.getCollection("blog");
    }

    @Override
    public void createBlog(CreateBlogRequest request, StreamObserver<CreateBlogResponse> responseObserver) {
        System.out.println("Creating blog :");
        Blog blog = request.getBlog();
        Document document = new Document("author_id",blog.getAuthorId())
                .append("title",blog.getTitle())
                .append("content",blog.getContent());

        System.out.println("Inserting blog : {}"+blog.getTitle());
        mongoCollection.insertOne(document);

        String id = document.getObjectId("_id").toString();
        System.out.println("Inserted blog : {}"+blog.getTitle());


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
        System.out.println("Reading blog :");

        String blogId = request.getBlogId();

        System.out.println("Searching blog with Id :{}"+blogId);

        Document result = mongoCollection.find(eq("_id",new ObjectId(blogId)))
                    .first();

        if(result == null) {
            System.out.println("Not found the blog :{}"+blogId);
            responseObserver.onError(Status.NOT_FOUND
            .withDescription("blog not found")
            .asRuntimeException());
        } else {
            System.out.println("Found blog..");
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

        System.out.println("Updating blog");

        Blog blog = request.getBlog();
        String blogId = blog.getId();

        System.out.println("Searching the blog to update");

        Document result = mongoCollection.find(eq("_id",new ObjectId(blogId)))
                .first();

        if(result == null){
            System.out.println("Blog not found to update : {}"+blogId);
            responseObserver.onError(Status.NOT_FOUND
            .withDescription("Blog not found")
            .asRuntimeException());
        } else {
            Document updateDocument = new Document("author_id",blog.getAuthorId())
                    .append("title",blog.getTitle())
                    .append("content",blog.getContent())
                    .append("_id",new ObjectId(blogId));

            mongoCollection.replaceOne(eq("_id",result.getObjectId("_id")),updateDocument);

            System.out.println("Updated the content of blog : {}"+blogId);

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

        System.out.println("Deleting blog");

        String blogId = request.getBlogId();

        DeleteResult result = mongoCollection.deleteOne(eq("_id",new ObjectId(blogId)));

        if(result.getDeletedCount() == 0) {
            System.out.println("Blog to be deleted not found : {}"+blogId);
            responseObserver.onError(Status.NOT_FOUND
            .withDescription("Blog not found to delete")
            .asRuntimeException());
        } else {
            System.out.println("Blog deleted ");

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
        System.out.println("Listing all the blogs");

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
