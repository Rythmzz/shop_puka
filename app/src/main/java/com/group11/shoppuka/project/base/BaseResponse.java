package com.group11.shoppuka.project.base;

import androidx.annotation.NonNull;

public abstract class BaseResponse {
    public static final class Success<T> extends BaseResponse {
        private final T data;


        public Success(T data) {
            this.data = data;
        }

        public T getData() {
            return data;
        }

        @NonNull
        @Override
        public String toString() {
            return "Sucess[data=" + data + "]";
        }
    }


    public static final class Error extends BaseResponse{
        private final Exception exception;

        public Error(Exception exception) {
            this.exception = exception;
        }

        @NonNull
        @Override
        public String toString() {
            return "Error[exception="+exception+"]";
        }
    }

    public static final class Loading extends BaseResponse{
        @NonNull
        @Override
        public String toString() {
            return "Loading....";
        }
    }

    @NonNull
    @Override
    public String toString() {
        if (this instanceof Success) {
            return ((Success<?>) this).toString();
        } else if (this instanceof Error) {
            return ((Error) this).toString();
        } else if (this instanceof Loading) {
            return ((Loading) this).toString();
        } else {
            return "Unknown type of BaseResponse";
        }
    }
}
