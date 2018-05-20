package com.example.android.bakingapp.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.bakingapp.CustomIdlingResource;
import com.example.android.bakingapp.data.Recipe;

import org.json.JSONArray;
import org.json.JSONException;

public class NetworkUtils {

    private static final String RECIPES_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    public static void requestRecipes(Context context, final RecipesCallback recipesCallback, final CustomIdlingResource idlingResource)
    {
        if(idlingResource != null)
        {
            idlingResource.setIdleState(false);
        }

        RequestQueue queue = Volley.newRequestQueue(context);

        // Get string from URL
        StringRequest stringRequest = new StringRequest(Request.Method.GET, RECIPES_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(idlingResource!=null)
                            idlingResource.setIdleState(false);

                        if(!response.isEmpty())
                        {
                            try {

                                JSONArray jsonArray =new JSONArray(response);
                                Recipe[] recipeList = new Recipe[jsonArray.length()];

                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    recipeList[i] = Recipe.fromJson(jsonArray.getJSONObject(i));
                                }

                                if(idlingResource != null)
                                {
                                    idlingResource.setIdleState(true);
                                }

                                recipesCallback.onFinishRequest(recipeList);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                recipesCallback.onFinishRequest(null);
                            }

                        }else
                            recipesCallback.onFinishRequest(null);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                recipesCallback.onFinishRequest(null);
            }
        });
        // Add to the RequestQueue.
        queue.add(stringRequest);
    }

    public interface RecipesCallback{
        void onFinishRequest(Recipe[] recipeList);
    }

    // Check if device is online or not
    public static boolean isConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm!=null) {
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }else
            return false;
    }
}
