package com.example.rakna.raknagraduationproject.View.azerCode;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rakna.raknagraduationproject.R;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordcarowner extends AppCompatActivity {

    EditText email_forgotPassword, token;
    Button btn_forgotPassword, btn_checkToken;
    ProgressBar loading, loading_token;

    private static String URL_REGIST = "https://rakna-app.000webhostapp.com/forgetpassword_carowner.php";

    private static String URL_LOGIN = "https://rakna-app.000webhostapp.com/check_token_validation_carowner.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_passwordcarowner);

        email_forgotPassword = (EditText)findViewById(R.id.email_forgot);
        btn_forgotPassword = (Button)findViewById(R.id.btn_forgotPassword);
        loading = (ProgressBar)findViewById(R.id.loading);
        token = (EditText)findViewById(R.id.token);
        btn_checkToken = (Button)findViewById(R.id.btn_checkToken);
        loading_token = (ProgressBar)findViewById(R.id.loading_token);

        btn_forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (email_forgotPassword.getText().toString().isEmpty()) {

                    Toast("Please enter the Email");
                    return;
                }
                sendToken();
            }
        });

        btn_checkToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = email_forgotPassword.getText().toString().trim();
                String tok = token.getText().toString().trim();
                checkToken(mail, tok);
            }
        });

    }

    private void sendToken(){
        loading.setVisibility(View.VISIBLE);
        btn_forgotPassword.setVisibility(View.GONE);
        final String email = this.email_forgotPassword.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
//                    if(success.equals("1")){
                    Toast("Register success!");
                    loading.setVisibility(View.GONE);
                    btn_forgotPassword.setVisibility(View.VISIBLE);



//                    }
                }catch (JSONException e){
                    e.printStackTrace();
//                    Toast.makeText(RegistrationActivity.this, "Register error! 15 \n"+ e.toString(),Toast.LENGTH_SHORT).show();
                    Toast("This email already exists");
                    loading.setVisibility(View.GONE);
                    btn_forgotPassword.setVisibility(View.VISIBLE);
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                Toast.makeText(ForgotPasswordcarowner.this, "Register error! 16 \n"+ error.toString(),Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.GONE);
                btn_forgotPassword.setVisibility(View.VISIBLE);

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("email", email);


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void checkToken(final String email, final String token) {
        loading_token.setVisibility(View.VISIBLE);
        btn_checkToken.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("login");
                    if (success) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String name = object.getString("name").trim();
                            String email = object.getString("email").trim();
                            Log.d("eheck emIL", email);
                            Intent intent = new Intent(ForgotPasswordcarowner.this, ResetPasswordcarowner.class);
                            intent.putExtra("email", email);
//                            String token = object.getString("token");
//                            String id = object.getString("id").trim();
//                            String photo = object.getString("photo").trim();
//                            Log.d("test", photo);
                            // Toast.makeText(LoginActivity.this, "Success Login  \nyour name : " + name + " \nyour email : " + email, Toast.LENGTH_LONG).show();
                            Toast("success");
                            startActivity(intent);
                            loading_token.setVisibility(View.GONE);
                            btn_checkToken.setVisibility(View.VISIBLE);

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    loading_token.setVisibility(View.GONE);
                    btn_checkToken.setVisibility(View.VISIBLE);
                    Toast("Not Valid");
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading_token.setVisibility(View.GONE);
                btn_checkToken.setVisibility(View.VISIBLE);
                // Toast.makeText(ForgotPasswordcarowner.this, "Error 3" + error.toString(), Toast.LENGTH_LONG).show();


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("token", token);

                return params;
            }
        };
        // MySingleton.getInstance(RegisterActivity.this).addToRequestQueue(stringRequest);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    public void backbutton(View view) {
        startActivity(new Intent(ForgotPasswordcarowner.this, Login.class));
        finish();
    }

    public void Toast(String text) {
        StyleableToast st = new StyleableToast(getApplicationContext(), text, Toast.LENGTH_LONG);
        st.setBackgroundColor(Color.parseColor("#e12a2a"));
        st.setTextColor(Color.WHITE);
        st.show();
    }


}
