package com.tittech.chatgpt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerview;
    EditText message_edit_text;
    ImageButton sent_btn;
    List<Messege> messegeList;
    MessageAdapter messageAdapter;
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messegeList = new ArrayList<>();
        recyclerview = findViewById(R.id.recyclerview);
        message_edit_text = findViewById(R.id.message_edit_text);
        sent_btn = findViewById(R.id.sent_btn);

        messageAdapter = new MessageAdapter(messegeList);
        recyclerview.setAdapter(messageAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerview.setLayoutManager(layoutManager);

        sent_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question =message_edit_text.getText().toString().trim();
                addToChat(question,Messege.SENT_BY_ME);
                message_edit_text.setText("");
                callApi(question);
            }
        });
    }

    void addToChat(String message,String sentBy){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messegeList.add(new Messege(message,sentBy));
                messageAdapter.notifyDataSetChanged();
                recyclerview.smoothScrollToPosition(messageAdapter.getItemCount());
            }
        });
    }
    void addResponse(String response){
        messegeList.remove(messegeList.size()-1);
        addToChat(response,Messege.SENT_BY_BOT);
    }

    void callApi(String question){

        messegeList.add(new Messege("Typing....",Messege.SENT_BY_BOT));

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("model","text-davinci-003");
            jsonObject.put("prompt",question);
            jsonObject.put("max_tokens",4000);
            jsonObject.put("temperature",0);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        RequestBody body = RequestBody.create(jsonObject.toString(),JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/completions")
                .header("Authorization","Bearer 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")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("Fail to load response due tu"+e.getMessage());

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonObject1 = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject1.getJSONArray("choices");
                        String result = jsonArray.getJSONObject(0).getString("text");
                        addResponse(result.trim());

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }else {
                    addResponse("Fail to load response due tu"+response.body().toString());
                }

            }
        });


    }
}