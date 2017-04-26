package com.example.klc.my_first_project.Functions;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.klc.my_first_project.Lottery_Activity;
import com.example.klc.my_first_project.LuckActivity;
import com.example.klc.my_first_project.Object.Comments;
import com.example.klc.my_first_project.Object.FeedPost;
import com.example.klc.my_first_project.Object.JSONObjectList;
import com.example.klc.my_first_project.Object.Likes;
import com.example.klc.my_first_project.Object.PostComments;
import com.example.klc.my_first_project.Object.PostLikes;
import com.example.klc.my_first_project.Object.PostShared;
import com.example.klc.my_first_project.Object.Sharedposts;
import com.example.klc.my_first_project.Object.picture;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.gson.Gson;

import es.dmoral.toasty.Toasty;

/**
 * Created by klc on 2017/4/23.
 */

public class SendRequest {
    public static boolean boolean_post=false,boolean_likes=false,boolean_commends=false,boolean_shared=false,boolean_picture=false;
    public static String tmp="";
    public static AccessTokenTracker accessTokenTracker;
    public static String pageid;
    public static int limit_counts =1;
    public static void getAllPosted(String groupID, final AccessToken Token, final int data_limit, String afterdata){

        final GraphRequest request = GraphRequest.newGraphPathRequest(
                Token,
                "/"+pageid+"/feed",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        Gson gson = new Gson();
                        FeedPost feedPosts = gson.fromJson(response.getJSONObject().toString(),FeedPost.class);
                        //存入所有文章至List
                        JSONObjectList.FeedPostDetialList.addAll(feedPosts.getData());
                        String after = feedPosts.getPages().getCursors().getAfter().toString();
                        if(feedPosts.getPages().getNext()!=null && limit_counts>0) {
                            getAllPosted("", Token, data_limit, after);
                            limit_counts--;
                        }else{
                            Log.d("MYLOG","共 "+JSONObjectList.FeedPostDetialList.size()+" 篇文章加載完畢.");
                            boolean_post = true;
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("pretty","0");
        parameters.putString("fields", "id,message,created_time");
        parameters.putString("limit", data_limit+"");
        if(afterdata.length()>10){
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
                        Likes postLikes = gson.fromJson(response.getJSONObject().toString(),Likes.class);

                        //存入所有文章至List
                        //for(int i=0;i<postLikes.getData().size();i++)
                        if(postLikes!=null)
                        if(postLikes.getData()!=null)
                            JSONObjectList.PostLikeDetialList.addAll(postLikes.getData());
                        if(postLikes!=null)
                        if(postLikes.getPaging() != null && postLikes.getPaging().getCursors() != null) {
                            String after = postLikes.getPaging().getCursors().getAfter().toString();
                            if (postLikes.getPaging().getNext() != null) {
                                getlikeUser(Token, after, contentID);
                            } else {
                                boolean_likes=true;
                                Log.d("MYLOG", "getlikeUser "+JSONObjectList.PostLikeDetialList.size()+"筆加載完畢.");
                            }
                        }else{
                            boolean_likes=true;
                            Log.d("MYLOG", "getlikeUser 沒有資料.");
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("pretty","0");
        parameters.putString("fields", "id,name");
        parameters.putString("limit", "1000");
        if(afterdata.length()>1){
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
                        Comments postComments = gson.fromJson(response.getJSONObject().toString(),Comments.class);
                        //存入所有文章至List
                        //for(int i=0;i<postComments.getData().size();i++)
                        if(postComments!=null)
                        if(postComments.getData()!=null)
                            JSONObjectList.PostCommentsDetialList.addAll(postComments.getData());
                        if(postComments!=null)
                        if(postComments.getPaging() != null && postComments.getPaging().getCursors() != null) {
                            String after = postComments.getPaging().getCursors().getAfter().toString();
                            if (postComments.getPaging().getNext() != null) {
                                getcommentsUser(Token, after, contentID);
                            } else {
                                boolean_commends = true;
                                Log.d("MYLOG", "getcommentsUser "+JSONObjectList.PostCommentsDetialList.size()+"筆加載完畢.");
                            }
                        }else{
                            boolean_commends = true;
                            Log.d("MYLOG", "getcommentsUser 沒有資料.");
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("pretty","0");
        parameters.putString("fields", "from,message");
        parameters.putString("limit", "100");
        if(afterdata.length()>1){
            parameters.putString("after", afterdata);
        }
        request.setParameters(parameters);
        request.executeAsync();
    }

    public static void getShareUser(final AccessToken Token, String afterdata, final String contentID) {
        Log.d("MYLOG","afterdata: "+afterdata);

        String contentID2 = contentID.split("_")[1];
        GraphRequest request = GraphRequest.newGraphPathRequest(
                Token,
                contentID2+"/sharedposts",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        Log.d("MYLOG",response.toString());
                        Gson gson = new Gson();
                        String jsonString = response.toString().split("graphObject:")[1].split(", error")[0];
                        Sharedposts postShared = gson.fromJson(jsonString,Sharedposts.class);

                        //存入所有文章至List
                        //for(int i=0;i<postShared.getData().size();i++)
                        if(postShared!=null)
                        if(postShared.getData()!=null)
                            JSONObjectList.PostSharedDetialList.addAll(postShared.getData());
                        if(postShared!=null) {
                            if (postShared.getPaging() != null && postShared.getPaging().getCursors() != null) {
                                String after = postShared.getPaging().getCursors().getAfter().toString();
                                if (postShared.getPaging() != null)
                                    if (postShared.getPaging().getNext() != null) {
                                        getShareUser(Token, after, contentID);
                                    } else {
                                        boolean_shared = true;
                                        Log.d("MYLOG", "getShareUser " + JSONObjectList.PostSharedDetialList.size() + "筆加載完畢.");
                                    }
                            } else {
                                boolean_shared = true;
                                Log.d("MYLOG", "getShareUser 沒有資料.");
                            }
                        }else{
                            boolean_shared = true;
                            Log.d("MYLOG", "getShareUser 沒有資料.");
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("pretty","0");
        parameters.putString("fields", "from,message");
        parameters.putString("limit", "25");
        if(afterdata.length()>1){
            parameters.putString("after", afterdata);
        }
        request.setParameters(parameters);
        request.executeAsync();
    }

    public static void getPageID(final Context context, final AccessToken Token, String url) {
        final GraphRequest request = GraphRequest.newGraphPathRequest(
                Token,
                "/"+url,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        Log.d("MYLOG",response.toString());
                        if(response.toString().contains("Unsupported") || response.toString().contains("803")
                                || response.toString().contains("OAuthException")){
                            pageid = "xxxxxxxxxxxxxxxxxxxx";
                            Toasty.error(context,"URL解析出問題, 請填入專頁ID",Toast.LENGTH_LONG,true).show();
                        }else {
                            pageid = response.toString().split("id\":\"")[1].split("\"")[0];
                            Log.d("MYLOG", "pageid: "+pageid);
                        }
                    }
                });

        request.executeAsync();
    }

    public static void getLuckerPicture(final AccessToken Token, final String contentID){
        Log.d("MYLOG",contentID);
        GraphRequest request = GraphRequest.newGraphPathRequest(
                Token,
                "/"+contentID+"/picture?type=large",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        String str = response.getJSONObject().toString().replace("\\","");
                        Log.d("MYLOG",str);

                        if(response.getJSONObject().toString().length()>30) {
                            String pictureUrl = str.split("url\":\"")[1].split("\"")[0];
                            if (pictureUrl.length() > 10) {
                                Log.d("MYLOG",pictureUrl);
                                Lottery_Activity.pictureUrl = pictureUrl;
                                boolean_picture = true;
                            }
                        }else{
                            Log.d("MYLOG","取得相片失敗");
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putBoolean("redirect", false);
        request.setParameters(parameters);
        request.executeAsync();
    }


}
