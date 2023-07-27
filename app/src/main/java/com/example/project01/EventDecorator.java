package com.example.project01;

//import com.prolificinteractive.materialcalendarview.DayViewFacade;
//import com.prolificinteractive.materialcalendarview.DayViewSelectable;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.android.material.datepicker.DayViewDecorator;

public class EventDecorator extends DayViewDecorator implements Parcelable {
    private final int color;
    private final long timeInMillis;

//    public void decorate(DayViewFacade view) {
//        // set the background color of the day
//        view.setBackgroundDrawable(new ColorDrawable(color));
//
//        // add a dot marker to the day
//        view.addSpan(new DotSpan(5, color));
//    }


    public EventDecorator(int color, long timeInMillis) {
        this.color = color;
        this.timeInMillis = timeInMillis;
    }

    protected EventDecorator(Parcel in) {
        color = in.readInt();
        timeInMillis = in.readLong();
    }

    public static final Creator<EventDecorator> CREATOR = new Creator<EventDecorator>() {
        @Override
        public EventDecorator createFromParcel(Parcel in) {
            return new EventDecorator(in);
        }

        @Override
        public EventDecorator[] newArray(int size) {
            return new EventDecorator[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {

    }
}
