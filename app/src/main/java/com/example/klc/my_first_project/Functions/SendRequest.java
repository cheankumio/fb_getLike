package com.example.klc.my_first_project.Functions;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.klc.my_first_project.Object.Comments;
import com.example.klc.my_first_project.Object.FeedPost;
import com.example.klc.my_first_project.Object.JSONObjectList;
import com.example.klc.my_first_project.Object.Likes;
import com.example.klc.my_first_project.Object.PostComments;
import com.example.klc.my_first_project.Object.PostLikes;
import com.example.klc.my_first_project.Object.PostShared;
import com.example.klc.my_first_project.Object.Sharedposts;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.gson.Gson;

/**
 * Created by klc on 2017/4/23.
 */

public class SendRequest {
    public static String tmp="";

    public static void getAllPosted(String url, final AccessToken Token, final int data_limit, String afterdata){
        String groupID = "/258681614328001/feed";
        final GraphRequest request = GraphRequest.newGraphPathRequest(
                Token,
                groupID,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        Gson gson = new Gson();
                        //Log.d("MYLOG","response: "+response.toString());
                        String jsonString = response.toString().split("graphObject:")[1].split(", error")[0];
                        FeedPost feedPosts = gson.fromJson(jsonString,FeedPost.class);
                        //存入所有文章至List
                        JSONObjectList.FeedPostDetialList.addAll(feedPosts.getData());
                        String after = feedPosts.getPages().getCursors().getAfter().toString();
                        if(feedPosts.getPages().getNext()!=null) {
                            getAllPosted("", Token, data_limit, after);
                        }else{
                            Log.d("MYLOG","共 "+JSONObjectList.FeedPostDetialList.size()+" 篇文章加載完畢.");
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,message,created_time");
        parameters.putString("limit", data_limit+"");
        if(afterdata.length()>30){
            parameters.putString("after", afterdata);
        }
        request.setParameters(parameters);
        request.executeAsync();
    }


    public static void getlikeUser(final AccessToken Token, String afterdata, final String contentID) {
        GraphRequest request = GraphRequest.newGraphPathRequest(
                Token,
                contentID+"/likes",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        Gson gson = new Gson();
                        //Log.d("MYLOG","response: "+response.toString());
                        String jsonString = response.toString().split("graphObject:")[1].split(", error")[0];
                        Likes postLikes = gson.fromJson(jsonString,Likes.class);

                        //存入所有文章至List
                        //for(int i=0;i<postLikes.getData().size();i++)
                            JSONObjectList.PostLikeDetialList.addAll(postLikes.getData());

                        String after = postLikes.getPaging().getCursors().getAfter().toString();
                        if(postLikes.getPaging().getNext()!=null) {
                            getlikeUser(Token, after, contentID);
                        }else{
                            Log.d("MYLOG","getlikeUser 加載完畢.");
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name");
        parameters.putString("limit", "1000");
        if(afterdata.length()>30){
            parameters.putString("after", afterdata);
        }
        request.setParameters(parameters);
        request.executeAsync();
    }

    public static void getcommentsUser(final AccessToken Token, String afterdata, final String contentID) {
        GraphRequest request = GraphRequest.newGraphPathRequest(
                Token,
                contentID+"/comments",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        Gson gson = new Gson();
                        String jsonString = response.toString().split("graphObject:")[1].split(", error")[0];
                        Comments postComments = gson.fromJson(jsonString,Comments.class);
                        //存入所有文章至List
                        //for(int i=0;i<postComments.getData().size();i++)
                            JSONObjectList.PostCommentsDetialList.addAll(postComments.getData());
                        String after = postComments.getPaging().getCursors().getAfter().toString();
                        if(postComments.getPaging().getNext()!=null){
                            getcommentsUser(Token,after,contentID);
                        }else{
                            Log.d("MYLOG","getcommentsUser 加載完畢.");
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "from,message");
        parameters.putString("limit", "100");
        if(afterdata.length()>30){
            parameters.putString("after", afterdata);
        }
        request.setParameters(parameters);
        request.executeAsync();
    }

    public static void getShareUser(final AccessToken Token, String afterdata, final String contentID) {
        GraphRequest request = GraphRequest.newGraphPathRequest(
                Token,
                contentID+"/sharedposts",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        Gson gson = new Gson();
                        String jsonString = response.toString().split("graphObject:")[1].split(", error")[0];
                        Sharedposts postShared = gson.fromJson(jsonString,Sharedposts.class);
                        if(postShared.getPaging().getNext()!=null)
                        Log.d("MYLOG",postShared.getPaging().getCursors().getAfter());
                        //存入所有文章至List
                        //for(int i=0;i<postShared.getData().size();i++)
                            JSONObjectList.PostSharedDetialList.addAll(postShared.getData());
                        String after = postShared.getPaging().getCursors().getAfter().toString();
                        if(postShared.getPaging().getNext()!=null) {

                            getShareUser(Token, after, contentID);
                        }else{
                            Log.d("MYLOG","getShareUser 加載完畢.");
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "from,message");
        parameters.putString("limit", "100");
        if(afterdata.length()>30){
            parameters.putString("after", afterdata);
        }
        request.setParameters(parameters);
        request.executeAsync();
    }
}
