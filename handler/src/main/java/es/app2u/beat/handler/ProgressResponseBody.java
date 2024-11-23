package es.app2u.beat.handler;

import androidx.annotation.NonNull;

import java.io.IOException;

import io.reactivex.subjects.PublishSubject;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

public class ProgressResponseBody extends ResponseBody {

    public static class Progress {
        long bytesRead;
        long contentLength;

        Progress(long bytesRead, long contentLength) {
            this.bytesRead = bytesRead;
            this.contentLength = contentLength;
        }

        public long getBytesRead() {
            return bytesRead;
        }

        public long getContentLength() {
            return contentLength;
        }
    }

    private final ResponseBody responseBody;
    private BufferedSource bufferedSource;

    private final PublishSubject<Progress> progressListener;

    public ProgressResponseBody(ResponseBody responseBody,
                                PublishSubject<Progress> progressListener) {
        this.responseBody = responseBody;
        this.progressListener = progressListener;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @NonNull
    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long receivedContent = 0L;
            final long contentLength = responseBody.contentLength();

            @Override
            public long read(@NonNull Buffer sink, long byteCount) throws IOException {
                // read() returns the number of bytes read, or -1 if this source is exhausted
                long bytesRead = super.read(sink, byteCount);

                // Increment current bytes
                this.receivedContent += bytesRead != -1 ? bytesRead : 0;

                // Notify progress
                progressListener.onNext(new Progress(this.receivedContent, contentLength));
                return bytesRead;
            }
        };
    }

}