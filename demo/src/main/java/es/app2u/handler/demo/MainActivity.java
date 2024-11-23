package es.app2u.handler.demo;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.File;
import java.util.concurrent.TimeUnit;

import es.app2u.demo.R;
import es.app2u.handler.demo.remote.GetFileWS;
import es.app2u.handler.demo.remote.GetJSONWS;
import es.app2u.handler.demo.remote.ListJSONWS;
import es.app2u.handler.demo.remote.PostJSONWS;
import es.app2u.handler.demo.remote.PutJSONWS;
import es.app2u.handler.demo.remote_errors.TimeoutErrorHandler;
import es.app2u.handler.demo.remote_errors.UnknownErrorHandler;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private Button btnRequest;

    private final ListJSONWS listJSONWS = new ListJSONWS();
    private final GetJSONWS getJSONWS = new GetJSONWS();
    private final GetFileWS getFileWS = new GetFileWS();

    static class WSObserver<T> implements SingleObserver<T> {

        private final Context context;
        private final ProgressBar progressBar;
        private final Button button;

        WSObserver(Context context, ProgressBar progressBar, Button button) {
            this.context = context;
            this.progressBar = progressBar;
            this.button = button;
        }

        @Override
        public void onSubscribe(Disposable d) {
            button.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onSuccess(T ignore) {
            button.setEnabled(true);
            progressBar.setVisibility(View.GONE);
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(Throwable e) {
            button.setEnabled(true);
            progressBar.setVisibility(View.GONE);
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressbar);

        // Hide progress bar by default
        progressBar.setVisibility(View.GONE);

        btnRequest = findViewById(R.id.btn_request);
        btnRequest.setOnClickListener( v -> onClick());
    }

    private void onClick() {
        //actionPost();
        //actionPut();
        //actionList();
        //actionGet();
        //actionFile();
        actionUnknownError();
        //actionTimeoutError();
        // ConnectError: In order to test the ConnectError you have to disable the internet connection
    }

    private void actionList() {
        WSObserver<JSONObject> observer = new WSObserver<>(this, progressBar, btnRequest);
        listJSONWS.executeGetArray().observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    private void actionGet() {
        WSObserver<JSONObject> observer = new WSObserver<>(this, progressBar, btnRequest);
        getJSONWS.executeGetObject(1).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }


    private void actionPost() {
        WSObserver<JSONObject> observer = new WSObserver<>(this, progressBar, btnRequest);
        new PostJSONWS().execute().observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    private void actionPut() {
        WSObserver<JSONObject> observer = new WSObserver<>(this, progressBar, btnRequest);
        new PutJSONWS().execute().observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    private void actionFile() {

        // Update progress every second
        getFileWS.getProgressListener()
                .throttleLast(1, TimeUnit.SECONDS)
                .subscribe(progressListenerObject -> runOnUiThread(() -> {
                    float progress = ((float) progressListenerObject.getBytesRead() / progressListenerObject.getContentLength());
                    int progressInt = (int) (progress * 100);
                    Log.d("PROGRESS", "Progress: " + progressInt);
                    progressBar.setProgress(progressInt);
                    progressBar.setIndeterminate(false);
        }));

        WSObserver<File> observer = new WSObserver<>(this, progressBar, btnRequest);
        getFileWS.execute(this).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    private void actionTimeoutError() {
        WSObserver<JSONObject> observer = new WSObserver<>(this, progressBar, btnRequest);
        new TimeoutErrorHandler().execute().observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    private void actionUnknownError() {
        WSObserver<JSONObject> observer = new WSObserver<>(this, progressBar, btnRequest);
        new UnknownErrorHandler().execute().observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }
}
